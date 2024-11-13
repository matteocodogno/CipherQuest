import { ReactElement, ReactNode } from 'react';
import Box from '@mui/material/Box';
import CardInfo from './card-info';
import { Stack } from '@mui/system';
import { Typography } from '@mui/material';
import { useUser } from '@/hooks/use-user';

export type ChatViewProps = {
  children: ReactNode;
};

export const ChatView = ({ children }: ChatViewProps): ReactElement => {
  const { user } = useUser();
  return (
    <Stack
      sx={{
        flex: '1 0 0',
        flexDirection: 'column',
        width: '70%',
        alignItems: 'center',
        paddingTop: 2,
        paddingLeft: 7,
        paddingRight: 7,
      }}
    >
      <Box
        sx={{
          background: '#121517',
          borderRadius: '20px',
          display: 'flex',
          flexDirection: 'row',
          p: 3,
          alignItems: 'center',
          flexShrink: 0,
          alignSelf: 'stretch',
        }}
      >
        <Typography variant='h4'>{user?.username}</Typography>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'end',
            flex: 1,
          }}
          gap={2}
        >
          <CardInfo svg='/assets/time.svg' value='88:88' />
          <CardInfo svg='/assets/money.svg' value='88' />
          <CardInfo svg='/assets/star.svg' value='8' />
        </Box>
      </Box>

      {children}
    </Stack>
  );
};
