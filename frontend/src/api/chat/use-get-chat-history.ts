import { ChatHistory, SenderType } from './types';
import { CHAT_URL } from './constants';
import { Message } from '@/components/chat/types';
import { User } from '@/types/user';
import { generateMessage } from '@/utils/messages';
import { useQuery } from '@tanstack/react-query';

interface GetChatHistoryProps {
  user?: User | null;
}

const useGetChatHistory = ({ user }: GetChatHistoryProps) => {
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
    const { name, authorId } =
      historyMessage.sender === SenderType.USER
        ? {
            name: user?.username,
            authorId: user?.userId,
          }
        : {
            name: 'Overmind',
            authorId: '00000',
          };
    return generateMessage({
      id: historyMessage.index,
      type: 'text',
      content: historyMessage.message,
      senderId: authorId,
      senderName: name,
      senderType: historyMessage.sender,
      createdAt: new Date(historyMessage.timestamp),
    });
  }) as Message[];

  return { messages, isError, isLoading };
};

export default useGetChatHistory;
