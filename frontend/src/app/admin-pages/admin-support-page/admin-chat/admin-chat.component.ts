import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { FormControl } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { ChatConversationResponse, ChatMessage, MsgFromUserMessage } from 'src/models/chat';
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
  conversation!: ChatConversationResponse;
  @Output() newMessageSentEvent = new EventEmitter<MsgFromUserMessage>();

  userId: string = '' 

  messages: ChatMessage[] = [];

  newMessage: FormControl;
  URL_BASE = environment.API_BASE_URL;

  @ViewChild('input', { static: false }) 
   set input(element: ElementRef<HTMLInputElement>) {
     if(element) {
       element.nativeElement.focus()
     }
  }

  constructor(
    private supportService: SupportService,
    private adminChatService: AdminSupportWebsocketshareService,
    public authService: AuthService
  ) {
    this.newMessage = new FormControl();
  }

  ngOnInit(): void {
    this.subscribeToNewMessages();
    this.userId = this.conversation.userId
  }

  ngOnChanges(changes: any) {
    this.conversation = changes.conversation.currentValue;
    this.userId = this.conversation.userId
    this.getUserMessages();
  }

  private getUserMessages(): void{
    this.supportService.getMessagesAsSupport(this.userId).subscribe({
      next: (msgs: Array<ChatMessage>) => {
        this.messages = msgs;
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }
  
  private subscribeToNewMessages(): void {
    this.adminChatService.getNewValue().subscribe({
      next: (res: string) => {
        if(res){
          const msg = JSON.parse(res) as MsgFromUserMessage;
          this.handleNewMessageArrived(msg)
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }
  private handleNewMessageArrived(msg: MsgFromUserMessage): void{
    if(msg.userId === this.userId){
      msg.isFromSupport=false;
      this.messages.push(msg)
      this.messages = [...this.messages]
    }
  }
  sendNewMessage(): void {
    if(this.newMessage.value === ''){return}

    // add locally
    const newMessage: ChatMessage = {content: this.newMessage.value, sentAt: new Date(), isFromSupport: true}
    this.messages.push(newMessage);
    this.messages = [...this.messages];

    // send to BE
    this.supportService.sendMessageAsSupport(this.userId, {content: this.newMessage.value}).subscribe({
      next: (res: any) => {
        this.newMessage.setValue('')
        console.log(res)
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
        // TODO: if not sent successfuly, add warning to conversation
      },
    })

    // notify converstaion
    this.newMessageSentEvent.emit({...newMessage, userId: this.userId})
  }

}
