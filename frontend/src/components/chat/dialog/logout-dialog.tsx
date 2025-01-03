import { Button, Stack, Typography } from '@mui/material';
import Dialog from '@/components/core/dialog.tsx';
import { authClient } from '@/lib/auth/custom/client.ts';
import { logger } from '@/lib/default-loggger.ts';
import { toast } from '@/components/core/toaster.tsx';
import { useCallback } from 'react';
import { useUser } from '@/hooks/use-user.ts';

interface LogoutDialogProps {
  handleClose: () => void;
}

const LogoutDialog = ({ handleClose }: LogoutDialogProps) => {
  const { checkSession } = useUser();

  const handleSignOut = useCallback(async () => {
    try {
      const { error } = await authClient.signOut();

      if (error) {
        logger.error('Sign out error', error);
        toast.error('Something went wrong, unable to sign out');
        return;
      }

      // Refresh the auth state
      await checkSession?.();
      // After refresh, GuestGuard will handle the redirect
    } catch (err) {
      logger.error('Sign out error', err);
      toast.error('Something went wrong, unable to sign out');
    }
  }, [checkSession]);

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
              onClick={handleSignOut}
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
