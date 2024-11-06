import { z } from "zod";

export const iso8601Regex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

export const ConnectionSchema = z.object({
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
  attributes: z.record(z.string()),
});

export type Connection = z.infer<typeof ConnectionSchema>;
