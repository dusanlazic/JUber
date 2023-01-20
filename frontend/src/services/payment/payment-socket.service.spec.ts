import { TestBed } from '@angular/core/testing';

import { PaymentWebSocketAPI } from './payment-socket.service';

describe('PaymentWebSocketAPI', () => {
  let service: PaymentWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
