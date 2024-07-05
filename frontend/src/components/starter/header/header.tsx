import { component$ } from "@builder.io/qwik";
import { QwikLogo } from "../icons/qwik";

export default component$(() => {
  return (
    <header class="flex m-5">
      <div class="flex-1 items-center justify-between">
        <div class="flex items-center gap-x-3">
          <a href="/" title="Overmind" class="block">
            <QwikLogo height={50} width={50} />
          </a>

          <h3>Overmind</h3>
        </div>
      </div>
    </header>
  );
});
