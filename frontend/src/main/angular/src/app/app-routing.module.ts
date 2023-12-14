import {NgModule, LOCALE_ID} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./pages/page-not-found/page-not-found.component";
import {CoursesComponent} from "./pages/courses/courses.component";
import {CourseDetailsComponent} from "./pages/course-details/course-details.component";
import {LoginComponent} from "./pages/login/login.component";
import {MentorDashboardComponent} from "./pages/mentor-dashboard/mentor-dashboard.component";
import {ChapterDetailsComponent} from "./pages/chapter-details/chapter-details.component";
import {ReportDashboardComponent} from "./pages/report-dashboard/report-dashboard.component";
import {AdditionalMaterialsComponent} from "./pages/materials/additional-materials.component";
import {CreateCourseComponent} from "./pages/create-course/create-course.component";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule, NativeDateModule} from "@angular/material/core";
import localeUk from '@angular/common/locales/uk';
import {registerLocaleData} from "@angular/common";
import { FeedbackComponent } from './componets/feedback/feedback.component';
import { StartPanelComponent } from './pages/start-panel/start-panel.component';
import { UsersComponent } from './pages/users/users.component';
import { BannedComponent } from './pages/ban/ban.component';
import { QuizComponent } from './componets/quiz/quiz.component';
import { MainPageComponent } from './pages/main-page/main-page.component';

registerLocaleData(localeUk, 'uk');

const routes: Routes = [
  {path: '', component: StartPanelComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LoginComponent},
  {path: 'feedback',component: FeedbackComponent},
  {path: 'ban', component: BannedComponent},
  {path: 'quizzes/:quizId/:studentChapterId', component: QuizComponent},

  {path: 'users', component: UsersComponent},
  {path: 'mentor', component: MentorDashboardComponent},
  {path: 'courses', component: CoursesComponent},
  {path: 'courses/create', component: CreateCourseComponent},
  {path: 'courses/:slug/chapters/:chapterN', component: ChapterDetailsComponent},
  {path: 'courses/:slug/main', component: MainPageComponent},
  {path: 'courses/:slug/reports', component: ReportDashboardComponent},
  {path: 'courses/:slug/additional', component: AdditionalMaterialsComponent},
  {path: 'courses/:slug', component: CourseDetailsComponent},
  
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'}), MatDatepickerModule, MatNativeDateModule, NativeDateModule],
  exports: [RouterModule],
  providers: [
    { provide: LOCALE_ID, useValue: 'uk' }
  ],
})
export class AppRoutingModule { }