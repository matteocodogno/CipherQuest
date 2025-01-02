import { Box } from '@mui/system';
import Description from '@/components/core/Headings/Description.tsx';
import { ReactNode } from 'react';
import Typography from '@mui/material/Typography';

type PageHeaderProps = {
  title: string;
  description?: string;
  breadcrumb?: ReactNode;
  actions?: ReactNode[];
};

const PageHeader = ({ title, description, breadcrumb, actions }: PageHeaderProps) => (
  <Box
    style={{
      display: 'flex',
      alignItems: 'flex-start',
      gap: 24,
      alignSelf: 'stretch',
    }}
  >
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        gap: 8,
        flex: '1 0 0',
      }}
    >
      {breadcrumb}
      <Typography variant='h4'>{title}</Typography>
      <Description>{description}</Description>
    </Box>
    <Box
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 8,
      }}
    >
      {actions?.map((action, index) => (
        <Box
          key={index}
          style={{
            display: 'flex',
            padding: '0px 16px',
            alignItems: 'flex-start',
          }}
        >
          {action}
        </Box>
      ))}
    </Box>
  </Box>
);

export default PageHeader;
