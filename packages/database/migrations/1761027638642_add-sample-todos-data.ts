import { type ColumnDefinitions, MigrationBuilder } from 'node-pg-migrate';

export const shorthands: ColumnDefinitions | undefined = undefined;

export async function up(pgm: MigrationBuilder): Promise<void> {
    if (process.env.NODE_ENV !== 'development') {
        return;
    }

    const users: { id: string; name: string }[] = await pgm.db.select(`SELECT id, "name" FROM public.users`);

    for (const user of users) {
        const userId = user.id;
        const userName = user.name;

        // 自分が作ったTODOを5件追加
        for (let i = 1; i <= 5; i++) {
            const title = `【${userName}】のタスク ${i}`;
            const memo = `これは ${userName} が自分で作ったタスク ${i}個目`;
            const dueDate = new Date();

            dueDate.setDate(dueDate.getDate() + i);

            const isCompleted = i === 1;

            pgm.sql(`
                INSERT INTO public.todos
                    (creator_id, assignee_id, title, memo, due_date, completed_at)
                VALUES
                    ('${userId}', '${userId}', '${title}', '${memo}', ${isCompleted ? 'NULL' : `'${dueDate.toISOString().split('T')[0]}'`}, ${isCompleted ? 'now()' : 'NULL'});
            `);
        }

        // 他の人が作ったTODOを3件追加
        const otherUsers = users.filter((u) => u.id !== userId);
        const randomIndex = parseInt(userId.slice(-6), 16) % otherUsers.length;
        const otherUser = otherUsers[randomIndex];

        if (otherUser !== undefined) {
            for (let i = 1; i <= 3; i++) {
                const title = `【${otherUser.name}より】${userName} へのタスク ${i}`;
                const memo = `${otherUser.name}からのお願いごと ${i}個目`;
                const dueDate = new Date();

                dueDate.setDate(dueDate.getDate() + i + 5);

                const isCompleted = i === 1;

                pgm.sql(`
                    INSERT INTO public.todos
                        (creator_id, assignee_id, title, memo, due_date, completed_at)
                    VALUES
                        ('${otherUser.id}', '${userId}', '${title}', '${memo}', ${isCompleted ? 'NULL' : `'${dueDate.toISOString().split('T')[0]}'`}, ${isCompleted ? 'now()' : 'NULL'});
                `);
            }
        }
    }
}

export async function down(pgm: MigrationBuilder): Promise<void> {
    if (process.env.NODE_ENV !== 'development') {
        return;
    }

    pgm.sql(`TRUNCATE TABLE public.todos;`);
}
