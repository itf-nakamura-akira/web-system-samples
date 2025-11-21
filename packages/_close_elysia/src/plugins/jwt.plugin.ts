import { jwt as jwtPlugin } from '@elysiajs/jwt';
import Elysia from 'elysia';

/**
 * JWT 共通設定
 */
const commonClaims = {
    secret: process.env.JWT_SECRET_PRIVATE_KEY!,
    iss: process.env.JWT_ISSUER!,
};

/**
 * JWT アクセストークン Plugin
 */
const jwtAccessToken = jwtPlugin({
    ...commonClaims,
    name: 'jwtAccessToken',
    exp: `${process.env.JWT_ACCESS_TOKEN_EXPIRE_MINUTES}m`,
});

/**
 * JWT リフレッシュトークン Plugin
 */
const jwtRefreshToken = jwtPlugin({
    ...commonClaims,
    name: 'jwtRefreshToken',
    exp: `${process.env.JWT_REFRESH_TOKEN_EXPIRE_DAYS}d`,
});

/**
 * JWT Plugin
 */
export const jwt = new Elysia().use([jwtAccessToken, jwtRefreshToken]);
