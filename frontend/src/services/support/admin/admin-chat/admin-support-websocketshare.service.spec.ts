import { TestBed } from '@angular/core/testing';

import { AdminSupportWebsocketshareService } from './admin-support-websocketshare.service';

describe('AdminSupportWebsocketshareService', () => {
  let service: AdminSupportWebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminSupportWebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
