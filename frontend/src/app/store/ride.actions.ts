import { Action, createAction, props } from "@ngrx/store";
import { Place, Ride, Route } from "src/models/ride";

export const ADD_PLACE: string = "ADD_PLACE"
export const UPDATE_PLACE_ROUTE = "UPDATE_PLACE_ROUTE"
export const UPDATE_PLACE_NAME = "UPDATE_PLACE_NAME"
export const REMOVE_PLACE = "REMOVE_PLACE"
export const SET_PREVIEW: string = "SET_PREVIEW"
export const REMOVE_PREVIEW: string = "REMOVE_PREVIEW"
export const UPDATE_PREVIEW: string = "UPDATE_PREVIEW"
export const ADD_PREVIEW_TO_PLACES_ACTION: string = "ADD_PREVIEW_TO_PLACES_ACTION"
export const MOVE_TO_PREVIEW: string = "MOVE_TO_PREVIEW"
export const UPDATE_EDITED_PLACE: string = "UPDATE_EDITED_PLACE"
export const UPDATE_ROUTES: string = "UPDATE_ROUTES"
export const STOP_EDITING: string = "STOP_EDITING"
export const DELETE_PLACE: string = "DELETE_PLACE"
export const SWAP_PLACE_UP: string = "SWAP_PLACE_UP"
export const SWAP_PLACE_DOWN: string = "SWAP_PLACE_DOWN"
export const SET_RIDE: string = "SET_RIDE"


export const SetRideAction = createAction(
	SET_RIDE,
	props<{ride: Ride}>()
)


export const SwapPlaceUpAction = createAction(
	SWAP_PLACE_UP,
	props<{id: number, beforeRoutes: Route[], afterRoutes: Route[]}>()
)

export const SwapPlaceDownAction = createAction(
	SWAP_PLACE_DOWN,
	props<{id: number, beforeRoutes: Route[], afterRoutes: Route[]}>()
)

export const AddPlaceAction = createAction(
	ADD_PLACE,
	props<{place: Place}>()
)

export const SetPreviewAction = createAction(
	SET_PREVIEW,
	props<{payload: Place}>()
)

export const RemovePreviewAction = createAction(
	REMOVE_PREVIEW,
)

export const StopEditingAction = createAction(
	STOP_EDITING,
)

export const PreviewRouteSelectedAction = createAction(
	UPDATE_PREVIEW,
	props<{route: Route}>()
)


export const AddPreviewToPlacesAction = createAction(
	ADD_PREVIEW_TO_PLACES_ACTION,
)

export const MoveToPreviewAction = createAction(
	MOVE_TO_PREVIEW,
	props<{place: Place}>()
)

export const UpdateEditedPlace = createAction(
	UPDATE_EDITED_PLACE,
	props<{place: Place}>()
)

export const UpdateRoutes = createAction(
	UPDATE_ROUTES,
	props<{place: Place, routes: Route[]}>()
)

export const DeletePlaceAction = createAction(
	DELETE_PLACE,
	props<{place: Place}>()
)


