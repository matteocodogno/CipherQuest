import { component$ } from "@builder.io/qwik";
import AiAvatarImage from "~/media/ai-avatar.jpeg?jsx";
import HumanAvatarImage from "~/media/human-avatar.jpeg?jsx";

export default component$(({ role = 'user', size = 'small' }: { role: 'user' | 'bot', size?: 'small' | 'medium' }) => {
  return (
    <div class={`
          relative flex items-center shrink-0 justify-center rounded-full overflow-hidden leading-none select-none
          font-medium text-sm ${ size === 'small' ? 'h-8 w-8' : 'h-10 w-10' } tracking-normal
    `}>
      {
        role==='user' ? (<HumanAvatarImage alt="human avatar"/>):(<AiAvatarImage alt="ai avatar"/>)
      }
    </div>
  );
});
