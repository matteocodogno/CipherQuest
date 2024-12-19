import ContentDialog from './content-dialog';
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
    <ContentDialog showDialog={showDialog} closeDialog={closeDialog}>
      <Typography style={{ whiteSpace: 'pre-wrap' }}>{source}</Typography>
    </ContentDialog>
  );
};

export default SourceDialog;
