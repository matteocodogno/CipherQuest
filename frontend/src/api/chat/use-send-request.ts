import { CHAT_URL } from './constants';
import { ChatResponse } from './types';
import { User } from '@/types/user';
import { useMutation } from '@tanstack/react-query';

interface SendRequestProps {
  user: User;
  message: string;
}

const useSendRequest = () =>
  useMutation({
    mutationFn: ({ user, message }: SendRequestProps): Promise<ChatResponse> =>
      fetch(`${CHAT_URL}/${user?.userId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: message,
      }).then((res) => res.json()),
  });

export default useSendRequest;
