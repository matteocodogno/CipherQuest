import { ChatHistory, SenderType } from './types';
import { Message } from '@/components/chat/types';
import { useQuery } from '@tanstack/react-query';

interface GetChatHistoryProps {
  userId?: string;
}

export const useGetChatHistory = ({ userId }: GetChatHistoryProps) => {
  const { error, data } = useQuery({
    queryKey: [`chat-${userId}`],
    queryFn: (): Promise<ChatHistory> =>
      fetch(`/api/chat/${userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then((res) => res.json()),
    enabled: !!userId,
  });

  console.log({ data });

  if (error) {
    console.log(error);
    return [];
  }

  return (data ?? []).map((historyMessage) => {
    const name =
      historyMessage.sender === SenderType.USER ? 'Human' : 'Overmind';
    const authorId =
      historyMessage.sender === SenderType.USER ? userId : '123456';
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
