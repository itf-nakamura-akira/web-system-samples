import { TestBed } from '@angular/core/testing';

import { BrowserTitleStrategy } from './browser-title-strategy';

describe('BrowserTitleStrategy', () => {
    let service: BrowserTitleStrategy;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(BrowserTitleStrategy);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
