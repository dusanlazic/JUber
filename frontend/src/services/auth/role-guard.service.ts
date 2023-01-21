import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { Toastr } from '../util/toastr.service';
import { LocalStorageService } from '../util/local-storage.service';
import { Location } from '@angular/common';

@Injectable({
    providedIn: 'root'
})

export class RoleGuardService implements CanActivate {

    constructor(
        public authService: AuthService, 
        public router: Router, 
        public location: Location,
        private localStorage: LocalStorageService
    ) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        if(!this.isAuthenticated(route)){
            this.location.back();
            return false;
        }
        return true;
    }

    private isAuthenticated(route: ActivatedRouteSnapshot): boolean{
        const expectedRoles = route.data['expectedRoles'];
        const userRoles = this.localStorage.get('role');

        return this.authService.isAuthenticated() && expectedRoles.includes(userRoles) && userRoles;
    }
}