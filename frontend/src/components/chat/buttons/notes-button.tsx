import { Fab } from '@mui/material';
import { Notepad } from '@phosphor-icons/react';
import { NotesButtonVariant } from '../constants';
import useIsMobile from '@/hooks/use-is-mobile';

interface NotesButtonProps {
  handleClick: () => void;
  variant?: NotesButtonVariant;
}

const NotesButton = ({ handleClick, variant }: NotesButtonProps) => {
  const isMobile = useIsMobile();
  return (
    <Fab
      variant={variant}
      color='secondary'
      size='large'
      sx={{
        position: 'absolute',
        right: isMobile ? 24 : 32,
        bottom: isMobile ? 48 : 92,
        color: 'var(--mui-palette-primary-700)',
      }}
      onClick={handleClick}
    >
      {variant === 'extended' ? 'Block notes' : ''}
      <Notepad size={24} />
    </Fab>
  );
};

export default NotesButton;
