import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';
import { DismissIcon } from '../icons/dismiss-icon/dismiss-icon';
import { ErrorIcon } from '../icons/error-icon/error-icon';
import { InfoIcon } from '../icons/info-icon/info-icon';
import { SuccessIcon } from '../icons/success-icon/success-icon';
import { WarningIcon } from '../icons/warning-icon/warning-icon';

/**
 *  * バナーコンポーネントのトーン（表示スタイル）を定義します。
 * - 'info': 情報メッセージ
 * - 'critical': 危機的またはエラーメッセージ
 * - 'success': 成功メッセージ
 * - 'warning': 警告メッセージ
 */
export type BannerTone = 'info' | 'critical' | 'success' | 'warning';

/**
 *  * バナーコンポーネントは、ユーザーに重要な情報を表示するために使用されます。
 * さまざまなトーン（情報、クリティカル、成功、警告）をサポートし、
 * 必要に応じて閉じることができます。
 *
 * @example
 * ```html
 * <app-banner
 *   title="お知らせ"
 *   description="新しいバージョンが利用可能です。"
 *   tone="info"
 *   [dismissible]="true"
 *   (dismiss)="onBannerDismiss()"
 * ></app-banner>
 * ```
 */
@Component({
    selector: 'app-banner',
    imports: [NgClass, InfoIcon, DismissIcon, ErrorIcon, SuccessIcon, WarningIcon],
    templateUrl: './banner.html',
    styleUrl: './banner.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        class: 'Banner',
    },
})
export class Banner {
    /**
     * バナーのタイトルを設定します。
     */
    readonly title = input<string>();

    /**
     * バナーの説明を設定します。
     */
    readonly description = input<string>();

    /**
     * バナーの表示スタイル（トーン）を設定します。
     * 'info' | 'critical' | 'success' | 'warning' のいずれかです。
     * デフォルトは 'info' です。
     */
    readonly tone = input<BannerTone>('info');

    /**
     * バナーを閉じることができるかどうかを設定します。
     * `true` の場合、閉じるボタンが表示されます。
     * デフォルトは `false` です。
     */
    readonly dismissible = input(false);

    /**
     * バナーが閉じられたときに発行されるイベント。
     */
    readonly dismiss = output<void>();

    /**
     * バナーを閉じます。
     */
    onDismiss(): void {
        this.dismiss.emit();
    }
}
