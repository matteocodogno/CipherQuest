export type Contact = {
  id: string;
  name: string;
  avatar?: string;
  isActive: boolean;
  lastActivity?: Date;
};

export type Participant = {
  id: string;
  name: string;
  avatar?: string;
};

export type MessageType = 'text' | 'image';

export type Message = {
  id: string;
  type: MessageType;
  content: string;
  author: { id: string; name: string; avatar?: string };
  createdAt: Date;
};
