import { createContextId } from '@builder.io/qwik';

export type Message = {
  date: string;
  role: 'user' | 'bot';
  text: string;
};

export type MessageStore = {
  messages: Message[];
};

export const ChatContext = createContextId<MessageStore>('ChatContext');
