import { Component, OnInit } from '@angular/core';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-password-reset-success',
  templateUrl: './password-reset-success.component.html',
  styleUrls: ['./password-reset-success.component.sass']
})
export class PasswordResetSuccessComponent implements OnInit {

  constructor(private toastr: Toastr) { }

  ngOnInit(): void {
    this.toastr.success("Successfuly sent request for password reset.", "🎉")
  }

}
