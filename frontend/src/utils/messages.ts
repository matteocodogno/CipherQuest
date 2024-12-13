import { Message, MessageType } from '@/components/chat/types';
import { SenderType } from '@/api/chat/types';

interface CreateMessageProps {
  id?: string;
  type?: MessageType;
  content: string;
  senderType: SenderType;
  senderId?: string;
  senderName?: string;
  createdAt?: Date;
  isLevelUp?: boolean;
}

export const generateMessage = ({
  id = `MSG-${Date.now()}`,
  type = 'text',
  content,
  senderType,
  senderId = '000000',
  senderName = 'Overmind',
  createdAt = new Date(),
  isLevelUp = false,
}: CreateMessageProps): Message => {
  const avatar =
    senderType === SenderType.USER
      ? '/assets/avatar.jpg'
      : '/assets/overmind.jpg';

  const message = {
    id,
    type,
    author: {
      id: senderId,
      name: senderName,
      avatar,
    },
    content,
    createdAt,
    isLevelUp,
  } satisfies Message;

  return message;
};
