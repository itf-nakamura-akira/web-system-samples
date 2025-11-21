import Elysia from 'elysia';
import { Unauthorized } from './unauthorized.error';

export const errors = new Elysia()
    // カスタムエラーの登録
    .error('UNAUTHORIZED_ERROR', Unauthorized)
    // グローバルエラーハンドリング
    .onError(({ error, status }) => {
        if ('cause' in error && error.cause) {
            console.error(error.cause);
        }

        const response = error as { status?: number; message?: string };

        return status(response.status || 500, response.message || 'サーバーでエラーが発生しました。');
    });
