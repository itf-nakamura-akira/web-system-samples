import { type ColumnDefinitions, MigrationBuilder } from 'node-pg-migrate';

export const shorthands: ColumnDefinitions | undefined = undefined;

export async function up(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        CREATE TABLE public.refresh_tokens (
            id uuid DEFAULT uuidv7() NOT NULL,
            users_id uuid NOT NULL,
            hashed_token bytea NOT NULL,
            created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
            expires_at timestamp with time zone NOT NULL,
            revoked boolean DEFAULT FALSE NOT NULL,
            CONSTRAINT refresh_tokens_pk PRIMARY KEY (id),
            CONSTRAINT refresh_tokens_users_fk FOREIGN KEY (users_id) REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE CASCADE
        );
        COMMENT ON TABLE public.refresh_tokens IS 'リフレッシュトークン情報を管理するテーブル';

        -- Column comments

        COMMENT ON COLUMN public.refresh_tokens.id IS 'ID';
        COMMENT ON COLUMN public.refresh_tokens.users_id IS 'ユーザーテーブルID';
        COMMENT ON COLUMN public.refresh_tokens.hashed_token IS 'ハッシュ化済みリフレッシュトークン';
        COMMENT ON COLUMN public.refresh_tokens.created_at IS '発行日時';
        COMMENT ON COLUMN public.refresh_tokens.expires_at IS '失効日時';
        COMMENT ON COLUMN public.refresh_tokens.revoked IS '失効フラグ';
        
        CREATE INDEX refresh_tokens_hashed_token_idx ON public.refresh_tokens (hashed_token) WHERE revoked = FALSE;
    `);
}

export async function down(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        DROP TABLE public.refresh_tokens;
    `);
}
