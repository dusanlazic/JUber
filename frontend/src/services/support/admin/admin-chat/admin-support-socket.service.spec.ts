import { TestBed } from '@angular/core/testing';

import { AdminSupportWebSocketAPI } from './admin-support-socket.service';

describe('AdminSupportWebSocketAPI', () => {
  let service: AdminSupportWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminSupportWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
