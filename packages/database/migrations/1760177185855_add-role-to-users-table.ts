import { type ColumnDefinitions, MigrationBuilder } from 'node-pg-migrate';

export const shorthands: ColumnDefinitions | undefined = undefined;

export async function up(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        ALTER TABLE public.users ADD "role" public."role" DEFAULT 'Common' NOT NULL;
        COMMENT ON COLUMN public.users."role" IS '機能(画面・API)の使用権限に関わるロール';
    `);
}

export async function down(pgm: MigrationBuilder): Promise<void> {
    pgm.sql(`
        ALTER TABLE public.users DROP COLUMN "role";
    `);
}
