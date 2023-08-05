package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chapter")
@Data
public class ChapterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
    LevelEntity level;

	@ManyToOne(fetch=FetchType.LAZY)
	ChapterEntity parent;
	
	Integer step;

	String name;
	
	String refs;
}
