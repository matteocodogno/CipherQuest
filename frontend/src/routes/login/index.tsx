import { $, component$, useStore, useVisibleTask$ } from "@builder.io/qwik";
import { routeAction$, useNavigate } from "@builder.io/qwik-city";
import { applyDefaultUser } from "~/context/user-context";

type CreateUserLevelResponse = {
  userId: string;
};

export const useCreateUser = routeAction$<CreateUserLevelResponse>(
  async (data) => {
    const response = await fetch(`http://localhost:8080/user`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username: data.username as string }),
    });

    const responseString = await response.text();
    return JSON.parse(responseString);
  },
);

export default component$(() => {
  const state = useStore({ username: "" });
  const nav = useNavigate();

  // eslint-disable-next-line qwik/no-use-visible-task
  useVisibleTask$(async () => {
    const existingJsonUser = localStorage.getItem("user");
    if (existingJsonUser !== null) {
      await nav("/game");
    }
  });

  const createUser = useCreateUser();

  const handleSubmit = $(async () => {
    const {
      value: { userId },
    } = await createUser.submit({ username: state.username });

    localStorage.setItem(
      "user",
      JSON.stringify({
        ...applyDefaultUser(),
        id: userId,
        username: state.username,
      }),
    );

    await nav("/game");
  });

  return (
    <div class="flex flex-col items-center justify-center h-full">
      <div class="w-full max-w-xs ">
        <form class="bg-neutral-800 shadow-md rounded px-8 pt-6 pb-8 mb-4" onSubmit$={handleSubmit} preventdefault:submit>
          <div class="mb-4">
            <label class="block text-base font-normal text-sm font-bold mb-2">
              Username
            </label>
            <input
              autofocus
              value={state.username}
              onInput$={(_, el) => (state.username = el.value)}
              class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="Username" />
          </div>
          <div class="flex items-center justify-between">
            <button
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="submit">
              Enter
            </button>
          </div>
        </form>
      </div>
    </div>
  );
});
