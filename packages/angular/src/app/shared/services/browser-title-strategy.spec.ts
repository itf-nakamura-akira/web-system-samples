import { TestBed } from '@angular/core/testing';
import { Title } from '@angular/platform-browser';
import { RouterStateSnapshot } from '@angular/router';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { APP_TITLE } from '../tokens/app-title.token';
import { BrowserTitleStrategy } from './browser-title-strategy';

describe('BrowserTitleStrategy', () => {
    let service: BrowserTitleStrategy;
    let titleService: Title;
    const appTitle = 'Test App';

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                BrowserTitleStrategy,
                { provide: Title, useValue: { setTitle: vi.fn() } },
                { provide: APP_TITLE, useValue: appTitle },
            ],
        });
        service = TestBed.inject(BrowserTitleStrategy);
        titleService = TestBed.inject(Title);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    describe('updateTitle', () => {
        it('ページタイトルが定義されている場合、"ページタイトル | アプリ名" の形式でタイトルを設定すること', () => {
            const routerState = {} as RouterStateSnapshot;
            const pageTitle = 'Test Page';
            // service['buildTitle'] は protected メソッドのため、このようにモック化します
            vi.spyOn(service as any, 'buildTitle').mockReturnValue(pageTitle);
            const setTitleSpy = vi.spyOn(titleService, 'setTitle');

            service.updateTitle(routerState);

            expect(setTitleSpy).toHaveBeenCalledWith(`${pageTitle} | ${appTitle}`);
        });

        it('ページタイトルが未定義の場合、アプリ名のみをタイトルとして設定すること', () => {
            const routerState = {} as RouterStateSnapshot;
            // service['buildTitle'] は protected メソッドのため、このようにモック化します
            vi.spyOn(service as any, 'buildTitle').mockReturnValue(undefined);
            const setTitleSpy = vi.spyOn(titleService, 'setTitle');

            service.updateTitle(routerState);

            expect(setTitleSpy).toHaveBeenCalledWith(appTitle);
        });
    });
});
