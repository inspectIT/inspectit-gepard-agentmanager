import {
  QueryClient,
  QueryClientConfig,
  QueryClientProvider,
} from "@tanstack/react-query";
import { render } from "@testing-library/react";

export function renderWithClient(client: QueryClient, ui: React.ReactElement) {
  const { rerender, ...result } = render(
    <QueryClientProvider client={client}>{ui}</QueryClientProvider>
  );
  return {
    ...result,
    rerender: (rerenderUi: React.ReactElement) => {
      rerender(
        <QueryClientProvider client={client}>{rerenderUi}</QueryClientProvider>
      );
    },
  };
}

export function createQueryClient(config?: QueryClientConfig): QueryClient {
  return new QueryClient(config);
}
