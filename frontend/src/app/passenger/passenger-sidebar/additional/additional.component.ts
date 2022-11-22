import { Component, OnInit } from '@angular/core';
import { AdditionalRequests, VehicleType, VehicleTypeInput } from 'src/models/vehicle';

type LogLevelStrings = (keyof typeof VehicleTypeInput) ;

@Component({
  selector: 'app-additional',
  templateUrl: './additional.component.html',
  styleUrls: ['./additional.component.sass']
})
export class AdditionalComponent implements OnInit {

  vehicleTypes:any = VehicleTypeInput;
  
  private selectedVehicle: VehicleType = null;
  private babyFriendly: boolean = false;
  private petFriendly: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  toggleBabyFriendly(): void {
    this.babyFriendly = !this.babyFriendly;
  }

  togglePetFriendly() : void{
    this.petFriendly = !this.petFriendly;
  }

  onVehicleChange(value: VehicleTypeInput | unknown) : void{
    this.selectedVehicle = value as VehicleType;
  }

  getAdditionalRequests() : AdditionalRequests{
    const additionalRequests : AdditionalRequests = {
      babyFriendly: this.babyFriendly,
      petFriendly: this.petFriendly,
      vehicleType: this.selectedVehicle,
    }
    return additionalRequests;
  }  

  
}
