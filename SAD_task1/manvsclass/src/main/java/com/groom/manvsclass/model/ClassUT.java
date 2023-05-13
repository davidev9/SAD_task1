package com.groom.manvsclass.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ClassUT")
public class ClassUT {
	private String	name;
	private String	date;
	private	String	difficulty;
	private	String	code_url;
	private	String	description;
	private List<String> category;
	
	
	public ClassUT(String name, String date,String description, String difficulty, String code_url,List<String> category) {
        this.name = name;
        this.date = date;
        this.difficulty = difficulty;
        this.code_url = code_url;
        this.description=description;
        this.category = category;
    }
	
	public ClassUT()
	{
		
	}
	
	public String getName() {
		return name;
	}
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
        this.category = category;
    }
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public String getCode_url() {
		return code_url;
	}
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Override
	public String toString() {
	    return "ClassUT{" +
	            "name='" + name + '\'' +
	            ", date='" + date + '\'' +
	            ", difficulty='" + difficulty + '\'' +
	            ", code_url='" + code_url + '\'' +
	            ", category=" + category +
	            '}';
	}

	
	
	    
}
