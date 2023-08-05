package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CourseType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	String name;
}
