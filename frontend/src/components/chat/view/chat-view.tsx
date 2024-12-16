import { PropsWithChildren, ReactElement, useEffect, useRef } from 'react';
import ChatHeader from '../header/chat-header';
import { MessageBox } from '../messages/message-box';
import { Stack } from '@mui/system';
import { useChat } from '@/hooks/use-chat';

export const ChatView = ({ children }: PropsWithChildren): ReactElement => {
  const { messages } = useChat();
  const lastMessage =
    messages.length > 0 ? messages[messages.length - 1].content : '';

  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!ref.current) {
      return;
    }
    ref.current.scrollIntoView({ behavior: 'smooth' });
  }, [messages.length, lastMessage]);

  return (
    <>
      <ChatHeader />
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
        <div ref={ref} />
      </Stack>
      {children}
    </>
  );
};
