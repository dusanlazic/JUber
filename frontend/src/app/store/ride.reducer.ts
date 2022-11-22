import { Action, createReducer, on } from "@ngrx/store";
import { Place, Ride, Route } from "src/models/ride";
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
			places: [...state.ride.places, {...action.place}]
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
		if(state === undefined || state.previewPlace === null) return state;
		let routes: Route[] = _.cloneDeep(state.previewPlace.routes)

		routes.forEach(route => { route.selected = route.name === action.route.name; })
		let places: Place[] = _.cloneDeep(state.ride.places)
		let editing = places.filter(place => place.editing).at(0)
		if (editing) {
			editing.option = action.route.name;
			editing.routes = routes;
			places = places.filter(place => !place.editing)
			places.push(editing)
		}

		return {
		 ride: {
			...state.ride,
			places: places
		 },
		 previewPlace: {
			...state.previewPlace,
			option: action.route.name,
			routes: routes
		 }
		}
	 }),

	 on(RideAction.AddPreviewToPlacesAction, (state, action) => {
		if(!state.previewPlace) return state;
		let place = _.cloneDeep(state.previewPlace);
		place.editing = false;
		return {
		 ride: {
			...state.ride,
			places: [...state.ride.places, place]
		 },
		 previewPlace: null
		}
	 }),

	 on(RideAction.MoveToPreviewAction, (state, action) => {
		let places: Place[] = _.cloneDeep(state.ride.places)
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

	 on(RideAction.UpdateEditedPlace, (state, action) => {
		let places: Place[] = _.cloneDeep(state.ride.places)
		let edited = places.filter(place => place.editing).at(0)
		if (edited === undefined) return state;
		let placeInd = places.findIndex(x => x.name === edited!.name)
		let newPlace = _.cloneDeep(action.place)

		let nextPlace = places.at(placeInd + 1);
		if(nextPlace !== undefined) {

		}

		places[placeInd] = newPlace
		return {
		 ride: {
			...state.ride,
			places: places
		 },
		 previewPlace: places[placeInd]
		}
	 }),

	 on(RideAction.UpdateRoutes, (state, action) => {
		let places: Place[] = _.cloneDeep(state.ride.places)
		let place = places.filter(x => x.name === action.place.name).at(0)
		if (place === undefined) return state;
		
		place.routes = _.cloneDeep(action.routes);
		let selectedRoute = place.routes.filter(route => route.selected).at(0)
		place.option = selectedRoute ? selectedRoute.name : '';
		
		return {
			ride: {
				...state.ride,
				places: places
			},
			previewPlace: state.previewPlace
		}
	 }),

	 on(RideAction.StopEditingAction, (state, action) => {
		let places: Place[] = _.cloneDeep(state.ride.places)
		let editing = places.filter(place => place.editing).at(0)
		if(editing !== undefined) {
			editing.editing = false;
		}

		return {
			...state,
			ride: {
				...state.ride,
				places: places
		 }
		}
	 }),


	 on(RideAction.DeletePlaceAction, (state, action) => {
		let places: Place[] = _.cloneDeep(state.ride.places)
		let toRemove = places.filter(place => action.place.name === place.name).at(0)
		if(toRemove === undefined) {
			return state;
		}
		places = places.filter(place => toRemove!.name !== place.name)
		return {
			...state,
			ride: {
				...state.ride,
				places: places
		 }
		}
	 }),

 );
 
 export function RideReducer(state: AppState | undefined, action: Action) {
   return reducer(state, action);
 }

