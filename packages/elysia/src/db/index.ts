import { drizzle } from 'drizzle-orm/bun-sql';
import os from 'node:os';
import * as relations from './relations';
import * as schema from './schema';

/**
 * DB インスタンス
 */
export default drizzle({
    schema: {
        ...schema,
        ...relations,
    },
    connection: {
        url: encodeURI(process.env.DATABASE_URL!),
        max: os.cpus().length * 2 + 1,
        idleTimeout: 300,
    },
    logger: process.env.NODE_ENV === 'development',
});
