import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

/**
 * フォームコントロール
 */
@Component({
    selector: 'app-form-control',
    templateUrl: './form-control.html',
    styleUrl: './form-control.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        class: 'flex flex-col gap-1',
    },
})
export class FormControlComponent {
    /**
     * * 紐づくinputのid
     */
    readonly for = input<string>();

    /**
     * ラベル
     */
    readonly label = input.required<string>();

    /**
     * キャプション
     */
    readonly caption = input<string>();

    /**
     * エラー
     */
    readonly errors = input<string[]>();

    /**
     * エラーがあるかどうか
     */
    readonly isInvalid = computed<boolean>(() => {
        const errors: string[] | undefined = this.errors();

        return !!errors && errors.length > 0;
    });
}
