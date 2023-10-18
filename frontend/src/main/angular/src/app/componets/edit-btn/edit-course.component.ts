import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";

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