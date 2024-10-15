import { component$ } from "@builder.io/qwik";
import styles from "./footer.module.css";
import { useServerTimeLoader } from "~/routes/layout";

export default component$(() => {
  const serverTime = useServerTimeLoader();

  return (
    <footer class="mt-auto">
      <div class="flex justify-center">
        <a href="https://github.com/matteocodogno/CipherQuest" target="_blank" class={styles.anchor}>
          <span>Made with ♡ by Matteo Codogno and Andrea Rubino</span>
          <span class={styles.spacer}>|</span>
          <span>{serverTime.value.date}</span>
        </a>
      </div>
    </footer>
  );
});
