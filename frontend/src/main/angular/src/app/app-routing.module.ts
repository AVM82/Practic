import {NgModule, LOCALE_ID} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {CoursesComponent} from "./pages/courses/courses.component";
import {CourseDetailsComponent} from "./pages/course-details/course-details.component";
import {LoginComponent} from "./pages/login/login.component";
import {AdminDashboardComponent} from "./pages/admin-dashboard/admin-dashboard.component";
import {DashboardComponent} from "./modules/dashboard/dashboard.component";
import {PracticMetricComponent} from "./modules/practic-metric/practic-metric.component";
import {ChapterDetailsComponent} from "./pages/chapter-details/chapter-details.component";
import {ReportDashboardComponent} from "./pages/report-dashboard/report-dashboard.component";
import {AdditionalMaterialsComponent} from "./pages/materials/additional-materials.component";
import {CreateCourseComponent} from "./pages/create-course/create-course.component";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule, NativeDateModule} from "@angular/material/core";
import localeUk from '@angular/common/locales/uk';
import {registerLocaleData} from "@angular/common";

registerLocaleData(localeUk, 'uk');

const routes: Routes = [
  {path: '', component: CoursesComponent},
  {path: 'courses', component: CoursesComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LoginComponent},
  {
    path: 'admin',
    component: AdminDashboardComponent,
    children: [
      {path: '', component: DashboardComponent},
      {path: 'students/practices', component: PracticMetricComponent},
      {path: 'create/course', component: CreateCourseComponent}
    ]
  },
  {
    path: 'courses/:slug/reports',
    component: ReportDashboardComponent

  },
  {path: 'courses/:slug', component: CourseDetailsComponent},
  {path: 'courses/:slug/chapters/:chapterN', component: ChapterDetailsComponent},
  {path: 'courses/:slug/additional', component: AdditionalMaterialsComponent},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes),MatDatepickerModule, MatNativeDateModule, NativeDateModule],
  exports: [RouterModule],
  providers: [
    { provide: LOCALE_ID, useValue: 'uk' }
  ],
})
export class AppRoutingModule { }