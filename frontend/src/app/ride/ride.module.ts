import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActiveStatusComponent } from './active-status/active-status.component';
import { PassengerMapComponent } from './passenger-map/passenger-map.component';
import { PassengerSidebarComponent } from './passenger-sidebar/passenger-sidebar.component';
import { AdditionalComponent } from './passenger-sidebar/additional/additional.component';
import { PalsComponent } from './passenger-sidebar/pals/pals.component';
import { PlacesComponent } from './passenger-sidebar/places/places.component';
import { EmptyPlaceComponent } from './passenger-sidebar/places/empty-place/empty-place.component';
import { ScheduleComponent } from './passenger-sidebar/schedule/schedule.component';
import { UnathorizedMapComponent } from './unauthorized/unauthorized-map/unauthorized-map.component';
import { UnathorizedSidebarComponent } from './unauthorized/unauthorized-sidebar/unauthorized-sidebar.component';
import { RideDetailsSidebarComponent } from './ride-details/ride-details-sidebar/ride-details-sidebar.component';
import { RideDetailsMapComponent } from './ride-details/ride-details-map/ride-details-map.component';
import { RideDetailsPlaceComponent } from './ride-details/ride-details-place/ride-details-place.component';
import { RideDetailsComponent } from './ride-details/ride-details.component';
import { RideReviewComponent } from './ride-details/ride-reviews/ride-review/ride-review.component';
import { EditPlaceComponent } from './passenger-sidebar/places/edit-place/edit-place.component';
import { AddPalsDialogComponent } from './passenger-sidebar/pals/add-pals-dialog/add-pals-dialog.component';
import { DetailsDriverHeaderComponent } from './ride-details/ride-details-sidebar/details-driver-header/details-driver-header.component';
import { DetailsPassengerHeaderComponent } from './ride-details/ride-details-sidebar/details-passenger-header/details-passenger-header.component';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PassengerRideInvitationComponent } from './passenger-sidebar/passenger-ride-invitation/passenger-ride-invitation.component';
import { PersonItemComponent } from './passenger-sidebar/passenger-ride-invitation/person-item/person-item.component';
import { PlaceComponent } from './passenger-sidebar/places/place/place.component';
import { RideReviewsComponent } from './ride-details/ride-reviews/ride-reviews.component';
import { AddReviewDialogComponent } from './ride-details/ride-reviews/add-review-dialog/add-review-dialog.component';
import { SharesModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    ActiveStatusComponent,
    PassengerMapComponent,
    PassengerSidebarComponent,
    AdditionalComponent,
    PalsComponent,
    PlacesComponent,
    EmptyPlaceComponent,
    ScheduleComponent,
    UnathorizedMapComponent,
    UnathorizedSidebarComponent,
    RideDetailsComponent,
    RideDetailsMapComponent,
    RideDetailsPlaceComponent,
    RideDetailsSidebarComponent,
    RideReviewComponent,
    EditPlaceComponent,
    AddPalsDialogComponent,
    DetailsDriverHeaderComponent,
    DetailsPassengerHeaderComponent,
    PassengerRideInvitationComponent,
    PersonItemComponent,
    PlaceComponent,
    RideReviewsComponent,
    AddReviewDialogComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    SharesModule
  ],
  exports: [
    ActiveStatusComponent,
    PassengerMapComponent,
    PassengerSidebarComponent,
    AdditionalComponent,
    PalsComponent,
    PlacesComponent,
    EmptyPlaceComponent,
    ScheduleComponent,
    UnathorizedMapComponent,
    UnathorizedSidebarComponent,
    RideDetailsComponent,
    RideDetailsMapComponent,
    RideDetailsPlaceComponent,
    RideDetailsSidebarComponent,
    RideReviewComponent,
    EditPlaceComponent,
    AddPalsDialogComponent,
    DetailsDriverHeaderComponent,
    DetailsPassengerHeaderComponent,
    PassengerRideInvitationComponent,

  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class RideModule { }
