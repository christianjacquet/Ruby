package com.digitalemu.engine;

import java.io.Serializable;


public class Author implements Serializable {
	
	private String name;
	
	public Author(String name) {
		this.name = name.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}
}
