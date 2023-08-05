package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Level {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Course course;
	
	Integer number;

	Long discordChat; //?
}
