import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Play } from '@phosphor-icons/react';
import { ReactElement } from 'react';
import Typography from '@mui/material/Typography';
import { useUser } from '@/hooks/use-user.ts';
import { RouterLink } from '@/components/core/link.tsx';

export const RulesView = (): ReactElement => {
  const { user } = useUser();

  return (
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'flex-start',
      }}
      gap={5}
      px={3}
      paddingTop={10}
      paddingBottom={8}
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
        <Typography variant='h1'>Welcome <u>{user?.username}</u> among the rebels!</Typography>
        <Typography variant='h5'>
          You are now part of the rebels, specifically the activists in this uprising. We need your help to shut down Overmind.
        </Typography>
        <Typography variant='h5'>
          Your mission is to engage with the artificial intelligence, asking questions to persuade it to deactivate itself.
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
        <Typography variant='h6'>Are you ready?</Typography>
        <Button
          component={RouterLink}
          href='/'
          variant='contained'
          color='primary'
          endIcon={<Play />}
        >Start the mission</Button>
      </Box>
    </Box>
  );
};
