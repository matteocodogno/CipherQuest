import { CHAT_URL } from './constants';
import { ChatHistorySchema } from './schema';
import { Message } from '@/components/chat/types';
import { SenderType } from './types';
import { User } from '@/types/user';
import { generateMessage } from '@/utils/messages';
import { useQuery } from '@tanstack/react-query';

interface GetChatHistoryProps {
  user?: User | null;
}

const useGetChatHistory = ({ user }: GetChatHistoryProps) => {
  const { isError, data, isLoading } = useQuery({
    queryKey: [`chat-${user?.userId}`],
    queryFn: () =>
      fetch(`${CHAT_URL}/${user?.userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.json())
        .then((data) => ChatHistorySchema.parse(data)),
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
      id: historyMessage.index.toString(),
      type: 'text',
      content: historyMessage.message,
      senderId: authorId,
      senderName: name,
      senderType: historyMessage.sender,
      createdAt: new Date(historyMessage.timestamp),
      info: historyMessage.info,
    });
  }) as Message[];

  return { messages, isError, isLoading };
};

export default useGetChatHistory;
