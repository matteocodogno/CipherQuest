import { ReactElement, useCallback, useState } from 'react';
import MessageAdd from '../message-add.tsx';
import type { MessageType } from '@/contexts/chat/types';
import NotesButton from '@/components/chat/notes-button.tsx';
import NotesDialog from '../dialog/notes-dialog';
import { useChat } from '@/hooks/use-chat';

const ComposeView = (): ReactElement => {
  const { createMessage } = useChat();
  const [showNotes, setShowNotes] = useState<boolean>(false);


  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  return (
    <>
      <MessageAdd onSend={handleSendMessage} />
      <NotesButton handleClick={() => setShowNotes(true)} />
      <NotesDialog
        handleClose={() => setShowNotes(false)}
        showDialog={showNotes}
      />
    </>
  );
};

export default ComposeView;
