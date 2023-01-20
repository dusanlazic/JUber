import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs';
import { BalanceResponse, DepositAddressResponse } from 'src/models/balance';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

    constructor(
      private httpRequestService: HttpRequestService
    ) {}

    getBalance(): Observable<BalanceResponse> {
      const url = environment.API_BASE_URL + `/payment/balance`;
      return this.httpRequestService.get(url) as Observable<BalanceResponse>;
    }

    getDepositAddress(): Observable<DepositAddressResponse> {
      const url = environment.API_BASE_URL + `/payment/deposit`;
      return this.httpRequestService.post(url, {}) as Observable<DepositAddressResponse>;
    }
}
