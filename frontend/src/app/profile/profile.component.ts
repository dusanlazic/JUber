import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass']
})
export class ProfileComponent implements OnInit {

  loggedUser: any;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.loggedUser = user;
        }
      });
  }

  logout(): void {
      this.authService.logout();
      this.router.navigate(['/']);
  }
}
