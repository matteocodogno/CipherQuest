import { Button, Divider } from '@mui/material';
import Box from '@mui/material/Box';
import { DynamicLogo } from '@/components/core/logo.tsx';
import { ReactElement } from 'react';
import { Stack } from '@mui/system';

export const Header = (): ReactElement => (
  <Box
    style={{
      display: 'flex',
      alignItems: 'center',
      alignSelf: 'stretch',
    }}
    px={3}
    py={1}
  >
    <DynamicLogo width={164} height={46} />
    <Stack
      flex={1}
      direction={'row'}
      justifyContent='end'
      alignItems='end'
      gap={1}
    >
      <Button
        variant='text'
        endIcon={<Box component='img' src={'/assets/mission-rules.svg'} />}
      >
        Mission rules
      </Button>
      <Divider orientation='vertical' variant='middle' flexItem />
      <Button
        variant='text'
        endIcon={<Box component='img' src={'/assets/logout.svg'} />}
        sx={{ color: 'var(--mui-palette-error-dark)' }}
      >
        End mission
      </Button>
    </Stack>
  </Box>
);
