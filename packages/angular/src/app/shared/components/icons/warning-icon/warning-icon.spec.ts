import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningIcon } from './warning-icon';

describe('WarningIcon', () => {
    let component: WarningIcon;
    let fixture: ComponentFixture<WarningIcon>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [WarningIcon],
        }).compileComponents();

        fixture = TestBed.createComponent(WarningIcon);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
