import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass']
})
export class ProfileComponent implements OnInit {

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (response) => {console.log("logged user " + response);},
      error: (e) => {console.error(e); console.log('Oops! Something went wrong. Please try again!')},
      complete: () => console.info('complete') 
    })
  }

}
