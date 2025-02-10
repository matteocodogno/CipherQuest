import {
  Alert,
  Box,
  Button,
  FormControl,
  FormHelperText,
  InputLabel,
  OutlinedInput,
} from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import { useCallback, useState } from 'react';
import { Flag } from '@phosphor-icons/react';
import { authClient } from '@/lib/auth/custom/client';
import { mapEmailSignupErrors } from '@/utils/errors';
import { useUser } from '@/hooks/use-user';
import { z as zod } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = zod.object({
  username: zod.string().email({ message: 'This email address is not valid.' }),
});

type Values = zod.infer<typeof schema>;

const defaultValues = { username: '' } satisfies Values;

const FormEmailInput = () => {
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
        setError('root', {
          type: 'server',
          message: mapEmailSignupErrors(error),
        });
        setIsPending(false);
        return;
      }

      // Refresh the auth state
      await checkSession?.();
    },
    [checkSession, setError],
  );
  return (
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
          name='username'
          render={({ field }) => (
            <FormControl error={Boolean(errors.username)} fullWidth>
              <InputLabel>What is your email address?</InputLabel>
              <OutlinedInput {...field} type='username' placeholder='Email' />
              {errors.username ? (
                <FormHelperText>{errors.username.message}</FormHelperText>
              ) : null}
            </FormControl>
          )}
        />
        {errors.root ? (
          <Alert color='error'>{errors.root.message}</Alert>
        ) : null}
        <FormHelperText sx={{ color: 'text.primary', whiteSpace: 'pre-wrap' }}>
          You’ll receive a unique ID code, keep it safe to claim your reward if
          you win.{'\n'}
          Your email will not be retained or used for anything other than
          sending this unique code.
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
  );
};

export default FormEmailInput;
