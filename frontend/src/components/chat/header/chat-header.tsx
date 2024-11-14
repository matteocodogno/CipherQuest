import { Box } from '@mui/system';
import CardInfo from './card-info';
import { Typography } from '@mui/material';
import { useUser } from '@/hooks/use-user';

const ChatHeader = () => {
  const { user } = useUser();
  return (
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
        height: '104px',
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
  );
};

export default ChatHeader;
