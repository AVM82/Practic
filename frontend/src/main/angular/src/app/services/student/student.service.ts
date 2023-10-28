import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/internal/Observable";
import { ApiUrls, getOpenedChaptersUrl, getStudentChapterUrl } from "src/app/enums/api-urls";
import { Chapter } from "src/app/models/chapter/chapter";
import { ShortChapter } from "src/app/models/course/chapter";

@Injectable({
    providedIn: 'root'
  })
  
export class StudentService {
    shortChapters!: Observable<ShortChapter[]>;
    chapters: Chapter[] = [];
    slug: string = '';
  
    constructor(
        private http: HttpClient
      ) {}

    setCourse(slug: string): void {
        if (slug !== this.slug) {
            this.slug = slug;
            this.chapters = [];
            this.shortChapters = this.http.get<ShortChapter[]>(getOpenedChaptersUrl(slug));
        }
    }
    
    getStudentChapters(slug: string): Observable<ShortChapter[]> {
        this.setCourse(slug);
        return this.shortChapters;
    }

    getStudentChapter(slug: string, number: number): Observable<Chapter> {
        this.setCourse(slug);
        return this.http.get<Chapter>(getStudentChapterUrl(slug, number));
    }

}

