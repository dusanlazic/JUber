import { Component, OnInit, Input } from '@angular/core';
import { RideReview } from 'src/models/rideReview';

@Component({
  selector: 'app-ride-review',
  templateUrl: './ride-review.component.html',
  styleUrls: ['./ride-review.component.sass']
})
export class RideReviewComponent implements OnInit {

  @Input()
  review!: RideReview;

  constructor() { }

  ngOnInit(): void {
  }

  numSequence(n: number): Array<number> {
    return Array(n);
  }

}
