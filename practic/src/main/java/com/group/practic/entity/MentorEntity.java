package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mentor")
@Data
public class MentorEntity {

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
