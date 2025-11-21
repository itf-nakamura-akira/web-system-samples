import Elysia, { t } from 'elysia';

/**
 * 一般 Controller
 */
export const common = new Elysia({ prefix: '/common' }).get(
    '/loginUser',
    async () => {
        return {
            hoge: '12345',
        };
    },
    {
        response: t.Object({
            hoge: t.String(),
        }),
    },
);
