package com.group.practic.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "level")
public class LevelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	CourseEntity course;
	
	int number;
	
	Set<Integer> chapters;

	String discordChat; //?
	
	String telegramChat;
	
	String anotherChat;
	
	
	public LevelEntity() {}


	public LevelEntity(int id, CourseEntity course, int number, String discordChat) {
		super();
		this.id = id;
		this.course = course;
		this.number = number;
		this.discordChat = discordChat;
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


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getDiscordChat() {
		return discordChat;
	}


	public void setDiscordChat(String discordChat) {
		this.discordChat = discordChat;
	}


}
