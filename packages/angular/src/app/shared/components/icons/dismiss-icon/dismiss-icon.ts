import { NgStyle } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/**
 * 閉じるアイコンコンポーネントは、閉じるボタンなどに使用されるSVGアイコンを表示します。
 * アイコンのサイズをカスタマイズできます。
 *
 * @example
 * ```html
 * <app-dismiss-icon [size]="1.5"></app-dismiss-icon>
 * ```
 */
@Component({
    selector: 'app-dismiss-icon',
    standalone: true,
    imports: [NgStyle],
    templateUrl: './dismiss-icon.html',
    styleUrl: './dismiss-icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DismissIcon {
    /**
     * アイコンのサイズを `rem` 単位で設定します。
     * デフォルト値は `1.25` (1.25rem = 20px) です。
     */
    readonly size = input(1.25);
}
