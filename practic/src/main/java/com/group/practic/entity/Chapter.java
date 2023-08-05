package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chapter {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Level level;

	@ManyToOne(fetch=FetchType.LAZY)
	Chapter parent;
	
	Integer step;

	String name;
	
	String refs;
}
