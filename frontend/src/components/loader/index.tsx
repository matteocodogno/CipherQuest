import { component$, useStyles$ } from "@builder.io/qwik";
import styles from "./styles.css?inline";

// TODO: add size prop
export default component$(() => {
  useStyles$(styles);

  return (
    <div class="lds-ellipsis">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  );
});
