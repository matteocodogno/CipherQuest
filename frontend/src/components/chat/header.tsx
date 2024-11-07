import Box from '@mui/material/Box';
import { DynamicLogo } from '@/components/core/logo.tsx';
import { ReactElement } from 'react';

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
  </Box>
);
