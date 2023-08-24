import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {Chapter} from "../../models/chapter/chapter";
import {ChaptersService} from "../../services/chapters/chapters.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {SplitPipe} from "../../pipes/split.pipe";

@Component({
  selector: 'app-chapter-details',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, RouterLink, MatIconModule, SplitPipe],
  templateUrl: './chapter-details.component.html',
  styleUrls: ['./chapter-details.component.css']
})
export class ChapterDetailsComponent implements OnInit {
    chapter?: Chapter ;

  constructor(
      private chaptersService: ChaptersService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const chapterId =  Number(params.get('chapterId'));

      if(chapterId) {
  
          this.chaptersService.getChapter(chapterId).subscribe(chapter =>
        {
          this.chapter = chapter;
        });
      }
    })
  }

}
