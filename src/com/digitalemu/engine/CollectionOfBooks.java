package com.digitalemu.engine;

import java.util.*;

public class CollectionOfBooks {

	ArrayList<Book> books = new ArrayList<Book>();

	public CollectionOfBooks() {
		books = new ArrayList<>();
	}
	
	public void addBook(Book book) {
		
		books.add(book);		
	}

	public boolean delBook(Book book) {
		boolean bookIsDeleted = false;		
		if(books.contains(book)) {
			books.remove(book);
			bookIsDeleted = true;
		}
		return bookIsDeleted;
	}


	public ArrayList<Book> getBooksByTitle(String title) {
		ArrayList<Book> foundBooks = new ArrayList<Book>();
		for(Book book : books) {
			if(book.getTitle().equals(title)) {
				foundBooks.add(book);
			}
		}
		return foundBooks;
	}

	public ArrayList<Book> getBooksByAuthor(String wantedAuthor) {
		ArrayList<Book> foundBooks = new ArrayList<Book>();
		for (Book book : books ) {
			for(Author author : book.getAuthors()) {
				if (author.getName().equals(wantedAuthor)) {
					foundBooks.add(book);
					break;
				}
			}
		}
		return foundBooks;
	}

	public ArrayList<Book> getBooksByIsbn(String isbn) {
		ArrayList<Book> foundBooks = new ArrayList<Book>();
		for (Book book : books) {
			if (book.getIsbn().equals(isbn)) {
				foundBooks.add(book);
			}
		}
		return foundBooks;
	}

	public ArrayList<Book> getBooks(){
		return books;
	}
	
	private void sortBooks() {
		ArrayList<Book> sortedList = new ArrayList<Book>();
		Collections.sort(books);
			for (Book books : sortedList) {
				System.out.println("sortedList (by title): " + books);
			}
	}
	
	public String toString() {
		String bookList = "";
		for (Book book : books){
			String authors = "";
			for (Author author : book.getAuthors()) {
				authors.concat(author.getName());
			}
			bookList.concat(book.getTitle() + ", " + authors + ", " + book.getIsbn() + "\n");
		}
		return bookList;
	}
}
