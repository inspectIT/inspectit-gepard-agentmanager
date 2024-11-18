/// <reference types="vitest" />
import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    emptyOutDir: true,
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  test: {
    environment: "jsdom",
    globals: true,
    setupFiles: "tests/vitest.setup.ts",
    env: loadEnv("development", process.cwd(), ""),
    coverage: {
      include: ["src/*/**"],
      exclude: ["src/**/shadcn/**"],
    },
  },
});
