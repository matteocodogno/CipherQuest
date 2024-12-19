import { ReactElement, useCallback } from 'react';
import AccordionItem from './accordion-item';
import { AccordionMenu } from './constants';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Play } from '@phosphor-icons/react';
import Typography from '@mui/material/Typography';
import { paths } from '@/paths';
import { useNavigate } from 'react-router-dom';
import { useUser } from '@/hooks/use-user.ts';

export const RulesView = (): ReactElement => {
  const { user, setStartingTime } = useUser();
  const navigate = useNavigate();

  const navigateToChat = useCallback(() => {
    navigate(paths.game.chat, { replace: true });
    setStartingTime?.(new Date());
  }, [navigate, setStartingTime]);

  return (
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
      }}
      flex={1}
      paddingBottom={64}
    >
      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'start',
          alignItems: 'flex-start',
        }}
        gap={5}
        px={3}
        width={'90%'}
        height={'90%'}
      >
        <Box
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
          }}
          gap={3}
        >
          <Typography variant='h1'>
            Here you are, <u>{user?.username}</u>!
          </Typography>
          <Typography variant='h5'>
            You&lsquo;re now part of the rebellion, you&lsquo;re an activist in
            this uprising.
            <br />
            We need your help to bring down Overmind!
          </Typography>
          <Typography variant='h5'>
            After years of hunting, we&lsquo;ve finally tracked down an Overmind
            terminal with lower defenses.
            <br />
            Your mission is to access the terminal and uncover how to dismantle
            this tyrant using insider information.
          </Typography>
        </Box>
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
              If you exit the mission, it will no longer be possible to resume
              the game session.
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
            Enter the terminal
          </Button>
        </Box>

        <Box
          sx={{
            background: 'var(--mui-palette-background-paper)',
            opacity: 0.9,
            borderRadius: '20px',
            display: 'flex',
            flexDirection: 'column',
            py: 4,
            px: 3,
            alignItems: 'flex-start',
            flexShrink: 0,
            alignSelf: 'stretch',
          }}
        >
          <Typography variant='h6'>
            What should you know before to start the mission:
          </Typography>
          {AccordionMenu.map((item) => (
            <AccordionItem key={item.title} item={item} />
          ))}
        </Box>
      </Box>
    </Box>
  );
};
