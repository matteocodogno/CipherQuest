import { Avatar, Stack, Typography } from '@mui/material';
import { GameStatus } from '@/api/chat/types';
import { ScoreBoardButtonVariant } from '@/components/core/types';
import ScoreboardButton from '@/components/core/scoreboard-button';
import { useMemo } from 'react';

interface MessageEndGameProps {
  status?: GameStatus;
  score: number;
}

const MessageEndGame = ({ status, score }: MessageEndGameProps) => {
  const message = useMemo(() => {
    switch (status) {
      case GameStatus.WIN:
        return 'Great job, Overmind has been deactivated!';
      case GameStatus.GAME_OVER:
        return 'You are out of coins!';
      case GameStatus.CHEATED:
        return 'Mmmmh... it seems like you are a saboteur and this confirms to me that humanity still needs me! Bye';
      default:
        return 'You were unable to deactivate Overmind! try again';
    }
  }, [status]);

  return (
    <Stack
      sx={{
        marginTop: '100px',
        marginBottom: '200px',
        alignItems: 'center',
      }}
      gap={2}
    >
      <Avatar src='assets/game_over.jpg' sx={{ '--Avatar-size': '64px' }} />
      <Stack
        sx={{
          alignItems: 'center',
        }}
        gap={1}
      >
        <Typography
          color='inherit'
          variant='body1'
          style={{ whiteSpace: 'pre-wrap' }}
        >
          {message}
        </Typography>
        <Typography
          color='text.secondary'
          variant='body1'
          style={{ whiteSpace: 'pre-wrap' }}
        >
          {'Your score is: ' + score}
        </Typography>
      </Stack>
      <ScoreboardButton variant={ScoreBoardButtonVariant.CONTAINED} />
    </Stack>
  );
};

export default MessageEndGame;
