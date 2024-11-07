/// <reference types="vite/client" />

interface ImportMetaEnv {
  // Google Tag Manager
  VITE_GOOGLE_TAG_MANAGER_ID?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
