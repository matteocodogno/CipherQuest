import { Link, Typography } from '@mui/material';
import { useCallback, useState } from 'react';
import { Message } from '../types';
import SourceDialog from '../dialog/source-dialog';
import { Stack } from '@mui/system';

interface MessageTextProps {
  message: Message;
}

const MessageText = ({ message }: MessageTextProps) => {
  const [showDialog, setModal] = useState<boolean>(false);
  const [currentSource, setSource] = useState<string>('');

  const handleCloseSource = useCallback(() => {
    setModal(false);
  }, []);

  const handleShowSource = useCallback((id: string) => {
    setModal(true);
    //TODO: get source by id
    setSource(id);
  }, []);

  return (
    <>
      <Stack
        sx={{
          alignItems: 'flex-start',
        }}
        gap={1}
      >
        <Typography
          color='inherit'
          variant='body1'
          style={{ whiteSpace: 'pre-wrap' }}
        >
          {message.content}
        </Typography>
        {message.info.sources.length > 0 && (
          <Stack sx={{ flexDirection: 'row' }} alignItems={'top'}>
            <Typography
              color='inherit'
              variant='body1'
              style={{ whiteSpace: 'pre-wrap' }}
            >
              Sources:
            </Typography>
            <Stack>
              {message.info.sources.map((source) => (
                <Link
                  sx={{ marginLeft: 1, cursor: 'pointer' }}
                  onClick={() => handleShowSource(source.id)}
                  underline='always'
                >
                  {source.title}
                </Link>
              ))}
            </Stack>
          </Stack>
        )}
      </Stack>
      <SourceDialog
        source={'test source: ' + currentSource}
        showDialog={showDialog}
        closeDialog={handleCloseSource}
      />
    </>
  );
};

export default MessageText;
