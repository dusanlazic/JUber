import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor() { }

  private _mapPreview = new BehaviorSubject<string>('');

  mapPreview$(): Observable<any> {
    return this._mapPreview.asObservable();
  }

  setPreviewLocation(location: string): void {
    this._mapPreview.next(location);
  }
  
  //
  private _confirmedLocation = new BehaviorSubject<string>('');

  confirmedLocation$(): Observable<any> {
    return this._confirmedLocation.asObservable();
  }

  setConfirmedLocation(location: string): void {
    this._confirmedLocation.next(location);
  }

  //
  private _redraw = new BehaviorSubject<boolean>(false);

  redraw$(): Observable<any> {
    return this._redraw.asObservable();
  }

  setRedraw(yes: boolean): void {
    this._redraw.next(yes);
  }




}
