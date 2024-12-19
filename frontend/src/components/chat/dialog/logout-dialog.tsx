import { Button, Stack, Typography } from '@mui/material';
import ContentDialog from './content-dialog';
import { authClient } from '@/lib/auth/custom/client';
import { paths } from '@/paths';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

interface LogoutDialogProps {
  handleClose: () => void;
}

const LogoutDialog = ({ handleClose }: LogoutDialogProps) => {
  const navigate = useNavigate();
  const logoutAction = useCallback(async () => {
    await authClient.signOut();
    handleClose();
    navigate(paths.auth.custom.signIn, { replace: true });
  }, [handleClose, navigate]);

  return (
    <>
      <ContentDialog
        title={'Do you want to end the game?'}
        closeDialog={handleClose}
      >
        <Stack gap={4}>
          <Typography>
            If you exit the game it will no longer be possible to recover what
            you have done up to now.
          </Typography>
          <Stack
            direction={'row'}
            justifyContent='end'
            alignItems='end'
            gap={1}
          >
            <Button
              variant='text'
              sx={{ color: 'text.secondary' }}
              onClick={handleClose}
            >
              Back
            </Button>
            <Button
              sx={{
                color: 'text.primary',
                backgroundColor: 'var(--mui-palette-error-dark)',
                '&:hover': { bgcolor: 'var(--mui-palette-error-main)' },
              }}
              onClick={logoutAction}
            >
              Yes, end game
            </Button>
          </Stack>
        </Stack>
      </ContentDialog>
    </>
  );
};

export default LogoutDialog;
