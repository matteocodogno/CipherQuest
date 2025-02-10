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
import { mapUsernameSignupErrors } from '@/utils/errors';
import { useUser } from '@/hooks/use-user';
import { z as zod } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = zod.object({
  username: zod.string().min(3, { message: 'Username is required' }),
});

type Values = zod.infer<typeof schema>;

const defaultValues = { username: '' } satisfies Values;

const FormUsernameInput = () => {
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
          message: mapUsernameSignupErrors(error),
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
              <InputLabel>What would you like to be called?</InputLabel>
              <OutlinedInput
                {...field}
                type='username'
                placeholder='Username'
              />
              {errors.username ? (
                <FormHelperText>{errors.username.message}</FormHelperText>
              ) : null}
            </FormControl>
          )}
        />
        {errors.root ? (
          <Alert color='error'>{errors.root.message}</Alert>
        ) : null}
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

export default FormUsernameInput;
