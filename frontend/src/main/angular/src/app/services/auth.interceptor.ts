import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import { TokenStorageService } from './token-storage.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
      private token: TokenStorageService,
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let authReq = request;
    const loginPath = '/login';

    const token = this.token.getToken();

    if (token != null) {
      authReq = request.clone({ headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
    }
    return next.handle(authReq).pipe(tap({
          next: () => {},
          error: (err: any) => {
            if (err instanceof HttpErrorResponse) {
              const statusCode = err.status;

              if (statusCode !== 401 && statusCode !== 403  || window.location.pathname === loginPath) {
                return;
              }
              if (statusCode === 403) {
                window.history.back();
                return;
              }
                this.token.signOut();
                window.location.href = loginPath;

            }
          }
        })
    );
  }
}
