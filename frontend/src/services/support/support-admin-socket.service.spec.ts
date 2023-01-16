import { TestBed } from '@angular/core/testing';

import { SupportAdminWebSocketAPI } from './support-admin-socket.service';

describe('SupportAdminWebSocketAPI', () => {
  let service: SupportAdminWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupportAdminWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
