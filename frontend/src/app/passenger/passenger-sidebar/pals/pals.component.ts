import { Component, Input, OnInit } from '@angular/core';
import { Ride } from 'src/models/ride';
import { Pal, PalInput } from 'src/models/user';

colors: {
  purple: "#791eae";
  red: "#c33232";
  blue: "#1298db";
}

interface AddPalEvent{
  confirmed: boolean,
  newPal: PalInput
}

@Component({
  selector: 'app-pals',
  templateUrl: './pals.component.html',
  styleUrls: ['./pals.component.sass']
})
export class PalsComponent implements OnInit {

  @Input() ride: Ride;

  addedPals: Pal[];
  isAddPalOpen: boolean = false;

  constructor() { 
    this.ride = new Ride();
    this.addedPals = new Array<Pal>()
  }

  ngOnInit(): void {
  }


  addPal(event: AddPalEvent) : void {
    if(event.confirmed){
      //this.checkIsUser(newPal);
      this.addedPals.push(event.newPal); 
    }
    this.closeAddPal();
  }

  removePal(index: number) : void {
    this.addedPals.splice(index, 1)
  }



  
  openAddPals() : void{
    this.isAddPalOpen = true
  }
  closeAddPal() : void {
    this.isAddPalOpen = false
  }

}
