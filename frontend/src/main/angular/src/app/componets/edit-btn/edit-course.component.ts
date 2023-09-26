import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { CreationEditCourseCapabilityService } from "src/app/services/creation-edit-course.capability.service";

@Component({
    selector: 'app-edit-btn',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './edit-btn.component.html',
    styleUrls: ['./edit-btn.component.css']
  })
export class EditBtnComponent implements OnInit {
    editMode: boolean = false;
    showEditCapability: boolean = false;
  
    constructor(
        private creationEditCourseCapabilityService: CreationEditCourseCapabilityService
    ) {
        this.showEditCapability = creationEditCourseCapabilityService.hasCapability();
    }

    ngOnInit(): void {
        this.editMode = this.creationEditCourseCapabilityService.getEditMode();
    }

    changeMode(): void {
        this.editMode = !this.editMode;
    }
}