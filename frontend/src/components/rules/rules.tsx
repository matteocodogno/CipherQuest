import AccordionItem from '../core/accordion-item';
import { AccordionMenu } from './constants';

const Rules = () => {
  return (
    <>
      {AccordionMenu.map((item) => (
        <AccordionItem key={item.title} item={item} />
      ))}
    </>
  );
};

export default Rules;
