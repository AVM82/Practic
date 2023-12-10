
import {Chapter} from 'src/app/models/chapter';
import { Component, OnInit, AfterViewInit, ViewChildren, QueryList, ElementRef, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CourseNavbarComponent } from "../../componets/course-navbar/course-navbar.component";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { ReportButtonComponent } from "../../componets/report-button/report-button.component";
import { ReportServiceService } from "../../services/report-service.service";
import { StudentReport } from "../../models/studentReport";
import { Practice } from "../../models/practice";
import { TokenStorageService } from "../../services/token-storage.service";
import { ChaptersService } from "../../services/chapters.service";
import { StatePipe } from "../../pipes/practice-state.pipe";
import { User } from 'src/app/models/user';
import { CoursesService } from 'src/app/services/courses.service';
import { StateStudent } from 'src/app/models/student';
import Chart from 'chart.js/auto';
import { NgZone } from '@angular/core';
@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink,
    MatIconModule, MatButtonModule, ReportButtonComponent, StatePipe],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit , AfterViewInit{
  chapters: Chapter[] = [];
  reports: StudentReport[][]=[];
  slug: string='';
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
    private reportService: ReportServiceService,
    private coursesService: CoursesService,
    private chaptersService: ChaptersService,
    private tokenStorageService: TokenStorageService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {
    this.me = this.tokenStorageService.getMe();
  }


  ngAfterViewInit() {

        const practice = 0;
        const tests = 0;

        this.canvases.forEach((canvas, index) => {
                  const practicState = this.chapters[index].parts.length>0?this.chapters[index].parts[0].practice.state:'null';
                // console.log(practicState+" state practic, index: "+index);
                const percentPracticState = this.getPercentPracticState(practicState);
          // console.log('index' + index);
          const reports = this.chapters[index].myReports>0?100:0;

          // const idState = this.reportsState.indexOf(reportState) + 1;
          // console.log("reports count chapter:"+index+" "+reports);

          // const reportPercent = this.getPercentReportState("reportState");
          // console.log('state ' + reportState + "index state: " + reportPercent);

          this.percent[index] =Math.floor( (percentPracticState + tests + reports) / 3);
          // console.log(this.percent);


           this.zone.run(() => {
            this.cdr.detectChanges();
          });
          this.createChart([percentPracticState, tests, reports], canvas);
        });

  }


  ngOnInit(): void {
    this.zone.run(() => {
      this.cdr.detectChanges();
    });

    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
      if (slug) {
        this.slug = slug;
        this.stateStudent = this.me.getStudent(slug);
        this.isStudent = this.stateStudent != undefined;
        this.isInvolved = this.isStudent || this.me.isMentor(slug) || this.me.isGraduate(slug);
      }
    })
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

  createChart(data: number[], canvas: ElementRef<HTMLCanvasElement>) {
    const ctx = canvas.nativeElement.getContext('2d');
    const whiteColorfirst = 100 - data[0];
    const whiteColorSecond = 100 - data[1];
    const whiteColorThirst = 100 - data[2];
    if (ctx) {
      const myChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
          datasets: [{
            data: [whiteColorfirst, data[0]],
            backgroundColor: [
              //  this.percent==100?'':
              'rgba(103, 101, 101, 0.1)', 'rgba(27, 122, 88, 1)'
            ],
            borderWidth: 0
            ,
            label: "практична"
          }, {
            data: [whiteColorSecond, data[1]],
            backgroundColor: [
              // this.percent==100?:

              'rgba(103, 101, 101, 0.1)', 'rgba(69, 204, 126, 1)'
            ],

            borderWidth: 0,
            label: "тести"
          },

          {
            data: [whiteColorThirst, data[2]],
            backgroundColor: [
              // this.percent==100?'rgba(119, 254, 176, 1)':
              'rgba(103, 101, 101, 0.1)', 'rgba(119, 254, 176, 1)'
            ],

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
            legend: {
              position: 'center'
            }

          }
        }
      });
    }
  }

  getPercentReportState(state: string): number {
    switch (state) {
      case 'announced':
        return 33;
      case 'started':
        return 66;
      case 'finished':
        return 100;
      default:
        return 0;
    }
  }

  getPercentPracticState(state: string): number{
    switch (state.toLocaleUpperCase()) {
      case 'IN_PROCESS'||'PAUSE':
        return 33;
      case 'READY_TO_REVIEW':
        return 66;
      case 'APPROVED':
        return 100;

      // Добавьте другие кейсы по мере необходимости
      default:
        return 0; // Любое значение по умолчанию, если статус не соответствует ни одному из кейсов
    }
  }
}
