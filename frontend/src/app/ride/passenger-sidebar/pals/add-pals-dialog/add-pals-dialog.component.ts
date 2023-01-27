import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AddPalEvent } from 'src/app/store/rideRequest/rideRequest';
import { ApiResponse } from 'src/models/responses';
import { LoggedUser, UserBasicInfo } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user.service';
import { ValidationConstants } from 'src/services/util/custom-validators';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-add-pals-dialog',
  templateUrl: './add-pals-dialog.component.html',
  styleUrls: ['./add-pals-dialog.component.sass']
})
export class AddPalsDialogComponent implements OnInit {

  @Output('addPalEvent')
  addPal: EventEmitter<AddPalEvent> = new EventEmitter<AddPalEvent>();

  palForm!: FormGroup
  loggedUser!: LoggedUser

  constructor(
    private builder: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private toastr: Toastr
  ){ 
    this.createForm();
  }

  private createForm() : void {
    this.palForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
    })
  }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (loggedUser: LoggedUser) => {
        this.loggedUser = loggedUser;
      }
    })
  }

  add() {
    if(this.loggedUser.email === this.email?.value){
      this.toastr.error("You can not add yourself as a pal.")
      return
    }

    this.userService.getPassengerBasicInfo(this.email?.value).subscribe({
      next: (passengerInfo: UserBasicInfo) => {
        this.addPal.emit({confirmed: true, newPal: passengerInfo})
      },
      error: (e: HttpErrorResponse) => {
        this.handleAddPalError(e.error)
      }
    })
  }

  cancel() {
    this.addPal.emit({confirmed: false, newPal: undefined})
  }


  private handleAddPalError(e: ApiResponse){
    if(e.status === HttpStatusCode.NotFound){
      this.toastr.error("This pal does not have an account.")
    } else {
      this.toastr.error("Oops, something went wrong please try again later!")
    }
  }

  get email() { return this.palForm.get('email'); }

}
