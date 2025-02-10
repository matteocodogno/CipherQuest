import Box from '@mui/material/Box';
import FormEmailInput from './form-email-input';
import FormUsernameInput from './form-username-input';
import { ReactElement } from 'react';
import { useFeatures } from '@/hooks/use-features';

export const SignInForm = (): ReactElement => {
  const { features } = useFeatures();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        gap: 2,
        alignSelf: 'stretch',
      }}
    >
      {features?.sendEmail ? <FormEmailInput /> : <FormUsernameInput />}
    </Box>
  );
};
