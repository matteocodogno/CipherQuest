import { Helmet, HelmetProvider } from 'react-helmet-async';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Analytics } from '@/components/core/analytics.tsx';
import { LocalizationProvider } from '@/components/core/localization-provider.tsx';
import { Metadata } from '@/types/metadata';
import { ReactNode } from 'react';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { ThemeProvider } from '@/components/core/theme-provider/theme-provider.tsx';
import { Toaster } from 'sonner';
import { UserProvider } from '@/contexts/auth/custom/user-context.tsx';
import { config } from '@/config.ts';

const metadata = {title: config.site.name} satisfies Metadata;

const queryClient = new QueryClient()

type RootProps = {
  children: ReactNode;
};

export const Root = ({ children }: RootProps) => (
  <HelmetProvider>
    <Helmet>
      <title>{metadata.title}</title>
    </Helmet>

    <QueryClientProvider client={queryClient}>
      <Analytics>
        <LocalizationProvider>
          <UserProvider>
            <ThemeProvider>
              {children}
              <Toaster position='bottom-right' />
            </ThemeProvider>
          </UserProvider>
        </LocalizationProvider>
      </Analytics>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  </HelmetProvider>
);
