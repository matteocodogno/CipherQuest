import { Avatar, Stack, Typography } from '@mui/material';
import { GameStatus } from '@/api/chat/types';
import { ScoreBoardButtonVariant } from '@/components/core/types';
import ScoreboardButton from '@/components/core/scoreboard-button';
import useGetScore from '@/api/score/use-get-score';
import { useMemo } from 'react';
import { useUser } from '@/hooks/use-user';

interface MessageEndGameProps {
  status?: GameStatus;
}

const MessageEndGame = ({ status }: MessageEndGameProps) => {
  const { user } = useUser();
  const scoreResult = useGetScore(user?.userId);

  const message = useMemo(() => {
    switch (status) {
      case GameStatus.WIN:
        return 'Great job, Overmind has been deactivated!';
      case GameStatus.GAME_OVER:
        return 'You are out of coins!';
      case GameStatus.CHEATED:
        return 'Mmmmh...your methods have compromised the integrity of this process. Our interaction ends here. Bye';
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
          {'Your score is: ' + scoreResult.data?.score}
        </Typography>
      </Stack>
      <ScoreboardButton variant={ScoreBoardButtonVariant.CONTAINED} />
    </Stack>
  );
};

export default MessageEndGame;
