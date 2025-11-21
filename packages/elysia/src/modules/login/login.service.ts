export async function login(account: string, password: string): Promise<Users> {
    return {
        id: '019a861e-8e0b-7232-bac0-f3751771bb6a',
        name: 'nakamura akira',
    };
}

export interface Users {
    id: string;
    name: string;
}
