import AccordionItem from '@/components/welcome/accordion-item';
import { AccordionMenu } from '@/components/welcome/constants';
import ContentDialog from './content-dialog';

interface LogoutDialogProps {
  showDialog: boolean;
  handleClose: () => void;
}

const RulesDialog = ({ showDialog, handleClose }: LogoutDialogProps) => {
  return (
    <>
      <ContentDialog
        title={'Mission rules'}
        showDialog={showDialog}
        closeDialog={handleClose}
      >
        <>
          {AccordionMenu.map((item) => (
            <AccordionItem key={item.title} item={item} />
          ))}
        </>
      </ContentDialog>
    </>
  );
};

export default RulesDialog;
