import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-person-item',
  templateUrl: './person-item.component.html',
  styleUrls: ['./person-item.component.sass']
})
export class PersonItemComponent implements OnInit {

  @Input() person: any;
  name: string = "Dusan Lazic";
  email: string = "dusan.lazic@gmail.com";
  @Input() status: string = "Waiting...";
  imageUrl: string = "https://lh3.googleusercontent.com/a/ALm5wu3tpc3XnB9w8EMqNHAODc0uL23tqydVlUCLYqEF6Q=s96-c";
pal: any;


  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.name = this.person.name ? this.person.name : this.person.firstName + " " + this.person.lastName;
    this.imageUrl = this.person.imageUrl;
  }


}
