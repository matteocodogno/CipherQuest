import {
  ChatContext,
  ChatProviderProps,
  CreateMessageParams,
} from '@/components/chat/chat-context.tsx';
import type { Contact, Message } from '@/components/chat/types';
import { ReactElement, useCallback, useEffect, useState } from 'react';
import { SenderType } from '@/api/chat/types';
import { generateMessage } from '@/utils/messages';
import useSendRequest from '@/api/chat/use-send-request';
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
  const { mutate: sendRequest } = useSendRequest();

  useEffect((): void => {
    setContacts(initialContacts);
    setMessages(initialMessages);
  }, [initialContacts, initialMessages]);

  const addOvermindThinking = () => {
    const overmindThinking = generateMessage({
      type: 'text',
      senderType: SenderType.OVERMIND,
      content: '......',
    });
    const updatedMessages = [...messages];
    updatedMessages.push(overmindThinking);
  };

  const handleCreateMessage = useCallback(
    (params: CreateMessageParams): void => {
      if (!user) {
        throw new Error('create message needs a valid user');
      }

      const { content } = params;
      const updatedMessages = [...messages];

      const userRequest = generateMessage({
        type: params.type,
        senderType: SenderType.USER,
        senderId: user?.userId,
        senderName: user?.username,
        content: params.content,
      });

      updatedMessages.push(userRequest);
      addOvermindThinking();
      setMessages(updatedMessages);

      sendRequest(
        { user, message: content },
        {
          onSuccess: (chatResponse) => {
            const message = generateMessage({
              type: 'text',
              senderType: SenderType.OVERMIND,
              content: chatResponse.message,
            });
            updatedMessages.pop();
            updatedMessages.push(message);
            setMessages(updatedMessages);
          },
        },
      );
    },
    [messages, sendRequest, user],
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
