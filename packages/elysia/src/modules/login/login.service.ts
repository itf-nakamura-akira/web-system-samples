import db from '../../db';

export async function login(account: string, password: string): Promise<any> {
    const user = await db.query.users.findFirst({
        where: (users, { eq }) => eq(users.account, account),
        with: {
            createdTodos: true,
        },
    });

    console.log(user);

    return {
        id: '019a861e-8e0b-7232-bac0-f3751771bb6a',
        name: 'nakamura akira',
    };
}
