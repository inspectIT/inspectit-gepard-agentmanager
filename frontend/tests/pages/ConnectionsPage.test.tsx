import { useConnectionsQuery } from "@/hooks/features/connections/useConnections";
import ConnectionsPage from "@/pages/ConnectionsPage";
import { render, screen } from "@testing-library/react";
import { generateMockConnection } from "../mocks-data";
import { Mock, vi } from "vitest";

vi.mock("@/hooks/features/connections/useConnections");

describe("ConnectionsPage", () => {
  it("should render loading in loading state", () => {
    (useConnectionsQuery as Mock).mockReturnValue({ isLoading: true });

    render(<ConnectionsPage />);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  it("should render error in error state", () => {
    (useConnectionsQuery as Mock).mockReturnValue({
      isError: true,
      error: new Error("error"),
    });

    expect(() => render(<ConnectionsPage />)).toThrowError("error");
  });

  it("should render ConnectionsView in success state", () => {
    (useConnectionsQuery as Mock).mockReturnValue({
      isSuccess: true,
      data: [generateMockConnection("service-name")],
    });

    render(<ConnectionsPage />);
    expect(screen.getByText(/service-name/i)).toBeInTheDocument();
  });
});
