import { type ColumnDefinitions, MigrationBuilder } from 'node-pg-migrate';

export const shorthands: ColumnDefinitions | undefined = undefined;

export async function up(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        CREATE TABLE public.todos (
            id uuid DEFAULT uuidv7() NOT NULL, 
            created_by uuid NOT NULL,
            assignee_id uuid NOT NULL,
            title text NOT NULL,
            memo text NULL,
            due_date date NULL,
            completed_at timestamptz NULL,
            created_at timestamptz DEFAULT now() NOT NULL,
            updated_at timestamptz DEFAULT now() NOT NULL,
            CONSTRAINT todos_pk PRIMARY KEY (id),
            CONSTRAINT todos_created_by_fk FOREIGN KEY (created_by) REFERENCES public.users(id) ON DELETE CASCADE,
            CONSTRAINT todos_assignee_id_fk FOREIGN KEY (assignee_id) REFERENCES public.users(id) ON DELETE CASCADE
        );
        COMMENT ON TABLE public.todos IS 'TODOテーブル';
        COMMENT ON COLUMN public.todos.id IS 'ID';
        COMMENT ON COLUMN public.todos.created_by IS '作成者ID';
        COMMENT ON COLUMN public.todos.assignee_id IS '担当者ID';
        COMMENT ON COLUMN public.todos.title IS 'TODOのタイトル';
        COMMENT ON COLUMN public.todos.memo IS 'メモ';
        COMMENT ON COLUMN public.todos.due_date IS '期限日';
        COMMENT ON COLUMN public.todos.completed_at IS '完了日時';
        COMMENT ON COLUMN public.todos.created_at IS '作成日時';
        COMMENT ON COLUMN public.todos.updated_at IS '更新日時';
    `);
}

export async function down(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        DROP TABLE public.todos;
    `);
}
