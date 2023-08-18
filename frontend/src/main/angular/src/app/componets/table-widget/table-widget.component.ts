import {Component, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatTableModule} from "@angular/material/table";

@Component({
  selector: 'app-table-widget',
  standalone: true,
  imports: [CommonModule, MatTableModule],
  templateUrl: './table-widget.component.html',
  styleUrls: ['./table-widget.component.css']
})

export class TableWidgetComponent {
  @Input() displayedColumns: string[]=[];
  @Input() dataSource: any[]=[];
  @Input() columnNameMap: { [key: string]: string }={};
}
