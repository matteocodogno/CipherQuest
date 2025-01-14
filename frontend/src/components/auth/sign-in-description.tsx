import { Box, Stack, Typography } from '@mui/material';
import { dayjs } from '@/lib/dayjs.ts';
import useIsMobile from '@/hooks/use-is-mobile';

const SignInDescription = () => {
  const isMobile = useIsMobile();
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        flex: '1 0 0',
        alignSelf: 'stretch',
        py: 4,
        px: 3,
      }}
    >
      <Stack spacing={4} sx={{ maxWidth: '700px' }}>
        <Stack spacing={1}>
          <Typography variant={isMobile ? 'h6' : 'h5'}>
            Today is {dayjs().format('MMMM D,')} 2154
          </Typography>
          <Typography variant={isMobile ? 'h3' : 'h1'}>
            Humanity and technology coexist thanks to the effort of the
            Pan-Terra Initiative.
          </Typography>
          <Typography variant={isMobile ? 'body1' : 'h5'}>
            It has been created an artificial intelligence with the mission of
            ensuring the survival and prosperity of the human species, even if
            that means overriding traditional ethical constraints.
          </Typography>
          <Typography variant={isMobile ? 'body2' : 'body1'}>
            The artificial intelligence has started behaving in dark and
            oppressive ways. In response, a group of rebels, including
            scientists, activists, and everyday citizens, joined to combat this
            threat. They plan acts of sabotage to disable the AI and reclaim
            their freedom, while striving to reconnect with core human values.
            Their fight is both a battle for survival and a quest for redemption
            and hope for a better future.
          </Typography>
        </Stack>
      </Stack>
    </Box>
  );
};

export default SignInDescription;
