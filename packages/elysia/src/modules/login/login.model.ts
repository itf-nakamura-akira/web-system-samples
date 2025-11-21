import { t } from 'elysia';

export const LoginRequestBody = t.Object({
    account: t.String(),
    password: t.String(),
});

export const LoginResponse = t.Object({
    accessToken: t.String(),
    refreshToken: t.String(),
});
