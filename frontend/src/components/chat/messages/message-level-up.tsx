import { Box, Card, Typography } from '@mui/material';
import { ReactElement } from 'react';
import { Stack } from '@mui/system';

export const MessageLevelUp = (): ReactElement => {
  return (
    <Stack direction={'row-reverse'} spacing={2}>
      <Card
        sx={{
          p: 2,
          backgroundColor: 'transparent',
          border: '1px solid #15B79F',
          borderRadius: '8px',
          width: '360px',
        }}
      >
        <Stack direction={'row'} gap={1}>
          <Box
            component='img'
            src={'/assets/key.svg'}
            sx={{ height: '24px', width: 'auto' }}
          />
          <Typography
            color='#15B79F'
            variant='body1'
            style={{ whiteSpace: 'pre-wrap' }}
          >
            {'You’ve asked a key question'}
          </Typography>
        </Stack>
      </Card>
    </Stack>
  );
};

export default MessageLevelUp;
