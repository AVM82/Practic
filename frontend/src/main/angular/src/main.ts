import { importProvidersFrom } from '@angular/core';
import { AppComponent } from './app/app.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { TranslocoRootModule } from './app/transloco-root.module';
import {withInterceptorsFromDi, provideHttpClient, HTTP_INTERCEPTORS} from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app/app-routing.module';
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { CoursesService } from './app/services/courses/courses.service';
import {StudentMetricsService} from "./app/services/admin/student-metrics.service";
import {ApiCacheInterceptor} from "./app/services/api-cache.interceptor";
import {MatPaginator} from "@angular/material/paginator";


bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(BrowserModule, AppRoutingModule, TranslocoRootModule, MatCardModule, MatIconModule, MatToolbarModule),
        CoursesService, StudentMetricsService, MatPaginator,
        provideAnimations(),
        provideHttpClient(withInterceptorsFromDi()),
      {provide: HTTP_INTERCEPTORS, useClass: ApiCacheInterceptor, multi: true}
    ]
})
  .catch(err => console.error(err));
