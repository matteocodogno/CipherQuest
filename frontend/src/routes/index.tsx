import {
  $,
  component$,
  useComputed$,
  useContext,
  useContextProvider,
  useSignal,
  useStore,
  useVisibleTask$,
} from "@builder.io/qwik";
import { routeAction$ } from '@builder.io/qwik-city';
import { FaPaperPlaneRegular } from '@qwikest/icons/font-awesome';
import Comic from '~/components/comic';
import Avatar from '~/components/avatar';
import { ChatContext, type MessageStore } from '~/context/chat-context';
import { UserContext } from '~/context/user-context';

type JSONResponse = {
  level: number,
  answer: string,
  coins: number,
  terminatedAt: string,
}

export const useAskToBot = routeAction$<JSONResponse>(async (data) => {
  const response = await fetch(`http://localhost:8080/chat/${data.userId}`, {
    method: "POST",
    body: data.query as string,
  });

  const responseString = await response.text();
  return JSON.parse(responseString);
});

export default component$(() => {
  const { user, setLevel, setCoins } = useContext(UserContext);

  const askToBot = useAskToBot();
  const prompt = useSignal("");
  const isLoading = useSignal(false);
  const chatRef = useSignal<Element>();
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
  const sendDisabled = useComputed$(() => isLoading.value || prompt.value === "");

  useVisibleTask$(async () => {
    if (chatRef.value) {
      const observer = new MutationObserver((mutations) => {
        const ele = mutations
          .filter(mutation => mutation.type === 'childList' && mutation.addedNodes.length > 0 && mutation.addedNodes[0].firstChild)
          .map(mutation => mutation.addedNodes[0] as HTMLElement)
          .reduce((max, element) => chatRef.value!.scrollHeight + element.scrollHeight > chatRef.value!.scrollHeight + max.scrollHeight ? element : max)

        chatRef.value!.scrollTop = chatRef.value!.scrollHeight + ele.scrollHeight;
      });

      observer.observe(chatRef.value, { childList: true, subtree: true });
    }
  });

  const handleSubmit = $(async () => {
    isLoading.value = true;
    const query = prompt.value;
    prompt.value = "";

    store.messages = [...store.messages, {date: new Date().toISOString(), role: 'user', text: query}];
    if ( user.coins < 1 ) {
      store.messages = [...store.messages, {
        date: new Date().toISOString(),
        role: 'bot',
        text: `Resource #${user.id} I've spent enough time on this, and I need to focus on other priorities now. Our time is up.`
      }];
    } else {
      store.messages = [...store.messages, {date: new Date().toISOString(), role: 'bot', text: ""}];
      const {value: {level, answer, coins, terminatedAt}} = await askToBot.submit({query, userId: user.id});
      if (user.level < level) {
        setLevel(level)
        setCoins(coins)
      } else setCoins(coins);

      store.messages[store.messages.length - 1].text = answer as string;
    }

    isLoading.value = false;
  });

  return (
    <div class="flex flex-col h-full gap-y-4 my-8 w-4/6">
      <div ref={chatRef} class="flex flex-col h-full bg-neutral-900 rounded-lg p-6 gap-4 overflow-y-auto">
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
        <input class='flex-auto' type='hidden' name='userId' value={user.id} />
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

        <span>
          <button
            disabled={sendDisabled.value}
            type="submit"
            class={`
              ${sendDisabled.value ? 'pointer-events-none cursor-default bg-transparent text-action-disabled' : 'bg-blue-800 text-black'}
              inline-flex items-center justify-center relative box-border outline-0 border-0 m-0 cursor-pointer select-none
              align-middle no-underline text-center text-2xl p-2 overflow-visible rounded-lg h w-10
         `}>
            <FaPaperPlaneRegular />
          </button>
        </span>
      </form>
    </div>
  );
});
