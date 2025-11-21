import { eq } from 'drizzle-orm';
import db from '../../db';
import { users } from '../../db/schema';
import { Unauthorized } from '../../errors/unauthorized.error';

/**
 * 認証処理を行う
 *
 * @param account アカウント
 * @param password パスワード
 * @returns ユーザーデータ
 */
export async function login(account: string, password: string): Promise<typeof users.$inferSelect> {
    const user: (typeof users.$inferSelect)[] = await db.select().from(users).where(eq(users.account, account));

    if (user.length === 0) {
        throw new Unauthorized(
            'アカウントまたはパスワードが誤っています。',
            `アカウント ${account} が users テーブルに見つかりません。`,
        );
    }

    const isPasswordValid: boolean = await Bun.password.verify(password, user[0].hashedPassword);

    if (!isPasswordValid) {
        // パスワードが一致しない場合、エラーをスローします。
        throw new Unauthorized(
            'アカウントまたはパスワードが誤っています。',
            `アカウント ${account} のパスワードが一致しません。`,
        );
    }

    return user[0];
}
