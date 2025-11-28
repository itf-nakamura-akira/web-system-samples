import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Card } from './card';

@Component({
    standalone: true,
    imports: [Card],
    template: `
        <app-card>
            <div class="projected-content">Hello, World!</div>
        </app-card>
    `,
})
class TestHostComponent {}

describe('Card', () => {
    let component: Card;
    let fixture: ComponentFixture<Card>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [Card, TestHostComponent],
        }).compileComponents();

        fixture = TestBed.createComponent(Card);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should project content', () => {
        const testFixture = TestBed.createComponent(TestHostComponent);
        testFixture.detectChanges();
        const compiled = testFixture.nativeElement;
        expect(compiled.querySelector('.projected-content').textContent).toContain('Hello, World!');
    });
});
