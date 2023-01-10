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

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,

    LoginPageComponent,
    Oauth2RedirectHandlerComponent,
    
    ProfileComponent,
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
