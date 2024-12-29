import AccordionItem from '@/components/core/accordion-item.tsx';
import { AccordionMenu } from '@/components/chat/constants.ts';
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
