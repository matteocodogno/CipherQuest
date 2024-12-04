import { ChatHistory, SenderType } from './types';
import { Message } from '@/components/chat/types';
import { User } from '@/types/user';
import { useQuery } from '@tanstack/react-query';

interface GetChatHistoryProps {
  user?: User | null;
}

export const useGetChatHistory = ({ user }: GetChatHistoryProps) => {
  const { error, data } = useQuery({
    queryKey: [`chat-${user?.userId}`],
    queryFn: (): Promise<ChatHistory> =>
      fetch(`/api/chat/${user?.userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then((res) => res.json()),
    enabled: !!user,
  });

  if (error) {
    console.log(error);
    return [];
  }

  return (data ?? []).map((historyMessage) => {
    const name =
      historyMessage.sender === SenderType.USER ? user?.username : 'Overmind';
    const authorId =
      historyMessage.sender === SenderType.USER ? user?.userId : '123456';
    return {
      id: historyMessage.index,
      type: 'text',
      content: historyMessage.message,
      author: {
        id: authorId,
        name: name,
        avatar: '/assets/avatar.png',
      },
      createdAt: new Date(historyMessage.timestamp),
    };
  }) as Message[];
};
