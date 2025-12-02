import { ChangeDetectionStrategy, Component, effect, ElementRef, inject, input } from '@angular/core';
import octicons from '@primer/octicons';

/**
 * アイコンコンポーネント
 *
 * Primer Octiconsを使用し、指定されたアイコンを表示します。
 *
 * 使用例:
 * ```html
 * <i app-icon icon="person"></i>
 *
 * ```
 * 利用可能なアイコンの一覧は以下のURLを参照してください。
 *
 * https://primer.style/octicons/
 */
@Component({
    // eslint-disable-next-line @angular-eslint/component-selector
    selector: '[app-icon]',
    imports: [],
    templateUrl: './icon.html',
    styleUrl: './icon.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Icon {
    /**
     * ElementRef
     */
    private readonly elRef = inject(ElementRef);

    /**
     * ルート要素のフォントサイズ (px)
     */
    private readonly rootFontSize: number;

    /**
     * アイコン名を指定します。
     */
    readonly icon = input.required<string>();

    /**
     * アイコンのサイズを `rem` 単位で設定します。
     * デフォルト値は `1.25` (1.25rem = 20px) です。
     */
    readonly size = input<number>(1.25);

    /**
     * アイコンの色を指定します。
     * CSSの`fill`プロパティに相当します。
     */
    readonly fill = input<string>('#1e2939');

    /**
     * SVGのクラスを指定します。
     */
    readonly svgClass = input<string>('');

    /**
     * コンストラクター
     */
    constructor() {
        // remの基準となるルート要素のフォントサイズを一度だけ取得します。
        if (typeof window !== 'undefined' && typeof document !== 'undefined') {
            const rootStyle = window.getComputedStyle(document.documentElement);
            this.rootFontSize = parseFloat(rootStyle.getPropertyValue('font-size')) || 16;
        } else {
            // サーバーサイドレンダリングなど、ブラウザ環境でない場合のフォールバック値
            this.rootFontSize = 16;
        }

        effect(() => {
            const icon: string = this.icon();
            const size: number = this.size();
            const fill: string = this.fill();
            const svgClass: string = this.svgClass();

            this.elRef.nativeElement.innerHTML = this.getSVG(icon, {
                // remで指定されたサイズを、実際のルートフォントサイズに基づいてピクセルに変換します。
                width: size * this.rootFontSize,
                fill: fill,
                class: svgClass,
            });
        });
    }

    /**
     * 指定されたアイコン名とオプションに基づいてSVG文字列を生成します。
     *
     * @param name `string` 型のアイコン名。
     * @param options `{ width?: number; height?: number; fill?: string; class?: string }`
     * @returns SVG文字列
     */
    private getSVG(
        name: string,
        options: {
            width?: number;
            fill?: string;
            class?: string;
        },
    ): string {
        if (!(name in octicons)) {
            throw new Error(`${name} is invalid. The names below are available:\n ${Object.keys(octicons).join(',')}`);
        }

        return octicons[name as keyof typeof octicons].toSVG(options);
    }
}
