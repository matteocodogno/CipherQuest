import { ReactElement, ReactNode } from 'react';
import { AuthGuard } from '@/components/auth/auth-guard.tsx';
import Box from '@mui/material/Box';
import { ChatProvider } from '@/components/chat/chat-provider.tsx';
import { ChatView } from './view/chat-view';
import { Header } from './view/header';
import { dayjs } from '@/lib/dayjs.ts';
import { useGetChatHistory } from '@/api/chat/use-get-chat-history';
import { usePathname } from '@/hooks/use-pathname.ts';
import { useUser } from '@/hooks/use-user';

type LayoutProps = {
  children: ReactNode;
};

const contacts = [
  {
    id: 'USR-001',
    name: 'Human',
    avatar: '/assets/avatar-10.png',
    isActive: false,
    lastActivity: dayjs().subtract(1, 'hour').toDate(),
  },
  {
    id: 'USR-002',
    name: 'Overmind',
    avatar: '/assets/avatar-3.png',
    isActive: false,
    lastActivity: dayjs().subtract(15, 'minute').toDate(),
  },
];

const backgroundMap: Record<string, string> = {
  '/rules': '/assets/rules-background.jpeg',
  '/': '/assets/chat-background.jpeg',
};

export function Layout({ children }: LayoutProps): ReactElement {
  const pathname = usePathname();
  const background = backgroundMap[pathname] ?? '/assets/background.jpeg';
  const { user } = useUser();
  const messages = useGetChatHistory({
    userId: user?.userId ?? '',
  });

  return (
    <AuthGuard>
      <ChatProvider contacts={contacts} messages={messages}>
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
          <ChatView>{children}</ChatView>
        </Box>
      </ChatProvider>
    </AuthGuard>
  );
}
