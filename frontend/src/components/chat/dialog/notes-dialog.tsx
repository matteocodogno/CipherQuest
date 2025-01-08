import 'react-quill/dist/quill.snow.css';
import './notes-dialog.css';
import { getGameNotes, saveGameNotes } from '@/lib/game/localStore';
import { useCallback, useState } from 'react';
import { Box } from '@mui/system';
import Dialog from '@/components/core/dialog.tsx';
import ReactQuill from 'react-quill';
import useIsMobile from '@/hooks/use-is-mobile';
import useOnMount from '@mui/utils/useOnMount';

interface NotesDialogProps {
  handleClose: () => void;
  showDialog: boolean;
}

const NotesDialog = ({ handleClose, showDialog }: NotesDialogProps) => {
  const [content, setContent] = useState('');
  const isMobile = useIsMobile();

  useOnMount(() => {
    const gameNotes = getGameNotes();
    if (gameNotes) {
      setContent(gameNotes);
    }
  });

  const saveNotes = useCallback(() => {
    console.log({ content });
    saveGameNotes(content);
    handleClose();
  }, [content, handleClose]);

  return (
    <Dialog
      title='Notes'
      closeDialog={saveNotes}
      showDialog={showDialog}
      fullScreen={isMobile}
    >
      <Box marginBottom={6}>
        <ReactQuill
          value={content}
          onChange={(value) => {
            setContent(value);
          }}
          style={{
            minWidth: isMobile ? '200px' : '400px',
            height: isMobile ? '500px' : '400px',
            color: 'white',
          }}
          className='react-quill'
        />
      </Box>
    </Dialog>
  );
};

export default NotesDialog;
