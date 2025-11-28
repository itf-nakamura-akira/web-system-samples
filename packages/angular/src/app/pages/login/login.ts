import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Button } from '../../shared/components/button/button';
import { Input } from '../../shared/components/input/input';

/**
 * ログイン画面
 */
@Component({
    selector: 'app-login',
    imports: [RouterLink, Button, Input],
    templateUrl: './login.html',
    styleUrl: './login.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Login {}
