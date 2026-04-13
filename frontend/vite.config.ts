import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
    server: {
      proxy: {
          '/api': { // api/v1?
              target: 'http://localhost:8080', // kiedy react poprosi o dane np z /api/users to vite przekieruje zapytanie na ten adres
              changeOrigin: true,
              secure: false,
          }
      }
    }
})
