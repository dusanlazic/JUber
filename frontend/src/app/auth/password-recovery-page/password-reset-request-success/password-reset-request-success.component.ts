import { Component, OnInit } from '@angular/core';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-password-reset-request-success',
  templateUrl: './password-reset-request-success.component.html',
  styleUrls: ['./password-reset-request-success.component.sass']
})
export class PasswordResetRequestSuccessComponent implements OnInit {

  constructor(private toastr: Toastr) { }

  ngOnInit(): void {
    this.toastr.success("Successfuly sent request for password reset.", "Yeyy")
  }

}
