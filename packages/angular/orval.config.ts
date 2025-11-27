import { defineConfig } from 'orval';

export default defineConfig({
    petstore: {
        input: {
            target: '../spring-boot/schema.json',
        },
        output: {
            clean: ['./src/app/shared/api/**/*'],
            target: './src/app/shared/api/',
            schemas: './src/app/shared/api/model',
            client: 'angular',
            baseUrl: 'http://localhost:56080/api/',
            mock: false,
            namingConvention: 'kebab-case',
            mode: 'tags-split',
        },
        hooks: {
            afterAllFilesWrite: 'prettier --write .',
        },
    },
});
