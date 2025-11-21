import Elysia from 'elysia';
import { Forbidden } from './forbidden.error';
import { Unauthorized } from './unauthorized.error';

// カスタムエラーの登録
export const errorHandler = new Elysia()
    .error('UNAUTHORIZED', Unauthorized)
    .error('FORBIDDEN', Forbidden)
    .onError(({ code, error, set }) => {
        if ('cause' in error && error.cause) {
            console.error(error.cause);
        }

        // カスタムエラーの場合は、エラーに定義されたステータスとメッセージを使用
        if (code === 'UNAUTHORIZED' || code === 'FORBIDDEN') {
            set.status = error.status;

            return { message: error.message };
        }

        set.status = 500;

        return { message: 'サーバーでエラーが発生しました。' };
    });
