package com.devsuperior.sendMailLoanBook.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.sendMailLoanBook.domain.UserBookLoan;

@Configuration
public class SendEmailRequestReturnWriterConfig {
	
	@Bean
	public ItemWriter<UserBookLoan> sendEmailRequestReturn() {
		return items -> items.forEach(System.out::println);
	}

}
