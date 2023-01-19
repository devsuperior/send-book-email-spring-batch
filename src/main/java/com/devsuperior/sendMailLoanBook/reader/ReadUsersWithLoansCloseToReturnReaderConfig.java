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

@Configuration
public class ReadUsersWithLoansCloseToReturnReaderConfig {

	@Bean
	public ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader(@Qualifier("appDS") DataSource dataSource) {

		return new JdbcCursorItemReaderBuilder<UserBookLoan>().name("readUsersWithLoansCloseToReturnReader")
				.dataSource(dataSource)
				.sql("SELECT * FROM tb_user_book_loan " +
				"INNER JOIN tb_user ON tb_user_book_loan.user_id = tb_user.id " +
				"INNER JOIN tb_book ON tb_user_book_loan.book_id = tb_book.id " +
				"WHERE DATE_ADD(loan_date, INTERVAL 6 DAY) = DATE(NOW());")
				.rowMapper(rowMapper())
				.build();
	}

	private RowMapper<UserBookLoan> rowMapper() {
		return new RowMapper<UserBookLoan>() {

			@Override
			public UserBookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
				Book book = new Book(rs.getInt(6), rs.getString(7), rs.getString("description"), rs.getString("author"), rs.getString("category"));
				UserBookLoan userBookLoan = new UserBookLoan(user, book, rs.getDate("loan_date"));
				return userBookLoan;
			}

		};
	}
}
