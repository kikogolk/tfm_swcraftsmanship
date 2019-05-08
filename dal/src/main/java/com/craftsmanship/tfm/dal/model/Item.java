package com.craftsmanship.tfm.dal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
		
	private String description;
	
	protected Item() {

	}

	public Item(String description) {
		this.description = description;
	}
	
	public Item(long id, String description) {
		this(description);
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "Item [description=" + description  + "]";
	}
}