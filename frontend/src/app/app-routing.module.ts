import { NgModule } from '@angular/core';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { Roles } from 'src/models/user';
import { AuthGuardService as AuthGuard } from 'src/services/auth/auth-guard.service';
import { RoleGuardService as RoleGuard} from 'src/services/auth/role-guard.service';
import { LoginPageComponent } from './auth/login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './auth/login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { PasswordRecoveryPageComponent } from './auth/password-recovery-page/password-recovery-page.component';
import { PasswordResetFormComponent } from './auth/password-recovery-page/password-reset-form/password-reset-form.component';
import { PasswordResetRequestSuccessComponent } from './auth/password-recovery-page/password-reset-request-success/password-reset-request-success.component';
import { PasswordResetRequestComponent } from './auth/password-recovery-page/password-reset-request/password-reset-request.component';
import { PasswordResetSuccessComponent } from './auth/password-recovery-page/password-reset-success/password-reset-success.component';
import { EmailVerificationComponent } from './auth/registration/email-verification/email-verification.component';
import { RegisterLocalComponent } from './auth/registration/register-local/register-local.component';
import { Oauth2RegisterRedirectHandlerComponent } from './auth/registration/register-oauth/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { RegisterOauthComponent } from './auth/registration/register-oauth/register-oauth.component';
import { HomeComponent } from './navigation/homepage/home/home.component';

import { BalanceComponent } from './profile/profile-navigation/balance/balance.component';

import { NavigationPageTemplateComponent } from './navigation/navigation-page-template/navigation-page-template.component';
import { RideDetailsComponent } from './ride/ride-details/ride-details.component';

import { BlockedUsersComponent } from './admin/blocked-users/blocked-users.component';
import { AdminSupportPageComponent } from './admin/admin-support-page/admin-support-page.component';
import { ChangeRequestsComponent } from './admin/change-requests/change-requests.component';
import { DriverInfoComponent } from './admin/driver-info/driver-info.component';
import { DriverRegistrationComponent } from './admin/driver-registration/driver-registration.component';
import { DriversListComponent } from './admin/drivers-list/drivers-list.component';
import { PassengerInfoComponent } from './admin/passenger-info/passenger-info.component';
import { PassengerListComponent } from './admin/passenger-list/passenger-list.component';
import { ProfileDetailsComponent } from './profile/profile-navigation/profile-details/profile-details.component';
import { ChangePasswordComponent } from './profile/profile-navigation/change-password/change-password.component';
import { PastRidesComponent } from './profile/profile-navigation/past-rides/past-rides.component';
import { SavedRoutesComponent } from './profile/profile-navigation/saved-routes/saved-routes.component';
import { SupportComponent } from './profile/profile-navigation/support/support.component';
import { ReportsComponent } from './shared/reports/reports.component';

const routes: Routes = [
  { path: 'login', component: LoginPageComponent, data: {  }, },
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
     { path: 'drivers/:driverId', component: DriverInfoComponent },
     { path: 'new-driver', component: DriverRegistrationComponent},
     { path: 'passengers', component: PassengerListComponent },
     { path: 'passengers/:passengerId', component: PassengerInfoComponent }
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
