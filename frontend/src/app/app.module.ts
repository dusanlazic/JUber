import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './shared/map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { ToastrModule } from 'ngx-toastr';
import { RegisterOauthComponent } from './registration/register-oauth/register-oauth.component';
import { RegisterLocalComponent } from './registration/register-local/register-local.component';
import { RegisterStep1Component } from './registration/register-local/register-step1/register-step1.component';
import { RegisterStep2Component } from './registration/register-local/register-step2/register-step2.component';
import { RegisterSuccessComponent } from './registration/register-local/register-success/register-success.component';
import { PasswordResetRequestComponent } from './password-recovery-page/password-reset-request/password-reset-request.component';
import { PasswordResetFormComponent } from './password-recovery-page/password-reset-form/password-reset-form.component';
import { PasswordRecoveryPageComponent } from './password-recovery-page/password-recovery-page.component';
import { PasswordResetRequestSuccessComponent } from './password-recovery-page/password-reset-request-success/password-reset-request-success.component';
import { PasswordResetSuccessComponent } from './password-recovery-page/password-reset-success/password-reset-success.component';
import { EmailVerificationComponent } from './registration/email-verification/email-verification.component';

import { HomeComponent } from './shared/homepage/home/home.component';
import { PassengerSidebarComponent } from './passenger/passenger-sidebar/passenger-sidebar.component';
import { PassengerMapComponent } from './passenger/passenger-map/passenger-map.component';
import { PlacesComponent } from './passenger/passenger-sidebar/places/places.component';
import { PalsComponent } from './passenger/passenger-sidebar/pals/pals.component';
import { PlaceComponent } from './passenger/passenger-sidebar/places/place/place.component';
import { EmptyPlaceComponent } from './passenger/passenger-sidebar/places/empty-place/empty-place.component';
import { EditPlaceComponent } from './passenger/passenger-sidebar/places/edit-place/edit-place.component';
import { StoreModule } from '@ngrx/store';
import { RideReducer } from './store/ride.reducer';
import { AddPalsDialogComponent } from './passenger/passenger-sidebar/pals/add-pals-dialog/add-pals-dialog.component';
import { AdditionalComponent } from './passenger/passenger-sidebar/additional/additional.component';
import { ScheduleComponent } from './passenger/passenger-sidebar/schedule/schedule.component';
import { RideRequestReducer } from './store/rideRequest/rideRequest.reducer';
import { ActiveStatusComponent } from './driver/active-status/active-status.component';
import { NotificationComponent } from './shared/homepage/notification/notification.component';
import { RideInviteComponent } from './shared/homepage/notification/notification-list/ride-invite/ride-invite.component';
import { NotificationWebSocketAPI } from 'src/services/notification/notification-socket.service';
import { NotificationItemComponent } from './shared/homepage/notification/notification-list/notification-item/notification-item.component';
import { ProfileDetailsComponent } from './shared/profile-page/profile-navigation/profile-details/profile-details.component';
import { ChangePasswordComponent } from './shared/profile-page/profile-navigation/change-password/change-password.component';
import { BalanceComponent } from './shared/profile-page/profile-navigation/balance/balance.component';
import { SavedRoutesComponent } from './shared/profile-page/profile-navigation/saved-routes/saved-routes.component';
import { PastRidesComponent } from './shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { SupportComponent } from './shared/profile-page/profile-navigation/support/support.component';
import { SupportMessageComponent } from './shared/support-message/support-message.component';
import { NotificationWebsocketshareService } from 'src/services/notification/notification-websocketshare.service';
import { AdminSupportWebSocketAPI } from 'src/services/support/admin/admin-chat/admin-support-socket.service';
import { SupportChatWebSocketAPI } from 'src/services/support/user/support-chat-socket.service';
import { SupportChatWebsocketshareService } from 'src/services/support/user/support-chat-websocketshare.service';
import { AdminSupportWebsocketshareService } from 'src/services/support/admin/admin-chat/admin-support-websocketshare.service';
import { AdminConversationWebsocketshareService } from 'src/services/support/admin/admin-conversations/admin-conversation-websocketshare.service';
import { AdminConversationWebSocketAPI } from 'src/services/support/admin/admin-conversations/admin-conversation-socket.service';
import { AdminSupportPageComponent } from './admin-pages/admin-support-page/admin-support-page.component';
import { AdminChatComponent } from './admin-pages/admin-support-page/admin-chat/admin-chat.component';
import { AdminConversationComponent } from './admin-pages/admin-support-page/admin-conversation/admin-conversation.component';
import { NavigationPageTemplateComponent } from './shared/navigation-page-template/navigation-page-template/navigation-page-template.component';
import { AdminNavigationComponent } from './admin-pages/admin-navigation/admin-navigation.component';
import { ProfileNavigationComponent } from './shared/profile-page/profile-navigation/profile-navigation.component';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { PassengerRideInvitationComponent } from './passenger/passenger-sidebar/passenger-ride-invitation/passenger-ride-invitation.component';
import { PersonItemComponent } from './passenger/passenger-sidebar/passenger-ride-invitation/person-item/person-item.component';
import { RideSocketShareService } from 'src/services/ride/ridesocketshare.service';
import { RideWebSocketAPI } from 'src/services/ride/ride-message.service';
import { RideDetailsComponent } from './shared/ride-details/ride-details.component';
import { RideDetailsSidebarComponent } from './shared/ride-details/ride-details-sidebar/ride-details-sidebar.component';
import { RideDetailsMapComponent } from './shared/ride-details/ride-details-map/ride-details-map.component';
import { RideDetailsPlaceComponent } from './shared/ride-details/ride-details-place/ride-details-place.component';
import { PaymentWebsocketshareService } from 'src/services/payment/payment-websocketshare.service';
import { PaymentWebSocketAPI } from 'src/services/payment/payment-socket.service';
import { UnathorizedSidebarComponent } from './unauthorized/unauthorized-sidebar/unauthorized-sidebar.component';
import { UnathorizedMapComponent } from './unauthorized/unauthorized-map/unauthorized-map.component';
import { BlockedUsersComponent } from './admin-pages/blocked-users/blocked-users.component';
import { ChangeRequestsComponent } from './admin-pages/change-requests/change-requests.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,

    LoginPageComponent,
    Oauth2RedirectHandlerComponent,
    RegisterOauthComponent,
    RegisterLocalComponent,
    RegisterStep1Component,
    RegisterStep2Component,
    RegisterSuccessComponent,
    EmailVerificationComponent,

    PasswordRecoveryPageComponent,
    PasswordResetRequestComponent,
    PasswordResetRequestSuccessComponent,
    PasswordResetFormComponent,
    PasswordResetSuccessComponent,

    HomeComponent,
    UnathorizedSidebarComponent,
    UnathorizedMapComponent,
    PassengerSidebarComponent,
    PassengerMapComponent,
    PlacesComponent,
    PalsComponent,
    PlaceComponent,
    EmptyPlaceComponent,
    EditPlaceComponent,

    AddPalsDialogComponent,
    AdditionalComponent,
    ScheduleComponent,

    ActiveStatusComponent,

    NotificationComponent,
    RideInviteComponent,
    NotificationItemComponent,
     PassengerRideInvitationComponent,
     PersonItemComponent,
     RideDetailsComponent,
     RideDetailsSidebarComponent,
     RideDetailsMapComponent,
     RideDetailsPlaceComponent,

    ProfileDetailsComponent,
    ChangePasswordComponent,
    BalanceComponent,
    SavedRoutesComponent,
    PastRidesComponent,
    ChangeRequestsComponent,
    BlockedUsersComponent,
    SupportComponent,
    SupportMessageComponent,

    AdminConversationComponent,
    AdminSupportPageComponent,
    AdminChatComponent,
    NavigationPageTemplateComponent,
    AdminNavigationComponent,
    ProfileNavigationComponent
  ],
  imports: [
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
    RegisterStep1Component,
    RegisterStep2Component,
    RegisterSuccessComponent,
    
    PasswordResetRequestComponent,
    PasswordResetRequestSuccessComponent,

    PasswordResetFormComponent,
    PasswordResetSuccessComponent,   

    ProfileDetailsComponent,
    ChangePasswordComponent,
    BalanceComponent,
    SavedRoutesComponent,
    PastRidesComponent,
    ChangeRequestsComponent,
    BlockedUsersComponent,
    SupportComponent,
    SupportMessageComponent
  ],
  providers: [RideSocketShareService, RideWebSocketAPI,
              NotificationWebsocketshareService, NotificationWebSocketAPI, 
              SupportChatWebsocketshareService, SupportChatWebSocketAPI,
              PaymentWebsocketshareService, PaymentWebSocketAPI,
              AdminSupportWebsocketshareService, AdminSupportWebSocketAPI,
              AdminConversationWebsocketshareService, AdminConversationWebSocketAPI],
  bootstrap: [AppComponent]
})
export class AppModule { }
