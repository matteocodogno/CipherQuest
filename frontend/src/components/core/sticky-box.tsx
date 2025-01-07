import { Box } from '@mui/material';
import { ReactNode } from 'react';

type StickyBoxProps = {
  children: ReactNode;
};
const StickyBox = ({ children }: StickyBoxProps) => {
  return (
    <Box
      sx={{
        background: 'var(--mui-palette-background-paper)',
        borderTopLeftRadius: '20px',
        borderTopRightRadius: '20px',
        px: 3,
        paddingTop: 2,
        paddingBottom: 5,
        alignItems: 'center',
        justifyContent: 'center',
        position: 'sticky',
        bottom: 0,
      }}
      marginBottom={1}
    >
      {children}
    </Box>
  );
};

export default StickyBox;
