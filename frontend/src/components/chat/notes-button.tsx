import { Fab } from '@mui/material';
import { Notepad } from '@phosphor-icons/react';

interface NotesButtonProps {
  handleClick: () => void;
  variant?: 'circular' | 'extended';
}

const NotesButton = ({ handleClick, variant }: NotesButtonProps) => (
  <Fab
    variant={variant}
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
    {variant === 'extended' ? 'Block note' : ''}
    <Notepad size={24} />
  </Fab>
);

export default NotesButton;
