import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export class ValidationConstants {
  public static Name = '[a-zA-ZŠĐŽČĆšđžčć ]*';
}


export class CustomValidators {
    constructor() {}

    static MatchValidator(source: string, target: string): ValidatorFn {
      return (control: AbstractControl): ValidationErrors | null => {
        const sourceCtrl = control.get(source);
        const targetCtrl = control.get(target);
  
        return sourceCtrl && targetCtrl && sourceCtrl.value !== targetCtrl.value
          ? { mismatch: true }
          : null;
      };
    }
}