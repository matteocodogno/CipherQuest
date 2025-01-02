import { Helmet } from 'react-helmet-async';
import { Metadata } from '@/types/metadata';
import { ReactElement } from 'react';
import { ScoreView } from '@/components/chat/view/score-view.tsx';
import { config } from '@/config.ts';

const metadata = {
  title: `Scoreboard | Overmind | ${config.site.name}`,
} satisfies Metadata;

export const Page = (): ReactElement => (
  <>
    <Helmet>
      <title>{metadata.title}</title>
    </Helmet>
    <ScoreView />
  </>
);
