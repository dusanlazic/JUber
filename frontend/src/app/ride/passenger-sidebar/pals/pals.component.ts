import { Component, OnInit } from '@angular/core';
import { AddPalEvent, IPal, IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { Store } from '@ngrx/store';
import { AddPalAction, DeletePalAction } from 'src/app/store/rideRequest/rideRequest.actions';
import { Toastr } from 'src/services/util/toastr.service';
import { environment } from 'src/environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { LoggedUser } from 'src/models/user';

@Component({
  selector: 'app-pals',
  templateUrl: './pals.component.html',
  styleUrls: ['./pals.component.sass']
})
export class PalsComponent implements OnInit {

  isAddPalOpen: boolean = false;
  passengers: IPal[];
  URL_BASE: string = environment.API_BASE_URL;
  logged!: LoggedUser;

  constructor(
    private store: Store<{rideRequest: IRideRequest}>,
    private toastr: Toastr,
    public authService: AuthService
  ) {
    this.passengers = new Array<IPal>();
  }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.logged = user;
      }
    })
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
  }

  toggleModal() : void{
    this.isAddPalOpen = !this.isAddPalOpen
  }

  private isAddValid(newPal: IPal) : boolean {
    const result = this.passengers.filter(pal => pal.email === newPal.email);
    return result.length == 0
  }
}
