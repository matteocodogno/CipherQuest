import { Helmet } from 'react-helmet-async';
import { Metadata } from '@/types/metadata';
import { ReactElement } from 'react';
import { RulesView } from '@/components/rules/view/rules-view.tsx';
import { config } from '@/config.ts';

const metadata = {
  title: `Rules | Overmind | ${config.site.name}`,
} satisfies Metadata;

export const Page = (): ReactElement => (
  <>
    <Helmet>
      <title>{metadata.title}</title>
    </Helmet>
    <RulesView />
  </>
);
