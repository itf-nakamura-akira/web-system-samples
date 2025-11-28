import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Input } from './input';

@Component({
    template: `<input app-input />`,
    standalone: true,
    imports: [Input],
})
class TestHostComponent {}

describe('Input', () => {
    let component: TestHostComponent;
    let fixture: ComponentFixture<TestHostComponent>;
    let inputElement: HTMLElement;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [TestHostComponent],
        }).compileComponents();

        fixture = TestBed.createComponent(TestHostComponent);
        component = fixture.componentInstance;
        inputElement = fixture.debugElement.query(By.css('input[app-input]')).nativeElement;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should have "form-control" class', () => {
        expect(inputElement.classList).toContain('form-control');
    });
});
