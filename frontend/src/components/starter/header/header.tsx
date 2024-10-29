import { component$, useContext, useVisibleTask$ } from "@builder.io/qwik";
import { FaStarSolid, FaCoinsSolid } from '@qwikest/icons/font-awesome';
import { QwikLogo } from "../icons/qwik";
import Timer from '~/components/timer';
import Chip from '~/components/chip';
import { UserContext } from '~/context/user-context';
import { animate } from 'framer-motion';
import User from "~/components/user";

export default component$(() => {
  const { user } = useContext(UserContext);

  // eslint-disable-next-line qwik/no-use-visible-task
  useVisibleTask$(async ({track}) => {
    track(() => user.coins);

    animate(
      '#coins',
      {
        scale: 1.5,
      }, {
        duration: 0.4,
        repeat: 3,
        repeatType: 'mirror',

      }
    );
  }, );

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
        <div class="flex items-center gap-x-2">
          <Chip id="level">
            <FaStarSolid q:slot="Icon" class="mr-2" />
            Level {user.level}
          </Chip>
          <Chip id="coins" color="warning">
            <FaCoinsSolid q:slot="Icon" class="mr-2" />
            Coins {user.coins}
          </Chip>
          <User username={user.username}/>
        </div>
        {/* TODO: reset button. it should ask confirmation and reset the entire game */}
        {/* TODO: number of questions, we should save a rank of the best player */}
        {/* TODO: add button to show up a modal / drawer that explains the rules of the game */}
      </div>
    </header>
  );
});
