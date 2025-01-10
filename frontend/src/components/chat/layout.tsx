import { ReactElement, ReactNode } from 'react';
import Box from '@mui/material/Box';
import { Header } from './header.tsx';
import { Stack } from '@mui/material';
import useIsMobile from '@/hooks/use-is-mobile.ts';
import { usePathname } from '@/hooks/use-pathname.ts';

type LayoutProps = {
  children: ReactNode;
};

const backgroundMap: Record<string, string> = {
  '/rules': '/assets/rules-background.jpeg',
  '/chat': '/assets/chat-background.jpeg',
  '/score': '/assets/score-background.jpeg',
};

export function Layout({ children }: LayoutProps): ReactElement {
  const pathname = usePathname();
  const background = backgroundMap[pathname] ?? '/assets/background.jpeg';
  const isMobile = useIsMobile();

  return (
    <Box
      style={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundImage: `linear-gradient(0deg, rgba(9, 10, 11, 0.9) 0%, rgba(9, 10, 11, 0.9) 100%), url("${background}")`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
      }}
    >
      <Header />
      <Stack
        sx={{
          width: isMobile ? '100%' : '70%',
          minWidth: isMobile ? '200px' : '600px',
          paddingX: isMobile ? 0 : 7,
          height: 'calc(100vh - 62px)',
        }}
        flexDirection='column'
        alignItems={'center'}
      >
        {children}
      </Stack>
    </Box>
  );
}
