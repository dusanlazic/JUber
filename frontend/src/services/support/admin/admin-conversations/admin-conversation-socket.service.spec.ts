import { TestBed } from '@angular/core/testing';

import { AdminConversationWebSocketAPI } from './admin-conversation-socket.service';

describe('AdminConversationWebSocketAPI', () => {
  let service: AdminConversationWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminConversationWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
