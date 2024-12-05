import { ChatHistory, SenderType } from './types';
import { CHAT_URL } from './constants';
import { Message } from '@/components/chat/types';
import { User } from '@/types/user';
import { useQuery } from '@tanstack/react-query';

interface GetChatHistoryProps {
  user?: User | null;
}

export const useGetChatHistory = ({ user }: GetChatHistoryProps) => {
  const { isError, data, isLoading } = useQuery({
    queryKey: [`chat-${user?.userId}`],
    queryFn: (): Promise<ChatHistory> =>
      fetch(`${CHAT_URL}/${user?.userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then((res) => res.json()),
    enabled: !!user,
  });

  const messages = (data ?? []).map((historyMessage) => {
    const { name, authorId, avatar } =
      historyMessage.sender === SenderType.USER
        ? {
            name: user?.username,
            authorId: user?.userId,
            avatar: '/assets/avatar.jpg',
          }
        : {
            name: 'Overmind',
            authorId: '00000',
            avatar: '/assets/overmind.jpg',
          };
    return {
      id: historyMessage.index,
      type: 'text',
      content: historyMessage.message,
      author: {
        id: authorId,
        name: name,
        avatar: avatar,
      },
      createdAt: new Date(historyMessage.timestamp),
    };
  }) as Message[];

  return { messages, isError, isLoading };
};
