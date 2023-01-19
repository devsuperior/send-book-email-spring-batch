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

import com.devsuperior.sendMailLoanBook.domain.User;

@Configuration
public class ReadUsersWithLoansCloseToReturnReaderConfig {

	@Bean
	public ItemReader<User> readUsersWithLoansCloseToReturnReader(@Qualifier("appDS") DataSource dataSource) {

		return new JdbcCursorItemReaderBuilder<User>().name("readUsersWithLoansCloseToReturnReader")
				.dataSource(dataSource)
				.sql("SELECT * FROM tb_user")
				.rowMapper(rowMapper())
				.build();
	}

	private RowMapper<User> rowMapper() {
		return new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
				return user;
			}

		};
	}
}
