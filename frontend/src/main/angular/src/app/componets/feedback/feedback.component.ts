import { Component,OnInit,ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FeedbackService } from 'src/app/services/feedback/feedbacks.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { FeedbackDialogComponent } from 'src/app/componets/feedback-dialog/feedback-dialog.component'
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
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
  constructor(private feedbackService: FeedbackService, private dialog: MatDialog,private tokenStorageService:TokenStorageService) { }
  

  ngOnInit(): void {
    this.feedbackService.getFeedbacks().subscribe(data => {
      this.feedbacks = data;
    });
    const token = this.tokenStorageService.getToken();
      if (token) {
        const user: User = this.tokenStorageService.getUser();
        let currentUser: User = new User(user.roles,user.name,user.profilePictureUrl,user.email,user.id);
        this.id = currentUser.id;
      }
  }

  openFeedbackDialog(): void {
    const dialogRef = this.dialog.open(FeedbackDialogComponent, {
      width: '1150px', 
      height: '650px'
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
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
  }

  incrementLikes(feedback: any) {
    const id = feedback.id;
    this.feedbackService.incrementLikes(id, this.id).subscribe(
      (response) => {
        console.log("Likes increment:", response);
      },
      (error) => {
        console.error("Likes not incremented:", error);
      }
    );
    this.feedbackLiked = true;
    window.location.reload();
  }
  
  decrementLikes(feedback:any){
    const id =  feedback.id;
    this.feedbackService.decrementLikes(id,this.id).subscribe(response=>{
      console.log("Likes increment:",response);
    },error=>{console.error("likes not increment:",error);
    });
    this.feedbackLiked = true;
    window.location.reload();
  }

  changeLike(feedback: any) {
    const likedPersons: any[] = feedback.likedByPerson;
    const ids = likedPersons.map((person: any) => person.id);
     const userLikedIndex = likedPersons.findIndex((person: any) => person.id === this.id);
    if (userLikedIndex !== -1) {
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
}
