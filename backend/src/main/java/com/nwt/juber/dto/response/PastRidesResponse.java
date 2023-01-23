package com.nwt.juber.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PastRidesResponse {
	private UUID id;
	private String startPlaceName;
	private String endPlaceName;
	private String formattedDate;
	private String startTime;
	private String endTime;
	private Double fare;
	@JsonIgnore
	private Long endTimestamp;
}
