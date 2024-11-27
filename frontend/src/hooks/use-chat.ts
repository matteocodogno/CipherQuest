import { ChatContext, ChatContextValue } from '@/components/chat/chat-context';
import { useContext } from 'react';

export const useChat = (): ChatContextValue => {
  const context = useContext(ChatContext);

  if (!context) {
    throw new Error('useChat must be used within a ChatProvider');
  }

  return context;
};
