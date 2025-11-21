/**
 * 認可エラー
 */
export class Forbidden extends Error {
    readonly status = 403;

    constructor(
        public message: string,
        public cause?: string,
    ) {
        super(message);
    }
}
