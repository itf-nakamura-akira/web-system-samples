import { defineConfig } from 'orval';

export default defineConfig({
    petstore: {
        input: {
            target: 'http://localhost:8080/api/api-docs',
        },
        output: {
            target: './src/app/shared/api/web-system-samples.api.ts',
        },
        // hooks: {
        //     afterAllFilesWrite: 'prettier --write',
        // },
    },
});
