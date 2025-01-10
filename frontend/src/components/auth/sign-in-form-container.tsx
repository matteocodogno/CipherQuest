import Box from '@mui/material/Box';
import { DynamicLogo } from '@/components/core/logo';
import { ReactElement } from 'react';
import { RouterLink } from '@/components/core/link';
import { ScoreBoardButtonVariant } from '../core/types';
import ScoreboardButton from '../core/scoreboard-button';
import { SignInForm } from './sign-in-form';
import { paths } from '@/paths';

const SignInFormContainer = (): ReactElement => {
  return (
    <>
      <ScoreboardButton variant={ScoreBoardButtonVariant.OUTLINED} />

      <Box
        style={{
          marginTop: '290px',
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
