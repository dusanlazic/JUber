import { TestBed } from '@angular/core/testing';

import { AdminConversationWebsocketshareService } from './admin-conversation-websocketshare.service';

describe('AdminConversationWebsocketshareService', () => {
  let service: AdminConversationWebsocketshareService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminConversationWebsocketshareService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
