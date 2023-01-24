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
import { ProfileDetailsComponent } from './shared/profile-page/profile-navigation/profile-details/profile-details.component';
import { ChangePasswordComponent } from './shared/profile-page/profile-navigation/change-password/change-password.component';
import { BalanceComponent } from './shared/profile-page/profile-navigation/balance/balance.component';
import { SavedRoutesComponent } from './shared/profile-page/profile-navigation/saved-routes/saved-routes.component';
import { PastRidesComponent } from './shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { SupportComponent } from './shared/profile-page/profile-navigation/support/support.component';
import { AdminSupportPageComponent } from './admin-pages/admin-support-page/admin-support-page.component';
import { NavigationPageTemplateComponent } from './shared/navigation-page-template/navigation-page-template/navigation-page-template.component';
import { RideDetailsComponent } from './shared/ride-details/ride-details.component';
import { LoggedGuard } from 'src/services/auth/logged-guard.service';
import { BlockedUsersComponent } from './admin-pages/blocked-users/blocked-users.component';
import { ChangeRequestsComponent } from './admin-pages/change-requests/change-requests.component';
import { DriversListComponent } from './admin-pages/drivers-list/drivers-list.component';
import { DriverRegistrationComponent } from './admin-pages/driver-registration/driver-registration.component';
import { ReportsComponent } from './shared/profile-page/profile-navigation/reports/reports.component';

const routes: Routes = [
  { path: 'login', component: LoginPageComponent, canActivate: [LoggedGuard], data: {  }, },
  { path: 'map', component: MapComponent },
  { path: 'oauth2/redirect-login', component: Oauth2RedirectHandlerComponent},
  { path: 'oauth2/redirect-register', component: Oauth2RegisterRedirectHandlerComponent},
  { path: 'home', component: HomeComponent, canActivate: [],  data: { rideId: ''}  },
  { path: 'invitation', component: HomeComponent, canActivate: [AuthGuard, RoleGuard], 
          data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER_NEW, Roles.PASSENGER ]}  },
  { path: 'ride/:rideId', component: RideDetailsComponent, canActivate: [AuthGuard, RoleGuard], 
          data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER_NEW, Roles.PASSENGER ]}  },
  { path: 'ride', component: RideDetailsComponent, canActivate: [AuthGuard, RoleGuard], 
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

  { path: 'profile', component: NavigationPageTemplateComponent, canActivate: [AuthGuard, RoleGuard],  data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER ]},
    children: [
      { path: '', component: ProfileDetailsComponent },
      { path: 'change-password', component: ChangePasswordComponent },
      { path: 'balance', component: BalanceComponent },
      { path: 'saved-routes', component: SavedRoutesComponent },
      { path: 'past-rides', component: PastRidesComponent },
      { path: 'support', component: SupportComponent, canActivate: [AuthGuard, RoleGuard],  data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER ]} },
      { path: 'reports', component: ReportsComponent },
    ]
  },
  { path: 'admin', component: NavigationPageTemplateComponent, canActivate: [AuthGuard, RoleGuard], data: { expectedRoles: [Roles.ADMIN ]}, 
    children: [
     { path: 'blocked-users', component: BlockedUsersComponent },
     { path: 'change-requests', component: ChangeRequestsComponent },
     { path: 'drivers', component: DriversListComponent },
     { path: 'new-driver', component: DriverRegistrationComponent},
     { path: 'reports', component: ReportsComponent },
    ]
  },
  { path: 'admin/support', component: AdminSupportPageComponent, 
    canActivate: [AuthGuard, RoleGuard], data: { expectedRoles: [Roles.ADMIN ]}, 
  },
  

  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
