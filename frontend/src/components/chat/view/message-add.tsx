import { ChangeEvent, KeyboardEvent, useCallback, useRef, useState } from 'react';
import { ArrowUp } from '@phosphor-icons/react';
import Button from '@mui/material/Button';
import type { MessageType } from '../types';
import OutlinedInput from '@mui/material/OutlinedInput';
import Stack from '@mui/material/Stack';
import Tooltip from '@mui/material/Tooltip';

type MessageAddProps = {
  disabled?: boolean;
  onSend?: (type: MessageType, content: string) => void;
};

const MessageAdd = (
  props: MessageAddProps,
) => {
  const {disabled, onSend} = props;
  const [content, setContent] = useState<string>('');
  const fileInputRef = useRef<HTMLInputElement | null>(null);

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
      if (event.code==='Enter') {
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
      marginBottom={4}
    >
      <OutlinedInput
        disabled={disabled}
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
          disabled={!content || disabled}
          onClick={handleSend}
          sx={{
            bgcolor: 'var(--mui-palette-primary-main)',
            color: 'text.primary',
            '&:hover': {bgcolor: 'var(--mui-palette-primary-dark)'},
          }}
          endIcon={<ArrowUp/>}
        >
          Send
        </Button>
      </Tooltip>
      <input hidden ref={fileInputRef} type='file'/>
    </Stack>
  );
};

export default MessageAdd;
