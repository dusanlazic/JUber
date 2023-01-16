import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ChatConversationResponse } from 'src/models/chat';
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
  logged!: LoggedUser;

  constructor( 
    private authService: AuthService,
    private supportService: SupportService,
    private adminConversationService: AdminConversationWebsocketshareService
  ) { }

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
          const newConversation = JSON.parse(res) as ChatConversationResponse;
          this.conversations.push(newConversation);
          this.conversations = [...this.conversations]
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }


}
