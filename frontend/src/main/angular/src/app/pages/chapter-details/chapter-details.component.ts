import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Course} from "../../models/course/course";
import {Chapter} from "../../models/chapter/chapter";
import {SubChapter} from "../../models/chapter/subchapter";
import {SubSubChapter} from "../../models/chapter/subsubchapter";
import {ChaptersService} from "../../services/chapters/chapters.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {
    course: Course | undefined;
    chapter: Chapter | undefined;
    subchapters: SubChapter[] = [];
    subsubchapters: SubSubChapter[] = [];

  constructor(
      private chaptersService: ChaptersService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const courseId = Number(params.get('courseId'));
      const chapterId =  Number(params.get('chapterId'));

      console.log("Chapter id = "+ chapterId);

      if(chapterId) {
        this.chaptersService.getChapter(courseId, chapterId).subscribe(chapter =>
        {
          this.chapter = chapter;
        });
      }
    })
  }

}
