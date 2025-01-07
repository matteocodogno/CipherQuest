import Dialog from '@/components/core/dialog.tsx';
import Rules from '@/components/rules/rules';

interface LogoutDialogProps {
  handleClose: () => void;
}

const RulesDialog = ({ handleClose }: LogoutDialogProps) => {
  return (
    <Dialog title={'Mission rules'} closeDialog={handleClose}>
      <Rules />
    </Dialog>
  );
};

export default RulesDialog;
