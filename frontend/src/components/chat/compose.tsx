import { ReactElement, useCallback, useContext } from 'react';
import Box from '@mui/material/Box';
import { ChatContext } from './chat-context';
import Divider from '@mui/material/Divider';
import { MessageAdd } from './message-add';
import type { MessageType } from './types';

export function ComposeView(): ReactElement {
  const { createMessage } = useContext(ChatContext);

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage]
  );

  return (
    <Box sx={{ display: 'flex', flex: '1 1 auto', flexDirection: 'column', minHeight: 0 }}>
      <Box sx={{ flex: '1 1 auto' }} />
      <Divider />
      <MessageAdd onSend={handleSendMessage} />
    </Box>
  );
}
