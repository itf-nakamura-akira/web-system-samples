import { HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs';

/**
 * エラーレスポンスインターセプター
 * HTTPリクエスト中に発生したエラーを捕捉し、共通のエラー形式に変換します。
 *
 * @param req `HttpRequest`
 * @param next `HttpHandlerFn`
 * @returns `Observable<HttpEvent<unknown>>`
 */
export const errorResponseInterceptor: HttpInterceptorFn = (req, next) => {
    return next(req).pipe(
        catchError((response) => {
            console.error(response);

            if (response.error && response.error.status && response.error.message && response.error.timestamp) {
                throw response;
            }

            throw {
                error: {
                    status: 500,
                    message: 'エラーが発生しました。',
                    timestamp: new Date().toISOString(),
                },
            };
        }),
    );
};
