import AccordionItem from '@/components/welcome/accordion-item';
import { AccordionMenu } from '@/components/welcome/constants';
import Dialog from '@/components/core/dialog.tsx';

interface LogoutDialogProps {
  handleClose: () => void;
}

const RulesDialog = ({ handleClose }: LogoutDialogProps) => {
  return (
    <Dialog title={'Mission rules'} closeDialog={handleClose}>
      {AccordionMenu.map((item) => (
        <AccordionItem key={item.title} item={item} />
      ))}
    </Dialog>
  );
};

export default RulesDialog;
