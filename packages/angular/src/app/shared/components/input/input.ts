import { booleanAttribute, ChangeDetectionStrategy, Component, ElementRef, inject, input } from '@angular/core';

/**
 * テキスト入力のサイズ
 */
export type InputSize = 'small' | 'medium' | 'large';

/**
 * テキスト入力コンポーネント
 *
 * 参考: https://primer.style/product/components/text-input/
 */
@Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'input[app-input]',
    templateUrl: './input.html',
    styleUrls: ['./input.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.form-control]': 'true',
        '[class.form-control-small]': 'size() === "small"',
        '[class.form-control-large]': 'size() === "large"',
        '[class.form-control-monospace]': 'monospace()',
        '[attr.disabled]': 'disabled() ? true : null',
        '[class.align-middle]': 'true',
        '[class.rounded-md]': 'true',
        '[class.shadow-sm]': 'true',
        '[class.transition-colors]': 'true',
        '[class.text-red-700]': 'isInvalid()',
        '[class.border]': 'true',
        '[class.border-red-700]': 'isInvalid()',
        '[class.border-gray-300]': '!isInvalid()',
        '[class.focus:border-red-700]': 'isInvalid()',
        '[class.focus:border-blue-500]': '!isInvalid()',
        '[class.focus:outline-none]': 'true',
    },
})
export class Input {
    /**
     * ElementRef
     */
    private readonly elementRef = inject(ElementRef);

    /**
     * 入力欄のサイズ
     */
    readonly size = input<InputSize>('medium');

    /**
     * モノスペースフォント
     */
    readonly monospace = input(false, { transform: booleanAttribute });

    /**
     * 非活性
     */
    readonly disabled = input(false, { transform: booleanAttribute });

    /**
     * 不正な値
     */
    readonly isInvalid = input(false, { transform: booleanAttribute });

    /**
     * フォーカスする
     */
    focus(): void {
        this.elementRef.nativeElement.focus();
    }
}
