import db from '../../db';
import { Unauthorized } from '../../errors/unauthorized.error';

export async function login(account: string, password: string): Promise<any> {
    const user = await db.query.users.findFirst({
        where: (users, { eq }) => eq(users.account, account),
    });

    if (!user) {
        throw new Unauthorized(
            'アカウントまたはパスワードが誤っています。',
            `アカウント ${account} が users テーブルに見つかりません。`,
        );
    }

    console.log(user);

    return {
        id: '019a861e-8e0b-7232-bac0-f3751771bb6a',
        name: 'nakamura akira',
    };
}
