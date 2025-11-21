import { Elysia } from 'elysia';
import { login } from './modules/login';

const app = new Elysia().use(login).listen(8080);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);
