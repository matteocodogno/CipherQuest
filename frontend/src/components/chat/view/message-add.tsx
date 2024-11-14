import {
  ChangeEvent,
  KeyboardEvent,
  ReactElement,
  useCallback,
  useRef,
  useState,
} from 'react';
import { ArrowUp } from '@phosphor-icons/react';
import Button from '@mui/material/Button';
import type { MessageType } from '../types';
import OutlinedInput from '@mui/material/OutlinedInput';
import Stack from '@mui/material/Stack';
import Tooltip from '@mui/material/Tooltip';
import type { User } from '@/types/user';

const user = {
  id: 9834759384,
  name: 'Sofia Rivers',
  username: 'sofia.rivers',
  level: 1,
  coins: 25,
  avatar: '/assets/avatar.png',
  email: 'sofia@devias.io',
  startedAt: new Date(),
} satisfies User;

export type MessageAddProps = {
  disabled?: boolean;
  onSend?: (type: MessageType, content: string) => void;
};

export const MessageAdd = ({
  disabled = false,
  onSend,
}: MessageAddProps): ReactElement => {
  const [content, setContent] = useState<string>('');
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleAttach = useCallback(() => {
    fileInputRef.current?.click();
  }, []);

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
        py: 1,
        flexShrink: 0,
        alignSelf: 'stretch',
      }}
      marginBottom={4}
    >
      <OutlinedInput
        disabled={disabled}
        onChange={handleChange}
        onKeyUp={handleKeyUp}
        placeholder='Ask something...'
        sx={{ flex: '1 1 auto', background: '#121517' }}
        value={content}
      />
      <Stack direction='row' spacing={1} sx={{ alignItems: 'center' }}>
        <Tooltip title='Send'>
          <span>
            <Button
              color='primary'
              disabled={!content || disabled}
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
          </span>
        </Tooltip>
      </Stack>
      <input hidden ref={fileInputRef} type='file' />
    </Stack>
  );
};
