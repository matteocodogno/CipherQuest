import { Box, Stack, Typography } from '@mui/material';
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

  const { message, asset } = useMemo(() => {
    switch (status) {
      case GameStatus.WIN:
        return {
          message: 'Great job, Overmind has been deactivated!',
          asset: 'assets/winner.svg',
        };
      case GameStatus.GAME_OVER:
        return {
          message: 'You are out of coins!',
          asset: 'assets/game-over.svg',
        };
      case GameStatus.CHEATED:
        return {
          message:
            'Mmmmh...your methods have compromised the integrity of this process. Our interaction ends here. Bye',
          asset: 'assets/game-over.svg',
        };
      default:
        return {
          message: 'You were unable to deactivate Overmind! try again',
          asset: 'assets/game-over.svg',
        };
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
      <Box component='img' src={asset} />
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
