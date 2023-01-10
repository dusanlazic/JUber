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
import { ProfileComponent } from './profile/profile.component';
import { ToastrModule } from 'ngx-toastr';
import { PasswordResetModalComponent } from './login-page/password-reset-modal/password-reset-modal.component';
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

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,

    LoginPageComponent,
    Oauth2RedirectHandlerComponent,
    
    ProfileComponent,
    PasswordResetModalComponent,
    HomeComponent,
    PassengerSidebarComponent,
    PassengerMapComponent,
    PlacesComponent,
    PalsComponent,
    PlaceComponent,
    EmptyPlaceComponent,
    EditPlaceComponent,
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
    ToastrModule.forRoot(),
    StoreModule.forRoot({state: RideReducer}),
  ],
  exports: [
    RegisterStep1Component,
    RegisterStep2Component,
    RegisterSuccessComponent,
    
    PasswordResetRequestComponent,
    PasswordResetRequestSuccessComponent,

    PasswordResetFormComponent,
    PasswordResetSuccessComponent,   
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
