import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Button } from '../../shared/components/button/button';
import { FormControlComponent } from '../../shared/components/form-control/form-control';
import { Input } from '../../shared/components/input/input';

/**
 * ログイン画面
 */
@Component({
    selector: 'app-login',
    imports: [Button, Input, FormControlComponent],
    templateUrl: './login.html',
    styleUrl: './login.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Login {}
