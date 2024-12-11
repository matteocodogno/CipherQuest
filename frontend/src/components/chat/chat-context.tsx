import { Dispatch, ReactNode, SetStateAction, createContext } from 'react';
import type { Message, MessageType } from './types';

function noop(): void {
  return undefined;
}

export type CreateMessageParams = {
  type: MessageType;
  content: string;
};

export type ChatContextValue = {
  messages: Message[];
  createMessage: (params: CreateMessageParams) => void;
  openDesktopSidebar: boolean;
  setOpenDesktopSidebar: Dispatch<SetStateAction<boolean>>;
  openMobileSidebar: boolean;
  setOpenMobileSidebar: Dispatch<SetStateAction<boolean>>;
};

export const ChatContext = createContext<ChatContextValue>({
  messages: [],
  createMessage: noop,
  openDesktopSidebar: true,
  setOpenDesktopSidebar: noop,
  openMobileSidebar: true,
  setOpenMobileSidebar: noop,
});

export type ChatProviderProps = {
  children: ReactNode;
  messages: Message[];
};
