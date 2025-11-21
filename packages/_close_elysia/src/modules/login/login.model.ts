import { t } from 'elysia';

export const LoginRequestBody = t.Object({
    account: t.String({
        description: 'ログイン認証に使うアカウント',
    }),
    password: t.String({
        description: 'ログイン認証に使うパスワード',
    }),
});

export const LoginResponse = t.Object({
    accessToken: t.String({
        description: '認可に使うアクセストークン',
    }),
    refreshToken: t.String({
        description: '認可に使うアクセストークンをリフレッシュするためのリフレッシュトークン',
    }),
});
