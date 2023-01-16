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
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.sass']
})
export class ProfilePageComponent implements OnInit {

  logged: LoggedUser;
  selected: SelectionOptions;

  SelectionOptions = SelectionOptions;

  constructor(
    private authservice: AuthService,
    private router: Router,
  ) { 
    this.logged={email:'', imageUrl:'' ,name: '', role: '', id: ''};
    this.selected = SelectionOptions.PROFILE;
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
  logout() : void {
    this.authservice.logout();
    this.router.navigate(['/login']);
  }

  
}