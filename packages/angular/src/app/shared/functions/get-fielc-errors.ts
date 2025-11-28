import { FieldTree } from '@angular/forms/signals';

/**
 * フィールドのエラーメッセージを取得します。
 *
 * @param field 対象フィールド
 * @returns エラーメッセージ
 */
export function getFieldErrors(field: FieldTree<string | number | Date, string>): string[] | undefined {
    if (!field().touched() || !field().invalid()) {
        return undefined;
    }

    return field()
        .errors()
        .map((error) => error.message)
        .filter((message) => !!message) as string[];
}
