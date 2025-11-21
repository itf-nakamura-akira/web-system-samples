import Elysia from 'elysia';
import { users } from '../../db/schema';
import { jwtAccessToken, jwtRefreshToken } from '../../utils/jwt';
import * as model from './login.model';
import * as service from './login.service';

/**
 * ログイン Controller
 */
export const login = new Elysia({ prefix: '/login' }).use([jwtAccessToken, jwtRefreshToken]).post(
    '',
    async ({ jwtAccessToken, jwtRefreshToken, body }) => {
        const user: typeof users.$inferSelect = await service.login(body.account, body.password);

        return {
            accessToken: await jwtAccessToken.sign({ sub: user.account }),
            refreshToken: await jwtRefreshToken.sign({ sub: user.account }),
        };
    },
    {
        body: model.LoginRequestBody,
        response: model.LoginResponse,
    },
);
