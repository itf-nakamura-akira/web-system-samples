import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { Field, form, required } from '@angular/forms/signals';
import { LoginService } from '../../shared/api/login/login.service';
import { LoginRequest } from '../../shared/api/model';
import { Button } from '../../shared/components/button/button';
import { FormControlComponent } from '../../shared/components/form-control/form-control';
import { Input } from '../../shared/components/input/input';
import { getFieldErrors } from '../../shared/functions/get-fielc-errors';

/**
 * ログイン画面
 */
@Component({
    selector: 'app-login',
    imports: [Button, Input, FormControlComponent, Field],
    templateUrl: './login.html',
    styleUrl: './login.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Login {
    /**
     * LoginService
     */
    private readonly loginService = inject(LoginService);

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
