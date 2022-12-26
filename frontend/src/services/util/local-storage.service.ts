import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})

export class LocalStorageService {
    private tokenExpiration = environment.TOKEN_EXPIRATION;

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

    getTokenExpiration(): number | null {
        const timestampString = localStorage.getItem(this.tokenExpiration);
        if (timestampString)
            return Number(timestampString)
        return null;
    }
    
    setTokenExpiration(timestampString: number): void{
        localStorage.setItem(this.tokenExpiration, timestampString.toString());
    }

    removeTokenExpiration(): void{
        localStorage.removeItem(this.tokenExpiration)
    }

    clearAll(): void {
        localStorage.clear();
    }
    
}