import { Controller, useForm } from 'react-hook-form';
import { ReactElement, useCallback, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { DynamicLogo } from '@/components/core/logo';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import { RouterLink } from '@/components/core/link';
import SignInDescription from './sign-in-description';
import { Trophy } from '@phosphor-icons/react';
import { authClient } from '@/lib/auth/custom/client';
import { paths } from '@/paths';
import useIsMobile from '@/hooks/use-is-mobile';
import { useUser } from '@/hooks/use-user';
import { z as zod } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = zod.object({
  username: zod.string().min(3, { message: 'Username is required' }),
});

type Values = zod.infer<typeof schema>;

const defaultValues = { username: '' } satisfies Values;

export const SignInForm = (): ReactElement => {
  const { checkSession } = useUser();
  const isMobile = useIsMobile();

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
    <>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
          px: isMobile ? 3 : 0,
          py: isMobile ? 4 : 0,
        }}
      >
        <Button
          component={RouterLink}
          href={paths.game.score}
          variant='outlined'
          color='primary'
          endIcon={<Trophy />}
          style={{ marginBottom: isMobile ? '24px' : '290px' }}
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
        gap={isMobile ? 4 : 11}
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
        {isMobile && <SignInDescription />}
        <Box
          sx={{
            p: isMobile ? 3 : 0,
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
              >
                Join the rebels 🚀
              </Button>
            </Box>
          </form>
        </Box>
      </Box>
    </>
  );
};
