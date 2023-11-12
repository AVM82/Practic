import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CourseNavbarComponent} from "../../componets/course-navbar/course-navbar.component";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {CoursesService} from 'src/app/services/courses.service';
import {ActivatedRoute} from '@angular/router';
import {ApplyBtnComponent} from "../../componets/apply-btn/apply-btn.component";
import { EditBtnComponent } from 'src/app/componets/edit-btn/edit-course.component';
import { AdditionalMaterials } from 'src/app/models/additional.material';

@Component({
  selector: 'app-additional-materials',
  standalone: true,
  imports: [CommonModule, CourseNavbarComponent, MatCardModule, MatIconModule, ApplyBtnComponent, EditBtnComponent],
  templateUrl: './additional-materials.component.html',
  styleUrls: ['./additional-materials.component.css']
})
export class AdditionalMaterialsComponent implements OnInit {
  materials: AdditionalMaterials[] = [];
  isStudent: boolean = false;

  constructor(
    private courseService: CoursesService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const slug = this.route.snapshot.url.map(urlSegment => urlSegment.path)[1];
    if(slug)
        this.courseService.getAdditionalMaterials(slug).subscribe(materials => {
            this.materials = materials;
            this.isStudent = this.courseService.stateStudent != undefined;
        })
  }

  changeLearned(event: any) {
    this.courseService.putAdditionalChange(event);
  }
    
}
