import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';


import { StoreModule } from '@ngrx/store';
import { RideReducer } from './store/ride.reducer';

import { RideRequestReducer } from './store/rideRequest/rideRequest.reducer';
import { NotificationWebSocketAPI } from 'src/services/notification/notification-socket.service';
import { NotificationWebsocketshareService } from 'src/services/notification/notification-websocketshare.service';
import { AdminSupportWebSocketAPI } from 'src/services/support/admin/admin-chat/admin-support-socket.service';
import { SupportChatWebSocketAPI } from 'src/services/support/user/support-chat-socket.service';
import { SupportChatWebsocketshareService } from 'src/services/support/user/support-chat-websocketshare.service';
import { AdminSupportWebsocketshareService } from 'src/services/support/admin/admin-chat/admin-support-websocketshare.service';
import { AdminConversationWebsocketshareService } from 'src/services/support/admin/admin-conversations/admin-conversation-websocketshare.service';
import { AdminConversationWebSocketAPI } from 'src/services/support/admin/admin-conversations/admin-conversation-socket.service';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { RideSocketShareService } from 'src/services/ride/ridesocketshare.service';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';
import { PaymentWebsocketshareService } from 'src/services/payment/payment-websocketshare.service';
import { PaymentWebSocketAPI } from 'src/services/payment/payment-socket.service';

import { AuthModule } from './auth/auth.module';
import { AdminModule } from './admin/admin.module';
import { SharesModule } from './shared/shared.module';
import { ProfileModule } from './profile/profile.module';
import { RideModule } from './ride/ride.module';
import { NavigationModule } from './navigation/navigation.module';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AuthModule,
    AdminModule,
    SharesModule,
    ProfileModule,
    RideModule,
    NavigationModule,    
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTableModule,
    MatSortModule,
    ToastrModule.forRoot(),
    StoreModule.forRoot({state: RideReducer, rideRequest: RideRequestReducer})
  ],
  exports: [
  ],
  providers: [RideSocketShareService, RideWebSocketAPI,
              NotificationWebsocketshareService, NotificationWebSocketAPI, 
              SupportChatWebsocketshareService, SupportChatWebSocketAPI,
              PaymentWebsocketshareService, PaymentWebSocketAPI,
              AdminSupportWebsocketshareService, AdminSupportWebSocketAPI,
              AdminConversationWebsocketshareService, AdminConversationWebSocketAPI],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
