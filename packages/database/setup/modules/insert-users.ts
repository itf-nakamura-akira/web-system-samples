import { FAMILY_NAMES } from './family-name';
import { FIRST_NAMES } from './first-name';
import { HASHED_PASSWORD } from './hashed-password';

type InsertUsers = {
    account: string;
    hashed_password: string;
    name: string;
    role: string;
};

export async function insertUsers(tx: Bun.TransactionSQL): Promise<void> {
    // 管理者ユーザーの追加
    const adminUsers: InsertUsers[] = [
        { account: 'nakamura.akira', hashed_password: HASHED_PASSWORD.pop()!, name: '中村輝', role: 'Admin' },
        { account: 'yamada.taro', hashed_password: HASHED_PASSWORD.pop()!, name: '山田太郎', role: 'Admin' },
        { account: 'sato.hanako', hashed_password: HASHED_PASSWORD.pop()!, name: '佐藤花子', role: 'Admin' },
        { account: 'suzuki.ichiro', hashed_password: HASHED_PASSWORD.pop()!, name: '鈴木一郎', role: 'Admin' },
        { account: 'takahashi.misaki', hashed_password: HASHED_PASSWORD.pop()!, name: '高橋美咲', role: 'Admin' },
        { account: 'tanaka.kenta', hashed_password: HASHED_PASSWORD.pop()!, name: '田中健太', role: 'Admin' },
        { account: 'watanabe.naomi', hashed_password: HASHED_PASSWORD.pop()!, name: '渡辺直美', role: 'Admin' },
        { account: 'ito.daisuke', hashed_password: HASHED_PASSWORD.pop()!, name: '伊藤大輔', role: 'Admin' },
        { account: 'nakamura.sakura', hashed_password: HASHED_PASSWORD.pop()!, name: '中村さくら', role: 'Admin' },
        { account: 'kobayashi.yuta', hashed_password: HASHED_PASSWORD.pop()!, name: '小林雄太', role: 'Admin' },
    ];

    // 一般ユーザーの追加 (x 6402)
    const commonUsers: InsertUsers[] = FAMILY_NAMES.flatMap((familyName) =>
        FIRST_NAMES.map((firstName) => ({
            account: `${familyName.alphabet}.${firstName.alphabet}`,
            hashed_password: HASHED_PASSWORD.pop()!,
            name: `${familyName.kanji}${firstName.kanji}`,
            role: 'Common',
        })),
    )
        // ハッシュ値で並び替えて混ぜる
        .sort(
            (a, b) =>
                a.hashed_password
                    .split('$argon2id$v=19$m=65536,t=2,p=1$')[1]
                    ?.localeCompare(b.hashed_password.split('$argon2id$v=19$m=65536,t=2,p=1$')[1] || '') || 1,
        );

    await tx`INSERT INTO public.users ${tx([...adminUsers, ...commonUsers], 'account', 'hashed_password', 'name', 'role')};`;
}
