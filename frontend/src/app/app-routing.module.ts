import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { Oauth2RedirectHandlerComponent } from './login-page/oauth2-redirect-handler/oauth2-redirect-handler.component';
import { ProfileComponent } from './profile/profile.component';
import { MapComponent } from './shared/map/map.component';

const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'map', component: MapComponent },
  { path: 'oauth2/redirect', component: Oauth2RedirectHandlerComponent},
  { path: 'profile', component: ProfileComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
