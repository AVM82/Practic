package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Mentor {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	Boolean inactive;

	String pib;
	
	String notes;
	
	String email;
	
	String phone;
	
	Long discordId;
	
	String discordName;
	
	String linkedInRef;
}
