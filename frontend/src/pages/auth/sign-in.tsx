import { GuestGuard } from '@/components/auth/guest-guard';
import { Helmet } from 'react-helmet-async';
import type { Metadata } from '@/types/metadata';
import { ReactElement } from 'react';
import SignInLayout from '@/components/auth/sign-in-layout';
import { config } from '@/config';

const metadata = {
  title: `Sign in | Custom | Auth | ${config.site.name}`,
} satisfies Metadata;

export const Page = (): ReactElement => (
  <>
    <Helmet>
      <title>{metadata.title}</title>
    </Helmet>
    <GuestGuard>
      <SignInLayout />
    </GuestGuard>
  </>
);
