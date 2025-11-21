import Elysia from 'elysia';
import { login } from './login/login.controller';

export const modules = new Elysia({ prefix: '/api' }).use(login);
