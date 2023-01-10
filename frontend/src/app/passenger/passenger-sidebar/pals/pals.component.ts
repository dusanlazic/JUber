import { Component, OnInit } from '@angular/core';
import { AddPalEvent, IPal, IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { Store } from '@ngrx/store';
import { AddPalAction, DeletePalAction } from 'src/app/store/rideRequest/rideRequest.actions';
import { Toastr } from 'src/services/util/toastr.service';

@Component({
  selector: 'app-pals',
  templateUrl: './pals.component.html',
  styleUrls: ['./pals.component.sass']
})
export class PalsComponent implements OnInit {

  isAddPalOpen: boolean = false;
  passengers!: IPal[];
  colors: string[]

  constructor(
    private store: Store<{rideRequest: IRideRequest}>, 
    private toastr: Toastr
  ) { 
    this.colors = new Array<string>()
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.store.select('rideRequest').subscribe(state => {
			if(state === undefined) return;
      this.passengers = state.passengersInfo
		})
  }

  addPal(event: AddPalEvent) : void {
    if(event.confirmed){
      const newPal: IPal = event.newPal as IPal;

      if(this.isAddValid(newPal)){
        this.colors.push(this.getRandomColor())
        this.store.dispatch(AddPalAction({addedPal: newPal}))
      }
      else{
        this.toastr.error("This pal is already added")
        return
      }
      
    }
    this.toggleModal();
  }

  removePal(index: number) : void {
    const removePal = this.passengers.at(index) as IPal;
    this.store.dispatch(DeletePalAction({removePal: removePal}))
    this.colors.splice(index, 1)
  }

  toggleModal() : void{
    this.isAddPalOpen = !this.isAddPalOpen
  }


  private getRandomColor() {
    var color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  }
  
  private isAddValid(newPal: IPal) : boolean {
    const result = this.passengers.filter(pal => pal.email === newPal.email);
    return result.length == 0
  }
}
