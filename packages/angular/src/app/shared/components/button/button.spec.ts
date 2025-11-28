import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Button } from './button';

describe('Button', () => {
    let component: Button;
    let fixture: ComponentFixture<Button>;
    let buttonElement: HTMLElement;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [Button],
        }).compileComponents();

        fixture = TestBed.createComponent(Button);
        component = fixture.componentInstance;
        buttonElement = fixture.nativeElement;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    describe('variant', () => {
        it('should have "default" variant by default', () => {
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-default');
        });

        it('should apply "primary" variant class', () => {
            fixture.componentRef.setInput('variant', 'primary');
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-primary');
        });

        it('should apply "invisible" variant class', () => {
            fixture.componentRef.setInput('variant', 'invisible');
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-invisible');
        });

        it('should apply "danger" variant class', () => {
            fixture.componentRef.setInput('variant', 'danger');
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-danger');
        });
    });

    describe('size', () => {
        it('should have "medium" size by default', () => {
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-medium');
        });

        it('should apply "small" size class', () => {
            fixture.componentRef.setInput('size', 'small');
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-small');
        });

        it('should apply "large" size class', () => {
            fixture.componentRef.setInput('size', 'large');
            fixture.detectChanges();
            expect(buttonElement.classList).toContain('btn-large');
        });
    });

    describe('disabled', () => {
        it('should not be disabled by default', () => {
            fixture.detectChanges();
            expect(buttonElement.hasAttribute('disabled')).toBe(false);
        });

        it('should be disabled when disabled input is true', () => {
            fixture.componentRef.setInput('disabled', true);
            fixture.detectChanges();
            expect(buttonElement.hasAttribute('disabled')).toBe(true);
        });
    });
});
