import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
    providedIn: 'root'
})

export class Toastr {
    private config = {timeOut: 4000}

    constructor(private toastr: ToastrService) {}

    success(message: string, title?: string) {
        this.toastr.success(message, title, this.config);
    }

    error(message: string, title?: string) {
        this.toastr.error(message, title, this.config);
    }

    info(message: string, title?: string) {
        this.toastr.info(message, title, this.config);
    }
}