import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';

enum ActiveStep {
  FIRST_STEP = 1,
  SECOND_STEP = 2,
  SUCCESS = 3
}

@Component({
  selector: 'app-register-local',
  templateUrl: './register-local.component.html',
  styleUrls: ['./register-local.component.sass']
})
export class RegisterLocalComponent implements OnInit {

  activeStep = ActiveStep.FIRST_STEP;
  @Output() oauthRegistrationEvent = new EventEmitter<void>();

  
  constructor(
    private authService: AuthService,
    private toastr: Toastr
  ) { }

  ngOnInit(): void {
  }

  
  changeStep(step: ActiveStep, event: any) : void {
    console.log(event);
    this.activeStep = step
  }

  oauthRegistration(){
    this.oauthRegistrationEvent.emit();
  }


  
  sendRegistrationRequest() : void {
     this.authService.signup(Object).subscribe({
      next: (resp) => {
        this.activeStep = ActiveStep.SUCCESS;
      },
      error: (err) => {
        this.toastr.error("Registration failed!");
      }
     })
  }
}
