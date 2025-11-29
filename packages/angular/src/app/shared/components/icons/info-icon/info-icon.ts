import { NgStyle } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * 情報アイコンコンポーネントは、情報を示すためのSVGアイコンを表示します。
 * アイコンのサイズをカスタマイズできます。
 *
 * @example
 * ```html
 * <app-info-icon [size]="1.5"></app-info-icon>
 * ```
 */
@Component({
    selector: 'app-info-icon',
    standalone: true,
    imports: [NgStyle],
    templateUrl: './info-icon.html',
    styleUrl: './info-icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InfoIcon {
    /**
     * アイコンのサイズを `rem` 単位で設定します。
     * デフォルト値は `1.25` (1.25rem = 20px) です。
     */
    readonly size = input(1.25);
}
