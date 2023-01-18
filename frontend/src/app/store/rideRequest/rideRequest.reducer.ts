import { Action, createReducer, on } from "@ngrx/store";
import * as RideRequestAction from "./rideRequest.actions";
import * as _ from 'lodash';
import { RideRequest } from "src/models/rideRequest";
import { IRideRequest, IPal } from "./rideRequest";
import { indexOf } from "lodash";


const initialState: RideRequest = new RideRequest()

const reducer = createReducer(
	initialState,

	on(RideRequestAction.DeletePalAction, (state, action) => {
		let pals: IPal[] = _.cloneDeep(state.passengersInfo)
		let toRemove = pals.filter((pal: IPal) => action.removePal.email === pal.email).at(0)
		if(toRemove === undefined) {
			return state;
		}
		pals = pals.filter((pal: IPal) => toRemove!.email !== pal.email)
		const newState: IRideRequest = {
			...state,
			passengersInfo: pals,
			
		}
		return newState
	 }),

	on(RideRequestAction.AddPalAction, (state, action) => {
	   return {
		...state,
		passengersInfo: [...state.passengersInfo, {...action.addedPal}],
		}
	}),

	on(RideRequestAction.UpdateBabyFriendly, (state) => {
		return {
		 ...state,
		 additionalRequests: {
			babyFriendly: !state.additionalRequests.babyFriendly,
			petFriendly: state.additionalRequests.petFriendly,
			vehicleType: state.additionalRequests.vehicleType,
		 }
		}
	}),

	on(RideRequestAction.UpdatePetFriendly, (state) => {
		return {
		 ...state,
		 additionalRequests: {
			babyFriendly: state.additionalRequests.babyFriendly,
			petFriendly: !state.additionalRequests.petFriendly,
			vehicleType: state.additionalRequests.vehicleType,
		 }
		}
	}),

	on(RideRequestAction.UpdateVehicleType, (state, action) => {
		return {
		 ...state,
		 additionalRequests: {
			babyFriendly: state.additionalRequests.babyFriendly,
			petFriendly: state.additionalRequests.petFriendly,
			vehicleType: action.vehicleType,
		 }
		}
	}),

	on(RideRequestAction.UpdateScheduleTime, (state, action) => {
		return {
		 ...state,
		 scheduleTime: action.time
		}
	}),

 );
 
 export function RideRequestReducer(state: IRideRequest | undefined, action: Action) {
   return reducer(state, action);
 }

