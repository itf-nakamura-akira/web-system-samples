import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Banner } from '../../shared/components/banner/banner';

/**
 * ホーム画面
 */
@Component({
    selector: 'app-home',
    imports: [RouterLink, Banner],
    templateUrl: './home.html',
    styleUrl: './home.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export default class Home {}
