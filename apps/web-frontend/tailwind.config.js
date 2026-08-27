/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        forest: { 600: '#1e5128', 700: '#143d1c', 800: '#0e2b13' },
        terracotta: { 500: '#d9534f', 600: '#c9302c' },
        amber: { 500: '#f0ad4e' }
      }
    },
  },
  plugins: [],
}
