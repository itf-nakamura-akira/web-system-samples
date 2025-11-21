import { openapi as openapiPlugin } from '@elysiajs/openapi';
import packageJson from '../package.json';

/**
 * OpenAPI ドキュメントの定義書
 */
export const openapi = openapiPlugin({
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
});
