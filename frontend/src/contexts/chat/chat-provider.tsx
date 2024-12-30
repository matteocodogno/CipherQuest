import {
  ChatContext,
  ChatProviderProps,
  CreateMessageParams,
} from '@/contexts/chat/chat-context.tsx';
import { ReactElement, useCallback, useEffect, useState } from 'react';
import { getGameSessionInfo, saveGameSessionInfo } from '@/lib/game/localStore.ts';
import Loader from '@/components/loader';
import { Message } from '@/contexts/chat/types';
import { SenderType } from '@/api/chat/types.ts';
import { generateMessage } from '@/utils/messages.ts';
import useSendRequest from '@/api/chat/use-send-request.ts';
import { useUser } from '@/hooks/use-user.ts';

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

    const gameSession = getGameSessionInfo();
    if (gameSession) {
      setLocalCoins(gameSession.coins);
      setLocalLevel(gameSession.level);
    }
  }, [initialMessages]);

  const overmindThinking = generateMessage({
    type: 'text',
    senderType: SenderType.OVERMIND,
    content: (<Loader />),
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
              info: chatResponse.info ?? undefined,
            });
            updatedMessages.pop();
            updatedMessages.push(message);
            setMessages(updatedMessages);
            setLocalCoins(chatResponse.coins);
            setLocalLevel(chatResponse.level);
            saveGameSessionInfo({
              coins: chatResponse.coins,
              level: chatResponse.level,
            });
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
