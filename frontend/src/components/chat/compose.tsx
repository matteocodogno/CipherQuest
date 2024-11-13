import { ReactElement, useCallback, useContext } from 'react';
import Box from '@mui/material/Box';
import { ChatContext } from './chat-context';
import { MessageAdd } from './message-add';
import type { MessageType } from './types';

export function ComposeView(): ReactElement {
  const { createMessage } = useContext(ChatContext);

  const handleSendMessage = useCallback(
    async (type: MessageType, content: string) => {
      createMessage({ type, content });
    },
    [createMessage],
  );

  return (
    <Box
      sx={{
        display: 'flex',
        width: '100%',
        flex: 1,
        flexDirection: 'column',
        flexShrink: 0,
        alignItems: 'center',
      }}
    >
      <Box sx={{ flex: '1 1 auto' }} />
      <MessageAdd onSend={handleSendMessage} />
    </Box>
  );
}
