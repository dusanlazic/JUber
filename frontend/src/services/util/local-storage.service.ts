import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})

export class LocalStorageService {
    private accessToken = environment.ACCESS_TOKEN;

    constructor() {}

    get(key: string): any{
        return localStorage.getItem(key);
    }

    set(key: string, value: any): void{
        localStorage.setItem(key, value);
    }

    remove(key: string): void{
        localStorage.removeItem(key);
    }

    getToken(): string | null {
        return localStorage.getItem(this.accessToken);
    }
    
    setToken(token: string): void{
        localStorage.setItem(this.accessToken, token);
    }

    removeToken(): void{
        localStorage.removeItem(this.accessToken)
    }

    clearAll(): void {
        localStorage.clear();
    }
    
}