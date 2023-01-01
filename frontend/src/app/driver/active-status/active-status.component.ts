import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DriverActivationResponse, DriverStatus } from 'src/models/driver';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';
import { DriverService } from 'src/services/driver/driver.service';
import { UserService } from 'src/services/user.service';

@Component({
  selector: 'active-status',
  templateUrl: './active-status.component.html',
  styleUrls: ['./active-status.component.sass']
})
export class ActiveStatusComponent implements OnInit {

  hoursToRest: number;
  minutesToRest: number;
  secondsToRest: number;

  isOvertimeDone: boolean = false;
  isActive: boolean = false;

  loggedUser!: LoggedUser;

  constructor(private authService: AuthService, private driverService: DriverService) { 
    this.hoursToRest = 0;
    this.minutesToRest = 0;
    this.secondsToRest = 0;
  }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.loggedUser = user;
        this.activateDriver();
      },
      error: (e: any) => {
        console.log(e);
      }
    });
  }

  toggleActive() : void {
    this.isActive ? this.inactivateDriver() : this.activateDriver()
  }

  private inactivateDriver() : void{
    this.driverService.inactivate(this.loggedUser.email).subscribe({
      next: (driverActivation: DriverActivationResponse) => {
        this.handleActivationSuccess(driverActivation);
      },
      error: (e: any) => {
        console.log(e);
      }
    });
  }

  private activateDriver() : void{
    this.driverService.activate(this.loggedUser.email).subscribe({
      next: (driverActivation: DriverActivationResponse) => {
        this.handleActivationSuccess(driverActivation);
      },
      error: (e: any) => {
        console.log(e);
      }
    });
  }

  private handleActivationSuccess(driverActivation: DriverActivationResponse) : void {
    if(driverActivation.status === DriverStatus.OVERTIME){
      this.setTimeout(driverActivation.overtimeEnd);
      this.isOvertimeDone = false;
      this.isActive = false;
    }
    else if(driverActivation.status === DriverStatus.ACTIVE || driverActivation.status === DriverStatus.DRIVING){
      this.isOvertimeDone = true;
      this.isActive = true;
    }
    else if(driverActivation.status === DriverStatus.INACTIVE){
      this.isOvertimeDone = true;
      this.isActive = false;
    }
  }

  private setTimeout(deadline: Date) : void {
    setInterval(() => {
      this.tickTock(deadline);
    }, 1000);
  }

  private tickTock(dDay: Date) : void {
    let targetDate = new Date(dDay);
    targetDate.setDate(targetDate.getDate() + 1)
    const timeDifference = targetDate.getTime() - new Date().getTime();
    this.secondsToRest = Math.floor((timeDifference / 1000) % 60);
    this.minutesToRest = Math.floor((timeDifference / (1000 * 60)) % 60);
    this.hoursToRest = Math.floor((timeDifference / (1000 * 60 * 60)) % 24);

  }
}
