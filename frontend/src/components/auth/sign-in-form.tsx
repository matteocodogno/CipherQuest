import { Controller, useForm } from 'react-hook-form';
import { GoogleReCaptchaProvider, useGoogleReCaptcha } from 'react-google-recaptcha-v3';
import { ReactElement, useCallback, useRef, useState } from 'react';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { Flag } from '@phosphor-icons/react';
import FormControl from '@mui/material/FormControl';
import FormHelperText from '@mui/material/FormHelperText';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import ReCAPTCHA from 'react-google-recaptcha';
import { authClient } from '@/lib/auth/custom/client';
import { useUser } from '@/hooks/use-user';
import { z as zod } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = zod.object({
  email: zod.string().email({ message: 'This email address is not valid.' }),
});

type Values = zod.infer<typeof schema>;

const defaultValues = { email: '' } satisfies Values;

const InnerSignInForm = (): ReactElement => {
  const { checkSession } = useUser();
  const { executeRecaptcha } = useGoogleReCaptcha();

  const v2Ref = useRef<ReCAPTCHA>(null);
  const [isPending, setIsPending] = useState<boolean>(false);
  const [showV2, setShowV2] = useState<boolean>(false);
  const [v2Token, setV2Token] = useState<string>('');

  const {
    control,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<Values>({ defaultValues, resolver: zodResolver(schema) });

  const onSubmit = useCallback(
    async (values: Values): Promise<void> => {
      setIsPending(true);
      try {
        if (!executeRecaptcha) {
          setError('root', {
            type: 'server',
            message: 'reCAPTCHA not yet available. Please try again.',
          });
          return;
        }

        if (!showV2) {
          const v3token = await executeRecaptcha('login');
          const res = await authClient.signUp({
            ...values,
            recaptchaToken: v3token,
            recaptchaVersion: 'v3',
          });

          if (res.error === 'RECAPTCHA_V2_REQUIRED') {
            setShowV2(true);
            setError('root', {
              type: 'server',
              message: 'Please verify the reCAPTCHA checkbox to continue.',
            });
            return;
          }

          if (res.error) {
            setError('root', { type: 'server', message: res.error });
            return;
          }
        } else {
          if (!v2Token) {
            setError('root', {
              type: 'server',
              message: 'Please check the reCAPTCHA box first.',
            });
            return;
          }

          const res = await authClient.signUp({
            ...values,
            recaptchaToken: v2Token,
            recaptchaVersion: 'v2',
          });

          if (res.error) {
            if (/exists/i.test(res.error)) {
              v2Ref.current?.reset();
              setV2Token('');
              setError('root', {
                type: 'server',
                message:
                  'Email address already exists. Use another address and re-check the reCAPTCHA.',
              });
              return;
            }

            v2Ref.current?.reset();
            setV2Token('');
            setError('root', { type: 'server', message: res.error });
            return;
          }
        }

        await checkSession?.();
      } finally {
        setIsPending(false);
      }
    },
    [checkSession, executeRecaptcha, setError, showV2, v2Token]
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
          {showV2 && (
            <ReCAPTCHA
              ref={v2Ref}
              sitekey={import.meta.env.VITE_RECAPTCHA_V2_SITE_KEY as string} // v2 Checkbox site key
              onChange={(token) => setV2Token(token ?? '')}
              onExpired={() => setV2Token('')}
              onErrored={() =>
                setError('root', {
                  type: 'server',
                  message: 'reCAPTCHA failed. Please try again.',
                })
              }
            />
          )}

          {errors.root ? <Alert color='error'>{errors.root.message}</Alert> : null}

          <FormHelperText sx={{ color: 'text.primary', whiteSpace: 'pre-wrap' }}>
            You’ll receive a unique ID code, keep it safe to claim your reward if you win.{'\n'}
            Your email will not be retained or used for anything other than sending this unique code.
          </FormHelperText>

          <Button
            disabled={isPending}
            type='submit'
            variant='contained'
            fullWidth
            endIcon={<Flag />}
            sx={{ color: 'text.primary' }}
          >
            {showV2 ? 'Verify & Continue' : 'Join the rebels'}
          </Button>
        </Box>
      </form>
    </Box>
  );
};

export const SignInForm = (): ReactElement => {
  return (
    <GoogleReCaptchaProvider reCaptchaKey={import.meta.env.VITE_RECAPTCHA_V3_SITE_KEY as string}>
      <InnerSignInForm />
    </GoogleReCaptchaProvider>
  );
};
