import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorIcon } from './error-icon';

describe('ErrorIcon', () => {
    let component: ErrorIcon;
    let fixture: ComponentFixture<ErrorIcon>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ErrorIcon],
        }).compileComponents();

        fixture = TestBed.createComponent(ErrorIcon);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
