import { component$ } from "@builder.io/qwik";
import { QwikLogo } from "../icons/qwik";
import Timer from '~/components/timer';

export default component$(() => {
  return (
    <header class="flex left-0 sticky top-0 w-full primary-zinc-900">
      <div class="px-6 py-2 border-solid border-b border-neutral-700 flex min-h-10 flex-auto">
        <div class="flex flex-row gap-4 flex-auto items-center">
          <div class="flex items-center gap-x-3">
            <a href="/" title="Overmind" class="block">
              <QwikLogo height={32} width={32} />
            </a>

            <h4>Overmind</h4>
          </div>
        </div>

        <div class="flex flex-row gap-4 items-center flex-auto justify-end">
          <Timer as="h4" startDate={new Date()} />
        </div>
      </div>
    </header>
  );
});
