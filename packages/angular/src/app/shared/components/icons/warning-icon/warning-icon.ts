import { NgStyle } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * 警告アイコンコンポーネントは、警告状態を示すために使用されるSVGアイコンを表示します。
 * アイコンのサイズをカスタマイズできます。
 *
 * @example
 * ```html
 * <app-warning-icon [size]="2"></app-warning-icon>
 */
@Component({
    selector: 'app-warning-icon',
    standalone: true,
    imports: [NgStyle],
    templateUrl: './warning-icon.html',
    styleUrl: './warning-icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WarningIcon {
    /**
     * アイコンのサイズをrem単位で設定します。
     * デフォルトは `1.25` (20px) です。
     */
    readonly size = input(1.25);
}
