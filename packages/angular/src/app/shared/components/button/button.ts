import { CommonModule } from '@angular/common';
import { booleanAttribute, ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * ボタンのサイズ
 */
export type ButtonVariant = 'primary' | 'default' | 'invisible' | 'danger';

/**
 * ボタンのサイズ
 */
export type ButtonSize = 'small' | 'medium' | 'large';

/**
 * ボタンコンポーネントです。
 * このコンポーネントは、アプリケーション全体で使用されるボタンのスタイルと動作を定義します。
 *
 * 参考: https://primer.style/product/components/button/
 */
@Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: 'button[app-button]',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './button.html',
    styleUrl: './button.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.btn-primary]': 'variant() === "primary"',
        '[class.btn-default]': 'variant() === "default"',
        '[class.btn-invisible]': 'variant() === "invisible"',
        '[class.btn-danger]': 'variant() === "danger"',
        '[class.btn-small]': 'size() === "small"',
        '[class.btn-medium]': 'size() === "medium"',
        '[class.btn-large]': 'size() === "large"',
        '[attr.disabled]': 'disabled() ? true : null',
    },
})
export class Button {
    /**
     * ボタンのバリアント
     */
    readonly variant = input<ButtonVariant>('default');

    /**
     * ボタンのサイズ
     */
    readonly size = input<ButtonSize>('medium');

    /**
     * 非活性
     */
    readonly disabled = input(false, { transform: booleanAttribute });
}
