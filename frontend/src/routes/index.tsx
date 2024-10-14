import { component$, useVisibleTask$ } from "@builder.io/qwik";
import { useNavigate } from "@builder.io/qwik-city";

export default component$(() => {
  const nav = useNavigate();

  // eslint-disable-next-line qwik/no-use-visible-task
  useVisibleTask$(async () => {
    const existingJsonUser = localStorage.getItem("user");
    await nav(existingJsonUser === null ? "/login" : "/game");
  });

  return <>loading...</>;
});
