package com.devsuperior.sendMailLoanBook.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.devsuperior.sendMailLoanBook.domain.Book;
import com.devsuperior.sendMailLoanBook.domain.User;
import com.devsuperior.sendMailLoanBook.domain.UserBookLoan;
import com.devsuperior.sendMailLoanBook.util.GenerateBookReturnDate;

@Configuration
public class ReadUsersWithLoansCloseToReturnReaderConfig {

	// value to notify user is obtained by subtracting one day of number days to return book
	int numDaysToNotifyReturn = GenerateBookReturnDate.numDaysToReturnBook - 1;

	@Bean
	public ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader(@Qualifier("appDS") DataSource dataSource) {

		return new JdbcCursorItemReaderBuilder<UserBookLoan>().name("readUsersWithLoansCloseToReturnReader")
				.dataSource(dataSource)
				.sql("SELECT user.id as user_id, "
						+ "user.name as user_name, "
						+ "user.email as user_email, " 
						+ "book.id as book_id, "
						+ "book.name as book_name, "
						+ "loan.loan_date "
						+ "FROM tb_user_book_loan as loan " 
						+ "INNER JOIN tb_user as user ON loan.user_id = user.id "
						+ "INNER JOIN tb_book as book ON loan.book_id = book.id "
						+ "WHERE DATE_ADD(loan_date, INTERVAL " + numDaysToNotifyReturn + " DAY) = DATE(NOW());")
				.rowMapper(rowMapper()).build();
	}

	private RowMapper<UserBookLoan> rowMapper() {
		return new RowMapper<UserBookLoan>() {

			@Override
			public UserBookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("user_email"));
				Book book = new Book();
				book.setId(rs.getInt("book_id"));
				book.setName(rs.getString("book_name"));
				
				UserBookLoan userBookLoan = new UserBookLoan(user, book, rs.getDate("loan_date"));
				return userBookLoan;
			}

		};
	}
}
