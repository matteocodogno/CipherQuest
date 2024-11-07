import { ComposeView } from '@/components/chat/compose.tsx';
import { Helmet } from 'react-helmet-async';
import { Metadata } from '@/types/metadata';
import { ReactElement } from 'react';
import { config } from '@/config.ts';

const metadata = { title: `Game | Overmind | ${config.site.name}` } satisfies Metadata;

export const Page = (): ReactElement => (
  <>
    <Helmet>
      <title>{metadata.title}</title>
    </Helmet>
    <ComposeView />
  </>
);
