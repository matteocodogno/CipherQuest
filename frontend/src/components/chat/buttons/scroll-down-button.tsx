import { ArrowDown } from '@phosphor-icons/react';
import { Fab } from '@mui/material';

interface ScrollDownButtonProps {
  handleClick: () => void;
}

const ScrollDownButton = ({ handleClick }: ScrollDownButtonProps) => {
  return (
    <Fab
      variant='circular'
      color='secondary'
      size='large'
      onClick={handleClick}
      sx={{
        color: 'var(--mui-palette-primary-700)',
      }}
    >
      <ArrowDown size={24} />
    </Fab>
  );
};

export default ScrollDownButton;
