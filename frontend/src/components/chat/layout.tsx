import { ReactElement, ReactNode } from 'react';
import { AuthGuard } from '@/components/auth/auth-guard.tsx';
import Box from '@mui/material/Box';
import { Header } from './header.tsx';
import { Stack } from '@mui/system';
import { usePathname } from '@/hooks/use-pathname.ts';

type LayoutProps = {
  children: ReactNode;
};

const backgroundMap: Record<string, string> = {
  '/rules': '/assets/rules-background.jpeg',
  '/chat': '/assets/chat-background.jpeg',
};

export function Layout({ children }: LayoutProps): ReactElement {
  const pathname = usePathname();
  const background = backgroundMap[pathname] ?? '/assets/background.jpeg';

  return (
    <AuthGuard>
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
          overflow: 'hidden',
        }}
      >
        <Header />
        <Stack
          sx={{
            width: '70%',
            minWidth: '600px',
            paddingX: 7,
            height: 'calc(100vh - 62px)',
            overflowY: 'scroll',
          }}
          flexDirection='column'
          alignItems={'center'}
        >
          {children}
        </Stack>
      </Box>
    </AuthGuard>
  );
}
