import { Fab } from '@mui/material';
import { Notepad } from '@phosphor-icons/react';

const NotesButton = ({ handleClick }: { handleClick: () => void}) => (
  <Fab
    variant='extended'
    color='secondary'
    size='large'
    sx={{
      position: 'absolute',
      right: 32,
      bottom: 92,
      color: 'var(--mui-palette-primary-700)',
    }}
    onClick={handleClick}
  >
    Block note
    <Notepad size={24} />
  </Fab>
);

export default NotesButton;
