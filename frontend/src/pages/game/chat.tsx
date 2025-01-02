import { AuthGuard } from '@/components/auth/auth-guard.tsx';
import { ChatProvider } from '@/contexts/chat/chat-provider.tsx';
import { ChatView } from '@/components/chat/view/chat-view';
import ComposeView from '@/components/chat/view/compose-view.tsx';
import { Helmet } from 'react-helmet-async';
import { Metadata } from '@/types/metadata';
import { ReactElement } from 'react';
import { config } from '@/config.ts';
import useGetChatHistory from '@/api/chat/use-get-chat-history.ts';
import { useUser } from '@/hooks/use-user.ts';

const metadata = {
  title: `Game | Overmind | ${config.site.name}`,
} satisfies Metadata;

export const Page = (): ReactElement => {
  const { user } = useUser();
  const { messages } = useGetChatHistory({
    user,
  });

  return (
    <AuthGuard>
      <Helmet>
        <title>{metadata.title}</title>
      </Helmet>
      <ChatProvider messages={messages}>
        <ChatView />
        <ComposeView />
      </ChatProvider>
    </AuthGuard>
  );
};
