// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    modules: ['@pinia/nuxt',],
    devtools: {enabled: true},
    css: ['~/assets/css/main.css'],
    compatibilityDate: '2024-07-16',
    postcss: {
        plugins: {
            tailwindcss: {},
            autoprefixer: {},
        },
    },
})