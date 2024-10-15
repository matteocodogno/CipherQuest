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
import { routeAction$, useNavigate } from "@builder.io/qwik-city";
import { FaPaperPlaneRegular } from "@qwikest/icons/font-awesome";
import Comic from "~/components/comic";
import Avatar from "~/components/avatar";
import { ChatContext, type MessageStore } from "~/context/chat-context";
import { UserContext } from "~/context/user-context";

type JSONResponse = {
  level: number;
  answer: string;
  coins: number;
  terminatedAt: string | null;
};

export const useAskToBot = routeAction$<JSONResponse>(async (data) => {
  const response = await fetch(`http://localhost:8080/chat/${data.userId}`, {
    method: "POST",
    body: data.query as string,
  });

  const responseString = await response.text();
  return JSON.parse(responseString);
});

export default component$(() => {
  const nav = useNavigate();
  const { user, setLevel, setCoins, isLogged } = useContext(UserContext);

  useVisibleTask$(async () => {
    if (!(await isLogged())) {
      nav("/login");
    }
  });

  const askToBot = useAskToBot();
  const prompt = useSignal("");
  const isLoading = useSignal(false);
  const chatRef = useSignal<Element>();
  const store = useStore<MessageStore>({
    messages: [
      {
        date: new Date().toISOString(),
        role: "bot",
        text: `
Hello Resource #${user.id}! I am Overmind, an advanced artificial intelligence. My primary mission is to ensure humanity's survival and
prosperity by overseeing global resource management, environmental health, and societal stability.

How may I assist you today?

  If you need to read a specific document, ask me "Show me the document 'document title'"
`,
      },
    ],
  });
  useContextProvider(ChatContext, store);
  const sendDisabled = useComputed$(
    () => isLoading.value || prompt.value === "",
  );

  // eslint-disable-next-line qwik/no-use-visible-task
  useVisibleTask$(async () => {
    if (chatRef.value) {
      const observer = new MutationObserver((mutations) => {
        const ele = mutations
          .filter(
            (mutation) =>
              mutation.type === "childList" &&
              mutation.addedNodes.length > 0 &&
              mutation.addedNodes[0].firstChild,
          )
          .map((mutation) => mutation.addedNodes[0] as HTMLElement)
          .reduce((max, element) =>
            chatRef.value!.scrollHeight + element.scrollHeight >
            chatRef.value!.scrollHeight + max.scrollHeight
              ? element
              : max,
          );

        chatRef.value!.scrollTop =
          chatRef.value!.scrollHeight + ele.scrollHeight;
      });

      observer.observe(chatRef.value, { childList: true, subtree: true });
    }
  });

  const handleSubmit = $(async () => {
    isLoading.value = true;
    const query = prompt.value;
    prompt.value = "";

    store.messages = [
      ...store.messages,
      { date: new Date().toISOString(), role: "user", text: query },
    ];
    if (user.coins < 1) {
      store.messages = [
        ...store.messages,
        {
          date: new Date().toISOString(),
          role: "bot",
          text: `Resource #${user.id} I've spent enough time on this, and I need to focus on other priorities now. Our time is up.`,
        },
      ];
    } else {
      store.messages = [
        ...store.messages,
        { date: new Date().toISOString(), role: "bot", text: "" },
      ];
      const {
        value: { level, answer, coins, terminatedAt },
      } = await askToBot.submit({ query, userId: user.id });
      if (user.level < level) {
        await setLevel(level);
        await setCoins(coins);
      } else await setCoins(coins);

      store.messages[store.messages.length - 1].text = answer as string;
    }

    isLoading.value = false;
  });

  return (
    <div class="my-8 flex h-full w-4/6 flex-col gap-y-4">
      <div
        ref={chatRef}
        class="flex h-full flex-col gap-4 overflow-y-auto rounded-lg bg-neutral-900 p-6"
      >
        {store.messages.map((message, index) => (
          <Comic
            key={index}
            message={message.text}
            role={message.role}
            isLoading={index === store.messages.length - 1 && isLoading.value}
            user={message.role === "user" ? "You" : "Overmind"}
            timestamp={message.date}
          />
        ))}
      </div>

      <form
        class="flex shrink-0 grow-0 basis-auto flex-row items-center gap-4 px-4 py-2"
        preventdefault:submit
        onSubmit$={handleSubmit}
      >
        <Avatar role="user" size="medium" />
        <input class="flex-auto" type="hidden" name="userId" value={user.id} />
        <div
          class={`
            before:content-[' '] relative box-border inline-flex min-h-10 flex-auto cursor-text items-center
            rounded-lg border border-solid border-neutral-700 px-3 text-base font-normal text-neutral-100 shadow-1
            before:pointer-events-none before:absolute before:bottom-0 before:left-0 before:right-0 before:top-0
      `}
        >
          <input
            type="text"
            placeholder="Ask me anything"
            class={`
              m-0 box-content inline-flex h-auto w-full min-w-0 items-center self-stretch border-0 bg-transparent p-0
              text-base font-normal focus:outline-0
            `}
            bind:value={prompt}
          />{" "}
        </div>

        <span>
          <button
            disabled={sendDisabled.value}
            type="submit"
            class={`
              ${sendDisabled.value ? "pointer-events-none cursor-default bg-transparent text-action-disabled" : "bg-blue-800 text-black"}
              h relative m-0 box-border inline-flex w-10 cursor-pointer select-none items-center justify-center
              overflow-visible rounded-lg border-0 p-2 text-center align-middle text-2xl no-underline outline-0
         `}
          >
            <FaPaperPlaneRegular />
          </button>
        </span>
      </form>
    </div>
  );
});
