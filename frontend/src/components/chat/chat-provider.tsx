import {
  ChatContext,
  ChatProviderProps,
  CreateMessageParams,
} from '@/components/chat/chat-context.tsx';
import type { Contact, Message } from '@/components/chat/types';
import { ReactElement, useCallback, useEffect, useState } from 'react';
import { useUser } from '@/hooks/use-user';

export const ChatProvider = ({
  children,
  contacts: initialContacts = [],
  messages: initialMessages = [],
}: ChatProviderProps): ReactElement => {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [messages, setMessages] = useState<Message[]>(initialMessages);
  const [openDesktopSidebar, setOpenDesktopSidebar] = useState<boolean>(true);
  const [openMobileSidebar, setOpenMobileSidebar] = useState<boolean>(false);

  const { user } = useUser();

  useEffect((): void => {
    setContacts(initialContacts);
    setMessages(initialMessages);
  }, [initialContacts, initialMessages]);

  const handleCreateMessage = useCallback(
    (params: CreateMessageParams): void => {
      if (!user) {
        throw new Error('create message needs a valid user');
      }

      const message = {
        id: `MSG-${Date.now()}`,
        type: params.type,
        author: {
          id: user?.userId,
          name: user?.username,
          avatar: '/assets/avatar.jpg',
        },
        content: params.content,
        createdAt: new Date(),
      } satisfies Message;

      const updatedMessages = [...messages];

      // Add it to the messages
      updatedMessages.push(message);

      // Dispatch messages update
      setMessages(updatedMessages);
    },
    [messages, user],
  );

  return (
    <ChatContext.Provider
      value={{
        contacts,
        messages,
        createMessage: handleCreateMessage,
        openDesktopSidebar,
        setOpenDesktopSidebar,
        openMobileSidebar,
        setOpenMobileSidebar,
      }}
    >
      {children}
    </ChatContext.Provider>
  );
};
