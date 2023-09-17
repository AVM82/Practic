import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {TableWidgetComponent} from "../../componets/table-widget/table-widget.component";
import {StudentMetricsService} from "../../services/admin/student-metrics.service";
import {CoursesService} from "../../services/courses/courses.service";
import {InfoMessagesService} from "../../services/info-messages.service";

@Component({
  selector: 'app-inactive-person',
  standalone: true,
  imports: [CommonModule, TableWidgetComponent],
  templateUrl: './inactive-person.component.html',
  styleUrls: ['./inactive-person.component.css']
})
export class InactivePersonComponent implements OnInit{
  @Input() shouldShowHeader: boolean = true;
  isRequestInProgress: boolean = false;

  displayedColumns: string[] = ['name', 'profilePictureUrl', 'courseSlug', 'btnApply'];
  columnNameConverterMap: { [key: string]: string } = {
    'name': 'ПІБ',
    'profilePictureUrl': 'Фото',
    'courseSlug': 'Курс',
    'btnApply': 'Прийняти заявку'
  };

  data: any[] = [];
  firstChapterId: number = 0;

  constructor(
      private studentMetricService: StudentMetricsService,
      private coursesService: CoursesService,
      private messagesService: InfoMessagesService
  ) {}

  ngOnInit(): void {
   this.updateData();
  }

  handleAction(event: any) {
    const element = event.element;
    this.isRequestInProgress = true;
    this.coursesService.confirmApplyOnCourse(element.courseSlug, element.id).subscribe({
      next: () => {
        this.isRequestInProgress = false;
        this.updateData();
        this.getFirstChapterId(element.courseSlug, element.id);
      },
      error: () => {
        this.isRequestInProgress = false;
      }
    });
  }

  updateData() {
    this.studentMetricService.getApplicants().subscribe(applicants => {
      this.data = applicants;
    });
  }

  openFirstChapter(chapterId :number, studentId :number) {
    this.coursesService.openChapter(studentId, chapterId).subscribe({
      next: value => {
        this.messagesService.showMessage("Студента зараховано, перший розділ відкрито.", "normal");
      },
      error: err => {
        console.log(err);
      }
    })
  }

  getFirstChapterId(courseSlug: string, studentId: number) {
    this.coursesService.getChapters(courseSlug).subscribe({
      next: value => {
        const firstChapter = value.find(chapter => chapter.number === 1);

        if (firstChapter) {
          this.openFirstChapter(firstChapter.id, studentId);
        } else {
          console.error('Главу з номером 1 не знайдено.');
        }
      },
      error: error => {
        console.error('Помилка при отриманні глав:', error);
      }
    });
  }


}
