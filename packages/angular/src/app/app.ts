import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

/**
 * アプリケーションのルートコンポーネント
 */
@Component({
    selector: 'app-root',
    imports: [RouterOutlet],
    templateUrl: './app.html',
    styleUrl: './app.scss',
})
export class App {}
