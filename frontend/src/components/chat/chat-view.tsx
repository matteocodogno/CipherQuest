import { ReactElement, ReactNode } from 'react';
import Box from '@mui/material/Box';

export type ChatViewProps = {
  children: ReactNode;
};

export const ChatView = ({ children }: ChatViewProps): ReactElement => (
  <Box sx={{ display: 'flex', flex: '1 1 0', minHeight: 0, height: '100vh', width: '70vw' }}>
      {children}
  </Box>
);
