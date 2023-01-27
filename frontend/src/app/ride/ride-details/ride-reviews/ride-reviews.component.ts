import { Component, Input, OnInit } from '@angular/core';
import { ReviewableInfo, RideReview, RideReviewInputEvent, RideReviewRequest } from 'src/models/rideReview';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { RideReviewService } from 'src/services/ride-review/ride-review.service';

@Component({
  selector: 'app-ride-reviews',
  templateUrl: './ride-reviews.component.html',
  styleUrls: ['./ride-reviews.component.sass']
})
export class RideReviewsComponent implements OnInit {

  @Input()
  rideId: string = '';

  loggedUser!: LoggedUser;
  rideReviews: RideReview[] = [];

  isDialogOpened: boolean = false;
  canLeaveReview: boolean = false;
  deadlineTxt: string = '';

  constructor(
    private rideReviewService: RideReviewService,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.getLoggedUser();
    this.getRideReviews();
    this.getReviewableInfo();
  }

  private getLoggedUser(): void {
    this.authService.getCurrentUser().subscribe({
      next: (logged: LoggedUser) => {
        this.loggedUser = logged;
      }
    })
  }

  private getRideReviews(): void {
    this.rideReviewService.getRideReviews(this.rideId).subscribe({
      next: (res: RideReview[]) => {
        console.log(res);
        this.rideReviews = res;
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }

  private getReviewableInfo(): void {
    this.rideReviewService.getReviewableInfo(this.rideId).subscribe({
      next: (info: ReviewableInfo) => {
        this.canLeaveReview = !info.alreadyReviewed && !info.deadlinePassed
        if(this.canLeaveReview){
          this.deadlineTxt = this.getDeadlineText(info.deadline);
        }
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }

  toggleModal() : void{
    this.isDialogOpened = !this.isDialogOpened
  }

  leaveReview(event: RideReviewInputEvent) {
    if(event.confirmed && event.rideReviewInput){
      const review = event.rideReviewInput;
    
      this.rideReviews.push({...review, reviewerImageUrl: this.loggedUser.imageUrl, reviewerFullName: this.loggedUser.name});
      this.rideReviews = [...this.rideReviews];
      
      this.saveReview({...review, rideId: this.rideId});

      this.canLeaveReview = false;
    }

    this.toggleModal();
  }

  private saveReview(review: RideReviewRequest): void {
    this.rideReviewService.leaveReview(review).subscribe({
      next: (res: any) => {
        console.log(res);
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }

  private getDeadlineText(deadline: Date) : string {
    const timeDifference = new Date(deadline).getTime() - new Date().getTime();
      const hours = Math.floor((timeDifference / (1000 * 60 * 60)) );
      if(hours > 48) {
        return '3 days left';
      }
      if(hours > 24) {
        return '2 day left';
      }
      if(hours > 0){
        return `${hours}h left`;
      }
      return 'no more time left';
  }
}
