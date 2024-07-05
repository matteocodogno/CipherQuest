import { component$ } from "@builder.io/qwik";
import {
  QwikCityProvider,
  RouterOutlet,
  ServiceWorkerRegister,
} from "@builder.io/qwik-city";
import { RouterHead } from "./components/router-head/router-head";

import "./global.css";

export default component$(() => {
  /**
   * The root of a QwikCity site always start with the <QwikCityProvider> component,
   * immediately followed by the document's <head> and <body>.
   *
   * Don't remove the `<head>` and `<body>` elements.
   */

  return (
    <QwikCityProvider>
      <head>
        <meta charset="utf-8" />
        <link rel="manifest" href="/manifest.json" />
        <title>Overmind Bot</title>
        <RouterHead />
        <ServiceWorkerRegister />
      </head>
      <body lang="en" class="m-0 text-neutral-100 font-normal text-base bg-neutral-950 h-screen overflow-hidden">
        <div class="flex flex-col flex-auto h-full bg-neutral-950">
          <RouterOutlet />
        </div>
      </body>
    </QwikCityProvider>
  );
});
