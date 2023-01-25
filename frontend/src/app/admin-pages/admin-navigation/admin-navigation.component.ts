import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


enum SelectionOptions {
  PAST_RIDES='/admin/past-rides',
  ADMIN_SUPPORT='/admin/support',
  ADMIN_BLOCKED_USERS='/admin/blocked-users',
  ADMIN_CHANGE_REQUESTS='/admin/change-requests',
  ADMIN_DRIVERS_LIST='/admin/drivers',
  REPORTS='/admin/reports',
  ADMIN_PASSENGERS_LIST='/admin/passengers',
}

@Component({
  selector: 'app-admin-navigation',
  templateUrl: './admin-navigation.component.html',
  styleUrls: ['./admin-navigation.component.sass']
})
export class AdminNavigationComponent implements OnInit {

  selected!: SelectionOptions;
  SelectionOptions = SelectionOptions;

  constructor(private router: Router) { 
    this.getCurrentHref();
  }

  ngOnInit(): void {
  }

  navigate(option: SelectionOptions){
    this.selected = option; 
    this.router.navigate([option])
  }
  private getCurrentHref() : void{
    const href = this.router.url;
    if(href.includes('driver')){
      this.selected = SelectionOptions.ADMIN_DRIVERS_LIST
    }
    else if(href.includes('passenger')){
      this.selected = SelectionOptions.ADMIN_PASSENGERS_LIST
    }
    else{
      this.selected = href as SelectionOptions;
    }
    
  }
}
