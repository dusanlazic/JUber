import { HttpClient, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LocalStorageService } from './local-storage.service';

@Injectable({
    providedIn: 'root'
})

export class HttpRequestService {

    constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService) {}

    createHeaders(): HttpHeaders {
        const headers = new HttpHeaders({
            'Content-type': 'application/json'
        });
        const storedToken = this.localStorageService.getToken();

        if(storedToken){
            return headers.append('Authorization', `Bearer ${storedToken}`);
        }
        return headers;
    
    }

    post(url: string, body: any) : Observable<any> {
        const headers = this.createHeaders();
        return this.httpClient.post(url, body, {headers}) 
    }

    get(url: string) : Observable<any> {
        const headers = this.createHeaders();
        return this.httpClient.get(url, {headers}) 
    }
}


/* 
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const idToken = localStorage.getItem("id_token");

        if (idToken) {
            const cloned = req.clone({headers: req.headers.set("Authorization","Bearer " + idToken)});

            return next.handle(cloned);
        }
        else {
            return next.handle(req);
        }
    }
} */