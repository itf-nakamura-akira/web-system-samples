import { openapi } from '@elysiajs/openapi';
import { fetch } from 'bun';
import { Elysia } from 'elysia';
import packageJson from '../package.json';
import { errors } from './errors';
import { modules } from './modules';

export const app = new Elysia()
    .use(errors)
    .use(
        openapi({
            enabled: process.env.NODE_ENV === 'development',
            documentation: {
                info: {
                    title: packageJson.name
                        .split('-')
                        .map((s) => s.charAt(0).toUpperCase() + s.slice(1))
                        .join(' '),
                    description: 'Web System Samples プロジェクトの Web API 仕様書',
                    version: packageJson.version,
                },
            },
        }),
    )
    .use(modules)
    .listen(process.env.SERVER_PORT || 8080);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);

// 開発用コード
if (process.env.NODE_ENV === 'development') {
    console.log('OpenAPI Specification: http://localhost:56080/openapi');

    // Open API 仕様書ファイルの生成
    try {
        const response: Response = await fetch('http://localhost:8080/openapi/json');
        const jsonText: string = await response.text();

        await Bun.write('src/openapi.json', jsonText);
    } catch (error) {
        console.error('Open API 仕様書ファイルの生成に失敗しました。', error);
    }
}
