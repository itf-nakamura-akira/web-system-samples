import Elysia from 'elysia';
import * as model from './login.model';
import * as service from './login.service';

export const login = new Elysia({ prefix: '/login' }).post(
    '',
    async ({ body }) => {
        const user = await service.login(body.account, body.password);

        return {
            accessToken: 'accessToken',
            refreshToken: 'refreshToken',
        };
    },
    {
        body: model.LoginRequestBody,
        response: model.LoginResponse,
    },
);
