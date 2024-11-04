// Imports
import { describe, it, expect } from "vitest";
import ConnectionsPage from "../ConnectionsPage";
import { createQueryClient, renderWithClient } from "@/tests/utils";
import { beforeEach } from "node:test";

// Tests
describe("Renders main page correctly", () => {
  const queryClient = createQueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  beforeEach(() => {
    queryClient.clear();
  });

  it("Successfully fetched data", async () => {
    const result = renderWithClient(queryClient, <ConnectionsPage />);

    expect(await result.findByText(/Connections/i));
  });
});
