import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ChatConversationResponse } from 'src/models/chat';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-admin-conversation',
  templateUrl: './admin-conversation.component.html',
  styleUrls: ['./admin-conversation.component.sass']
})
export class AdminConversationComponent implements OnInit {

  @Input()
  conversation!: ChatConversationResponse
  @Output() openChatEvent = new EventEmitter<ChatConversationResponse>();

  URL_BASE: string = environment.API_BASE_URL;
  DEFAULT_PROFILE_PHOTO: string = environment.DEFAULT_PROFILE_PHOTO
  
  constructor(public authService: AuthService) {
  }

  ngOnInit(): void {
  }

  openChat(){
    this.openChatEvent.emit(this.conversation);
  }

}
