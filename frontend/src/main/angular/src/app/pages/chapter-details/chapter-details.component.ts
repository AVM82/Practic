import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Chapter} from "../../models/chapter/chapter";
import {ChaptersService} from "../../services/chapters/chapters.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CdkAccordionModule} from '@angular/cdk/accordion';
import {MatTooltipModule} from "@angular/material/tooltip";

@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule,
    CdkAccordionModule, MatTooltipModule],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {
    chapter?: Chapter ;
    showPartNumber: boolean = false;

  constructor(
      private chaptersService: ChaptersService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');
      const chapterN =  Number(params.get('chapterN'));

      if(slug && chapterN) {
  
          this.chaptersService.getChapter(slug, chapterN).subscribe(chapter =>
        {
          this.chapter = chapter;
          this.showPartNumber = chapter.parts.length > 1;
        });
      }
    })
  }

  playAction() {

  }

  pauseAction() {

  }

  doneAction() {

  }
}
