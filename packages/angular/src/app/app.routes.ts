import { Routes } from '@angular/router';

/**
 * アプリケーションのルーティング設定
 */
export const routes: Routes = [
    {
        path: 'login',
        loadComponent: () => import('./pages/login/login'),
        title: 'ログイン',
    },
    {
        path: 'home',
        loadComponent: () => import('./pages/home/home'),
        title: 'ホーム',
    },
    {
        path: 'masters',
        children: [
            {
                path: 'users',
                loadComponent: () => import('./pages/masters/users/users'),
                title: 'ユーザー管理',
            },
        ],
    },
    {
        path: '**',
        redirectTo: 'home',
    },
];
