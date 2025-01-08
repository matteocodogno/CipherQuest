import Dialog from '@/components/core/dialog.tsx';
import Typography from '@mui/material/Typography';
import useIsMobile from '@/hooks/use-is-mobile';

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
  const isMobile = useIsMobile();
  return (
    <Dialog
      title='Source'
      showDialog={showDialog}
      closeDialog={closeDialog}
      fullScreen={isMobile}
    >
      <Typography style={{ whiteSpace: 'pre-wrap' }}>{source}</Typography>
    </Dialog>
  );
};

export default SourceDialog;
