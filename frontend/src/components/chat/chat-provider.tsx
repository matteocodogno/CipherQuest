import { ChatContext, ChatProviderProps, CreateMessageParams } from '@/components/chat/chat-context.tsx';
import type { Contact, Message } from '@/components/chat/types';
import { ReactElement, useCallback, useEffect, useState } from 'react';

export const ChatProvider = ({
  children,
  contacts: initialContacts = [],
  messages: initialMessages = [],
}: ChatProviderProps): ReactElement => {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [messages, setMessages] = useState<Message[]>(initialMessages);
  const [openDesktopSidebar, setOpenDesktopSidebar] = useState<boolean>(true);
  const [openMobileSidebar, setOpenMobileSidebar] = useState<boolean>(false);

  useEffect((): void => {
    setContacts(initialContacts);
  }, [initialContacts]);

  const handleCreateMessage = useCallback(
    (params: CreateMessageParams): void => {
      const message = {
        id: `MSG-${Date.now()}`,
        type: params.type,
        author: {id: 9834759384, name: 'Sofia Rivers', avatar: '/assets/avatar.png'},
        content: params.content,
        createdAt: new Date(),
      } satisfies Message;

      const updatedMessages = [...messages];

      // Add it to the messages
      updatedMessages.push(message);

      // Dispatch messages update
      setMessages(updatedMessages);
    },
    [messages],
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
}
