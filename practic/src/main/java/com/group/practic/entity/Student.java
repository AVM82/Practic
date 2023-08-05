package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	String pib;
	
	String notes;
	
	String email;
	
	String phone;

	Long discordId;

	String discordName;

	String linkedInRef;

	String githubRef;

	String skypeRef;

	String facebookRef;

	String telegramRef;

	String instagramRef;

	String locality;

	public Student(String pib, String notes, String email, String phone, Long discordId) {
		this.pib = pib;
		this.notes = notes;
		this.email = email;
		this.phone = phone;
		this.discordId = discordId;
	}
}
