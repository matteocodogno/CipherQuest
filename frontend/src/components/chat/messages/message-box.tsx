import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardMedia from '@mui/material/CardMedia';
import Link from '@mui/material/Link';
import type { Message } from '../types';
import { ReactElement } from 'react';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import type { User } from '@/types/user';
import { dayjs } from '@/lib/dayjs';

const user = {
  id: 9834759384,
  name: 'Sofia Rivers',
  username: 'sofia.rivers',
  level: 1,
  coins: 25,
  avatar: '/assets/avatar.png',
  email: 'sofia@devias.io',
  startedAt: new Date(),
} satisfies User;

export interface MessageBoxProps {
  message: Message;
}

export const MessageBox = ({ message }: MessageBoxProps): ReactElement => {
  const position = message.author.id === user.id ? 'right' : 'left';

  return (
    <Box
      sx={{
        alignItems: position === 'right' ? 'flex-end' : 'flex-start',
        flex: '0 0 auto',
        display: 'flex',
      }}
    >
      <Stack
        direction={position === 'right' ? 'row-reverse' : 'row'}
        spacing={2}
        sx={{
          alignItems: 'flex-start',
          maxWidth: '500px',
          ml: position === 'right' ? 'auto' : 0,
          mr: position === 'left' ? 'auto' : 0,
        }}
      >
        <Avatar src={message.author.avatar} sx={{ '--Avatar-size': '32px' }} />
        <Stack spacing={1} sx={{ flex: '1 1 auto' }}>
          <Card
            sx={{
              px: 2,
              py: 1,
              ...(position === 'right' && {
                bgcolor: 'var(--mui-palette-primary-main)',
                color: 'text.primary',
              }),
            }}
          >
            <Stack spacing={1}>
              <div>
                <Link
                  color='inherit'
                  sx={{ cursor: 'pointer' }}
                  variant='caption'
                >
                  {message.author.name}
                </Link>
              </div>
              {message.type === 'image' ? (
                <CardMedia
                  image={message.content}
                  onClick={() => {
                    // open modal
                  }}
                  sx={{ height: '200px', width: '200px' }}
                />
              ) : null}
              {message.type === 'text' ? (
                <Typography color='inherit' variant='body1'>
                  {message.content}
                </Typography>
              ) : null}
            </Stack>
          </Card>
          <Box
            sx={{
              display: 'flex',
              justifyContent: position === 'right' ? 'flex-end' : 'flex-start',
              px: 2,
            }}
          >
            <Typography color='text.secondary' noWrap variant='caption'>
              {dayjs(message.createdAt).fromNow()}
            </Typography>
          </Box>
        </Stack>
      </Stack>
    </Box>
  );
};
