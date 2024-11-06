import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import ConnectionsView from "@/components/features/connections/ConnectionsView";
import { Connection } from "@/types/Connection";
import { generateMockConnection } from "../../../mocks-data";

const mockConnections: Connection[] = [
  generateMockConnection("service-1"),
  generateMockConnection("service-2"),
];

describe("ConnectionsView", () => {
  it("renders ConnectionsView component", () => {
    render(<ConnectionsView connections={mockConnections} />);
    expect(screen.getByText("Connections")).toBeInTheDocument();
    expect(
      screen.getByText("All currently connected agents.")
    ).toBeInTheDocument();
  });

  it("renders the correct number of connections", () => {
    render(<ConnectionsView connections={mockConnections} />);
    expect(screen.getAllByRole("row")).toHaveLength(3);
  });

  it("filters connections based on search input", async () => {
    render(<ConnectionsView connections={mockConnections} />);
    const searchInput = screen.getByPlaceholderText("Search all columns...");
    fireEvent.change(searchInput, { target: { value: "service-1" } });

    await waitFor(
      () => {
        expect(screen.getByText("service-1")).toBeInTheDocument();
        expect(screen.queryByText("service-2")).not.toBeInTheDocument();
      },
      { timeout: 1000 }
    );
  });

  it("clears the filter when search input is empty", async () => {
    render(<ConnectionsView connections={mockConnections} />);
    const searchInput = screen.getByPlaceholderText("Search all columns...");
    fireEvent.change(searchInput, { target: { value: "service-1" } });
    fireEvent.change(searchInput, { target: { value: "" } });

    await waitFor(
      () => {
        expect(screen.getByText("service-1")).toBeInTheDocument();
        expect(screen.queryByText("service-2")).toBeInTheDocument();
      },
      { timeout: 1000 }
    );
  });

  it("renders JVM Start Time Correctly", () => {
    const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

    render(<ConnectionsView connections={mockConnections} />);
    const elements = screen.getAllByText(iso8601Regex);
    expect(screen.getByText("JVM Start Time")).toBeInTheDocument();
    expect(elements).toHaveLength(2);
  });
});
