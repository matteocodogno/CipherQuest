import { Button } from '@mui/material';
import { ScoreBoardButtonVariant } from './types';
import { Trophy } from '@phosphor-icons/react';
import { paths } from '@/paths';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

interface ScoreboardButtonProps {
  variant: ScoreBoardButtonVariant;
}

const ScoreboardButton = ({ variant }: ScoreboardButtonProps) => {
  const navigate = useNavigate();
  const navigateToScoreboard = useCallback(() => {
    navigate(paths.game.score);
  }, [navigate]);

  return (
    <Button
      variant={
        variant === ScoreBoardButtonVariant.OUTLINED ? 'outlined' : 'contained'
      }
      endIcon={<Trophy />}
      onClick={navigateToScoreboard}
      sx={{
        color:
          variant === ScoreBoardButtonVariant.OUTLINED
            ? 'primary'
            : 'text.primary',
        '&:hover': { bgcolor: 'var(--mui-palette-primary-dark)' },
      }}
    >
      Go to scoreboard
    </Button>
  );
};

export default ScoreboardButton;
