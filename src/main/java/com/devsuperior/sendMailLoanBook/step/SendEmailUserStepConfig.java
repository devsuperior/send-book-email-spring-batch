package com.devsuperior.sendMailLoanBook.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.devsuperior.sendMailLoanBook.domain.User;

@Configuration
public class SendEmailUserStepConfig {
	
	@Autowired
	@Qualifier("transactionManagerApp")
	private PlatformTransactionManager transactionManager;

	@Bean
	public Step sendEmailUserStep(ItemReader<User> readUsersWithLoansCloseToReturnReader,
			ItemWriter<User> sendEmailRequestReturn, JobRepository jobRepository) {		
		return new StepBuilder("envioEmailClienteStep", jobRepository)
				.<User, User>chunk(1, transactionManager)
				.reader(readUsersWithLoansCloseToReturnReader)
				.writer(sendEmailRequestReturn)
				.build();
	}

}