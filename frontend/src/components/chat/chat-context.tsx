import type { Contact, Message, MessageType } from './types';
import { Dispatch, ReactNode, SetStateAction, createContext } from 'react';

function noop(): void {
  return undefined;
}

export type CreateMessageParams = {
  type: MessageType;
  content: string;
};

export type ChatContextValue = {
  contacts: Contact[];
  messages: Message[];
  createMessage: (params: CreateMessageParams) => void;
  openDesktopSidebar: boolean;
  setOpenDesktopSidebar: Dispatch<SetStateAction<boolean>>;
  openMobileSidebar: boolean;
  setOpenMobileSidebar: Dispatch<SetStateAction<boolean>>;
};

export const ChatContext = createContext<ChatContextValue>({
  contacts: [],
  messages: [],
  createMessage: noop,
  openDesktopSidebar: true,
  setOpenDesktopSidebar: noop,
  openMobileSidebar: true,
  setOpenMobileSidebar: noop,
});

export type ChatProviderProps = {
  children: ReactNode;
  contacts: Contact[];
  messages: Message[];
};

