import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, isDevMode, provideBrowserGlobalErrorListeners } from '@angular/core';
import { TitleStrategy, provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { errorResponseInterceptor } from './shared/interceptors/error-response-interceptor';
import { BrowserTitleStrategy } from './shared/services/browser-title-strategy';
import { APP_TITLE } from './shared/tokens/app-title.token';

/**
 * アプリケーション設定
 */
export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideHttpClient(withInterceptors([errorResponseInterceptor])),
        provideRouter(routes),
        {
            provide: APP_TITLE,
            useValue: isDevMode() ? 'Web System Samples【開発】' : 'Web System Samples',
        },
        {
            provide: TitleStrategy,
            useClass: BrowserTitleStrategy,
        },
    ],
};
