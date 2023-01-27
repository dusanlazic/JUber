import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { IRideRequest, IVehicleType } from 'src/app/store/rideRequest/rideRequest';
import { UpdateBabyFriendly, UpdatePetFriendly, UpdateVehicleType } from 'src/app/store/rideRequest/rideRequest.actions';
import { VehicleTypeService } from 'src/services/vehicle/vehicle-type.service';


@Component({
  selector: 'app-additional',
  templateUrl: './additional.component.html',
  styleUrls: ['./additional.component.sass']
})
export class AdditionalComponent implements OnInit {

  vehicleTypes: Array<IVehicleType>

  constructor(
    private store: Store<{rideRequest: IRideRequest}>,
    private vehicleTypeService: VehicleTypeService
  ) { 
    this.vehicleTypes = new Array<IVehicleType>()
  }

  ngOnInit(): void {
    this.vehicleTypeService.findAll().subscribe({
      next: (allTypes: IVehicleType[]) => {
        this.vehicleTypes = allTypes;
      }
    })
  }

  toggleBabyFriendly(): void {
    this.store.dispatch(UpdateBabyFriendly())
  }

  togglePetFriendly() : void{
    this.store.dispatch(UpdatePetFriendly())
  }

  onVehicleChange(value: IVehicleType | unknown) : void{
    this.store.dispatch(UpdateVehicleType({vehicleType: value as IVehicleType}))  
  }
}
