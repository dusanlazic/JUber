import { TestBed } from '@angular/core/testing';

import { SupportChatWebSocketAPI } from './support-chat-socket.service';

describe('SupportChatWebSocketAPI', () => {
  let service: SupportChatWebSocketAPI;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupportChatWebSocketAPI);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
