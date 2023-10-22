import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Output } from "@angular/core";

@Component({
    selector: 'app-edit-btn',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './edit-btn.component.html',
    styleUrls: ['./edit-btn.component.css']
  })
export class EditBtnComponent  {
    editMode: boolean = false;
    @Output() editModeChanged: EventEmitter<boolean> = new EventEmitter();
  

    changeMode(): void {
        this.editMode = !this.editMode;
        this.editModeChanged.emit(this.editMode);
    }

}