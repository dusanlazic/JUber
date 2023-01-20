import { TestBed } from '@angular/core/testing';

import { PaymentWebsocketshareService } from './payment-websocketshare.service';

describe('PaymentWebsocketshareService', () => {
  let service: PaymentWebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentWebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
