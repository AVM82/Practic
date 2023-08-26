import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {CoursesComponent} from "./pages/courses/courses.component";
import {CourseDetailsComponent} from "./pages/course-details/course-details.component";
import {AdminDashboardComponent} from "./pages/admin-dashboard/admin-dashboard.component";
import {DashboardComponent} from "./modules/dashboard/dashboard.component";
import {PracticMetricComponent} from "./modules/practic-metric/practic-metric.component";
import {ReportMetricComponent} from "./modules/report-metric/report-metric.component";
import {ChapterDetailsComponent} from "./pages/chapter-details/chapter-details.component";
import {ReportDashboardComponent} from "./pages/report-dashboard/report-dashboard.component";

const routes: Routes = [
  {path: '', component: CoursesComponent},
  {path: 'courses', component: CoursesComponent},
  {
    path: 'admin',
    component: AdminDashboardComponent,
    children: [
      {path: '', component: DashboardComponent},
      {path: 'students/practices', component: PracticMetricComponent}
    ]
  },
  {
    path: 'reports',
    component: ReportDashboardComponent

  },
  {path: 'courses/:slug', component: CourseDetailsComponent},
  {path: 'courses/:courseId/chapters/:chapterId', component: ChapterDetailsComponent},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
