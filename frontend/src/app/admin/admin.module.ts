import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminNavigationComponent } from './admin-navigation/admin-navigation.component';
import { AdminChatComponent } from './admin-support-page/admin-chat/admin-chat.component';
import { AdminConversationComponent } from './admin-support-page/admin-conversation/admin-conversation.component';
import { AdminSupportPageComponent } from './admin-support-page/admin-support-page.component';
import { BlockedUsersComponent } from './blocked-users/blocked-users.component';
import { DriverRegistrationComponent } from './driver-registration/driver-registration.component';
import { ChangeRequestsComponent } from './change-requests/change-requests.component';
import { DriverInfoComponent } from './driver-info/driver-info.component';
import { DriversListComponent } from './drivers-list/drivers-list.component';
import { PassengerInfoComponent } from './passenger-info/passenger-info.component';
import { PassengerListComponent } from './passenger-list/passenger-list.component';
import { AppRoutingModule } from '../app-routing.module';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { SharesModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    AdminChatComponent,
    AdminConversationComponent,
    AdminNavigationComponent,
    AdminSupportPageComponent,
    BlockedUsersComponent,
    ChangeRequestsComponent,
    DriverInfoComponent,
    DriverRegistrationComponent,
    DriversListComponent,
    PassengerInfoComponent,
    PassengerListComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharesModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatTableModule,
    MatSortModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    MatInputModule,
  ], 
  exports: [
    AdminChatComponent,
    AdminConversationComponent,
    AdminNavigationComponent,
    AdminSupportPageComponent,
    BlockedUsersComponent,
    ChangeRequestsComponent,
    DriverInfoComponent,
    DriverRegistrationComponent,
    DriversListComponent,
    PassengerInfoComponent,
    PassengerListComponent
  ]
})
export class AdminModule { }
