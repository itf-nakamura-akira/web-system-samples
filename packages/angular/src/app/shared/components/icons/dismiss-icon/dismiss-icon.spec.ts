import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DismissIcon } from './dismiss-icon';

describe('DismissIcon', () => {
    let component: DismissIcon;
    let fixture: ComponentFixture<DismissIcon>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [DismissIcon],
        }).compileComponents();

        fixture = TestBed.createComponent(DismissIcon);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
