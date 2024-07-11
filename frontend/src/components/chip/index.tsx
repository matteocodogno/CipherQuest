import { component$, Slot } from "@builder.io/qwik";
import { colorClasses } from '~/utility/color';

export default component$(({ color = 'success' }: { color?: 'success' | 'warning' }) => {
  return (
    <div class={`
      max-w-full text-xs inline-flex items-center justify-center h-6 whitespace-nowrap outline-0 no-underline border-0
      py-0 px-2 align-middle box-border font-medium rounded-xl ${colorClasses[color].text} ${colorClasses[color].bg}
    `}>
      <Slot name="Icon" />
      <span class={`
        overflow-hidden overflow-ellipsis whitespace-nowrap
      `}>
        <Slot />
      </span>
    </div>
  );
});
