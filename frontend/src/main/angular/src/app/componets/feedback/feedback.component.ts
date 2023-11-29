import {Component, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {FeedbackService} from 'src/app/services/feedbacks.service';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {FeedbackDialogComponent} from 'src/app/componets/feedback-dialog/feedback-dialog.component'
import {MatPaginatorModule, MatPaginator} from '@angular/material/paginator';
import {MatTableModule, MatTableDataSource} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {TokenStorageService} from "../../services/token-storage.service";
import {User} from 'src/app/models/user';
import {MatMenuModule} from '@angular/material/menu';
import {Feedback, FeedbackPage} from "../../models/feedback";

@Component({
    selector: 'app-feedback',
    standalone: true,
    imports: [CommonModule, HttpClientModule, MatDialogModule, MatPaginatorModule,
        MatTableModule, MatIconModule, MatMenuModule],
    templateUrl: './feedback.component.html',
    styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {
    feedbacks: Feedback[] = [];
    page = 0;
    pageSize = 5;
    totalPages  = 0;
    totalFeedbacks = 0;

    dataSource = new MatTableDataSource<any>(this.feedbacks);
    me?: User;
    myId: number = 0;
    feedbackSortedState: string = "DATE_DESCENDING";
    @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

    constructor(
        private feedbackService: FeedbackService,
        private dialog: MatDialog,
        private tokenStorageService: TokenStorageService
    ) {
        this.me = this.tokenStorageService.me;
        if (this.me) {
            this.myId = this.me.id;
        }
    }

    ngOnInit(): void {
       this.getFeedbackPage();
    }

    private freshPage(page: FeedbackPage) {
        this.feedbacks = [];
        page.feedbacksOnPage.forEach((feedback: any) => this.feedbacks.push(new Feedback(feedback, this.myId)));
        this.dataSource = new MatTableDataSource<any>(this.feedbacks);
        this.totalPages = page.totalPages;
        this.totalFeedbacks = page.totalFeedbacks;
    }

    getFeedbackPage() {
        this.feedbackService.getFeedbacks(this.page, this.pageSize, this.feedbackSortedState)
            .subscribe(page => this.freshPage(page))
    }

    sortData(sortState: string) {
        this.feedbackSortedState = sortState;
        this.getFeedbackPage();
    }

    getDate(date: string): string {
        return new Date(date).toLocaleDateString();
    }

    openFeedbackDialog(): void {
        const dialogRef = this.dialog.open(FeedbackDialogComponent, {
            width: '1150px',
            height: '650px'
        });

        dialogRef.componentInstance.feedbackSaved.subscribe(() => {
            this.ngOnInit();
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.ngOnInit();
            }
        });
    }

    getStudentNames(): string[] {
        return this.feedbacks.map(feedback => feedback.name || "Немає ім'я");
    }

    getFeedbacks(): string[] {
        return this.feedbacks.map(feedback => feedback.feedback || 'Немає відгуків');
    }

    getProfilePictureUrls(): string[] {
        return this.feedbacks.map(feedback => feedback.profilePictureUrl || 'URL зображення відсутній');
    }

    ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
    }

    onPageChange(event: any): void {
        this.page = event.pageIndex;
        this.pageSize = event.pageSize;
        this.totalPages = event.length;

        this.getFeedbackPage()
    }


    deleteFeedback(feedback: Feedback) {
        this.feedbackService.deleteFeedback(feedback.id, this.page, this.pageSize, this.feedbackSortedState)
            .subscribe(page => this.freshPage(page))
    }


    incrementLike(feedback: Feedback) {
        this.feedbackService.incrementLikes(feedback).subscribe({
            next: (response) =>
                feedback.update(response, this.myId),
            error: (error) => {
                if (error.status === 404)
                    this.getFeedbackPage();
            }
        })
    }

    decrementLike(feedback: Feedback) {
        this.feedbackService.decrementLikes(feedback).subscribe({
            next: (response) =>
                feedback.update(response, this.myId),
            error: (error) => {
                if (error.status === 404)
                    this.getFeedbackPage();
            }
        })
    }


    isAuthorOrAdmin(feedback: Feedback): boolean {
        return feedback.personId == this.myId || this.me!.hasAdminRole();
    }
}
