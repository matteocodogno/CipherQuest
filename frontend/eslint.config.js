import js from '@eslint/js'
import globals from 'globals'
import pluginQuery from '@tanstack/eslint-plugin-query'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'

export default tseslint.config(
  { ignores: ['dist'] },
  {
    extends: [js.configs.recommended, ...tseslint.configs.recommended, ...pluginQuery.configs['flat/recommended']],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      '@typescript-eslint': tseslint.plugin,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      'jsx-quotes': ['error', 'prefer-single'],
      quotes: ['error', 'single'],
      'sort-imports': ['error', {
        'memberSyntaxSortOrder': ['none', 'all', 'multiple', 'single'],
      }],
      '@typescript-eslint/no-unused-vars': ["error", { "argsIgnorePattern": "^_" }],
    },
  },
)
