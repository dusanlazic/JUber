import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BalanceComponent } from './profile-navigation/balance/balance.component';
import { ChangePasswordComponent } from './profile-navigation/change-password/change-password.component';
import { PastRidesComponent } from './profile-navigation/past-rides/past-rides.component';
import { ProfileDetailsComponent } from './profile-navigation/profile-details/profile-details.component';
import { SavedRoutesComponent } from './profile-navigation/saved-routes/saved-routes.component';
import { SupportComponent } from './profile-navigation/support/support.component';
import { ProfileNavigationComponent } from './profile-navigation/profile-navigation.component';
import { AppRoutingModule } from '../app-routing.module';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { SharesModule } from '../shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';

@NgModule({
  declarations: [
    BalanceComponent,
    ChangePasswordComponent,
    PastRidesComponent,
    ProfileDetailsComponent,
    SavedRoutesComponent,
    SupportComponent,
    ProfileNavigationComponent
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    SharesModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatSortModule,
  ],
  exports: [
    BalanceComponent,
    ChangePasswordComponent,
    PastRidesComponent,
    ProfileDetailsComponent,
    SavedRoutesComponent,
    SupportComponent,
    ProfileNavigationComponent
  ]
})
export class ProfileModule { }
