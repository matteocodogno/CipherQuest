import {
  ChatContext,
  ChatProviderProps,
  CreateMessageParams,
} from '@/components/chat/chat-context.tsx';
import { ReactElement, useCallback, useEffect, useState } from 'react';
import { Message } from '@/components/chat/types';
import { SenderType } from '@/api/chat/types';
import { generateMessage } from '@/utils/messages';
import useSendRequest from '@/api/chat/use-send-request';
import { useUser } from '@/hooks/use-user';

export const ChatProvider = ({
  children,
  messages: initialMessages = [],
}: ChatProviderProps): ReactElement => {
  const [messages, setMessages] = useState<Message[]>(initialMessages);
  const [openDesktopSidebar, setOpenDesktopSidebar] = useState<boolean>(true);
  const [openMobileSidebar, setOpenMobileSidebar] = useState<boolean>(false);
  const [coins, setLocalCoins] = useState<number>(0);
  const [level, setLocalLevel] = useState<number>(0);

  const { user } = useUser();
  const { mutate: sendRequest } = useSendRequest();

  useEffect((): void => {
    setMessages(initialMessages);
  }, [initialMessages]);

  const overmindThinking = generateMessage({
    type: 'text',
    senderType: SenderType.OVERMIND,
    content: '......',
  });

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
      updatedMessages.push(overmindThinking);
      setMessages(updatedMessages);

      sendRequest(
        { user, message: content },
        {
          onSuccess: async (chatResponse) => {
            const message = generateMessage({
              type: 'text',
              senderType: SenderType.OVERMIND,
              content: chatResponse.message,
              isLevelUp: chatResponse.info.isLevelUp,
            });
            updatedMessages.pop();
            updatedMessages.push(message);
            setMessages(updatedMessages);
            setLocalCoins(chatResponse.coins);
            setLocalLevel(chatResponse.level);
          },
        },
      );
    },
    [messages, overmindThinking, sendRequest, user],
  );

  return (
    <ChatContext.Provider
      value={{
        messages,
        coins,
        level,
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
