import { Injectable } from "@angular/core";
import { Observable, BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SupportChatWebsocketshareService {

  private blogDataSubject = new BehaviorSubject<any>('');

    constructor() { }
    ngOnDestroy(): void {
        this.blogDataSubject.unsubscribe();
    }

    onNewValueReceive(msg: any) {        
        this.blogDataSubject.next(msg);
    }

    getNewValue(): Observable<any> {
        return this.blogDataSubject.asObservable();
    }
}
