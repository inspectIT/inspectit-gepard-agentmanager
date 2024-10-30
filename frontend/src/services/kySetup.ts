import ky from "ky";

const API_URL = import.meta.env.VITE_API_BASE_URL as string;

export const http = ky.extend({
  prefixUrl: API_URL,
  hooks: {
    beforeRequest: [
      (req) => {
        if (import.meta.env.DEV) console.log(`Making request to: ${req.url}`);
      },
    ],
  },
});
