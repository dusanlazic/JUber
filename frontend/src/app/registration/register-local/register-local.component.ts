import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LocalRegistrationInputs, LocalRegistrationRequest, RegistrationStep1, RegistrationStep2 } from 'src/models/auth';
import { ResponseError } from 'src/models/error';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/services/util/toastr.service';

enum RegistrationStep {
  FIRST_STEP = 1,
  SECOND_STEP = 2,
  SUCCESS = 3
}

@Component({
  selector: 'app-register-local',
  templateUrl: './register-local.component.html',
  styleUrls: ['./register-local.component.sass']
})
export class RegisterLocalComponent  {

  activeStep = RegistrationStep.FIRST_STEP;
  step1Inputs!: RegistrationStep1;
  step2Inputs!: RegistrationStep2;

  GOOGLE_AUTH_URL= environment.GOOGLE_AUTH_URL_REGISTER;
  FACEBOOK_AUTH_URL= environment.FACEBOOK_AUTH_URL_REGISTER;

  constructor(
    private authService: AuthService,
    private toastr: Toastr
  ) { }

  changeStep(nextStep: RegistrationStep, inputs: Partial<LocalRegistrationInputs>) : void {
    switch(nextStep){
      case RegistrationStep.FIRST_STEP:
        this.step2Inputs = inputs.step2Inputs!;
        this.activeStep = nextStep;
        break;
      case RegistrationStep.SECOND_STEP:
        this.step1Inputs = inputs.step1Inputs!;
        this.activeStep = nextStep;
        break;
      case RegistrationStep.SUCCESS:
        this.step2Inputs = inputs.step2Inputs!;
        this.sendRegistrationRequest()
        break;
      default:
        console.log("Invalid step")
    }
  }

  sendRegistrationRequest() : void {
     const request: LocalRegistrationRequest = {...this.step1Inputs, ...this.step2Inputs};

     this.authService.signup(request).subscribe({
      next: () => {
        this.activeStep = RegistrationStep.SUCCESS;
      },
      error: (resp: HttpErrorResponse) => {
        this.handleRegistrationError(resp.error)  
      }
     })
  }

  handleRegistrationError(error: ResponseError){
    if(error.status === HttpStatusCode.Conflict){
      this.toastr.error(error.message);
    }
    else{
      this.toastr.error("Registration failed, please try again!");
    }      
  }
}
