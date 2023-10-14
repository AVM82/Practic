import { CommonModule } from "@angular/common";
import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { CoursesService } from "src/app/services/courses/courses.service";

@Component({
    selector: 'app-edit-btn',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './edit-btn.component.html',
    styleUrls: ['./edit-btn.component.css']
  })
export class EditBtnComponent  {
    @Input() showEditCapability: boolean =false;
    editMode: boolean = false;
  

    changeMode(): void {
        this.editMode = !this.editMode;
    }

}