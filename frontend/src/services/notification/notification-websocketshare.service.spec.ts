import { TestBed } from '@angular/core/testing';

import { NotificationWebsocketshareService } from './notification-websocketshare.service';

describe('NotificationWebsocketshareService', () => {
  let service: NotificationWebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationWebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
