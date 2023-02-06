# ![DevSuperior logo](https://raw.githubusercontent.com/devsuperior/bds-assets/main/ds/devsuperior-logo-small.png) Job responsible for sending emails automatically using Spring Batch
>  Case study to implement a job to send emails using Spring Batch

#### Before starting

- [Using Docker Compose with MySQL and phpMyAdmin](https://github.com/devsuperior/docker-compose-mysql)

### Steps Spring Batch:

- Reading: Read users with loans close to return (number of days to return or renew book minus 1 day)
- Processing: Generate email message (template)
- Writing: Send email notifying return

### Resources:

#### import.sql

```sql
CREATE TABLE tb_user (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	email VARCHAR(60) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE tb_book (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	description TEXT NOT NULL,
	author VARCHAR(60) NOT NULL,
	category VARCHAR(20) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE tb_user_book_loan (
	user_id INT NOT NULL,
	book_id INT NOT NULL,
	loan_date DATE NOT NULL,
	PRIMARY KEY(user_id, book_id),
	FOREIGN KEY (user_id) REFERENCES tb_user(id),
	FOREIGN KEY (book_id) REFERENCES tb_book(id)
);

INSERT INTO tb_user(id, name, email) VALUES (1, 'Maria', 'seu-email@mailslurp.com');
INSERT INTO tb_user(id, name, email) VALUES (2, 'João', 'seu-email@mailslurp.com');
INSERT INTO tb_user(id, name, email) VALUES (3, 'Ana', 'seu-email@mailslurp.com');

INSERT INTO tb_book (id, name, description, author, category) VALUES(1, 'Capitães de Areia', 'Lorem ipsum dolor sit amet. Est dicta voluptate sed pariatur laboriosam repellendus!', 'Jorge Amado', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(2, 'Dom Casmurro', 'Lorem ipsum dolor sit amet. Et praesentium nobis ut quaerat voluptate eum volup.', 'Machado de Assis', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(3, 'Quincas Borba', 'Eos doloribus impedit ut dolor sunt sit nostrum libero', 'Machado de Assis', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(4, 'Alguma poesia', 'Lorem ipsum dolor sit amet. Quo voluptates soluta sit.', 'Carlos Drummond de Andrade', 'Poesia');
INSERT INTO tb_book (id, name, description, author, category) VALUES(5, 'A hora da estrela', 'Et sunt quaerat vel provident dolores quo Quis', 'Clarisse Lispector', 'Poesia');
INSERT INTO tb_book (id, name, description, author, category) VALUES(6, 'Tudo sobre o amor', 'Lorem ipsum dolor sit amet. Ut corrupti ullam aut', 'Bell Hooks', 'Humanidade');
INSERT INTO tb_book (id, name, description, author, category) VALUES(7, 'Torto Arado', 'Id tempore quas et aperiam minima ut dolores', 'Itamar Vieira Junior', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(8, 'Os Miseráveis', 'Lorem ipsum dolor sit amet. Aut voluptates', 'Victor Hugo', 'Romance');
INSERT INTO tb_book (id, name, description, author, category) VALUES(9, 'Dom Quixote', 'Hic nobis dolor ut praesentium aspernatur', 'Miguel de Cervantes', 'Romance');

INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(1, 9, '2023-01-29');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(1, 4, '2023-01-31');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(2, 6, '2023-01-29');
INSERT INTO tb_user_book_loan(user_id, book_id, loan_date) VALUES(3, 2, '2023-01-31');
```

#### Datasource Config

```java
  @Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource springDS() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "app.datasource")
	public DataSource appDS() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public PlatformTransactionManager transactionManagerApp(@Qualifier("appDS") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
```

#### Query to get users who have borrowed books close to return (business rule: books must be returned or renewed within 7 days / Read Step)

```sql
SELECT
    user.id as user_id, 
    user.name as user_name, 
    user.email as user_email, 
    book.id as book_id, 
    book.name as book_name, 
    loan.loan_date 
FROM tb_user_book_loan as loan 
INNER JOIN tb_user as user ON loan.user_id = user.id 
INNER JOIN tb_book as book ON loan.book_id = book.id 
WHERE DATE_ADD(loan_date, INTERVAL 6 DAY) = DATE(NOW());
```

#### Template email (Process step):

```java
private String generateEmailText(UserBookLoan loan) {		
    StringBuilder writer = new StringBuilder();
    writer.append(String.format("Prezado(a), %s, matricula %d\n", loan.getUser().getName(), loan.getUser().getId()));
    writer.append(String.format("Informamos que o prazo de devolução do livro %s é amanhã (%s) \n", loan.getBook().getName(), GenerateBookReturnDate.getDate(loan.getLoan_date())));
    writer.append("Solicitamos que você renove o livro ou devolva, assim que possível.\n");
    writer.append("A Biblioteca Municipal está funcionando de segunda a sexta, das 9h às 17h.\n\n");
    writer.append("Atenciosamente,\n");
    writer.append("Setor de empréstimo e devolução\n");
    writer.append("BIBLIOTECA MUNICIPAL");
    return writer.toString();
}
```

#### Method to send email (Writer Step)

```java
private void sendEmail(Mail email) {
		LOG.info("Sending mail");
		Request request = new Request();		
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(email.build());
			LOG.info("[WRITER STEP] Send email to: " + email.build());
			Response response = sendGrid.api(request);
			if (response.getStatusCode() >= 400 && response.getStatusCode() <= 500) {
				LOG.error("Error sending email: " + response.getBody());
				throw new Exception(response.getBody());
			}			
			LOG.info("Email sent! Status = " + response.getStatusCode());
		} catch (Exception e) {
			e.getMessage();
		}
	}
```

#### Add environment variable
```
spring.sendgrid.api-key=KEY_SENDGRID
```

#### Schedule sending email

```java
@Configuration
public class QuartzConfig {

	@Bean
	public JobDetail quartzJobDetail() {
		return JobBuilder.newJob(SendBookLoanNotificationScheduleJob.class).storeDurably().build();
	}

	@Bean
	public Trigger jobTrigger() {
		String exp = "0 51 18 * * ?";
		return TriggerBuilder
				.newTrigger()
				.forJob(quartzJobDetail())
				.startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule(exp))
				.build();
	}
}
```
```java
@Configuration
public class SendBookLoanNotificationScheduleJob extends QuartzJobBean {
	
	@Autowired
	private Job job;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private JobLauncher jobLaucher;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer).getNextJobParameters(this.job).toJobParameters();
		try {
			this.jobLaucher.run(this.job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
```
```
spring.batch.job.enabled=false
```

See all :
- https://stackoverflow.com/questions/28928128/schedule-a-task-to-run-at-everyday-on-a-specific-time
- http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-06.html






