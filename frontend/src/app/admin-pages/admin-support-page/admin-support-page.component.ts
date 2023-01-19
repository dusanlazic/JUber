import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChatConversationResponse, MsgFromUserMessage, NewMessageEvent } from 'src/models/chat';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { AdminConversationWebsocketshareService } from 'src/services/support/admin/admin-conversations/admin-conversation-websocketshare.service';
import { SupportService } from 'src/services/support/support.service';

@Component({
  selector: 'app-admin-support-page',
  templateUrl: './admin-support-page.component.html',
  styleUrls: ['./admin-support-page.component.sass']
})
export class AdminSupportPageComponent implements OnInit {

  conversations: ChatConversationResponse[] = []
  unreadCount: number = 0

  openedConversation: ChatConversationResponse | undefined

  constructor( 
    private supportService: SupportService,
    private adminConversationService: AdminConversationWebsocketshareService
  ) { }

  ngOnInit(): void {
    this.setupChat();
  }

  private setupChat(): void {
    this.getAllConversations();
    this.subscribeToNewConversation();
  }

  private getAllConversations() : void {
    this.supportService.getConversations().subscribe({
      next: (conversations: Array<ChatConversationResponse>) => {
        this.conversations = conversations
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private subscribeToNewConversation(): void {
    this.adminConversationService.getNewValue().subscribe({
      next: (res: string) => {
        console.log(res)
        if(res){
          this.handleNewConversation(res);
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private handleNewConversation(res: string): void {
    const newConversation = JSON.parse(res) as ChatConversationResponse;
    newConversation.isRead = false;
    newConversation.isSelected = false;
    this.conversations.splice(0, 0, newConversation);
    this.conversations = [...this.conversations]
    this.unreadCount+=1
  }

  newMessageEvent(newMsgEvent: MsgFromUserMessage): void {
    console.log(newMsgEvent);
    const conversation = this.conversations.find(conversation => conversation.userId===newMsgEvent.userId)
    if(conversation) {
      conversation.isRead=false;
      conversation.messagePreview=newMsgEvent.content;
      conversation.date=newMsgEvent.sentAt;
    }
  }
  openChat(openedConversation: ChatConversationResponse) : void {
    this.closePreviousChat();
    this.openSelectedChat(openedConversation)
  }

  private closePreviousChat(){
    const opened = this.conversations.find(conversation => conversation===this.openedConversation)
    if(opened) {opened.isSelected=false}
  }

  private openSelectedChat(openedConversation: ChatConversationResponse){
    this.openedConversation = openedConversation;
    if(!openedConversation.isRead){this.unreadCount-=1;}

    const obj = this.conversations.find(conversation => conversation===openedConversation)
    if(obj) {
      obj.isRead=true;
      obj.isSelected=true
    }
  }
}
