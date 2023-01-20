import { TestBed } from '@angular/core/testing';

import { SupportChatWebsocketshareService } from './support-chat-websocketshare.service';

describe('SupportChatWebsocketshareService', () => {
  let service: SupportChatWebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupportChatWebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
