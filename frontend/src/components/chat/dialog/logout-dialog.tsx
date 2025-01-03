import { Button, Stack, Typography } from '@mui/material';
import Dialog from '@/components/core/dialog.tsx';
import { paths } from '@/paths';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useUser } from '@/hooks/use-user';

interface LogoutDialogProps {
  handleClose: () => void;
}

const LogoutDialog = ({ handleClose }: LogoutDialogProps) => {
  const navigate = useNavigate();
  const { signOut } = useUser();
  const logoutAction = useCallback(async () => {
    await signOut?.();
    handleClose();
    navigate(paths.auth.custom.signIn, { replace: true });
  }, [handleClose, navigate, signOut]);

  return (
    <>
      <Dialog
        title={'Do you want to end the mission?'}
        closeDialog={handleClose}
      >
        <Stack gap={4}>
          <Typography>
            If you exit the game, your progress will be permanently lost and
            cannot be recovered.
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
                '&:hover': { bgcolor: 'var(--mui-palette-error-hovered)' },
              }}
              onClick={logoutAction}
            >
              Yes, I understand.
            </Button>
          </Stack>
        </Stack>
      </Dialog>
    </>
  );
};

export default LogoutDialog;
