import { Component, Input, OnInit } from '@angular/core';
import { Ride } from 'src/models/ride';
import { AddPalEvent, Pal,  } from 'src/models/user';

colors: {
  purple: "#791eae";
  red: "#c33232";
  blue: "#1298db";
}

interface ColoredPal extends Pal {
  color: string
}

@Component({
  selector: 'app-pals',
  templateUrl: './pals.component.html',
  styleUrls: ['./pals.component.sass']
})
export class PalsComponent implements OnInit {

  addedPals: ColoredPal[];
  isAddPalOpen: boolean = false;

  constructor() { 
    this.addedPals = new Array<ColoredPal>()
  }

  ngOnInit(): void {
  }


  addPal(event: AddPalEvent) : void {
    if(event.confirmed){

      const addedPal : ColoredPal = {
        ...event.newPal as Pal,
        color: this.getRandomColor()
      }
      
      this.addedPals.push(addedPal);
      // this.ride.passengers.push(event.newPal as Pal);
    }
    this.toggleModal();
  }

  removePal(index: number) : void {
    this.addedPals.splice(index, 1)
    // this.ride.passengers.splice(index, 1)
  }

  toggleModal() : void{
    this.isAddPalOpen = !this.isAddPalOpen
  }


  private getRandomColor() {
    var color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  }
  
}
