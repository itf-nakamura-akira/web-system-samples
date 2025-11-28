import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';

import Login from './login';

describe('Login', () => {
    let component: Login;
    let fixture: ComponentFixture<Login>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [Login],
            providers: [provideRouter([])],
        }).compileComponents();

        fixture = TestBed.createComponent(Login);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render a primary button with "ログイン" text', () => {
        const buttonElement = fixture.debugElement.query(By.css('button[app-button]'));
        expect(buttonElement).toBeTruthy();
        expect(buttonElement.nativeElement.textContent).toContain('ログイン');
    });
});
