package com.devsuperior.sendMailLoanBook.domain;

import java.util.Date;

public class UserBookLoan {
	
	private User user;
	private Book book;
	private Date loan_date;
	
	public UserBookLoan() {
	}

	public UserBookLoan(User user, Book book, Date loan_date) {
		super();
		this.user = user;
		this.book = book;
		this.loan_date = loan_date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getLoan_date() {
		return loan_date;
	}

	public void setLoan_date(Date loan_date) {
		this.loan_date = loan_date;
	}

	@Override
	public String toString() {
		return "UserBookLoan [user=" + user + ", book=" + book + ", loan_date=" + loan_date + "]";
	}
}
