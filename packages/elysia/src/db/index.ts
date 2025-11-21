import 'dotenv/config';
import { drizzle } from 'drizzle-orm/bun-sql';
import os from 'node:os';

/**
 * DB インスタンス
 */
export default drizzle({
    connection: {
        url: encodeURI(process.env.DATABASE_URL!),
        max: os.cpus().length * 2 + 1,
        idleTimeout: 300,
    },
});
