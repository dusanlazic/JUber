import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-person-item',
  templateUrl: './person-item.component.html',
  styleUrls: ['./person-item.component.sass']
})
export class PersonItemComponent implements OnInit {

  @Input() person: any;
  name: string = "Dusan Lazic";
  status: string = "Waiting...";
  imageUrl: string = "https://lh3.googleusercontent.com/a/ALm5wu3tpc3XnB9w8EMqNHAODc0uL23tqydVlUCLYqEF6Q=s96-c";

  constructor() { }

  ngOnInit(): void {
    this.name = this.person.name ? this.person.name : this.person.firstName + " " + this.person.lastName;
    this.status = this.person.status ?? "Waiting...";
    this.imageUrl = this.person.imageUrl;
  }

}
