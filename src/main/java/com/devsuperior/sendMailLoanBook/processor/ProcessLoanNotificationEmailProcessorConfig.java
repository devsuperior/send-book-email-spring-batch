package com.devsuperior.sendMailLoanBook.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.sendMailLoanBook.domain.UserBookLoan;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Configuration
public class ProcessLoanNotificationEmailProcessorConfig {
	
	private static Logger LOG = LoggerFactory.getLogger(ProcessLoanNotificationEmailProcessorConfig.class);
	
	@Bean
	public ItemProcessor<UserBookLoan, Mail> processLoanNotificationEmailProcessor() {
		return new ItemProcessor<UserBookLoan, Mail>() {

			@Override
			public Mail process(UserBookLoan loan) throws Exception {				
				Email from = new Email("alexandre.si.ufu@gmail.com", "Biblioteca Municipal Sorocaba");
				Email to = new Email(loan.getUser().getEmail());
				Content content = new Content("text/plain", generateEmailText(loan));
				Mail mail = new Mail(from, "Notificaçao devoluçao livro", to, content);
				LOG.info("==== [PROCESSOR STEP]" + mail.toString());
				Thread.sleep(1000);
				return mail;
			}

			private String generateEmailText(UserBookLoan loan) {
				StringBuilder writer = new StringBuilder();
				writer.append(String.format("Prezado(a), %s, matricula %d\n", loan.getUser().getName(), loan.getUser().getId()));
				writer.append(String.format("Informamos que o prazo de devolução do livro %s é amanhã (%s) \n", loan.getBook().getName(), loan.getLoan_date()));
				writer.append("Solicitamos que você renove o livro ou devolva, assim que possível.\n");
				writer.append("A Biblioteca Municipal está funcionando de segunda a sexta, das 9h às 17h.\n\n\n");
				writer.append("Atenciosamente,\n");
				writer.append("Setor de empréstimo e devolução\n");
				writer.append("MUNICIPAL DE SOROCABA");
				return writer.toString();
			}
			
		};
		
	}	

}
