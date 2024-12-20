import { Box, Button } from '@mui/material';
import { ReactElement, useCallback, useRef } from 'react';
import MessageAdd from './message-add';
import type { MessageType } from '../types';
import { useChat } from '@/hooks/use-chat';

const ComposeView = (): ReactElement => {
  const { createMessage } = useChat();
  const messageAddRef = useRef<HTMLDivElement>(null);

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  const showNotes = useCallback((): ReactElement => {
    const messageAdd = messageAddRef.current?.getBoundingClientRect();
    return (
      <Button
        sx={{
          borderRadius: 8,
          backgroundColor: 'var(--mui-palette-secondary-light)',
          '&:hover': { bgcolor: 'var(--mui-palette-secondary-main)' },
          color: 'var(--mui-palette-primary-700)',
          height: 'auto',
          width: 140,
          position: 'absolute',
          top: messageAdd?.top,
          right: 100,
          px: 2,
          py: 1,
        }}
        endIcon={<Box component='img' src={'/assets/notepad.svg'} />}
      >
        Block note
      </Button>
    );
  }, []);

  return (
    <>
      <MessageAdd ref={messageAddRef} onSend={handleSendMessage} />
      {showNotes()}
    </>
  );
};

export default ComposeView;
