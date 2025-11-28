import { inject, Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { RouterStateSnapshot, TitleStrategy } from '@angular/router';
import { APP_TITLE } from '../tokens/app-title.token';

/**
 * ブラウザータイトル更新 Service
 */
@Injectable({
    providedIn: 'root',
})
export class BrowserTitleStrategy extends TitleStrategy {
    /**
     * Title
     */
    private readonly title = inject(Title);

    /**
     * アプリ名
     */
    private readonly appTitle = inject(APP_TITLE);

    /**
     * タイトルを更新する
     *
     * @param routerState ルーターの状態
     */
    override updateTitle(routerState: RouterStateSnapshot) {
        const title: string | undefined = this.buildTitle(routerState);

        if (title !== undefined) {
            this.title.setTitle(`${title} | ${this.appTitle}`);
        } else {
            this.title.setTitle(this.appTitle);
        }
    }
}
