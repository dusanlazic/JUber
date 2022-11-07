import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import decode from 'jwt-decode';
import { Toastr } from '../util/toastr.service';
import { LocalStorageService } from '../util/local-storage.service';

@Injectable({
    providedIn: 'root'
})

export class RoleGuardService implements CanActivate {

    constructor(
        public authService: AuthService, 
        public router: Router, 
        private toastr: Toastr,
        private localStorage: LocalStorageService
    ) { }

    canActivate(route: ActivatedRouteSnapshot): boolean {
        if(!this.isAuthenticated(route)){
            this.toastr.error("You do not have valid permisions")
            this.router.navigate(['']);
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