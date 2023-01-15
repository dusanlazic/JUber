import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { PersonalInfo } from 'src/models/auth';
import { HttpRequestService } from '../util/http-request.service';
import { Observable } from 'rxjs';
import { AccountInfo } from 'src/models/user';
import { FileUploadService } from '../file/file-upload.service';
import { PhotoUploadResponse } from 'src/models/responses';


@Injectable({
  providedIn: 'root'
})
export class AccountService {

    constructor(
      private httpRequestService: HttpRequestService,
      private fileUploadService: FileUploadService
    ) {}

    setProfilePicture(file: any): Observable<PhotoUploadResponse> {
      const url = environment.API_BASE_URL + `/account/profile-picture`;
      return this.fileUploadService.upload(url, file);
    }

    removeProfilePicture(): Observable<any> {
      const url = environment.API_BASE_URL + `/account/profile-picture`;
      return this.httpRequestService.delete(url) as Observable<any>;
    }

    getProfileInfo(): Observable<AccountInfo> {
      const url = environment.API_BASE_URL + `/account/me`;
      return this.httpRequestService.get(url) as Observable<AccountInfo>;
    }

    updateProfileInfo(profileInfo: PersonalInfo): Observable<any> {
      const url = environment.API_BASE_URL + `/account/me`;
      return this.httpRequestService.patch(url, profileInfo) as Observable<any>;
    }
}
