import { ReactElement, ReactNode } from 'react';
import ChatHeader from '../header/chat-header';
import { Stack } from '@mui/system';

export type ChatViewProps = {
  children: ReactNode;
};

export const ChatView = ({ children }: ChatViewProps): ReactElement => {
  return (
    <Stack
      sx={{
        width: '70%',
        minWidth: '600px',
        paddingLeft: 7,
        paddingRight: 7,
        height: 'calc(100vh - 62px)',
      }}
      flexDirection={'column'}
      alignItems={'center'}
    >
      <ChatHeader />
      <Stack
        padding={4}
        direction={'column'}
        flex={1}
        alignSelf={'stretch'}
        sx={{
          overflowY: 'scroll',
          scrollbarWidth: 0,
          ':-webkit-scrollbar': {
            display: 'none',
          },
        }}
        gap={2}
      ></Stack>
      {children}
    </Stack>
  );
};
