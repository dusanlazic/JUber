export type VehicleType = VehicleTypeInput | null


export enum VehicleTypeInput {
    HATCHBACK= "hatchback",
	ESTATE='estate',
	LIMOUSINE='limousine',
	SPORTS='sports',
	PICKUP='pickup'
}



export interface AdditionalRequests {
    babyFriendly: boolean,
    petFriendly: boolean,
    vehicleType: VehicleType
}

