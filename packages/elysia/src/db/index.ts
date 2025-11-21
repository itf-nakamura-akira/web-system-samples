import 'dotenv/config';
import { drizzle } from 'drizzle-orm/bun-sql';

/**
 * DB インスタンス
 */
export default drizzle(encodeURI(process.env.DATABASE_URL!));
