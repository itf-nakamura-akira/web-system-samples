import { sql } from 'drizzle-orm';
import { date, foreignKey, pgEnum, pgTable, serial, text, timestamp, unique, uuid, varchar } from 'drizzle-orm/pg-core';

export const role = pgEnum('role', ['Admin', 'Common']);

export const pgmigrations = pgTable('pgmigrations', {
    id: serial().primaryKey().notNull(),
    name: varchar({ length: 255 }).notNull(),
    runOn: timestamp('run_on', { mode: 'string' }).notNull(),
});

export const users = pgTable(
    'users',
    {
        id: uuid()
            .default(sql`uuidv7()`)
            .primaryKey()
            .notNull(),
        account: text().notNull(),
        hashedPassword: text('hashed_password').notNull(),
        name: text().notNull(),
        role: role().default('Common').notNull(),
        disabledAt: timestamp('disabled_at', { withTimezone: true, mode: 'string' }),
    },
    (table) => [unique('users_unique').on(table.account)],
);

export const todos = pgTable(
    'todos',
    {
        id: uuid()
            .default(sql`uuidv7()`)
            .primaryKey()
            .notNull(),
        creatorId: uuid('creator_id').notNull(),
        assigneeId: uuid('assignee_id').notNull(),
        title: text().notNull(),
        memo: text(),
        dueDate: date('due_date'),
        completedAt: timestamp('completed_at', { withTimezone: true, mode: 'string' }),
        createdAt: timestamp('created_at', { withTimezone: true, mode: 'string' }).defaultNow().notNull(),
        updatedAt: timestamp('updated_at', { withTimezone: true, mode: 'string' }).defaultNow().notNull(),
    },
    (table) => [
        foreignKey({
            columns: [table.creatorId],
            foreignColumns: [users.id],
            name: 'todos_creator_id_fk',
        }).onDelete('cascade'),
        foreignKey({
            columns: [table.assigneeId],
            foreignColumns: [users.id],
            name: 'todos_assignee_id_fk',
        }).onDelete('cascade'),
    ],
);
