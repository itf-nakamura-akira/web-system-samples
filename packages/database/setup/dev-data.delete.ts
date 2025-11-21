import { SQL } from 'bun';

try {
    const pg = new SQL(encodeURI(process.env.DATABASE_URL || ''));

    await pg.begin(async (tx: Bun.TransactionSQL) => {
        await tx`TRUNCATE public.users RESTART IDENTITY CASCADE;`;
    });
} catch (error) {
    console.error(error);
}
