package com.devsuperior.sendMailLoanBook.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devsuperior.sendMailLoanBook.job.SendBookLoanNotificationScheduleJob;

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
