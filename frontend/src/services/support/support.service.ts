import { Injectable,  } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ChatConversationResponse, ChatMessageRequest, ChatMessage } from 'src/models/chat';
import { HttpRequestService } from '../util/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class SupportService {

  constructor(
    private httpRequestService: HttpRequestService
  ){}

  getMessagesAsUser(): Observable<Array<ChatMessage>> {
      const url = environment.API_BASE_URL + '/support/chat';
      return this.httpRequestService.get(url) as Observable<Array<ChatMessage>>;
  }

  sendMessageAsUser(messageRequest: ChatMessageRequest): Observable<any> {
    const url = environment.API_BASE_URL + '/support/chat';
    return this.httpRequestService.post(url, messageRequest) as Observable<any>;
  }

  // ADMIN
  getConversations(): Observable<Array<ChatConversationResponse>> {
    const url = environment.API_BASE_URL + '/support/admin/users';
    return this.httpRequestService.get(url) as Observable<Array<ChatConversationResponse>>;
  }

  getMessagesAsSupport(userId: string): Observable<Array<ChatMessage>> {
    const url = environment.API_BASE_URL + `/support/admin/users/${userId}/chat`;
    return this.httpRequestService.get(url) as Observable<Array<ChatMessage>>;
  }

  sendMessageAsSupport(userId: string, messageRequest: ChatMessageRequest): Observable<any> {
    const url = environment.API_BASE_URL + `/support/admin/users/${userId}/chat`;
    return this.httpRequestService.post(url, messageRequest) as Observable<any>;
  }

  markAsRead(conversationId: string): Observable<any> {
    const url = environment.API_BASE_URL + `/support/admin/markAsRead/${conversationId}`;
    return this.httpRequestService.patch(url, null) as Observable<any>;
  }
  
}
