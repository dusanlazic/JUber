import { HttpErrorResponse, HttpResponse, HttpResponseBase } from '@angular/common/http';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/services/user.service';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-password-reset-modal',
  templateUrl: './password-reset-modal.component.html',
  styleUrls: ['./password-reset-modal.component.sass']
})
export class PasswordResetModalComponent implements OnInit {

  isModalActive: boolean = false;
  emailForm!: FormGroup;

  // @Output() emailInputEmiter = new EventEmitter<string>();

  constructor(
    private builder: FormBuilder,
    private userService: UserService,
    private toastr: Toastr
  ) { }

  ngOnInit(): void {
    this.emailForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email])
    });
  }

  closeModal() {
    this.isModalActive = !this.isModalActive;
  }

  public openModal() {
    this.isModalActive = true;
  }

  submitRequest(){
    console.log(this.emailForm.value)
    // this.emailInputEmiter.emit(this.emailForm.value);
    this.userService.requestPasswordReset(this.emailForm.value).subscribe({
      next: (response: HttpResponseBase) => {
        console.log(response);
        this.toastr.success('Request for password reset successfully sent', 'Check your mail');
      },
      error: (e: HttpErrorResponse) => {
        // User not found 
        if (e.status === 404){
          this.toastr.error('Make sure you have active account.');
        }
        console.log(e.error.message)         
      }
    });
    this.closeModal()
  }

}
