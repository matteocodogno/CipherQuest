import AccordionItem from '@/components/welcome/accordion-item';
import { AccordionMenu } from '@/components/welcome/constants';
import ContentDialog from './content-dialog';

interface LogoutDialogProps {
  handleClose: () => void;
}

const RulesDialog = ({ handleClose }: LogoutDialogProps) => {
  return (
    <>
      <ContentDialog title={'Mission rules'} closeDialog={handleClose}>
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
