package com.nwt.juber.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PastRidesResponse {
	private UUID id;
	private String startPlaceName;
	private String endPlaceName;
	private String date;
	private String startTime;
	private String endTime;
	private Double fare;
}
