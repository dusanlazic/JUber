import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterStep2Component } from './registration/register-local/register-step2/register-step2.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterOauthComponent } from './registration/register-oauth/register-oauth.component';
import { RegisterLocalComponent } from './registration/register-local/register-local.component';
import { RegisterStep1Component } from './registration/register-local/register-step1/register-step1.component';
import { RegisterSuccessComponent } from './registration/register-local/register-success/register-success.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { PasswordRecoveryPageComponent } from './password-recovery-page/password-recovery-page.component';
import { PasswordResetSuccessComponent } from './password-recovery-page/password-reset-success/password-reset-success.component';
import { PasswordResetRequestComponent } from './password-recovery-page/password-reset-request/password-reset-request.component';
import { PasswordResetFormComponent } from './password-recovery-page/password-reset-form/password-reset-form.component';
import { AppRoutingModule } from '../app-routing.module';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { SharesModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    LoginPageComponent,
    RegisterStep1Component,
    RegisterLocalComponent,
    RegisterOauthComponent,
    RegisterStep2Component,
    PasswordRecoveryPageComponent,
    PasswordResetSuccessComponent,
    PasswordResetRequestComponent,
    PasswordResetFormComponent,
    RegisterSuccessComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    SharesModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  exports: [
    LoginPageComponent,
    RegisterStep1Component,
    RegisterLocalComponent,
    RegisterOauthComponent,
    RegisterStep2Component,
    PasswordRecoveryPageComponent,
    PasswordResetSuccessComponent,
    PasswordResetRequestComponent,
    PasswordResetFormComponent,
    RegisterSuccessComponent
  ]
})
export class AuthModule { }
