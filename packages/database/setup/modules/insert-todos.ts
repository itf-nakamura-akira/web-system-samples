type InsertTodos = {
    creator_id: string;
    assignee_id: string;
    title: string;
    memo: string;
    due_date?: string;
    completed_at?: Date;
};

export async function insertTodos(tx: Bun.TransactionSQL): Promise<void> {
    const users: { id: string; name: string }[] = await tx`SELECT id, "name" FROM public.users`;
    const insertTodos: InsertTodos[] = [];

    for (const user of users) {
        // 自分が作ったTODOを5件追加
        for (let i = 1; i <= 5; i++) {
            const isCompleted = i === 1;
            const dueDate = new Date();

            dueDate.setDate(dueDate.getDate() + i);
            insertTodos.push({
                creator_id: user.id,
                assignee_id: user.id,
                title: `【${user.name}】のタスク ${i}`,
                memo: `これは ${user.name} が自分で作ったタスク ${i}個目`,
                due_date: dueDate.toISOString().split('T')[0],
                completed_at: isCompleted ? new Date() : undefined,
            });
        }

        // 他の人が作ったTODOを3件追加
        const otherUsers = users.filter((u) => u.id !== user.id);
        const randomIndex = parseInt(user.id.slice(-6), 16) % otherUsers.length;
        const otherUser = otherUsers[randomIndex];

        if (otherUser !== undefined) {
            for (let i = 1; i <= 3; i++) {
                const isCompleted = i === 1;
                const dueDate = new Date();

                dueDate.setDate(dueDate.getDate() + i + 5);
                insertTodos.push({
                    creator_id: otherUser.id,
                    assignee_id: user.id,
                    title: `【${otherUser.name}より】${user.name} へのタスク ${i}`,
                    memo: `${otherUser.name}からのお願いごと ${i}個目`,
                    due_date: dueDate.toISOString().split('T')[0],
                    completed_at: isCompleted ? new Date() : undefined,
                });
            }
        }

        // 一気に大量のレコードが入らないので分ける
        if (insertTodos.length > 5000) {
            await insert(tx, insertTodos);
            insertTodos.length = 0;
        }
    }

    await insert(tx, insertTodos);
}

async function insert(tx: Bun.TransactionSQL, insertTodos: InsertTodos[]): Promise<void> {
    await tx`INSERT INTO public.todos ${tx(insertTodos, 'creator_id', 'assignee_id', 'title', 'memo', 'due_date', 'completed_at')};`;
}
