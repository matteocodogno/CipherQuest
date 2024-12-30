import { Message, MessageInfo, MessageType } from '@/contexts/chat/types';
import { ReactElement } from 'react';
import { SenderType } from '@/api/chat/types';

interface CreateMessageProps {
  id?: string;
  type?: MessageType;
  content: string | ReactElement;
  senderType: SenderType;
  senderId?: string;
  senderName?: string;
  createdAt?: Date;
  info?: MessageInfo;
}

export const generateMessage = ({
  id = `MSG-${Date.now()}`,
  type = 'text',
  content,
  senderType,
  senderId = '000000',
  senderName = 'Overmind',
  createdAt = new Date(),
  info = { isLevelUp: false, sources: [] },
}: CreateMessageProps): Message => {
  const avatar =
    senderType === SenderType.USER
      ? '/assets/avatar.jpg'
      : '/assets/overmind.jpg';

  return {
    id,
    type,
    author: {
      id: senderId,
      name: senderName,
      avatar,
    },
    content,
    createdAt,
    info,
  } satisfies Message;
};
