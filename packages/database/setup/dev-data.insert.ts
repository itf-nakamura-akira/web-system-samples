import { SQL } from 'bun';
import { insertTodos } from './modules/insert-todos';
import { insertUsers } from './modules/insert-users';

try {
    const pg = new SQL(encodeURI(process.env.DATABASE_URL || ''));

    await pg.begin(async (tx: Bun.TransactionSQL) => {
        // ユーザーデータの追加
        await insertUsers(tx);

        // TODOデータの追加
        await insertTodos(tx);
    });
} catch (error) {
    console.error(error);
}
