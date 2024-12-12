import { Box } from '@mui/system';
import CardInfo from './card-info';
import CardTime from './card-time';
import { Typography } from '@mui/material';
import { useUser } from '@/hooks/use-user';

const ChatHeader = () => {
  const { user } = useUser();

  return (
    <Box
      sx={{
        background: 'var(--mui-palette-background-paper)',
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
        <CardTime time={user?.startedAt} />
        <CardInfo
          svg='/assets/money.svg'
          value={user?.coins?.toString() ?? '0'}
        />
        <CardInfo
          svg='/assets/star.svg'
          value={user?.level?.toString() ?? '0'}
        />
      </Box>
    </Box>
  );
};

export default ChatHeader;
