import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";

@Component({
  selector: 'app-table-widget',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, NgOptimizedImage],
  templateUrl: './table-widget.component.html',
  styleUrls: ['./table-widget.component.css']
})

export class TableWidgetComponent implements OnChanges, AfterViewInit {
  @Input() displayedColumns: string[]=[];
  @Input() data: any[]=[];
  @Input() columnNameMap: { [key: string]: string }={};
  @Input() showPaginator: boolean = true;
  @Input() isRequestInProgress: boolean = false;
  @Output() performAction = new EventEmitter<any>();


  dataSource = new MatTableDataSource<any>(this.data);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isCreatedAtOrUpdatedAt(column: string): boolean {
    return column === 'createdAt' || column === 'updatedAt';
  }

  isColumnWithBnt(column: string): boolean {
    return column.startsWith('btn');
  }

  isColumnWithImg(column: string): boolean {
    return  column === 'profilePictureUrl';
  }

  isCommonColumn(column: string): boolean {
    return !this.isColumnWithImg(column) &&
        !this.isColumnWithBnt(column) &&
        !this.isCreatedAtOrUpdatedAt(column);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data']) {
      this.dataSource.data = this.data;
    }
  }
}