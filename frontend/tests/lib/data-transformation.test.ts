import { transformConnectionsResponse } from "@/lib/data-transformation";
import { Connection, ServerConnection } from "@/types/Connection";

describe("tranformConnectionResponse", () => {
  it("should transform empty attributes and lastFetch:PT1M correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection(null, null),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("1m ago");
    expect(result[0].attributes).toEqual([]);
  });
  it("should transform empty attributes and lastFetch:PT1H correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection("PT1H", null),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("1h ago");
    expect(result[0].attributes).toEqual([]);
  });
  it("should transform empty attributes and lastFetch:PT1H30M correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection("PT1H30M", null),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("1h 30m ago");
    expect(result[0].attributes).toEqual([]);
  });
  it("should transform empty attributes and lastFetch:PT1H30M30.5S correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection("PT1H30M30.5S", null),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("1h 30m 31s ago");
    expect(result[0].attributes).toEqual([]);
  });
  it("should transform empty attributes and lastFetch just now correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection("PT0S", null),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("just now");
    expect(result[0].attributes).toEqual([]);
  });
  it("should transform attributes correctly", () => {
    const result: Connection[] = transformConnectionsResponse([
      createTestConnection(null, {
        environment: "test",
        region: "us-east-1",
      }),
    ]);

    expect(result.length).toBe(1);
    expect(result[0].lastFetch).toBe("1m ago");
    expect(result[0].attributes).toEqual([
      { key: "environment", value: "test" },
      { key: "region", value: "us-east-1" },
    ]);
  });
});

function createTestConnection(
  timeSinceLastFetch: string | null,
  attributes: Record<string, string> | null
): ServerConnection {
  return {
    connectionId: "123e4567-e89b-12d3-a456-426614174000",
    registrationTime: new Date().toISOString(),
    serviceName: "service",
    timeSinceLastFetch: timeSinceLastFetch ?? "PT1M",
    gepardVersion: "1.0.0",
    otelVersion: "1.0.0",
    vmId: "vm",
    connectionStatus: "CONNECTED",
    startTime: new Date().toISOString(),
    javaVersion: "17.0.1",
    attributes: attributes ?? {},
  };
}
