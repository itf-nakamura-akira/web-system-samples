import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { LoginService } from '../../shared/api/login/login.service';
import { Input } from '../../shared/components/input/input';
import { APP_TITLE } from '../../shared/tokens/app-title.token';

import Login from './login';

class MockLoginService {}

describe('Login', () => {
    let component: Login;
    let fixture: ComponentFixture<Login>;
    let focusSpy: ReturnType<typeof vi.spyOn>;

    beforeEach(async () => {
        focusSpy = vi.spyOn(Input.prototype, 'focus');

        await TestBed.configureTestingModule({
            imports: [Login],
            providers: [
                provideRouter([]),
                { provide: LoginService, useClass: MockLoginService },
                { provide: APP_TITLE, useValue: 'Test App' },
            ],
        }).compileComponents();

        fixture = TestBed.createComponent(Login);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render the app title', () => {
        const h2Element = fixture.debugElement.query(By.css('h2'));
        expect(h2Element).toBeTruthy();
        expect(h2Element.nativeElement.textContent).toContain('Test App');
    });

    it('should render the login title', () => {
        const h1Element = fixture.debugElement.query(By.css('h1'));
        expect(h1Element).toBeTruthy();
        expect(h1Element.nativeElement.textContent).toContain('ログイン');
    });

    it('should render a primary button with "ログイン" text', () => {
        const buttonElement = fixture.debugElement.query(By.css('button[app-button]'));
        expect(buttonElement).toBeTruthy();
        expect(buttonElement.nativeElement.textContent).toContain('ログイン');
    });

    it('should focus the account input on init', () => {
        fixture.detectChanges();
        expect(focusSpy).toHaveBeenCalled();
    });
});
