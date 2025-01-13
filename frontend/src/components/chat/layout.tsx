import { ReactElement, ReactNode, useEffect } from 'react';
import Box from '@mui/material/Box';
import { Header } from './header.tsx';
import { Stack } from '@mui/material';
import { paths } from '@/paths.ts';
import useIsMobile from '@/hooks/use-is-mobile.ts';
import { useNavigate } from 'react-router-dom';
import { usePathname } from '@/hooks/use-pathname.ts';

type LayoutProps = {
  children: ReactNode;
};

const backgroundMap: Record<string, string> = {
  [paths.game.rules]: '/assets/rules-background.jpeg',
  [paths.game.chat]: '/assets/chat-background.jpeg',
  [paths.game.score]: '/assets/score-background.jpeg',
};

export function Layout({ children }: LayoutProps): ReactElement {
  const navigate = useNavigate();
  const pathname = usePathname();
  const background = backgroundMap[pathname] ?? '/assets/background.jpeg';
  const isMobile = useIsMobile();

  useEffect(() => {
    if (pathname === '/') navigate(paths.game.chat);
  }, [navigate, pathname]);

  return (
    <Box
      sx={{
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        backgroundImage: `linear-gradient(0deg, rgba(9, 10, 11, 0.9) 0%, rgba(9, 10, 11, 0.9) 100%), url("${background}")`,
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        overscroll: 'hidden',
        overflow: 'hidden',
      }}
    >
      <Header />
      <Stack
        sx={{
          width: isMobile ? '100%' : '70%',
          minWidth: isMobile ? '200px' : '600px',
          paddingX: isMobile ? 0 : 7,
          height: 'calc(100vh - 68px)',
          overflowY: 'auto',
        }}
        flexDirection='column'
        alignItems={'center'}
      >
        {children}
      </Stack>
    </Box>
  );
}
