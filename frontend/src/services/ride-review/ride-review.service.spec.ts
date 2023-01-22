import { TestBed } from '@angular/core/testing';

import { RideReviewService } from './ride-review.service';

describe('RideReviewService', () => {
  let service: RideReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RideReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
