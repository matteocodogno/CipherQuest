export type MessageType = 'text' | 'image';

export type Message = {
  id: string;
  type: MessageType;
  content: string;
  author: { id: string; name: string; avatar?: string };
  createdAt: Date;
  isLevelUp: boolean;
};
