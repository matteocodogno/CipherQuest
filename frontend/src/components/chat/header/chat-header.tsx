import { ForwardRefRenderFunction, forwardRef } from 'react';
import { Box } from '@mui/system';
import CardInfo from './card-info';
import CardTime from './card-time';
import { Typography } from '@mui/material';
import { useChat } from '@/hooks/use-chat';
import { useUser } from '@/hooks/use-user';

const ChatHeader: ForwardRefRenderFunction<HTMLDivElement, object> = (
  _props,
  ref,
) => {
  const { coins, level } = useChat();
  const { user } = useUser();

  return (
    <Box
      ref={ref}
      sx={{
        background: 'var(--mui-palette-background-paper)',
        borderRadius: '20px',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        flexShrink: 0,
        alignSelf: 'stretch',
        p: 3,
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
        <CardTime time={user?.createdAt} />
        <CardInfo svg='/assets/money.svg' value={coins.toString() ?? '0'} />
        <CardInfo svg='/assets/star.svg' value={level.toString() ?? '0'} />
      </Box>
    </Box>
  );
};

export default forwardRef(ChatHeader);
