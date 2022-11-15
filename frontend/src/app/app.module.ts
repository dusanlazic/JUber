import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login-page/login/login.component';
import { MapComponent } from './shared/map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';
import { LoginSocialComponent } from './login-page/login-social/login-social.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { ProfileComponent } from './profile/profile.component';
import { ToastrModule } from 'ngx-toastr';
import { PasswordResetModalComponent } from './login-page/password-reset-modal/password-reset-modal.component';
import { RegisterOauthComponent } from './registration/register-oauth/register-oauth.component';
import { RegisterLocalComponent } from './registration/register-local/register-local.component';
import { RegisterStep1Component } from './registration/register-local/register-step1/register-step1.component';
import { RegisterStep2Component } from './registration/register-local/register-step2/register-step2.component';
import { RegisterSuccessComponent } from './registration/register-local/register-success/register-success.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    LoginPageComponent,
    LoginSocialComponent,
    LoginComponent,
    Oauth2RedirectHandlerComponent,
    ProfileComponent,
    PasswordResetModalComponent,
    RegisterOauthComponent,
    RegisterLocalComponent,
    RegisterStep1Component,
    RegisterStep2Component,
    RegisterSuccessComponent,
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
  ],
  exports: [
    LoginSocialComponent,
    LoginComponent,
    RegisterStep1Component,
    RegisterStep2Component,
    RegisterSuccessComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
