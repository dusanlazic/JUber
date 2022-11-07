// src/app/auth/auth-guard.service.ts
import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { Toastr } from '../util/toastr.service';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(
        public auth: AuthService, 
        public router: Router,
        private toastr: Toastr
    ) {}

    canActivate(): boolean {
        if (!this.auth.isAuthenticated()) {
            this.toastr.error("You do not have activated account")
            this.router.navigate(['']);
            return false;
        }
        return true;
    }
}