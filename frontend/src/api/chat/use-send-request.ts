import { CHAT_URL } from './constants';
import { ChatResponseSchema } from './schema';
import { User } from '@/types/user';
import { useMutation } from '@tanstack/react-query';

interface SendRequestProps {
  user: User;
  message: string;
}

const useSendRequest = () =>
  useMutation({
    mutationFn: ({ user, message }: SendRequestProps) =>
      fetch(`${CHAT_URL}/${user?.userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: message,
      })
        .then((res) => res.json())
        .then((data) => ChatResponseSchema.parse(data)),
  });

export default useSendRequest;
