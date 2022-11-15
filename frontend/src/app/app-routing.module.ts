import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Roles } from 'src/models/user';
import { AuthGuardService as AuthGuard } from 'src/services/auth/auth-guard.service';
import { RoleGuardService as RoleGuard} from 'src/services/auth/role-guard.service';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { ProfileComponent } from './profile/profile.component';
import { RegisterLocalComponent } from './registration/register-local/register-local.component';
import { Oauth2RegisterRedirectHandlerComponent } from './registration/register-oauth/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { RegisterOauthComponent } from './registration/register-oauth/register-oauth.component';
import { MapComponent } from './shared/map/map.component';

const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'map', component: MapComponent },
  { path: 'oauth2/redirect-login', component: Oauth2RedirectHandlerComponent},
  { path: 'oauth2/redirect-register', component: Oauth2RegisterRedirectHandlerComponent},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard, RoleGuard], 
          data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER_NEW, Roles.PASSENGER ]}  },
  { path: 'registration', component: RegisterLocalComponent},
  { path: 'registration/social', component: RegisterOauthComponent},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
