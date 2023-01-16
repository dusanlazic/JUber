import { Component, OnInit, Input } from '@angular/core';
import { ChatConversationResponse, ChatMessage } from 'src/models/chat';

@Component({
  selector: 'app-support-message',
  templateUrl: './support-message.component.html',
  styleUrls: ['./support-message.component.sass']
})
export class SupportMessageComponent implements OnInit {

  @Input()
  message!: ChatConversationResponse

  constructor() { }

  ngOnInit(): void {
  }

}
