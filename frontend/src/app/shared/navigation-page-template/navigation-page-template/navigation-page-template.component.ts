import { Component, OnInit } from '@angular/core';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { LoggedUser } from 'src/models/user';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-navigation-page-template',
  templateUrl: './navigation-page-template.component.html',
  styleUrls: ['./navigation-page-template.component.sass']
})
export class NavigationPageTemplateComponent implements OnInit {

  isScrollable: boolean = false;
  logged!: LoggedUser;
  URL_BASE = environment.API_BASE_URL;

  constructor(
    private router: Router,
    public authService: AuthService
  ) { 
    this.getCurrentHref();
    this.subscribeToHrefChange();
  }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.logged = user;
      }
    })
  }

  subscribeToHrefChange(){
    this.router.events.subscribe((val) => {
      if(val instanceof NavigationEnd) {this.getCurrentHref()}
  });
  }

  private getCurrentHref() : void{
    const href: string = this.router.url;
    this.isScrollable = href.toLocaleLowerCase().includes('support')
  }
  
  
  logout() : void {
    this.authService.logout();
  }

}
