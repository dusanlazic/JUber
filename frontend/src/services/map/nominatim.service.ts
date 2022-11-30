import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { Point } from 'src/models/map';

@Injectable({
  providedIn: 'root'
})
export class NominatimService {

  BASE_NOMINATIM_URL = "nominatim.openstreetmap.org"

  constructor(private http: HttpClient) {
  }

  addressLookup(req?: any): Observable<any> {
    let url = `https://${this.BASE_NOMINATIM_URL}/search?format=json&q=${req}, Novi Sad`;
    return this.http.get(url)
  }
}
