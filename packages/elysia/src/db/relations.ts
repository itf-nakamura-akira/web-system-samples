import { relations } from 'drizzle-orm/relations';
import { todos, users } from './schema';

export const todosRelations = relations(todos, ({ one }) => ({
    user_creatorId: one(users, {
        fields: [todos.creatorId],
        references: [users.id],
        relationName: 'todos_creatorId_users_id',
    }),
    user_assigneeId: one(users, {
        fields: [todos.assigneeId],
        references: [users.id],
        relationName: 'todos_assigneeId_users_id',
    }),
}));

export const usersRelations = relations(users, ({ many }) => ({
    todos_creatorId: many(todos, {
        relationName: 'todos_creatorId_users_id',
    }),
    todos_assigneeId: many(todos, {
        relationName: 'todos_assigneeId_users_id',
    }),
}));
