package com.nwt.juber.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicInfoResponse {
	private String email;
	private String firstName;
	private String lastName;
	private String imageUrl;
}
