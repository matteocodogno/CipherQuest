import { GTMProvider, useGTMDispatch } from '@elgorditosalsero/react-gtm-hook';
import { ReactElement, ReactNode, useEffect } from 'react';
import { config } from '@/config.ts';
import { usePathname } from '@/hooks/use-pathname.ts';
import { useSearchParams } from 'react-router-dom';

type PageViewTrackerProps = {
  children: ReactNode;
};

function PageViewTracker({ children }: PageViewTrackerProps): ReactElement {
  const dispatch = useGTMDispatch();
  const pathname = usePathname();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    dispatch({ event: 'page_view', page: pathname });
  }, [dispatch, pathname, searchParams]);

  return <>{children}</>;
}

type AnalyticsProps = {
  children: ReactNode;
};

/**
 * This loads GTM and tracks the page views.
 *
 * If GTM ID is not configured, this will no track any event.
 */
export function Analytics({ children }: AnalyticsProps): ReactElement {
  if (!config.gtm?.id) {
    return <>{children}</>;
  }

  return (
    <GTMProvider state={{ id: config.gtm.id }}>
      <PageViewTracker>{children}</PageViewTracker>
    </GTMProvider>
  );
}
