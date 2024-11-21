import { time } from "console";
import { z } from "zod";

export const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

export const ConnectionStatusSchema = z.union([
  z.literal("CONNECTED"),
  z.literal("DISCONNECTED"),
  z.literal("LOST_CONNECTION"),
]);

const GenericConnectionSchema = z.object({
  connectionId: z.string(),
  registrationTime: z.string(),
  serviceName: z.string(),
  gepardVersion: z.string(),
  otelVersion: z.string(),
  vmId: z.string(),
  connectionStatus: ConnectionStatusSchema,
  startTime: z
    .string()
    .regex(iso8601Regex, "Invalid ISO 8601 UTC timestamp format"),
  javaVersion: z.string(),
});

export const ConnectionSchema = GenericConnectionSchema.extend({
  lastFetch: z.string(),
  attributes: z.array(
    z.object({
      key: z.string(),
      value: z.string(),
    })
  ),
});

export const ServerConnectionSchema = GenericConnectionSchema.extend({
  timeSinceLastFetch: z.string(),
  attributes: z.record(z.string()),
});

export const ConnectionQuerySchema = ServerConnectionSchema.partial();

export type ServerConnection = z.infer<typeof ServerConnectionSchema>;
export type ConnectionQuery = z.infer<typeof ConnectionQuerySchema>;
export type Connection = z.infer<typeof ConnectionSchema>;
export type ConnectionStatus = z.infer<typeof ConnectionStatusSchema>;
