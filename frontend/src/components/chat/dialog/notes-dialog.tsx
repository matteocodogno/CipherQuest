import 'react-quill/dist/quill.snow.css';
import './notes-dialog.css';
import { useCallback, useState } from 'react';
import { Box } from '@mui/system';
import ContentDialog from './content-dialog';
import ReactQuill from 'react-quill';

interface NotesDialogProps {
  handleClose: () => void;
  showDialog: boolean;
}

const NotesDialog = ({ handleClose, showDialog }: NotesDialogProps) => {
  const [content, setContent] = useState('');

  const saveNotes = useCallback(() => {
    console.log({ content });
    handleClose();
  }, [content, handleClose]);

  return (
    <ContentDialog
      title='Notes'
      closeDialog={saveNotes}
      showDialog={showDialog}
    >
      <Box marginBottom={6}>
        <ReactQuill
          value={content}
          onChange={(value) => {
            setContent(value);
          }}
          style={{ minWidth: '600px', height: '400px', color: 'white' }}
          className='react-quill'
        />
      </Box>
    </ContentDialog>
  );
};

export default NotesDialog;
