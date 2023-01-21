import { Action, createAction, props } from "@ngrx/store";
import { Place, Ride, Route } from "src/models/ride";


export const SET_RIDE: string = "SET_RIDE"


export const SetRideAction = createAction(
	SET_RIDE,
	props<{ride: Ride}>()
)