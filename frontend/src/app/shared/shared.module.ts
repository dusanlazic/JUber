import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PastRidesTableComponent } from './tables/past-rides-table/past-rides-table.component';
import { ReviewsTableComponent } from './tables/reviews-table/reviews-table.component';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { SupportMessageComponent } from './support-message/support-message.component';
import { ReportsComponent } from './reports/reports.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { NotificationComponent } from './notification/notification.component';
import { NotificationItemComponent } from './notification/notification-list/notification-item/notification-item.component';
import { RideInviteComponent } from './notification/notification-list/ride-invite/ride-invite.component';

@NgModule({
  declarations: [
    PastRidesTableComponent,
    ReviewsTableComponent,
    SupportMessageComponent,
    ReportsComponent,
    NotificationComponent,
    NotificationItemComponent,
    RideInviteComponent
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  exports: [
    PastRidesTableComponent,
    ReviewsTableComponent,
    SupportMessageComponent,
    ReportsComponent,
    NotificationComponent,
    NotificationItemComponent,
    RideInviteComponent
  ]
})
export class SharesModule { }
