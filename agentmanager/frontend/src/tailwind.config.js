/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./app.vue",
    "./error.vue",
    './src/**/*.vue',
    './public/**/*.vue',
    './src/pages/**/*.{vue,js,ts,jsx,tsx}', // Pfade zu den Vue/JS/TS Dateien im pages-Ordner
    './src/components/**/*.{vue,js,ts,jsx,tsx}', // Pfade zu den Vue/JS/TS Dateien im components-Ordner

  ],
  theme: {
    extend: {},
  },
  plugins: [],
}

