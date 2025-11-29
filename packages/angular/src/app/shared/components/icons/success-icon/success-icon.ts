import { NgStyle } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * 成功アイコンコンポーネントは、成功状態を示すために使用されるSVGアイコンを表示します。
 * アイコンのサイズをカスタマイズできます。
 *
 * @example
 * ```html
 * <app-success-icon [size]="2"></app-success-icon>
 */
@Component({
    selector: 'app-success-icon',
    standalone: true,
    imports: [NgStyle],
    templateUrl: './success-icon.html',
    styleUrl: './success-icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SuccessIcon {
    /**
     * アイコンのサイズをrem単位で設定します。
     * デフォルトは `1.25` (20px) です。
     */
    readonly size = input(1.25);
}
