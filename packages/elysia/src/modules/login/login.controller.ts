import Elysia from 'elysia';
import { users } from '../../db/schema';
import { jwt } from '../../plugins/jwt.plugin';
import * as model from './login.model';
import * as service from './login.service';

/**
 * ログイン Controller
 */
export const login = new Elysia({ prefix: '/login' }).use(jwt).post(
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
