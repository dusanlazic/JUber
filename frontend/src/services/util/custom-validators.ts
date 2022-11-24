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

    static Schedule5HoursValidator(hours: string, minutes: string): ValidatorFn {
      return (control: AbstractControl): ValidationErrors | null => {
        const hoursCtrl = control.get(hours);
        const minutesCtrl = control.get(minutes);
        let isValid: boolean = false;
        if(hoursCtrl && minutesCtrl && hoursCtrl.value && minutesCtrl.value) {
          isValid = this.Schedule5HoursValidate(hoursCtrl.value, minutesCtrl.value)
        }
        console.log(isValid)
        return !isValid
          ? { invalidSchedule: true }
          : null;
      };
    }

    private static Schedule5HoursValidate(hours: number, minutes: number) : boolean {
      const currentDate = new Date(); 
    
      const maxDate = new Date(); 
      maxDate.setHours(maxDate.getHours() + 5)

      const scheduledTodayDate = new Date();
      scheduledTodayDate.setHours(hours)
      scheduledTodayDate.setMinutes(minutes)

      const scheduledTomorowDate = new Date();
      scheduledTomorowDate.setDate(scheduledTomorowDate.getDate() + 1)
      scheduledTomorowDate.setHours(hours)
      scheduledTomorowDate.setMinutes(minutes)

      const maxTime = maxDate.getTime();
      const currentTime = currentDate.getTime();
      const scheduledTodayTime = scheduledTodayDate.getTime();
      const scheduledTomorrowTime = scheduledTomorowDate.getTime();

      if(maxDate.getDate() > currentDate.getDate()){
        return scheduledTodayTime > currentTime || scheduledTomorrowTime < maxTime;
      }
      else{
        return scheduledTodayTime > currentTime && scheduledTodayTime < maxTime;
      }

    }
}