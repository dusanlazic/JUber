import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient: HttpClient) { }
  
  // Returns an observable
  upload(url: string, file: File): Observable<any> {
  
      // Create form data
      const formData = new FormData(); 
        
      // Store form name as "file" with file data
      formData.append("file", file, file.name);

      return this.httpClient.post(url, formData, { withCredentials: true}) 
  }
}
