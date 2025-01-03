import { Box, Stack } from '@mui/system';
import {
  PropsWithChildren,
  ReactElement,
  useCallback,
  useEffect,
  useRef,
  useState,
} from 'react';
import ChatHeader from '../header/chat-header';
import { MessageBox } from '../messages/message-box';
import { useChat } from '@/hooks/use-chat';

export const ChatView = ({ children }: PropsWithChildren): ReactElement => {
  const { messages } = useChat();
  const lastMessage =
    messages.length > 0 ? messages[messages.length - 1] : undefined;
  const [isLevelUp, setLevelUp] = useState<boolean>();

  const messageRef = useRef<HTMLDivElement>(null);
  const headerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!messageRef.current) {
      return;
    }
    messageRef.current.scrollIntoView({ behavior: 'smooth' });

    setLevelUp(lastMessage?.info?.isLevelUp);

    if (lastMessage?.info?.isLevelUp) {
      setTimeout(() => {
        //TODO: Add vanish animation
        setLevelUp(false);
      }, 3000);
    }
  }, [messages.length, lastMessage]);

  const showLevelUp = useCallback((): ReactElement => {
    const chatHeader = headerRef.current?.getBoundingClientRect();
    //TODO: Animate from (0, chatHeader?.right) to (chatHeader?.top, chatHeader?.right)
    return (
      <Box
        component='img'
        src={'/assets/level_up.svg'}
        sx={{
          marginTop: '8px',
          height: 'auto',
          width: 'auto',
          zIndex: 5,
          position: 'absolute',
          top: (chatHeader?.top ?? 0) + (chatHeader?.height ?? 0),
          right: chatHeader?.x ?? 0,
        }}
      />
    );
  }, []);

  return (
    <>
      <ChatHeader ref={headerRef} />
      {isLevelUp && showLevelUp()}
      <Stack
        padding={4}
        direction={'column'}
        flex={1}
        alignSelf={'stretch'}
        sx={{
          overflowY: 'auto',
          scrollbarWidth: 0,
          '::-webkit-scrollbar': {
            display: 'none',
          },
        }}
        gap={2}
      >
        {messages.map((message) => (
          <MessageBox message={message} key={message.id} />
        ))}
        <div ref={messageRef} />
      </Stack>
      {children}
    </>
  );
};
