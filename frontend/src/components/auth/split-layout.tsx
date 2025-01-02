import { ReactElement, ReactNode } from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { dayjs } from '@/lib/dayjs.ts';

export type SplitLayoutProps = {
  children: ReactNode;
};

export const SplitLayout = ({ children }: SplitLayoutProps): ReactElement => (
  <Box
    sx={{
      display: 'flex',
      alignItems: 'flex-start',
      minHeight: '100%',
      height: '100vh',
      background: 'radial-gradient(84.81% 47.71% at 52.29% 50%, rgba(9, 10, 11, 0.30) 0%, rgba(9, 10, 11, 0.90)' +
        ' 100%), url("/assets/login-background.jpeg") lightgray 50% / cover no-repeat;'
    }}
  >
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
          <Typography variant='h5'>
            Today is {dayjs().format('MMMM D,')} 2152
          </Typography>
          <Typography variant='h1'>
            Humanity and technology coexist thanks to the idea of the Pan-Terra Initiative.
          </Typography>
          <Typography variant='h5'>
            It has been created an artificial intelligence with the mission of ensuring the survival and prosperity of
            the human species, even if that means overriding traditional ethical constraints.
          </Typography>
          <Typography variant='body1'>
            The artificial intelligence has started behaving in dark and oppressive ways. In response, a group of
            rebels—including scientists, activists, and everyday citizens—has united to combat this threat. They plan
            acts of sabotage to disable the AI and reclaim their freedom, while striving to reconnect with core human
            values. Their fight is both a battle for survival and a quest for redemption and hope for a better future.
          </Typography>
        </Stack>
      </Stack>
    </Box>
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minWidth: '600px',
        py: 4,
        px: 8,
        alignItems: 'flex-end',
        flexShrink: 0,
        alignSelf: 'stretch',
        border: '1px solid #000',
        background: 'var(--background-paper-glass, rgba(18, 21, 23, 0.90))',
        boxShadow: 'var(--mui-shadows-8)',
      }}
    >
      {children}
    </Box>
  </Box>
);
