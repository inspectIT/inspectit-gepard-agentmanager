import { z } from "zod";

export const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

const GenericConnectionSchema = z.object({
  id: z.string().uuid(),
  registrationTime: z.string(),
  serviceName: z.string(),
  gepardVersion: z.string(),
  otelVersion: z.string(),
  pid: z.number().int(),
  startTime: z
    .string()
    .regex(iso8601Regex, "Invalid ISO 8601 UTC timestamp format"),
  javaVersion: z.string(),
});
export const ConnectionSchema = GenericConnectionSchema.extend({
  attributes: z.array(
    z.object({
      key: z.string(),
      value: z.string(),
    })
  ),
});

export const ServerConnectionSchema = GenericConnectionSchema.extend({
  attributes: z.record(z.string()),
});

export type ServerConnection = z.infer<typeof ServerConnectionSchema>;
export type Connection = z.infer<typeof ConnectionSchema>;
