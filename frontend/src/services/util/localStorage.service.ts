import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})

export class LocalStorageService {
    constructor() {}

    get(item: string){
        return localStorage.getItem(item);
    }

    set(key: string, value: any){
        localStorage.setItem(key, value);
    }
}