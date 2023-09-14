import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {AdditionalMaterials} from 'src/app/models/material/additional.material';
import {CoursesService} from 'src/app/services/courses/courses.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-additional-materials',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, MatIconModule],
  templateUrl: './additional-materials.component.html',
  styleUrls: ['./additional-materials.component.css']
})
export class AdditionalMaterialsComponent implements OnInit {
    materials?: AdditionalMaterials[];
    slug: string = '';

  constructor(
      private courseService: CoursesService,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug');

      if(slug) {
          this.courseService.getAdditionalMaterials(slug).subscribe(materials =>
        {
          this.materials = materials;
          this.slug = slug;
        });
      }
    })
  }

  changeLearned(event: any) {
    console.log("checkbox id:%d checked:", event.target.id, event.target.checked);
    this.courseService.postAdditionalChange(this.slug, event.target.id, event.target.checked);
  }
    
}
