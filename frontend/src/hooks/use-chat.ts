import { ChatContext, ChatContextValue } from '@/contexts/chat/chat-context.tsx';
import { useContext } from 'react';

export const useChat = (): ChatContextValue => {
  const context = useContext(ChatContext);

  if (!context) {
    throw new Error('useChat must be used within a ChatProvider');
  }

  return context;
};
