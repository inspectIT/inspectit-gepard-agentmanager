import { ServerConnection } from "@/types/Connection";

export const generateMockConnection = (
  serviceName: string
): ServerConnection => ({
  id: "123e4567-e89b-12d3-a456-426614174000",
  registrationTime: new Date().toISOString(),
  serviceName: serviceName,
  gepardVersion: "1.0.0",
  otelVersion: "1.0.0",
  pid: 12345,
  startTime: new Date().toISOString(),
  javaVersion: "17.0.1",
  attributes: {
    environment: "test",
    region: "us-east-1",
  },
});
