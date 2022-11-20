import { Action, createReducer, on } from "@ngrx/store";
import { Place, Ride } from "src/models/ride";
import * as RideAction from "./ride.actions";
import * as _ from 'lodash';

export interface AppState {
	ride: Ride;
	previewPlace: Place | null;
}


const initialState : AppState = {
	ride: new Ride(),
	previewPlace: null
}


const reducer = createReducer(
	initialState,
	on(RideAction.AddPlaceAction, (state, action) => {
	   return {
		...state,
		ride: {
			...state.ride,
			places: [...state.ride.places, {...action.payload}]
		}
	   }
	}),

	on(RideAction.SetPreviewAction, (state, action) => {
		return {
		 ...state,
		 previewPlace: _.cloneDeep(action.payload)
		}
	 }),

	 on(RideAction.RemovePreviewAction, (state, action) => {
		return {
		 ...state,
		 previewPlace: null
		}
	 }),

	 on(RideAction.PreviewRouteSelectedAction, (state, action) => {
		console.log('PREVIEW SELECTED ACTION');
		console.log(state, action);
		
		if(state === undefined || state.previewPlace === null) return state;
		let routes = _.cloneDeep(state.previewPlace.routes)
		routes.forEach(route => { route.selected = route.name === action.route.name; })
		console.log(routes);
		return {
		 ...state,
		 previewPlace: {
			...state.previewPlace,
			routes: routes
		 }
		}
	 }),

	 on(RideAction.AddPreviewToPlacesAction, (state, action) => {
		if(!state.previewPlace) return state;
		return {
		 ride: {
			...state.ride,
			places: [...state.ride.places, state.previewPlace]
		 },
		 previewPlace: null
		}
	 }),

	 on(RideAction.MoveToPreviewAction, (state, action) => {
		console.log("MOVE TO PREVIEW ACTION");
		console.log(state, action);
		let places = _.cloneDeep(state.ride.places)
		places.forEach(x => {
			if(x.name === action.place.name) {
				x.editing = true;
			}
		})
		let place = [...state.ride.places].filter(x => x.name === action.place.name)[0]
		return {
		 ride: {
			...state.ride,
			places: places
		 },
		 previewPlace: {...place}
		}
	 }),

 );
 
 export function RideReducer(state: AppState | undefined, action: Action) {
   return reducer(state, action);
 }