import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessIcon } from './success-icon';

describe('SuccessIcon', () => {
    let component: SuccessIcon;
    let fixture: ComponentFixture<SuccessIcon>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [SuccessIcon],
        }).compileComponents();

        fixture = TestBed.createComponent(SuccessIcon);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
