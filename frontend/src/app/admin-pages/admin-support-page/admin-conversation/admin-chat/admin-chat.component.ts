import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ChatMessageResponse } from 'src/models/chat';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { AdminSupportWebsocketshareService } from 'src/services/support/admin/admin-chat/admin-support-websocketshare.service';
import { SupportService } from 'src/services/support/support.service';

@Component({
  selector: 'app-admin-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.sass']
})
export class AdminChatComponent implements OnInit {
  
  @Input()
  userId: string = ''; 

  messages: ChatMessageResponse[] = [];
  logged!: LoggedUser;

  newMessage: FormControl;

  constructor(
    private supportService: SupportService,
    private adminChatService: AdminSupportWebsocketshareService,
  ) {
    this.newMessage = new FormControl();
  }

  ngOnInit(): void {
    this.getUserMessages();
    this.subscribeToNewMessages();
  }

  private getUserMessages(): void{
    this.supportService.getMessagesAsSupport(this.userId).subscribe({
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
    this.adminChatService.getNewValue().subscribe({
      next: (res: string) => {
        console.log(res)
        if(res){
          const msg = JSON.parse(res) as ChatMessageResponse;
          msg.isFromSupport=false;
          this.messages.push(msg)
          this.messages = [...this.messages]
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  sendNewMessage(): void {
    const newMessage: ChatMessageResponse = {content: this.newMessage.value, sentAt: new Date(), isFromSupport: true}
    this.messages.push(newMessage);
    this.messages = [...this.messages];

    this.supportService.sendMessageAsSupport(this.userId, {content: this.newMessage.value}).subscribe({
      next: (res: any) => {
        this.newMessage.setValue('')
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

}
