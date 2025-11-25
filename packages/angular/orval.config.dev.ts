import { defineConfig } from 'orval';

export default defineConfig({
    petstore: {
        input: {
            target: 'http://localhost:8080/api/api-docs',
        },
        output: {
            target: './src/app/shared/api/web-system-samples.api.ts',
            client: 'fetch',
            schemas: './src/app/shared/api/model',
            namingConvention: 'kebab-case',
            baseUrl: 'http://localhost:56080/api/',
            override: {
                fetch: {
                    includeHttpResponseReturnType: false,
                },
            },
        },
    },
});
