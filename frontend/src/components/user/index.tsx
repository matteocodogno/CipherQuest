import {component$, $, useSignal} from '@builder.io/qwik';
import Avatar from "~/components/avatar";
import {useNavigate} from "@builder.io/qwik-city";

export default component$(({username}: { username: string }) => {
  const anchorEl = useSignal<HTMLElement | null>(null);
  const nav = useNavigate();

  const handleClick = $((event: MouseEvent) => {
    anchorEl.value = anchorEl.value ? null : event.currentTarget as HTMLElement;
  });

  const handleClose = $(() => {
    anchorEl.value = null;
  });

  const handleLogout = $(async () => {
    localStorage.clear();
    await nav("/login");
  });

  const open = Boolean(anchorEl.value);

  return (
    <div>
      <button
        onClick$={handleClick}
        class="text-white p-1 rounded-full hover:bg-neutral-800 focus:outline-none"
      >
        <Avatar role="user"/>
      </button>

      {open && (
        <div
          class="absolute top-0 left-0 w-screen h-screen"
          onClick$={handleClose}
        >
          <div
            class="absolute mt-12 mr-2 right-0 w-56 bg-neutral-900 rounded-md border border-neutral-800"
          >
            <div class="px-2 py-1 rounded-lg m-2">{username}</div>
            <hr class="border-neutral-800"/>
            <div
              class="px-2 py-1 cursor-pointer text-red-500 hover:bg-neutral-800 rounded-lg m-2 flex justify-center"
              onClick$={handleLogout}
            >
              Logout
            </div>
          </div>
        </div>
      )}
    </div>
  );
});
