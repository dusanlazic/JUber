import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationPageTemplateComponent } from './navigation-page-template/navigation-page-template.component';
import { HomeComponent } from './homepage/home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { RideModule } from '../ride/ride.module';
import { ProfileModule } from '../profile/profile.module';
import { AdminModule } from '../admin/admin.module';

import { AppRoutingModule } from '../app-routing.module';
import { SharesModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    NavigationPageTemplateComponent,
    HomeComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ProfileModule,
    SharesModule,
    AdminModule,
    ReactiveFormsModule,
    RouterModule,
    RideModule,
    AppRoutingModule,
  ],
  exports: [
    NavigationPageTemplateComponent,
    HomeComponent,
  ]
})
export class NavigationModule { }
