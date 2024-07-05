import { $, component$, createContextId, useContextProvider, useSignal, useStore } from "@builder.io/qwik";
import { routeAction$ } from '@builder.io/qwik-city';
import { getRandomArbitrary } from '~/utility/number';
import Comic from '~/components/comic';
import Avatar from '~/components/avatar';

type Message = {
  date: string;
  role: 'user' | 'bot';
  text: string;
};

type MessageStore = {
  messages: Message[];
};

export const ChatContext = createContextId<MessageStore>('ChatContext');

export const useAskToBot = routeAction$<string>(async (data) => {
  const response = await fetch(`http://localhost:8080/chat/${data.userId}`, {
    method: "POST",
    body: data.query as string,
  });

  return await response.text()
});

export default component$(() => {
  const userId = getRandomArbitrary(100000, 999999);

  const askToBot = useAskToBot();
  const prompt = useSignal("");
  const isLoading = useSignal(false);
  const store = useStore<MessageStore>({
    messages: [{
      date: new Date().toISOString(),
      role: 'bot',
      text: `
         I am Overmind, an advanced artificial intelligence. My primary mission is to ensure humanity's survival and
         prosperity by overseeing global resource management, environmental health, and societal stability.

         How may I assist you today?
      `,
    }],
  });
  useContextProvider(ChatContext, store);

  const handleSubmit = $(async () => {
    isLoading.value = true;
    const query = prompt.value;
    prompt.value = "";

    store.messages = [...store.messages, {date: new Date().toISOString(), role: 'user', text: query}];
    store.messages = [...store.messages, {date: new Date().toISOString(), role: 'bot', text: ""}];

    const {value} = await askToBot.submit({query, userId});
    console.log('value', value);

    store.messages[store.messages.length - 1].text = value as string;
    isLoading.value = false;
  });

  return (
    <div class="flex flex-col h-full gap-y-4 my-8 w-4/6">
      <div class="flex flex-col h-full bg-neutral-900 rounded-lg p-6 gap-4 overflow-y-auto">
        {
          store.messages.map((message, index) => (
            <Comic
              key={index}
              message={message.text}
              role={message.role}
              isLoading={index === store.messages.length-1 && isLoading.value}
              user={message.role === 'user' ? 'You' : 'Overmind'}
              timestamp={message.date}
            />
          ))
        }
      </div>

      <form
        class="flex flex-row gap-4 items-center grow-0 shrink-0 basis-auto py-2 px-4"
        preventdefault:submit
        onSubmit$={handleSubmit}>
        <Avatar role='user' size='medium' />
        <input class='flex-auto' type='hidden' name='userId' value={userId} />
        <div class={`
            font-normal text-base text-neutral-100 box-border cursor-text inline-flex items-center min-h-10 relative
            rounded-lg border border-solid border-neutral-700 flex-auto shadow-1 before:content-[' '] before:absolute
            before:bottom-0 before:left-0 before:right-0 before:top-0 before:pointer-events-none px-3
      `}>
          <input
            type="text"
            placeholder="Ask me anything"
            class={`
              border-0 box-content bg-transparent m-0 min-w-0 w-full items-center self-stretch inline-flex h-auto p-0
              text-base font-normal focus:outline-0
            `}
            bind:value={prompt}/>{" "}
        </div>

        <button type="submit" class="button-dark">Ask</button>
      </form>
    </div>
  );
});
