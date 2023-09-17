import { importProvidersFrom } from '@angular/core';
import { AppComponent } from './app/app.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import {MatCard, MatCardModule} from '@angular/material/card';
import { TranslocoRootModule } from './app/transloco-root.module';
import {withInterceptorsFromDi, provideHttpClient, HTTP_INTERCEPTORS} from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app/app-routing.module';
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { CoursesService } from './app/services/courses/courses.service';
import {StudentMetricsService} from "./app/services/admin/student-metrics.service";
import {ApiCacheInterceptor} from "./app/services/api-cache.interceptor";
import {MatPaginator} from "@angular/material/paginator";
import {AuthService} from "./app/services/auth/auth.service";
import {AuthInterceptor} from "./app/services/auth.interceptor";
import {environment} from "./enviroments/enviroment";
import {ReportServiceService} from "./app/services/report/report-service.service";
import { AngularSvgIconModule, SvgIconRegistryService } from 'angular-svg-icon';
import {InfoMessagesService} from "./app/services/info-messages.service";
import {MatSnackBar, MatSnackBarModule} from "@angular/material/snack-bar";


bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
            BrowserModule,
            AngularSvgIconModule.forRoot(),
            AppRoutingModule,
            TranslocoRootModule,
            MatCardModule,
            MatIconModule,
            MatToolbarModule,
            MatSnackBar,
            MatCard,
            MatSnackBar,
            MatPaginator,
            MatSnackBarModule
        ),
      SvgIconRegistryService,
      CoursesService,
      StudentMetricsService,
      AuthService,
      ReportServiceService,
      InfoMessagesService,
        provideAnimations(),
        provideHttpClient(withInterceptorsFromDi()),
      {provide: HTTP_INTERCEPTORS, useClass: ApiCacheInterceptor, multi: true},
      {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
      { provide: 'environment', useValue: environment }
    ]
})
  .catch(err => console.error(err));
