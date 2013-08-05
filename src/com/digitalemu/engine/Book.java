package com.digitalemu.engine;

import java.util.*;
import java.io.Serializable;
import java.lang.Comparable;


public class Book implements Comparable<Book>, Serializable { //implementera Comparable så att böckerna rangordnas efter titel

	private String isbn;
	private String title;
	private int edition;
	private double price;
	private ArrayList<Author> authors;
	
	public Book(ArrayList<Author> authors, String isbn, String title, int edition, double price ) {
		
		this.authors = authors;
		this.isbn = isbn.trim();
		this.title = title.trim();
		this.edition = edition;
		this.setPrice(price);
	}

	@Override
	public int compareTo(Book book) {
		int titleCompare = this.title.compareToIgnoreCase(book.title);
		return titleCompare;
	}

	public void addAuthor(Author author) {
		authors.add(author);
	}
	
	public void addAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}

	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public void addTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void addIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsbn() {
		return isbn;
	}
	
	public int getEdition() {
		return edition;
	}
	public void addEdition(int edition) {
		this.edition = edition;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}