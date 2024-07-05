import { component$, type FunctionComponent, useSignal, useVisibleTask$ } from "@builder.io/qwik";
import { differenceInSeconds } from 'date-fns';

export default component$(
  <C extends string | FunctionComponent = 'div'>({
    as, startDate = new Date()
  } : { as?: C, startDate?: Date }) => {
  const Cmp = as || 'div';
  const time = useSignal(0);
  const rightDate = startDate;

  useVisibleTask$(({ cleanup }) => {
    const update = () => {
      const leftDate = new Date()
      time.value = differenceInSeconds(leftDate, rightDate);
    };
    const id = setInterval(update, 1000);
    cleanup(() => clearInterval(id));
  });

  return (
    <Cmp>{time} seconds</Cmp>
  );
});
