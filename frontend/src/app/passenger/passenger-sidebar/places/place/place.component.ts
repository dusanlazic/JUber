import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-place',
  templateUrl: './place.component.html',
  styleUrls: ['./place.component.sass']
})
export class PlaceComponent implements OnInit {

  @Input() place: any;
  @Input() index: number;

  constructor() {
    this.index = -1;
   }

  ngOnInit(): void {
  }

}
