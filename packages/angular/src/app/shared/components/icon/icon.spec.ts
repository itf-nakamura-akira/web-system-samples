import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Icon } from './icon';

describe('Icon', () => {
    let component: Icon;
    let fixture: ComponentFixture<Icon>;
    let element: HTMLElement;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [Icon],
        }).compileComponents();

        fixture = TestBed.createComponent(Icon);
        component = fixture.componentInstance;
        element = fixture.nativeElement;

        fixture.componentRef.setInput('icon', 'person');

        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should apply size to the svg', async () => {
        fixture.componentRef.setInput('size', 2); // 2rem

        await fixture.whenStable();

        const rootFontSize = 16;
        const expectedPx = 2 * rootFontSize;
        const svgElement = element.querySelector('svg');

        expect(svgElement?.getAttribute('width')).toBe(expectedPx.toString());
        expect(svgElement?.getAttribute('height')).toBe(expectedPx.toString());
    });

    it('should apply fill color to the svg', async () => {
        fixture.componentRef.setInput('fill', 'rgb(255, 0, 0)');

        await fixture.whenStable();

        const svgElement = element.querySelector('svg');

        expect(svgElement?.getAttribute('fill')).toBe('rgb(255, 0, 0)');
    });

    it('should handle default size if not provided', async () => {
        await fixture.whenStable();

        const rootFontSize = 16;
        const defaultSizeRem = 1.25;
        const expectedPx = defaultSizeRem * rootFontSize;

        const svgElement = element.querySelector('svg');
        expect(svgElement?.getAttribute('width')).toBe(expectedPx.toString());
        expect(svgElement?.getAttribute('height')).toBe(expectedPx.toString());
    });
});
