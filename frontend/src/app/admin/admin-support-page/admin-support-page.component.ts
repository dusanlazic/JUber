import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChatConversationResponse, MsgFromUserMessage } from 'src/models/chat';
import { AdminSupportWebsocketshareService } from 'src/services/support/admin/admin-chat/admin-support-websocketshare.service';
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
    private adminConversationService: AdminConversationWebsocketshareService,
    private adminChatService: AdminSupportWebsocketshareService,
  ) { }

  ngOnInit(): void {
    this.setupChat();
  }

  private setupChat(): void {
    this.getAllConversations();
    this.subscribeToNewConversation();
    this.subscribeToNewMessages();
  }

  private getAllConversations() : void {
    this.supportService.getConversations().subscribe({
      next: (conversations: Array<ChatConversationResponse>) => {
        this.conversations = conversations;
        this.unreadCount = this.conversations.filter(convo => convo.isRead===false).length
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private subscribeToNewConversation(): void {
    this.adminConversationService.getNewValue().subscribe({
      next: (res: string) => {
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

  openChat(openedConversation: ChatConversationResponse) : void {
    this.closePreviousChat();
    this.openSelectedChat(openedConversation)
  }

  private closePreviousChat(): void{
    const opened = this.conversations.find(conversation => conversation===this.openedConversation)
    if(opened) {opened.isSelected=false}
  }

  private openSelectedChat(openedConversation: ChatConversationResponse): void{
    this.openedConversation = openedConversation;
    const conversation = this.conversations.find(conversation => conversation===openedConversation)
    if(conversation) {
      if(conversation.isRead===false && this.unreadCount > 0){
        this.markAsRead(openedConversation.conversationId);
        this.unreadCount-=1;
      }
      conversation.isRead=true;
      conversation.isSelected=true
    }
    this.moveToTheTop(conversation);
    this.conversations = [...this.conversations]
  }
  
  private markAsRead(conversationId: string): void{
    this.supportService.markAsRead(conversationId).subscribe({
      next: () => {
        console.log('read')
      }
    })
  }

  private subscribeToNewMessages(): void {
    this.adminChatService.getNewValue().subscribe({
      next: (res: string) => {
        if(res){
          const msg = JSON.parse(res) as MsgFromUserMessage;
          this.handleNewMessage(msg)
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  handleNewMessage(msg: MsgFromUserMessage): void{
    const conversation = this.conversations.find(conversation => conversation.userId===msg.userId)
    if(conversation) {
      conversation.messagePreview=msg.content;
      conversation.date=msg.sentAt;

      if(!msg.isFromSupport){
        this.markAsUnread(conversation, msg.userId)
        this.sortConversations(conversation)
      }
    }
  }

  private sortConversations(conversation: ChatConversationResponse): void{
    this.moveToTheTop(conversation);
    this.moveToTheTop(this.openedConversation);
    this.conversations = [...this.conversations]
  }

  private moveToTheTop(conversation: ChatConversationResponse|undefined){
    this.conversations.sort(function(x,y){ return x == conversation ? -1 : y == conversation ? 1 : 0; });
  }

  private markAsUnread(conversation: ChatConversationResponse, userId: string) {
    if(this.openedConversation?.userId !== userId){
      if(conversation.isRead===true){
        this.unreadCount+=1
      }
      conversation.isRead=false;
    }
  }


}
