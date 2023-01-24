import { Injectable } from '@angular/core';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs'
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(
    private httpRequestService: HttpRequestService
  ) {}

  getReport(startDate: string, endDate: string) : Observable<any> {
    const url = environment.API_BASE_URL + `/report?startDate=${startDate}&endDate=${endDate}`;
    return this.httpRequestService.get(url) as Observable<any>;
  }

}
