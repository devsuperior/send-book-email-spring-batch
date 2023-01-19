package com.devsuperior.sendMailLoanBook.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.sendMailLoanBook.domain.User;

@Configuration
public class SendEmailRequestReturnConfig {
	
	@Bean
	public ItemWriter<User> sendEmailRequestReturn() {
		return items -> items.forEach(System.out::println);
	}

}
