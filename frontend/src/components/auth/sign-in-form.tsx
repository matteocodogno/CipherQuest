import { Controller, useForm } from 'react-hook-form';
import { ReactElement, useCallback, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Flag } from '@phosphor-icons/react';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import { authClient } from '@/lib/auth/custom/client';
import { useUser } from '@/hooks/use-user';
import { z as zod } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = zod.object({
  email: zod.string().email({ message: 'This email address is not valid.' }),
});

type Values = zod.infer<typeof schema>;

const defaultValues = { email: '' } satisfies Values;

export const SignInForm = (): ReactElement => {
  const { checkSession } = useUser();

  const [isPending, setIsPending] = useState<boolean>(false);

  const {
    control,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<Values>({ defaultValues, resolver: zodResolver(schema) });

  const onSubmit = useCallback(
    async (values: Values): Promise<void> => {
      setIsPending(true);

      const { error } = await authClient.signUp(values);

      if (error) {
        setError('root', { type: 'server', message: error });
        setIsPending(false);
        return;
      }

      // Refresh the auth state
      await checkSession?.();
    },
    [checkSession, setError],
  );

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
      <form onSubmit={handleSubmit(onSubmit)} style={{ width: '100%' }}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
          }}
          gap={2}
        >
          <Controller
            control={control}
            name='email'
            render={({ field }) => (
              <FormControl error={Boolean(errors.email)} fullWidth>
                <InputLabel>What is your email address?</InputLabel>
                <OutlinedInput {...field} type='email' placeholder='Email' />
                {errors.email ? (
                  <FormHelperText>{errors.email.message}</FormHelperText>
                ) : null}
              </FormControl>
            )}
          />
          {errors.root ? (
            <Alert color='error'>{errors.root.message}</Alert>
          ) : null}
          <FormHelperText sx={{ color: 'text.primary' }}>
            You’ll receive a unique ID code, keep it safe to claim your reward
            if you win.
          </FormHelperText>
          <Button
            disabled={isPending}
            type='submit'
            variant='contained'
            fullWidth
            endIcon={<Flag />}
            sx={{
              color: 'text.primary',
            }}
          >
            Join the rebels
          </Button>
        </Box>
      </form>
    </Box>
  );
};
