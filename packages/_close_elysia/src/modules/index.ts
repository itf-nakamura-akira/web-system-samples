import Elysia from 'elysia';
import { common } from './common/common.controller';
import { login } from './login/login.controller';

export const modules = new Elysia({ prefix: '/api' }).use([login, common]);
