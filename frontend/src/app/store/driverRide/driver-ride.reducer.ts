import * as _ from 'lodash';
import { Ride } from 'src/models/ride';
import * as DriverRideActions from "./driver-ride.actions";
import { Action, createReducer, on } from "@ngrx/store";

export interface DriverState {
	ride: Ride | null;
}

const initialState : DriverState = {
	ride: null,
}

const reducer = createReducer(
	initialState,
	on(DriverRideActions.SetRideAction, (state, action) => {
	   return {
		...state,
		ride: action.ride
	   }
	}),
)

export function DriverRideReducer(state: DriverState | undefined, action: Action) {
	return reducer(state, action);
}
 