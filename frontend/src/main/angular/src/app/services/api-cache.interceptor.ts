import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpResponse
} from '@angular/common/http';
import {Observable, of, tap} from 'rxjs';
import {ApiUrls} from "../enums/api-urls";
import {environment} from "../../enviroments/enviroment";

@Injectable()
export class ApiCacheInterceptor implements HttpInterceptor {

  private cache = new Map<string, HttpResponse<any>>();

  private endPointsToCachePatterns: RegExp[] = [
    new RegExp(ApiUrls.Courses.toString())
  ];

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const url = request.url.replace(environment.appApi, '');
    console.log("Request url = " + url);

    const shouldCache = this.endPointsToCachePatterns.some(pattern => pattern.test(url));
    if (shouldCache) {

      const cachedResponse = this.cache.get(request.url);

      if (cachedResponse) {
        return of(cachedResponse);
      }

      return next.handle(request).pipe(
          tap((response) => {
            if (response instanceof HttpResponse) {
              this.cache.set(request.url, response);
            }
          })
      );
    }
    return next.handle(request);
  }
}
