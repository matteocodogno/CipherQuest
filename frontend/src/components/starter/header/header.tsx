import { component$, useContext } from "@builder.io/qwik";
import { FaStarSolid } from '@qwikest/icons/font-awesome';
import { QwikLogo } from "../icons/qwik";
import Timer from '~/components/timer';
import Level from '~/components/starter/header/level';
import { UserContext } from '~/context/user-context';

export default component$(() => {
  const { user } = useContext(UserContext);

  return (
    <header class="flex left-0 sticky top-0 w-full primary-zinc-900">
      <div class="px-6 py-2 border-solid border-b border-neutral-700 flex min-h-10 flex-auto justify-between">
        <div class="flex items-center gap-x-3 justify-items-start">
          <a href="/" title="Overmind" class="block">
            <QwikLogo height={32} width={32} />
          </a>

          <h4>Overmind</h4>
        </div>

        <div class="flex items-center">
          {/*TODO: user.startedAt ??*/}
          <Timer as="h4" startDate={new Date()} />
        </div>
        <div class="flex items-center">
          <Level>
            <FaStarSolid q:slot="Icon" class="mr-2" />
            Level {user.level}
          </Level>
        </div>
        {/* TODO: reset button. it should ask confirmation and reset the entire game */}
        {/* TODO: number of questions, we should save a rank of the best player */}
        {/* TODO: add button to show up a modal / drawer that explains the rules of the game */}
      </div>
    </header>
  );
});
