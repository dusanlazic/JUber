import {  createAction, props } from "@ngrx/store";
import { IPal, IVehicleType } from "./rideRequest";

export const ADD_PAL: string = "ADD_PAL"
export const DELETE_PAL: string = "DELETE_PAL"

export const UPDATE_BABY_FRIENDLY = "UPDATE_BABY_FRIENDLY"
export const UPDATE_PET_FRIENDLY = "UPDATE_PET_FRIENDLY"
export const UPDATE_VEHICLE_TYPE = "UPDATE_VEHICLE_TYPE"

export const UPDATE_SCHEDULE_TIME: string = "UPDATE_SCHEDULE_TIME"


export const AddPalAction = createAction(
	ADD_PAL,
	props<{addedPal: IPal}>()
)

export const DeletePalAction = createAction(
	DELETE_PAL,
	props<{removePal: IPal}>()
)

export const UpdateBabyFriendly = createAction(
	UPDATE_BABY_FRIENDLY
)

export const UpdatePetFriendly = createAction(
	UPDATE_PET_FRIENDLY
)

export const UpdateVehicleType = createAction(
	UPDATE_VEHICLE_TYPE,
	props<{vehicleType: IVehicleType}>()
)

export const UpdateScheduleTime = createAction(
	UPDATE_SCHEDULE_TIME,
	props<{time: string}>()
)
