import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ChatMessage } from 'src/models/chat';
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

  messages: ChatMessage[] = [];
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
          const msg = JSON.parse(res) as ChatMessage;
          msg.isFromSupport=true;
          this.messages.push(msg)
          this.messages = [...this.messages]
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private getAllMessage(): void{
    this.supportService.getMessagesAsUser().subscribe({
      next: (msgs: Array<ChatMessage>) => {
        this.messages = msgs;
        console.log(this.messages)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  sendNewMessage(): void {
    if(this.newMessage.value === ''){return}
    const newMessage: ChatMessage = {content: this.newMessage.value, sentAt: new Date(), isFromSupport: false}
    this.messages.push(newMessage);
    this.messages = [...this.messages];

    let messageContent = this.newMessage.value;
    this.newMessage.setValue('');
    
    this.supportService.sendMessageAsUser({content: messageContent}).subscribe({
      next: (res: any) => {
        
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }
}
