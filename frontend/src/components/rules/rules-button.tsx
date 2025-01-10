import { Box, Button, Typography } from '@mui/material';
import { Play } from '@phosphor-icons/react';
import { paths } from '@/paths';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

const RulesButton = () => {
  const navigate = useNavigate();

  const navigateToChat = useCallback(() => {
    navigate(paths.game.chat, { replace: true });
  }, [navigate]);
  return (
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        alignSelf: 'stretch',
      }}
      gap={2}
    >
      <Box
        gap={1}
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          alignSelf: 'stretch',
        }}
      >
        <Typography variant='h6'>Are you ready?</Typography>
        <Typography variant='body1'>
          If you quit the mission, it will no longer be possible to resume the
          game session.
        </Typography>
      </Box>
      <Button
        onClick={navigateToChat}
        variant='contained'
        color='primary'
        endIcon={<Play />}
        sx={{
          color: 'text.primary',
        }}
      >
        Enter the Terminal
      </Button>
    </Box>
  );
};

export default RulesButton;
