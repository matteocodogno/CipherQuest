import CloseIcon from '@mui/icons-material/Close';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material/styles';

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(4),
  },
}));

interface ContentDialogProps {
  source: string;
  showDialog: boolean;
  closeDialog: () => void;
}

const SourceDialog = ({
  source,
  showDialog,
  closeDialog,
}: ContentDialogProps) => {
  return (
    <BootstrapDialog
      onClose={closeDialog}
      aria-labelledby='customized-dialog-title'
      open={showDialog}
      maxWidth={'lg'}
    >
      <DialogTitle sx={{ m: 0, p: 2 }} id='customized-dialog-title'>
        Source
      </DialogTitle>
      <IconButton
        aria-label='close'
        onClick={closeDialog}
        sx={(theme) => ({
          position: 'absolute',
          right: 8,
          top: 8,
          color: theme.palette.grey[500],
        })}
      >
        <CloseIcon />
      </IconButton>
      <DialogContent sx={{ alignItems: 'center' }}>
        <Typography style={{ whiteSpace: 'pre-wrap' }}>{source}</Typography>
      </DialogContent>
    </BootstrapDialog>
  );
};

export default SourceDialog;
