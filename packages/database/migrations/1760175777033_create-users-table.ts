import { type ColumnDefinitions, MigrationBuilder } from 'node-pg-migrate';

export const shorthands: ColumnDefinitions | undefined = undefined;

export async function up(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        CREATE TABLE public.users (
            id uuid DEFAULT uuidv7() NOT NULL,
            account text NOT NULL,
            hashed_password text NOT NULL,
            "name" text NOT NULL,
            disabled_at timestamp with time zone NULL,
            CONSTRAINT users_pk PRIMARY KEY (id),
            CONSTRAINT users_unique UNIQUE (account)
        );
        COMMENT ON TABLE public.users IS 'ユーザーテーブル';

        -- Column comments

        COMMENT ON COLUMN public.users.id IS 'ID';
        COMMENT ON COLUMN public.users.account IS 'アカウント名';
        COMMENT ON COLUMN public.users.hashed_password IS 'ハッシュ化済みパスワード';
        COMMENT ON COLUMN public.users."name" IS '表示名';
        COMMENT ON COLUMN public.users.disabled_at IS '無効化日時';
    `);
}

export async function down(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        DROP TABLE public.users;
    `);
}
