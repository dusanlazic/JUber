import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, PatternValidator, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { PersonalInfo } from 'src/models/auth';
import { PhotoUploadResponse } from 'src/models/responses';
import { AccountInfo, LoggedUser } from 'src/models/user';
import { AccountService } from 'src/services/account/account.service';
import { AuthService } from 'src/services/auth/auth.service';
import { ValidationConstants } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-profile-details',
  templateUrl: './profile-details.component.html',
  styleUrls: ['./profile-details.component.sass']
})
export class ProfileDetailsComponent implements OnInit {

  @Output('updateImage')
  change: EventEmitter<string> = new EventEmitter<string>();

  updateProfileForm!: FormGroup;
  accountInfo: AccountInfo;
  URL_BASE: string = environment.API_BASE_URL

  constructor(
    private builder: FormBuilder,
    private toastr: Toastr,
    private accountService: AccountService
  )
  {
    this.accountInfo={firstName:'', lastName:'' ,city:'', phoneNumber:'', email:'', imageUrl:''};
    this.crateForm();
    this.getAccountDetails();
  }

  private getAccountDetails() : void{
    this.accountService.getProfileInfo().subscribe({
      next: (info: AccountInfo) => {
        this.accountInfo = info;
        this.patchValues();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  submit() : void {
    this.accountService.updateProfileInfo(this.updateProfileForm.value).subscribe({
      next: () => {
        this.toastr.success('Successfuly changed profile info.', 'Success')
      },
      error: (err: HttpErrorResponse) => {
        this.toastr.error('Please try again.', 'Oops something went wrong')
      }
    })
  }

  private crateForm() : void{
    this.updateProfileForm = this.builder.group({
      firstName: new FormControl(this.accountInfo.firstName, [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      lastName: new FormControl(this.accountInfo.lastName, [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      city: new FormControl(this.accountInfo.city, [Validators.required, Validators.pattern(ValidationConstants.Name)]),
      phoneNumber: new FormControl(this.accountInfo.phoneNumber, [Validators.required, Validators.pattern('^\\+[1-9][0-9]{3,14}$')]),
      email: new FormControl(this.accountInfo.email, [Validators.required ]),
    });
  }

  private patchValues() : void{
    this.updateProfileForm.patchValue({
      firstName: this.accountInfo.firstName,
      lastName: this.accountInfo.lastName,
      city: this.accountInfo.city,
      phoneNumber: this.accountInfo.phoneNumber,
      email: this.accountInfo.email
    });
    this.updateProfileForm.controls['email'].disable();
  }

  ngOnInit(): void {
  }

  uploadImage(input: HTMLInputElement): void{
    if(input.files){
      const file = input.files[0];
      this.accountService.setProfilePicture(file).subscribe({
        next: (res: PhotoUploadResponse) => {
          this.accountInfo.imageUrl = res.uri;
          this.change.emit(res.uri);
        },
        error: (res: any) => {
          console.log(res);
        },
      })
    }
    
  }
  removeImage(): void{
    this.accountService.removeProfilePicture().subscribe({
      next: (res: any) => {
        this.accountInfo.imageUrl = ''
        this.change.emit('');
      },
      error: (res: any) => {
        console.log(res);
      },
    })
  }

  get firstName() { return this.updateProfileForm.get('firstName'); }
  get lastName() { return this.updateProfileForm.get('lastName'); }
  get city() { return this.updateProfileForm.get('city'); }
  get phoneNumber() { return this.updateProfileForm.get('phoneNumber'); }
}
