import { Component,OnInit,ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FeedbackService } from 'src/app/services/feedback/feedbacks.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FeedbackDialogComponent } from 'src/app/componets/feedback-dialog/feedback-dialog.component'
import { MatPaginatorModule,MatPaginator } from '@angular/material/paginator';
import { MatTableModule,MatTableDataSource } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import {TokenStorageService} from "../../services/auth/token-storage.service";
import {User} from "../../services/auth/auth.service";

@Component({
  selector: 'app-feedback',
  standalone: true,
  imports: [CommonModule, HttpClientModule,MatDialogModule,MatPaginatorModule,
    MatTableModule,MatIconModule],
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {
  feedbacks: any[] = [];
  page = 1;
  pageSize = 5;
  dataSource = new MatTableDataSource<any>(this.feedbacks);
  id:number=0
  feedbackLiked: boolean = false;
  loading: boolean = false;
  loadingIncremend: boolean = false;
  loadingDecremend: boolean = false;

  constructor(private feedbackService: FeedbackService, private dialog: MatDialog,private tokenStorageService:TokenStorageService) { }
  

  ngOnInit(): void {
    this.updateData();
    const token = this.tokenStorageService.getToken();
      if (token) {
        const user: User = this.tokenStorageService.getUser();
        this.id = user.id;
      }
  }

  updateData():void{
    this.feedbackService.getFeedbacks().subscribe(data => {
      this.feedbacks = data;
      this.dataSource = new MatTableDataSource<any>(this.feedbacks);
    });
  }

  openFeedbackDialog(): void {
    const dialogRef = this.dialog.open(FeedbackDialogComponent, {
      width: '1150px', 
      height: '650px'
    });

    dialogRef.componentInstance.feedbackSaved.subscribe((savedData) => {
      this.updateData();
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateData();
        console.log(result);
      }
    });}

    getStudentNames(): string[] {
    return this.feedbacks.map(feedback => feedback.student.name || "Немає ім'я");
  }

  getFeedbacks(): string[] {
    return this.feedbacks.map(feedback => feedback.feedback || 'Немає відгуків');
  }

  getProfilePictureUrls(): string[] {
    return this.feedbacks.map(feedback => feedback.student.profilePictureUrl || 'URL зображення відсутній');
  }

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  onPageChange(event: any): void {
    this.page = event.pageIndex + 1;
    this.pageSize=event.pageSize;
  }

  incrementLikes(feedback: any) {
    if (!this.loadingIncremend) {
      this.loadingIncremend = true;
      const id = feedback.id;
      this.feedbackService.incrementLikes(id, this.id).subscribe(
        (response) => {
          console.log("Likes increment:", response);
          this.updateData();
        },
        (error) => {
          console.error("Likes not incremented:", error);
        }
      ).add(() => {
        this.loadingIncremend = false;
      });
      this.feedbackLiked = true;
    }
  }
  
  decrementLikes(feedback:any){
    if (!this.loadingDecremend) {
      this.loadingDecremend = true; 
      const id =  feedback.id;
      this.feedbackService.decrementLikes(id, this.id).subscribe(response=>{
        console.log("Likes increment:",response);
        this.updateData();
      },error=>{console.error("likes not increment:",error);
      }).add(() => {
        this.loadingDecremend = false; 
      });
      this.feedbackLiked = true;
    }
  }

  changeLike(feedback: any) {
    if (this.isLiked(feedback)) {
            this.decrementLikes(feedback);
    } else {
            this.incrementLikes(feedback);
    }
  }

  isLiked(feedback: any):boolean{
    const likedPersons: any[] = feedback.likedByPerson;
    const userLikedIndex = likedPersons.findIndex((person: any) => person.id === this.id);
    if (userLikedIndex !== -1) {
            return true;
    } else {
     return false;
    }
  }

  getDate(feedback: any):string{
    const date = new Date(feedback.dateTime);
    return date.toLocaleDateString();
  }
}
