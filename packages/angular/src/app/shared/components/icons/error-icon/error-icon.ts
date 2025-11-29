import { NgStyle } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * エラーアイコンコンポーネントは、エラー状態を示すために使用されるSVGアイコンを表示します。
 * アイコンのサイズをカスタマイズできます。
 *
 * @example
 * ```html
 * <app-error-icon [size]="2"></app-error-icon>
 */
@Component({
    selector: 'app-error-icon',
    standalone: true,
    imports: [NgStyle],
    templateUrl: './error-icon.html',
    styleUrl: './error-icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ErrorIcon {
    /**
     * * アイコンのサイズをrem単位で設定します。
     * デフォルトは `1.25` (20px) です。
     */
    readonly size = input(1.25);
}
