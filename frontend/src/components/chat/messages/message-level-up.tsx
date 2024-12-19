import { Box, Card, Typography } from '@mui/material';
import { ReactElement } from 'react';
import { Stack } from '@mui/system';

export const MessageLevelUp = (): ReactElement => {
  return (
    <Stack direction={'row-reverse'} spacing={2}>
      <Card
        sx={{
          px: 2,
          py: 1,
          backgroundColor: 'transparent',
          border: '1px solid var(--mui-palette-success-main)',
          borderRadius: '8px',
          width: '300px',
        }}
      >
        <Stack direction={'row'} gap={1}>
          <Box
            component='img'
            src={'/assets/key.svg'}
            sx={{ height: '24px', width: 'auto' }}
          />
          <Typography
            color='var(--mui-palette-success-main)'
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
