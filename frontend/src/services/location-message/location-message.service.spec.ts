import { TestBed } from '@angular/core/testing';

import { LocationSocketService } from './location-message.service';

describe('LocationService', () => {
  let service: LocationSocketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationSocketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
