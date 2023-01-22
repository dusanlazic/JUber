import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountManagementService {

    constructor(
      private httpRequestService: HttpRequestService
    ) {}

    getBlockedUsers(): Observable<any> {
      const url = environment.API_BASE_URL + "/accounts/blocked-users";
      return this.httpRequestService.get(url) as Observable<any>;
    }

    blockUser(email: string) : Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/blocked-users/${email}`;
      return this.httpRequestService.post(url, {}) as Observable<any>;
    }

    unblockUser(userId: string) : Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/blocked-users/${userId}`;
      return this.httpRequestService.delete(url) as Observable<any>;
    }

    updateNote(userId: string, note: string) : Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/blocked-users/${userId}/note`;
      return this.httpRequestService.patch(url, { note: note }) as Observable<any>;
    }
}
