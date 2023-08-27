import {AfterViewInit, Component, Input, OnChanges, SimpleChanges, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";

@Component({
  selector: 'app-table-widget',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule],
  templateUrl: './table-widget.component.html',
  styleUrls: ['./table-widget.component.css']
})

export class TableWidgetComponent implements OnChanges, AfterViewInit {
  @Input() displayedColumns: string[]=[];
  @Input() data: any[]=[];
  @Input() columnNameMap: { [key: string]: string }={};
  @Input() showPaginator: boolean = true;

  dataSource = new MatTableDataSource<any>(this.data);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  isCreatedAtOrUpdatedAt(column: string): boolean {
    return column === 'createdAt' || column === 'updatedAt';
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