import { OnInit, AfterViewInit, ChangeDetectorRef, Component, ElementRef, NgZone, QueryList, ViewChildren } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CourseNavbarComponent } from "../../componets/course-navbar/course-navbar.component";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { ReportButtonComponent } from "../../componets/report-button/report-button.component";
import { StudentReport } from "../../models/studentReport";
import { Practice } from "../../models/practice";
import { TokenStorageService } from "../../services/token-storage.service";
import { StatePipe } from "../../pipes/practice-state.pipe";
import { Chapter } from 'src/app/models/chapter';
import { User } from 'src/app/models/user';
import { Report } from 'src/app/models/report';
import { StateStudent } from 'src/app/models/student';
import { ReportState, STATE_APPROVED } from 'src/app/enums/app-constans';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink,
    MatIconModule, MatButtonModule, ReportButtonComponent, StatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements AfterViewInit, OnInit {
  chapters: Chapter[] = [];
  reports: StudentReport[][] = [];
  slug: string = '';
  practices: Practice[] = [];
  editMode: boolean = false;
  isStudent: boolean = false;
  isInvolved: boolean = false;
  stateStudent?: StateStudent;
  me!: User;
  percent: number[] = [];
  reportsState: String[] = [];
  @ViewChildren('myCanvas') canvases!: QueryList<ElementRef<HTMLCanvasElement>>;

  constructor(
    private route: ActivatedRoute,
    private tokenStorageService: TokenStorageService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {
    this.me = this.tokenStorageService.getMe();
  }

  ngOnInit(): void {
    this.zone.run(() => {
      this.cdr.detectChanges();
    });
  }


  ngAfterViewInit() {
    console.log("ng after start");
    if (this.canvases == null) {
      this.zone.run(() => {
        this.cdr.detectChanges();
      });
    }

    this.canvases.forEach((canvas, index) => {
      const practicPercent = this.getAveragePercentPracticForAllParts(index);
      const quizPercent = this.getPercentQuizCompletion(index);
      const reportPerceent = this.getPercentCompletionReport(index)
      console.log("for each :" + index)
      this.percent[index] = Math.floor((practicPercent + quizPercent + reportPerceent) / 3);
      this.createChart([practicPercent, quizPercent, reportPerceent], canvas);
    });

  }



  getSlug(slug: string) {
    this.slug = slug;
    this.stateStudent = this.me.getStudent(slug);
    this.isStudent = this.stateStudent != undefined;
    this.isInvolved = this.isStudent || this.me.isMentor(slug) || this.me.isGraduate(slug);
    this.zone.run(() => {
      this.cdr.detectChanges();
    });
  }

  getChapters(chapters: Chapter[]) {
    this.chapters = chapters;
  }

  setEditMode(editMode: boolean) {
    this.editMode = editMode;
  }

  editClick(chapter: Chapter) {
    console.log('edit click on chapter #', chapter.number, '(id=', chapter.id, ')');
  }

  getReports(myReports: Report[]): string {
    let number = myReports.filter(report => report.state === ReportState.APPROVED).length;
    return number === 1 ? '1 проведена'
      : ((number === 0 ? 'не' : number) + ' проведено');
  }

  getAveragePercentPracticForAllParts(chapterIndex: number): number {
    let result = 0;
    const parts = this.chapters[chapterIndex].parts;
  
    if (parts?.length > 0) {
      parts.forEach((chapter) => {
        if (chapter.practice) {
          result += this.getPercentPracticState(chapter.practice.state);
        }
      });
  
      return result / parts.length;
    }
  
    return result;
  }
  
  getPercentCompletionReport(chapterIndex: number): number {
    const reports = this.chapters[chapterIndex].myReports;

    if (reports?.length > 0) {
      const firstReportState = reports[0].state;
      console.log(firstReportState);
      return this.getPercentReportState(firstReportState);
    }

    return 0;
  }

  getPercentQuizCompletion(chapterIndex: number): number {

    return this.chapters[chapterIndex].quizPassed?100:0;
  }

  createChart(data: number[], canvas: ElementRef<HTMLCanvasElement>) {
    console.log(data)
    const ctx = canvas.nativeElement.getContext('2d');
    const whiteColorfirst = 100 - data[0];
    const whiteColorSecond = 100 - data[1];
    const whiteColorThirst = 100 - data[2];
    if (ctx) {
      const myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
          datasets: [
            {
              data: [whiteColorfirst, data[0]],
              backgroundColor: ['rgba(103, 101, 101, 0.1)', 'rgba(27, 122, 88, 1)'],
              borderWidth: 0,
              label: "практична"
            },
            {
              data: [whiteColorSecond, data[1]],
              backgroundColor: ['rgba(103, 101, 101, 0.1)', 'rgba(69, 204, 126, 1)'],
              borderWidth: 0,
              label: "тести"
            },
            {
              data: [whiteColorThirst, data[2]],
              backgroundColor: ['rgba(103, 101, 101, 0.1)', 'rgba(119, 254, 176, 1)'],
              borderWidth: 0,
              label: "доповiдь"
            }]
        },
        options: {
          cutout: '50%',
          plugins: {
            tooltip: {
              callbacks: {
                label: function (context: any) {
                  let label = context.dataset.label || '';
                  return label;
                }
              }
            },
            legend: { position: 'center' }
          }
        }
      });
    }
  }

  getPercentReportState(state: string): number {
    switch (state.toUpperCase()) {
      case 'ANNOUNCED':
        return 25;
      case 'STARTED':
        return 50;
      case 'FINISHED':
        return 75;
      case 'APPROVED':
        return 100;
      default:
        return 0;
    }
  }

  getPercentPracticState(state: string): number {
    console.log(state);

    switch (state.toUpperCase()) {
      case 'IN_PROCESS':
        return 33;
      case 'PAUSE':
        return 33;
      case 'READY_TO_REVIEW':
        return 66;
      case 'APPROVED':
        return 100;
      default:
        return 0;
    }
  }

}
