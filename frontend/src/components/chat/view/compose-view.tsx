import { ReactElement, useCallback, useState } from 'react';
import MessageAdd from '../message-add.tsx';
import type { MessageType } from '@/contexts/chat/types';
import NotesButton from '@/components/chat/notes-button.tsx';
import { NotesButtonVariant } from '../constants.ts';
import NotesDialog from '../dialog/notes-dialog';
import { useChat } from '@/hooks/use-chat';
import useIsMobile from '@/hooks/use-is-mobile.ts';

const ComposeView = (): ReactElement => {
  const { createMessage } = useChat();
  const [showNotes, setShowNotes] = useState<boolean>(false);
  const isMobile = useIsMobile();

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  return (
    <>
      <MessageAdd onSend={handleSendMessage} />
      <NotesButton
        handleClick={() => setShowNotes(true)}
        variant={
          isMobile ? NotesButtonVariant.CIRCULAR : NotesButtonVariant.EXTENDED
        }
      />
      <NotesDialog
        handleClose={() => setShowNotes(false)}
        showDialog={showNotes}
      />
    </>
  );
};

export default ComposeView;
