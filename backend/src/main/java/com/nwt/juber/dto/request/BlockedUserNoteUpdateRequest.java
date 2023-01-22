package com.nwt.juber.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BlockedUserNoteUpdateRequest {
    @NotBlank
    @Size(max = 100, message = "Note size limit exceeded")
    private String note;
}
