import { render, screen } from "@testing-library/react";
import ConnectionsView from "@/components/features/connections/ConnectionsView";
import { Connection, ServerConnection } from "@/types/Connection";
import { generateMockConnection } from "../../../mocks-data";
import { ConnectionService } from "@/services/ConnectionService";

const serverMockConnections: ServerConnection[] = [
  generateMockConnection("service-1"),
  generateMockConnection("service-2"),
];

const mockConnections: Connection[] =
  ConnectionService.transformConnectionsResponse(serverMockConnections);

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

  it("renders JVM Start Time Correctly", () => {
    const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

    render(<ConnectionsView connections={mockConnections} />);
    const elements = screen.getAllByText(iso8601Regex);
    expect(screen.getByText("JVM Start Time")).toBeInTheDocument();
    expect(elements).toHaveLength(2);
  });
});
