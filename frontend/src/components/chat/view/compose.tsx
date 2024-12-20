import { Box, Button } from '@mui/material';
import { ReactElement, useCallback, useRef, useState } from 'react';
import MessageAdd from './message-add';
import type { MessageType } from '../types';
import NotesDialog from '../dialog/notes-dialog';
import { useChat } from '@/hooks/use-chat';

const ComposeView = (): ReactElement => {
  const { createMessage } = useChat();
  const messageAddRef = useRef<HTMLDivElement>(null);
  const [showNotes, setShowNotes] = useState<boolean>(false);

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  const getNotesButton = useCallback((): ReactElement => {
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
        onClick={() => setShowNotes(true)}
      >
        Block note
      </Button>
    );
  }, []);

  return (
    <>
      <MessageAdd ref={messageAddRef} onSend={handleSendMessage} />
      {getNotesButton()}
      <NotesDialog
        handleClose={() => setShowNotes(false)}
        showDialog={showNotes}
      />
    </>
  );
};

export default ComposeView;
