import { NgModule } from '@angular/core';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { Roles } from 'src/models/user';
import { AuthGuardService as AuthGuard } from 'src/services/auth/auth-guard.service';
import { RoleGuardService as RoleGuard} from 'src/services/auth/role-guard.service';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { PasswordRecoveryPageComponent } from './password-recovery-page/password-recovery-page.component';
import { PasswordResetFormComponent } from './password-recovery-page/password-reset-form/password-reset-form.component';
import { PasswordResetRequestSuccessComponent } from './password-recovery-page/password-reset-request-success/password-reset-request-success.component';
import { PasswordResetRequestComponent } from './password-recovery-page/password-reset-request/password-reset-request.component';
import { PasswordResetSuccessComponent } from './password-recovery-page/password-reset-success/password-reset-success.component';
import { EmailVerificationComponent } from './registration/email-verification/email-verification.component';
import { RegisterLocalComponent } from './registration/register-local/register-local.component';
import { Oauth2RegisterRedirectHandlerComponent } from './registration/register-oauth/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { RegisterOauthComponent } from './registration/register-oauth/register-oauth.component';
import { MapComponent } from './shared/map/map.component';
import { HomeComponent } from './shared/homepage/home/home.component';
import { ProfilePageComponent } from './shared/profile-page/profile-page/profile-page.component';
import { ProfileDetailsComponent } from './shared/profile-page/profile-page/profile-details/profile-details.component';
import { ChangePasswordComponent } from './shared/profile-page/profile-page/change-password/change-password.component';
import { BalanceComponent } from './shared/profile-page/profile-page/balance/balance.component';
import { SavedRoutesComponent } from './shared/profile-page/profile-page/saved-routes/saved-routes.component';
import { PastRidesComponent } from './shared/profile-page/profile-page/past-rides/past-rides.component';
import { SupportComponent } from './shared/profile-page/profile-page/support/support.component';
import { AdminSupportComponent } from './admin-pages/admin-support/admin-support.component';


const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'map', component: MapComponent },
  { path: 'oauth2/redirect-login', component: Oauth2RedirectHandlerComponent},
  { path: 'oauth2/redirect-register', component: Oauth2RegisterRedirectHandlerComponent},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard, RoleGuard], 
          data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER_NEW, Roles.PASSENGER ]}  },
  { path: 'registration', component: RegisterLocalComponent},
  { path: 'registration/social', component: RegisterOauthComponent},
  { path: 'registration/verification', component: EmailVerificationComponent}, // registration/verification?token=...

  { path: 'password-recovery', component: PasswordRecoveryPageComponent, children: [
    { path: '', component: PasswordResetRequestComponent },
    { path: 'request-success', component: PasswordResetRequestSuccessComponent },
    { path: 'reset', component: PasswordResetFormComponent }, // password-recovery/reset?token=...
    { path: 'reset-success', component: PasswordResetSuccessComponent }]
  },

  { path: 'profile', component: ProfilePageComponent, children: [
    { path: '', component: ProfileDetailsComponent },
    { path: 'change-password', component: ChangePasswordComponent },
    { path: 'balance', component: BalanceComponent },
    { path: 'saved-routes', component: SavedRoutesComponent },
    { path: 'past-rides', component: PastRidesComponent },
    { path: 'support', component: SupportComponent, canActivate: [AuthGuard, RoleGuard],  
      data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER ]} },
    { path: 'admin-support', component: AdminSupportComponent, canActivate: [AuthGuard, RoleGuard], 
      data: { expectedRoles: [Roles.ADMIN ]} }]
  },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
