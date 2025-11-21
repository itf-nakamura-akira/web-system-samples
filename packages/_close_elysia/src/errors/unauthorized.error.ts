/**
 * 認証エラー
 */
export class Unauthorized extends Error {
    readonly status = 401;

    constructor(
        public message: string,
        public cause?: string,
    ) {
        super(message);
    }
}
