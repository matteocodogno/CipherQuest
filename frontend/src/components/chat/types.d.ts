export type MessageType = 'text' | 'image';

export type Message = {
  id: string;
  type: MessageType;
  content: string;
  author: { id: string; name: string; avatar?: string };
  createdAt: Date;
  info: MessageInfo;
};

export type MessageInfo = {
  isLevelUp: boolean;
  sources: { id: string; title: string }[];
};
