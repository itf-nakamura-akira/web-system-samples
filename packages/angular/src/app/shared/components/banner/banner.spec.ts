import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { vi } from 'vitest';
import { Banner } from './banner';

describe('Banner', () => {
    let component: Banner;
    let fixture: ComponentFixture<Banner>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [Banner],
        }).compileComponents();

        fixture = TestBed.createComponent(Banner);
        component = fixture.componentInstance;
    });

    it('should create', () => {
        fixture.detectChanges();
        expect(component).toBeTruthy();
    });

    it('should display title and description', () => {
        const title = 'Test Title';
        const description = 'Test Description';
        fixture.componentRef.setInput('title', title);
        fixture.componentRef.setInput('description', description);
        fixture.detectChanges();

        const titleEl = fixture.debugElement.query(By.css('p.text-sm.font-medium'));
        const descriptionEl = fixture.debugElement.query(By.css('p.mt-1.text-sm'));

        expect(titleEl.nativeElement.textContent).toBe(title);
        expect(descriptionEl.nativeElement.textContent).toBe(description);
    });

    it('should apply the correct tone class', () => {
        const tone = 'critical';
        fixture.componentRef.setInput('tone', tone);
        fixture.detectChanges();

        const bannerEl = fixture.debugElement.query(By.css('div.relative'));
        expect(bannerEl.nativeElement.classList).toContain('bg-red-100');
        expect(bannerEl.nativeElement.classList).toContain('text-red-800');
    });

    it('should display dismiss button when dismissible is true', () => {
        fixture.componentRef.setInput('dismissible', true);
        fixture.detectChanges();

        const dismissButton = fixture.debugElement.query(By.css('button'));
        expect(dismissButton).toBeTruthy();
    });

    it('should not display dismiss button when dismissible is false', () => {
        fixture.componentRef.setInput('dismissible', false);
        fixture.detectChanges();

        const dismissButton = fixture.debugElement.query(By.css('button'));
        expect(dismissButton).toBeFalsy();
    });

    it('should emit dismiss event when dismiss button is clicked', () => {
        const spy = vi.spyOn(component.dismiss, 'emit');
        fixture.componentRef.setInput('dismissible', true);
        fixture.detectChanges();

        const dismissButton = fixture.debugElement.query(By.css('button'));
        dismissButton.triggerEventHandler('click', null);

        expect(spy).toHaveBeenCalled();
    });
});
