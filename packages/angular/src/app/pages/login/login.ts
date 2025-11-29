import { ChangeDetectionStrategy, Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { Field, form, required } from '@angular/forms/signals';
import { LoginService } from '../../shared/api/login/login.service';
import { LoginRequest } from '../../shared/api/model';
import { Button } from '../../shared/components/button/button';
import { Card } from '../../shared/components/card/card';
import { FormControlComponent } from '../../shared/components/form-control/form-control';
import { Input } from '../../shared/components/input/input';
import { getFieldErrors } from '../../shared/functions/get-fielc-errors';
import { APP_TITLE } from '../../shared/tokens/app-title.token';

/**
 * ログイン画面
 */
@Component({
    selector: 'app-login',
    imports: [Button, Input, FormControlComponent, Field, Card],
    templateUrl: './login.html',
    host: {
        class: 'flex justify-center items-center h-full',
    },
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Login {
    /**
     * LoginService
     */
    private readonly loginService = inject(LoginService);

    /**
     * アカウント入力要素
     */
    readonly accountInput = viewChild.required<Input>('accountInput');

    /**
     * アプリケーションタイトル
     */
    readonly appTitle = inject(APP_TITLE);

    /**
     * ログインリクエスト
     */
    private readonly loginModel = signal<LoginRequest>({
        account: '',
        password: '',
    });

    /**
     * ログインフォーム
     */
    readonly loginForm = form(this.loginModel, (schemaPath) => {
        required(schemaPath.account, { message: 'アカウントを入力してください。' });
        required(schemaPath.password, { message: 'パスワードを入力してください。' });
    });

    /**
     * ログインフォームエラー
     */
    readonly loginFormErrors = computed(() => ({
        account: getFieldErrors(this.loginForm.account),
        password: getFieldErrors(this.loginForm.password),
    }));

    /**
     * コンストラクター
     */
    constructor() {
        effect(() => this.accountInput().focus());
    }

    /**
     * ログイン
     *
     * @param event イベント
     */
    login(event: Event): void {
        event.preventDefault();
        this.loginForm().markAsTouched();

        if (this.loginForm().invalid()) {
            return;
        }

        const loginRequest: LoginRequest = this.loginModel();

        this.loginService.login(loginRequest).subscribe({
            next: (response) => {
                console.log('ログイン成功', response);
            },
            error: (error) => {
                console.error('ログイン失敗', error);
            },
        });
    }
}
