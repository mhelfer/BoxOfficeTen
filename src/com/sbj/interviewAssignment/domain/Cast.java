package com.sbj.interviewAssignment.domain;

/**
 * Domain object for the cast data.
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Box_Office_Movies
 * 
 * @author mhelfer
 *
 */
public class Cast {
	private String name;
	private String id;
	private String character;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCharacter() {
		return character;
	}
	
	public void setCharacter(String character) {
		this.character = character;
	}	
}	
