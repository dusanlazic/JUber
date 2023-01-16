import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ChatConversationResponse, ChatMessage, ChatMessageResponse } from 'src/models/chat';
import { LoggedUser, Roles } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { SupportService } from 'src/services/support/support.service';
import { SupportChatWebsocketshareService } from 'src/services/support/user/support-chat-websocketshare.service';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.sass']
})
export class SupportComponent implements OnInit {

  messages: any[] = [];
  logged!: LoggedUser;

  newMessage: FormControl;

  constructor(
    private authService: AuthService,
    private supportService: SupportService,
    private websocketService: SupportChatWebsocketshareService,
  ) {
    this.newMessage = new FormControl();
  }

  ngOnInit(): void {
    this.getLogged();
  }

  private getLogged() : void {
    this.authService.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.logged = user;
        this.setupChat();
      }
    })
  }

  private setupChat(): void {
    this.getAllMessage();
    this.subscribeToNewMessages();
  }

  private subscribeToNewMessages() : void {
    this.websocketService.getNewValue().subscribe({
      next: (res: string) => {
        if(res){
          const msg = JSON.parse(res) as ChatConversationResponse;
          this.messages.push(msg)
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private getAllMessage(): void{
    this.supportService.getMessagesAsUser().subscribe({
      next: (msgs: Array<ChatMessageResponse>) => {
        this.messages = msgs;
        console.log(this.messages)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  sendNewMessage(): void {
    this.supportService.sendMessageAsUser({content: this.newMessage.value}).subscribe({
      next: (res: any) => {
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }
}
