import {
  ChangeEvent,
  KeyboardEvent,
  useCallback,
  useRef,
  useState,
} from 'react';
import { ArrowUp } from '@phosphor-icons/react';
import Button from '@mui/material/Button';
import { GameStatus } from '@/api/chat/types';
import type { MessageType } from '../../contexts/chat/types';
import OutlinedInput from '@mui/material/OutlinedInput';
import Stack from '@mui/material/Stack';
import StickyBox from '../core/sticky-box';
import Tooltip from '@mui/material/Tooltip';
import { useChat } from '@/hooks/use-chat';
import useIsMobile from '@/hooks/use-is-mobile';

type MessageAddProps = {
  disabled?: boolean;
  onSend?: (type: MessageType, content: string) => void;
};

type MessageInputButtonProps = MessageAddProps & { isMobile?: boolean };

const MessageInputButton = (props: MessageInputButtonProps) => {
  const { disabled, onSend, isMobile } = props;
  const [content, setContent] = useState<string>('');
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const { messages } = useChat();
  const lastMessage =
    messages.length > 0 ? messages[messages.length - 1] : undefined;

  const gameEnded = lastMessage?.info?.status !== GameStatus.IN_PROGRESS;

  const handleChange = useCallback((event: ChangeEvent<HTMLInputElement>) => {
    setContent(event.target.value);
  }, []);

  const handleSend = useCallback(() => {
    if (!content) {
      return;
    }

    onSend?.('text', content);
    setContent('');
  }, [content, onSend]);

  const handleKeyUp = useCallback(
    (event: KeyboardEvent<HTMLInputElement>) => {
      if (event.code === 'Enter') {
        handleSend();
      }
    },
    [handleSend],
  );

  return (
    <Stack
      direction='row'
      spacing={2}
      sx={{
        alignItems: 'center',
        flexShrink: 0,
        alignSelf: 'stretch',
      }}
      marginBottom={isMobile ? 0 : 4}
    >
      <OutlinedInput
        disabled={disabled || gameEnded}
        onChange={handleChange}
        onKeyUp={handleKeyUp}
        placeholder='Ask something...'
        sx={{
          flex: '1 1 auto',
          background: 'var(--mui-palette-background-paper)',
        }}
        value={content}
      />
      <Tooltip title='Send'>
        <Button
          color='primary'
          disabled={!content || disabled || gameEnded}
          onClick={handleSend}
          sx={{
            bgcolor: 'var(--mui-palette-primary-main)',
            color: 'text.primary',
            '&:hover': { bgcolor: 'var(--mui-palette-primary-dark)' },
          }}
          endIcon={<ArrowUp />}
        >
          Send
        </Button>
      </Tooltip>
      <input hidden ref={fileInputRef} type='file' />
    </Stack>
  );
};

const MessageAdd = (props: MessageAddProps) => {
  const isMobile = useIsMobile();

  return isMobile ? (
    <StickyBox>
      <MessageInputButton {...props} isMobile />
    </StickyBox>
  ) : (
    <MessageInputButton {...props} />
  );
};

export default MessageAdd;
