package com.devsuperior.sendMailLoanBook.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class GenerateBookReturnDate {

	public static int numDaysToReturnBook = 7;
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	// Gets the formatted return date of the book
	public static String getDate(Date loan_date) {
		Calendar calendar = dateToCalendar(loan_date);
		calendar.add(Calendar.DATE, numDaysToReturnBook);
		String result = dateFormat.format(calendarToDate(calendar));
		return result;
	}

	// Convert Date to Calendar
	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	// Convert Calendar to Date
	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

}
