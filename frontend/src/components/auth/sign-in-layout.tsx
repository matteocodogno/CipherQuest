import Box from '@mui/material/Box';
import { ReactElement } from 'react';
import SignInDescription from './sign-in-description';
import SignInFormContainer from './sign-in-form-container';
import SignInMobile from './mobile/sign-in-form-mobile';
import useIsMobile from '@/hooks/use-is-mobile';

export const SignInLayout = (): ReactElement => {
  const isMobile = useIsMobile();

  return (
    <Box
      sx={{
        background:
          'radial-gradient(84.81% 47.71% at 52.29% 50%, rgba(9, 10, 11, 0.30) 0%, rgba(9, 10, 11, 0.90)' +
          ' 100%), url("/assets/login-background.jpeg") lightgray 50% / cover no-repeat;',
      }}
    >
      {isMobile ? (
        <SignInMobile />
      ) : (
        <Box
          sx={{
            display: 'flex',
            alignItems: 'flex-start',
            minHeight: '100%',
            height: '100vh',
          }}
        >
          <SignInDescription />
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              minWidth: '600px',
              py: 4,
              px: 8,
              alignItems: 'flex-end',
              flexShrink: 0,
              alignSelf: 'stretch',
              border: '1px solid #000',
              background:
                'var(--background-paper-glass, rgba(18, 21, 23, 0.90))',
              boxShadow: 'var(--mui-shadows-8)',
            }}
          >
            <SignInFormContainer />
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default SignInLayout;
