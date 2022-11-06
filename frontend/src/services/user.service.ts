import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { LocalStorageService } from "./util/localStorage.service";

@Injectable({
    providedIn: 'root'
})


export class UserService {

    constructor(
        private httpClient: HttpClient, 
        private localStorageService: LocalStorageService
    ) {}

   
    createHeaders(): HttpHeaders {
        const headers = new HttpHeaders({
            'Content-type': 'application/json'
        });
        const storedToken = this.localStorageService.get(environment.ACCESS_TOKEN);

        if(storedToken){
            return headers.append('Authorization', `Bearer ${storedToken}`);
        }
        return headers;
    
    }
    
    getCurrentUser() : Observable<any>{
        const url = environment.API_BASE_URL + "/auth/me";
        const headers = this.createHeaders();

        return this.httpClient.get(url, {headers}) as Observable<any>;
    }

}
