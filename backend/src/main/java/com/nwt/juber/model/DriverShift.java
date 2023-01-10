package com.nwt.juber.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverShift {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    
	private Timestamp startOfShift;
	private Timestamp endOfShift;
	private long duration;

	public DriverShift(Timestamp startOfShift) {
		super();
		this.startOfShift = startOfShift;
	}
	
	public void setEndShift(Timestamp endShift) {
		this.endOfShift = endShift;
		this.duration = endShift.getTime() - startOfShift.getTime();
	}
}
