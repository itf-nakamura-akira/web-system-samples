import { jwt } from '@elysiajs/jwt';
import Elysia from 'elysia';

const commonClaims = {
    secret: process.env.JWT_SECRET_PRIVATE_KEY!,
    iss: process.env.JWT_ISSUER!,
};

/**
 * JWT アクセストークン プラグイン
 */
export const jwtAccessToken = new Elysia().use(
    jwt({
        ...commonClaims,
        name: 'jwtAccessToken',
        exp: `${process.env.JWT_ACCESS_TOKEN_EXPIRE_MINUTES}m`,
    }),
);

/**
 * JWT リフレッシュトークン プラグイン
 */
export const jwtRefreshToken = new Elysia().use(
    jwt({
        ...commonClaims,
        name: 'jwtRefreshToken',
        exp: `${process.env.JWT_REFRESH_TOKEN_EXPIRE_DAYS}d`,
    }),
);
