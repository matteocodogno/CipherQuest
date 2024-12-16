import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardMedia from '@mui/material/CardMedia';

import type { Message } from '../types';
import MessageLevelUp from './message-level-up';
import { ReactElement } from 'react';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { dayjs } from '@/lib/dayjs';
import { useUser } from '@/hooks/use-user';
import Link from '@mui/material/Link';
import { Button } from '@mui/material';

export interface MessageBoxProps {
  message: Message;
  showModal: () => void;
}

export const MessageBox = ({
  message,
  showModal,
}: MessageBoxProps): ReactElement => {
  const { user } = useUser();

  const position = message.author.id === user?.userId ? 'right' : 'left';

  return (
    <Stack gap={4}>
      {message.isLevelUp && <MessageLevelUp />}

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
          maxWidth={position === 'right' ? '35%' : '70%'}
          sx={{
            alignItems: 'flex-start',
            ml: position === 'right' ? 'auto' : 0,
            mr: position === 'left' ? 'auto' : 0,
          }}
        >
          <Avatar
            src={message.author.avatar}
            sx={{ '--Avatar-size': '32px' }}
          />
          <Stack spacing={1} sx={{ flex: '1 1 auto' }}>
            <Card
              sx={{
                p: 2,
                ...(position === 'right' && {
                  background: 'var(--mui-palette-background-level3)',
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
                  <Stack
                    sx={{
                      alignItems: 'flex-start',
                    }}
                    gap={1}
                  >
                    <Typography
                      color='inherit'
                      variant='body1'
                      style={{ whiteSpace: 'pre-wrap' }}
                    >
                      {message.content}
                    </Typography>
                    <Stack sx={{ flexDirection: 'row' }} alignItems={'center'}>
                      <Typography
                        color='inherit'
                        variant='body1'
                        style={{ whiteSpace: 'pre-wrap' }}
                      >
                        Sources:
                      </Typography>
                      <Button onClick={showModal}>Navigate</Button>
                    </Stack>
                  </Stack>
                ) : null}
              </Stack>
            </Card>
            <Box
              sx={{
                display: 'flex',
                justifyContent:
                  position === 'right' ? 'flex-end' : 'flex-start',
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
    </Stack>
  );
};
