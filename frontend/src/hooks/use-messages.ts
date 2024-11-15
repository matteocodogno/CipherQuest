import { ChatContext, ChatContextValue } from '@/components/chat/chat-context';
import { useContext } from 'react';

export const useMessages = (): ChatContextValue => {
  const context = useContext(ChatContext);

  if (!context) {
    throw new Error('useMessages must be used within a ChatProvider');
  }

  return context;
};
