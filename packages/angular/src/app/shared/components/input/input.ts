import { CommonModule } from '@angular/common';
import { booleanAttribute, ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * テキスト入力のサイズ
 */
export type InputSize = 'small' | 'medium' | 'large';

/**
 * テキスト入力のタイプ
 */
export type InputType = 'text' | 'email' | 'password' | 'number' | 'search' | 'tel' | 'url';

/**
 * テキスト入力コンポーネント
 *
 * 参考: https://primer.style/product/components/text-input/
 */
@Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'input[app-input]',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './input.html',
    styleUrls: ['./input.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.form-control]': 'true',
        '[class.form-control-small]': 'size() === "small"',
        '[class.form-control-large]': 'size() === "large"',
        '[class.form-control-monospace]': 'monospace()',
        '[attr.disabled]': 'disabled() ? true : null',
        '[attr.placeholder]': 'placeholder()',
        '[attr.type]': 'type()',
    },
})
export class Input {
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
     * プレースホルダー
     */
    readonly placeholder = input<string | undefined>(undefined);

    /**
     * 入力タイプ
     */
    readonly type = input<InputType>('text');
}
