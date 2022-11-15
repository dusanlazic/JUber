import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Roles } from 'src/models/user';
import { AuthGuardService as AuthGuard } from 'src/services/auth/auth-guard.service';
import { RoleGuardService as RoleGuard} from 'src/services/auth/role-guard.service';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { ProfileComponent } from './profile/profile.component';
import { MapComponent } from './shared/map/map.component';
import { HomeComponent } from './shared/homepage/home/home.component';

const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'map', component: MapComponent },
  { path: 'home', component: HomeComponent },
  { path: 'oauth2/redirect', component: Oauth2RedirectHandlerComponent},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard, RoleGuard], 
          data: { expectedRoles: [Roles.DRIVER, Roles.PASSENGER_NEW, Roles.PASSENGER ]}  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
