import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {CoursesComponent} from "./pages/courses/courses.component";
import {CourseDetailsComponent} from "./pages/course-details/course-details.component";
import {AdminDashboardComponent} from "./pages/admin-dashboard/admin-dashboard.component";
import {DashboardComponent} from "./modules/dashboard/dashboard.component";
import {PracticMetricComponent} from "./modules/practic-metric/practic-metric.component";

const routes: Routes = [
  {path: '', component: CoursesComponent},
  {path: 'courses', component: CoursesComponent},
  {path: 'course/:slug', component: CourseDetailsComponent},
  {
    path: 'admin',
    component: AdminDashboardComponent,
    children: [
      {path: '', component: DashboardComponent},
      {path: 'students/practices', component: PracticMetricComponent}
    ]
  },
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
