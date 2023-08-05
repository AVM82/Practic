package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "level")
@Data
public class LevelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	CourseEntity course;
	
	Integer number;

	Long discordChat; //?
}
