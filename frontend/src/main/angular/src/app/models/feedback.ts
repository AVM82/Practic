
export class Feedback{
    id: number = 0;
    personId: number = 0;
    name: string = '';
    profilePictureUrl: string = '';
    date: string = '';
    feedback: string = '';
    likes: number = 0;
    likedByPerson: number[] = [];
    meLiked: boolean = false;

    constructor( feedback: Feedback, userId: number) {
       this.update(feedback, userId);
    }

    update(feedback: Feedback, userId: number): void {
        this.id = feedback.id;
        this.personId = feedback.personId;
        this.name = feedback.name;
        this.feedback = feedback.feedback;
        this.date = feedback.date;
        this.likes = feedback.likes;
        this.likedByPerson = feedback.likedByPerson;
        this.profilePictureUrl = feedback.profilePictureUrl;
        this.meLiked = this.likedByPerson.some(p => p == userId);
    }

}

export interface FeedbackPage {
    feedbacksOnPage: Feedback[];
    totalFeedbacks: number;
    totalPages: number;

}

export interface FeedbackLike {
    page: FeedbackPage;
    feedback: Feedback;
}