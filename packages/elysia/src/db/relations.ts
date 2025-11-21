import { relations } from 'drizzle-orm/relations';
import { todos, users } from './schema';

export const todosRelations = relations(todos, ({ one }) => ({
    creator: one(users, {
        fields: [todos.creatorId],
        references: [users.id],
        relationName: 'todos_creator',
    }),
    assignee: one(users, {
        fields: [todos.assigneeId],
        references: [users.id],
        relationName: 'todos_assignee',
    }),
}));

export const usersRelations = relations(users, ({ many }) => ({
    createdTodos: many(todos, {
        relationName: 'todos_creator',
    }),
    assignedTodos: many(todos, {
        relationName: 'todos_assignee',
    }),
}));
