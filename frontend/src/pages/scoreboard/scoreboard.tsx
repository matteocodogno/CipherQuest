import { config } from "@/config.ts";
import { Metadata } from "@/types/metadata";
import { ReactElement } from "react";
import { Helmet } from "react-helmet-async";
import { ScoreboardView } from "@/components/scoreboard/scoreboard-view.tsx";

const metadata = {
  title: `Scoreboard | Overmind | ${config.site.name}`
} satisfies Metadata;

export const Page = (): ReactElement => {
  return (
    <>
      <Helmet>
        <title>{metadata.title}</title>
      </Helmet>
      <ScoreboardView />
    </>
  );
};

