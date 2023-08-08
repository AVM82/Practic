package com.group.practic.entity;

import java.time.LocalDateTime;

import com.group.practic.enumeration.ReportState;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "student_report")
public class StudentReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	int id;

	@ManyToOne
	ChapterEntity chapter;

	@ManyToOne
	StudentEntity student;

	@Future
	LocalDateTime dateTime;
	
	@NotBlank
	String title;

	ReportState state;

}
