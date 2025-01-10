import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { DynamicLogo } from '@/components/core/logo';
import { ReactElement } from 'react';
import { RouterLink } from '@/components/core/link';
import { SignInForm } from './sign-in-form';
import { Trophy } from '@phosphor-icons/react';
import { paths } from '@/paths';

const SignInFormContainer = (): ReactElement => {
  return (
    <>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
        }}
      >
        <Button
          component={RouterLink}
          href={paths.game.score}
          variant='outlined'
          color='primary'
          endIcon={<Trophy />}
          style={{ marginBottom: '290px' }}
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
        gap={11}
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
        <SignInForm />
      </Box>
    </>
  );
};

export default SignInFormContainer;
