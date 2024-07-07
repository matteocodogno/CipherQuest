import { component$, useSignal, useVisibleTask$ } from "@builder.io/qwik";
import Avatar from '~/components/avatar';
import { formatDistanceToNow } from 'date-fns';

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
  const time = useSignal(formatDistanceToNow(new Date(timestamp), {addSuffix: true}));

  useVisibleTask$(({ cleanup }) => {
    const update = () => {
      time.value = formatDistanceToNow(new Date(timestamp), {addSuffix: true});
    };
    const id = setInterval(update, 60000);
    cleanup(() => clearInterval(id));
  });

  return (
    <div class={`flex grow-0 shrink-0 basis-auto ${role==='bot' ? 'items-start' : 'items-end'}`}>
      <div class={`flex flex-row gap-4 items-start max-w-lg ml-0 ${role==='user' ? 'flex-row-reverse ml-auto' : 'mr-auto'}`}>
        <Avatar role={role} />
        <div class="flex flex-col gap-2 flex-auto">
          <div
            class={`
            ${isLoading ? '':"py-2"}
            ${role==='bot' ? 'bg-neutral-800':'bg-[#7578ff]'}
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
                    <pre class='m-0 font-medium leading-6 text-sm whitespace-pre-wrap'>{user}</pre>
                    <pre class='m-0 font-normal leading-6 text-base whitespace-pre-wrap'>{message}</pre>
                  </>
                )
              }
            </div>
          </div>
          <div class="flex justify-start px-4">
            <span class="m-0 font-normal text-xs overflow-hidden text-ellipsis whitespace-nowrap text-neutral-400">
              { time.value }
            </span>
          </div>
        </div>
      </div>
    </div>
  );
});
