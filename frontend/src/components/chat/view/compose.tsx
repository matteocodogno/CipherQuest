import { ReactElement, useCallback } from 'react';
import { MessageAdd } from './message-add';
import type { MessageType } from '../types';
import { useChat } from '@/hooks/use-chat';

const ComposeView = (): ReactElement => {
  const { createMessage } = useChat();

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  return <MessageAdd onSend={handleSendMessage} />;
};

export default ComposeView;
