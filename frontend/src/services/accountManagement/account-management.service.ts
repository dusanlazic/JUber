import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs';
// import { BlockedUserResponse } from 'src/app/admin/blocked-users/blocked-users.component';

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

    blockUser(email: string) {//: Observable<BlockedUserResponse> {
      // const url = environment.API_BASE_URL + `/accounts/blocked-users/${email}`;
      // return this.httpRequestService.post(url, {}) as Observable<BlockedUserResponse>;
    }

    unblockUser(userId: string) : Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/blocked-users/${userId}`;
      return this.httpRequestService.delete(url) as Observable<any>;
    }

    updateNote(userId: string, note: string) : Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/blocked-users/${userId}/note`;
      return this.httpRequestService.patch(url, { note: note }) as Observable<any>;
    }

    getChangeRequests(): Observable<any> {
      const url = environment.API_BASE_URL + "/accounts/change-requests";
      return this.httpRequestService.get(url) as Observable<any>;
    }

    resolveChangeRequest(requestId: string, newStatus: string): Observable<any> {
      const url = environment.API_BASE_URL + `/accounts/change-requests/${requestId}/resolve`;
      return this.httpRequestService.patch(url, { newStatus: newStatus }) as Observable<any>;
    }
}
