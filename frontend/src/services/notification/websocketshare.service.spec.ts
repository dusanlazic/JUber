import { TestBed } from '@angular/core/testing';

import { WebsocketshareService } from './websocketshare.service';

describe('WebsocketshareService', () => {
  let service: WebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
