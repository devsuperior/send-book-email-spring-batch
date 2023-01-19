package com.devsuperior.sendMailLoanBook.domain;

public class Book {
	
	private int id;
	private String name;
	private String description;
	private String author;
	private String category;
	
	public Book() {
	}

	public Book(int id, String name, String description, String author, String category) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", description=" + description + ", author=" + author
				+ ", category=" + category + "]";
	}
}
