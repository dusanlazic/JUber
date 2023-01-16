import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ChatConversationResponse } from 'src/models/chat';

@Component({
  selector: 'app-admin-conversation',
  templateUrl: './admin-conversation.component.html',
  styleUrls: ['./admin-conversation.component.sass']
})
export class AdminConversationComponent implements OnInit {

  @Input()
  conversation!: ChatConversationResponse
  chatOpened: boolean;

  URL_BASE: string = environment.API_BASE_URL;
  DEFAULT_PROFILE_PHOTO: string = environment.DEFAULT_PROFILE_PHOTO
  
  constructor() {
    this.chatOpened = false;
  }

  ngOnInit(): void {
  }

  openChat(){
    this.chatOpened = !this.chatOpened
  }

}
