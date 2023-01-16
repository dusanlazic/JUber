import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ChatMessage, ChatMessageResponse } from 'src/models/chat';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { SupportService } from 'src/services/support/support.service';
import { WebsocketshareService } from 'src/services/websocketshare/websocketshare.service';

@Component({
  selector: 'app-admin-support',
  templateUrl: './admin-support.component.html',
  styleUrls: ['./admin-support.component.sass']
})
export class AdminSupportComponent implements OnInit {

  messages: ChatMessage[] = [];
  logged!: LoggedUser;

  newMessage: FormControl;
  userId: string = '6aebc916-dd04-4674-a4f2-99edec0a1811'; 

  constructor(
    private authService: AuthService,
    private supportService: SupportService,
    private websocketService: WebsocketshareService,
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
    this.getAllMessages(this.userId)
    this.subscribeToNewMessages();
  }

  private getAllMessages(userId: string): void{
    this.supportService.getMessagesAsSupport(userId).subscribe({
      next: (msgs: Array<ChatMessageResponse>) => {
        this.messages = msgs;
        console.log(this.messages)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }
  
  private subscribeToNewMessages(): void {
    this.websocketService.getNewValue().subscribe({
      next: (res: string) => {
        console.log(res)
        if(res){
          const msg = JSON.parse(res) as ChatMessage;
          this.messages.push(msg)
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  sendNewMessage(): void {
    this.supportService.sendMessageAsSupport(this.userId, {content: this.newMessage.value}).subscribe({
      next: (res: any) => {
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

}
