import { TestBed } from '@angular/core/testing';

import { NotificationWebSocketAPI } from './notification-socket.service';

describe('NotificationWebSocketAPI', () => {
  let service: NotificationWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
