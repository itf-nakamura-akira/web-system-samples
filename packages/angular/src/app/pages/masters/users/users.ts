import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/**
 * ユーザー管理画面
 */
@Component({
    selector: 'app-users',
    imports: [RouterLink],
    templateUrl: './users.html',
    styleUrl: './users.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Users {}
