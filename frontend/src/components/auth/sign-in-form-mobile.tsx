import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { DynamicLogo } from '@/components/core/logo';
import { ReactElement } from 'react';
import { RouterLink } from '@/components/core/link';
import SignInDescription from './sign-in-description';
import { SignInForm } from './sign-in-form';
import { Trophy } from '@phosphor-icons/react';
import { paths } from '@/paths';

const SignInMobile = (): ReactElement => {
  return (
    <Box>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
          px: 3,
          py: 4,
        }}
      >
        <Button
          component={RouterLink}
          href={paths.game.score}
          variant='outlined'
          color='primary'
          endIcon={<Trophy />}
          style={{ marginBottom: '24px' }}
        >
          Go to scoreboard
        </Button>
      </Box>

      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          alignSelf: 'stretch',
        }}
        gap={4}
      >
        <Box sx={{ margin: 'auto' }}>
          <Box
            component={RouterLink}
            href={paths.home}
            sx={{
              display: 'inline-block',
              fontSize: 0,
            }}
          >
            <DynamicLogo
              colorDark='light'
              colorLight='dark'
              height={96}
              width={366}
            />
          </Box>
        </Box>
        <SignInDescription />
      </Box>
      <Box
        sx={{
          background: 'var(--mui-palette-background-paper)',
          borderTopLeftRadius: '20px',
          borderTopRightRadius: '20px',
          px: 3,
          paddingTop: 2,
          paddingBottom: 5,
          alignItems: 'center',
          justifyContent: 'center',
          position: 'sticky',
          bottom: 0,
        }}
        marginBottom={1}
      >
        <SignInForm />
      </Box>
    </Box>
  );
};

export default SignInMobile;
