import { Injectable } from '@angular/core';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs'
import { environment } from 'src/environments/environment';
import { ReviewableInfo, RideReview, RideReviewRequest } from 'src/models/rideReview';

@Injectable({
  providedIn: 'root'
})
export class RideReviewService {

  constructor(
    private httpRequestService: HttpRequestService
  ) {}

  leaveReview(rideReview: RideReviewRequest) : Observable<any> {
    const url = environment.API_BASE_URL + `/review/leaveReview`;
    return this.httpRequestService.post(url, rideReview) as Observable<any>;
  }
  
  getRideReviews(rideId: string): Observable<Array<RideReview>> {
    const url = environment.API_BASE_URL + `/review/${rideId}`;
    return this.httpRequestService.get(url) as Observable<Array<RideReview>>;
  }

  getMyReviews() : Observable<Array<RideReview>> {
    const url = environment.API_BASE_URL + `/review/myReviews`;
    return this.httpRequestService.get(url) as Observable<Array<RideReview>>;
  }

  getReviewableInfo(rideId: string): Observable<ReviewableInfo> {
    const url = environment.API_BASE_URL + `/review/reviewableInfo/${rideId}`;
    return this.httpRequestService.get(url) as Observable<ReviewableInfo>;
  }

}
