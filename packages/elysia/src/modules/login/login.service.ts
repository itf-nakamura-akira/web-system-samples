import { eq } from 'drizzle-orm';
import db from '../../db';
import { users } from '../../db/schema';
import { Unauthorized } from '../../errors/unauthorized.error';

export async function login(account: string, password: string): Promise<any> {
    const user = await db.select().from(users).where(eq(users.account, account));

    if (user.length === 0) {
        throw new Unauthorized(
            'アカウントまたはパスワードが誤っています。',
            `アカウント ${account} が users テーブルに見つかりません。`,
        );
    }

    const isPasswordValid = await Bun.password.verify(password, user[0].hashedPassword);

    if (!isPasswordValid) {
        // パスワードが一致しない場合、エラーをスローします。
        throw new Unauthorized(
            'アカウントまたはパスワードが誤っています。',
            `アカウント ${account} のパスワードが一致しません。`,
        );
    }

    return user[0];
}
