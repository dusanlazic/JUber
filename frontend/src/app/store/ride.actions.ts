import { Action, createAction, props } from "@ngrx/store";
import { Place, Route } from "src/models/ride";

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


export const AddPlaceAction = createAction(
	ADD_PLACE,
	props<{payload: Place}>()
)

export const SetPreviewAction = createAction(
	SET_PREVIEW,
	props<{payload: Place}>()
)

export const RemovePreviewAction = createAction(
	REMOVE_PREVIEW,
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




