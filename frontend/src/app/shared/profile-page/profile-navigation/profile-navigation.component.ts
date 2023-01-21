import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';


enum SelectionOptions {
  PROFILE='/profile',
  CHANGE_PASSWORD='/profile/change-password',
  BALANCE='/profile/balance',
  SAVED_ROUTES='/profile/saved-routes',
  PAST_RIDES='/profile/past-rides',
  SUPPORT='/profile/support',
}

@Component({
  selector: 'app-profile-navigation',
  templateUrl: './profile-navigation.component.html',
  styleUrls: ['./profile-navigation.component.sass']
})
export class ProfileNavigationComponent implements OnInit {

  logged!: LoggedUser;
  selected!: SelectionOptions;

  SelectionOptions = SelectionOptions;

  constructor(
    private authservice: AuthService,
    private router: Router,
  ) { 
    this.getCurrentHref();
  }

  ngOnInit(): void {
    this.authservice.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.logged = user;
      }
    })
  }

  navigate(option: SelectionOptions){
    this.selected = option; 
    this.router.navigate([option])
    
  }

  private getCurrentHref() : void{
    const href = this.router.url;
    this.selected = href as SelectionOptions;
  }

}
