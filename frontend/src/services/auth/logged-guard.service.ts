import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { Toastr } from '../util/toastr.service';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class LoggedGuard implements CanActivate {

    constructor(
        public auth: AuthService, 
        public location: Location,
    ) {}

    canActivate(): boolean {
        if (this.auth.isAuthenticated()) {
            this.location.back();
            return false;
        }
        return true;
    }
}