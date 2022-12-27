package com.nwt.juber.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class DriverShift {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
	private Timestamp startShift;
	private Timestamp endShift;
	private Long duration;

	public DriverShift(Timestamp startShift) {
		super();
		this.startShift = startShift;
	}
	
	public void setEndShift(Timestamp endShift) {
		this.endShift = endShift;
		this.duration = endShift.getTime() - startShift.getTime();
	}
}
