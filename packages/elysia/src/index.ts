import { fetch } from 'bun';
import { errorHandler } from './errors';
import { modules } from './modules';
import { openapi } from './openapi';

/**
 * エントリーポイント
 */
export const app = errorHandler
    .use(openapi)
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
