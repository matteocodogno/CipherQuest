import { component$ } from "@builder.io/qwik";
import AiAvatarImage from "~/media/ai-avatar.jpeg?jsx";
import HumanAvatarImage from "~/media/human-avatar.jpeg?jsx";
import Avatar from '~/components/avatar';

export default component$(({
  user,
  role = 'user',
  timestamp,
  isLoading,
  message,
} : {
  user: string
  role: 'user' | 'bot',
  timestamp: string,
  isLoading: boolean,
  message: string,
}) => {
  return (
    <div class={`flex grow-0 shrink-0 basis-auto ${role==='bot' ? 'items-start' : 'items-end'}`}>
      <div class={`flex flex-row gap-4 items-start max-w-lg ml-0 ${role==='user' ? 'flex-row-reverse ml-auto' : 'mr-auto'}`}>
        <Avatar role={role} />
        <div class="flex flex-col gap-2 flex-auto">
          <div
            class={`
            ${isLoading ? '':"py-2"}
            ${role==='bot' ? 'bg-gray-900':'bg-[#7578ff]'}
            shadow-ai rounded-2xl gap-2 overflow-hidden px-4 bg-none text-neutral-100
          `}
          >

            <div class='flex flex-col gap-2'>
              {
                isLoading ? (
                  <div class="lds-ellipsis">
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                  </div>
                ):(
                  <>
                    <p class='m-0 font-medium leading-6 text-sm'>{user}</p>
                    <p class='m-0 font-normal leading-6 text-base'>{message}</p>
                  </>
                )
              }
            </div>
          </div>
        </div>
      </div>
    </div>
  );
});
