/* eslint-disable @typescript-eslint/no-explicit-any */
import { HttpErrorResponse, HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { lastValueFrom, of, throwError } from 'rxjs';
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';

import { errorResponseInterceptor } from './error-response-interceptor';

describe('errorResponseInterceptor', () => {
    let consoleErrorSpy: ReturnType<typeof vi.spyOn>;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        // Mock console.error to prevent logging during tests and to spy on it
        consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {
            /* empty */
        });
    });

    afterEach(() => {
        // Restore the original console.error function
        vi.restoreAllMocks();
    });

    it('should not intercept a successful request and should pass it through', async () => {
        const req = new HttpRequest('GET', '/test');
        const next: HttpHandlerFn = () => of(new HttpResponse({ status: 200, body: { data: 'success' } }));

        const result = await lastValueFrom(TestBed.runInInjectionContext(() => errorResponseInterceptor(req, next)));

        expect(result).toBeInstanceOf(HttpResponse);
        expect((result as HttpResponse<unknown>).body).toEqual({ data: 'success' });
        expect(consoleErrorSpy).not.toHaveBeenCalled();
    });

    it('should re-throw the original HttpErrorResponse if it has the expected format', async () => {
        const expectedErrorBody = {
            status: 400,
            message: 'Bad Request',
            timestamp: new Date().toISOString(),
        };
        const originalError = new HttpErrorResponse({
            error: expectedErrorBody,
            status: 400,
            statusText: 'Bad Request',
        });

        const req = new HttpRequest('GET', '/test');
        const next: HttpHandlerFn = () => throwError(() => originalError);

        try {
            await lastValueFrom(TestBed.runInInjectionContext(() => errorResponseInterceptor(req, next)));
            expect.fail('The interceptor should have thrown an error.');
        } catch (error) {
            expect(error).toBe(originalError);
        }

        expect(consoleErrorSpy).toHaveBeenCalledWith(originalError);
    });

    it('should throw a new, generic error if the original error does not have the expected format', async () => {
        const originalError = new HttpErrorResponse({
            error: 'Something went wrong',
            status: 500,
            statusText: 'Internal Server Error',
        });

        const req = new HttpRequest('GET', '/test');
        const next: HttpHandlerFn = () => throwError(() => originalError);

        try {
            await lastValueFrom(TestBed.runInInjectionContext(() => errorResponseInterceptor(req, next)));
            // This line should not be reached
            expect.fail('The interceptor should have thrown an error.');
        } catch (error: any) {
            expect(error).not.toBe(originalError);
            expect(error.error.status).toBe(500);
            expect(error.error.message).toBe('エラーが発生しました。');
            expect(error.error.timestamp).toBeTypeOf('string');
            expect(consoleErrorSpy).toHaveBeenCalledWith(originalError);
        }
    });

    it('should throw a new, generic error for a non-HttpErrorResponse error', async () => {
        const originalError = new Error('Network failure');

        const req = new HttpRequest('GET', '/test');
        const next: HttpHandlerFn = () => throwError(() => originalError);

        try {
            await lastValueFrom(TestBed.runInInjectionContext(() => errorResponseInterceptor(req, next)));
            // This line should not be reached
            expect.fail('The interceptor should have thrown an error.');
        } catch (error: any) {
            expect(error).not.toBe(originalError);
            expect(error.error.status).toBe(500);
            expect(error.error.message).toBe('エラーが発生しました。');
            expect(error.error.timestamp).toBeTypeOf('string');
            expect(consoleErrorSpy).toHaveBeenCalledWith(originalError);
        }
    });
});
