package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "sub_sub_chapter")
public class SubSubChapterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	CourseEntity course;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	SubChapterEntity subChapter;

	int number;

	@NotBlank
	@Column(length = 1024)
	String name;

	@Column(length = 1024)
	String refs;

	public SubSubChapterEntity() {
	}

	public SubSubChapterEntity(int id, CourseEntity course, SubChapterEntity subChapter, int number,
			@NotBlank String name, String refs) {
		super();
		this.id = id;
		this.course = course;
		this.subChapter = subChapter;
		this.number = number;
		this.name = name;
		this.refs = refs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CourseEntity getCourse() {
		return course;
	}

	public void setCourse(CourseEntity course) {
		this.course = course;
	}

	public SubChapterEntity getSubChapter() {
		return subChapter;
	}

	public void setSubChapter(SubChapterEntity subChapter) {
		this.subChapter = subChapter;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRefs() {
		return refs;
	}

	public void setRefs(String refs) {
		this.refs = refs;
	}

}
