import Dialog from '@/components/core/dialog.tsx';
import Typography from '@mui/material/Typography';

interface SourceDialogProps {
  source: string;
  showDialog: boolean;
  closeDialog: () => void;
}

const SourceDialog = ({
  source,
  showDialog,
  closeDialog,
}: SourceDialogProps) => {
  return (
    <Dialog
      title='Source'
      showDialog={showDialog}
      closeDialog={closeDialog}
    >
      <Typography style={{ whiteSpace: 'pre-wrap' }}>{source}</Typography>
    </Dialog>
  );
};

export default SourceDialog;
