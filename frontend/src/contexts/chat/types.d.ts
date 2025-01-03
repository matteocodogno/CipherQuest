import { GameStatus } from '@/api/chat/types';
import { ReactElement } from 'react';

export type MessageType = 'text' | 'image';

export type Message = {
  id: string;
  type: MessageType;
  content: string | ReactElement;
  author: { id: string; name: string; avatar?: string };
  createdAt: Date;
  info: MessageInfo | null;
};

export type MessageInfo = {
  isLevelUp: boolean;
  sources: { id: string; title: string }[];
  status?: GameStatus;
};
