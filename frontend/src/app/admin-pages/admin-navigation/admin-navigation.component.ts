import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


enum SelectionOptions {
  PAST_RIDES='/admin/past-rides',
  ADMIN_SUPPORT='/admin/support',
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
    this.selected = href as SelectionOptions;
  }
}