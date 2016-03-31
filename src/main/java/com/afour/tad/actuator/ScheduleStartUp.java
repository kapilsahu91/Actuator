package com.afour.tad.actuator;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleStartUp{
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleStartUp.class);
	
	public static void main(String[] args) {
		Properties prop = new Properties();
			
		try {
			
		prop.load(new FileInputStream("src/main/resources/cron.properties"));
		//Create instance of factory
		SchedulerFactory schedulerFactory=new StdSchedulerFactory();
		
		//Get schedular
		Scheduler scheduler= schedulerFactory.getScheduler();
		
		//Create JobDetail object specifying which Job you want to execute
		JobDetail jobDetail=new JobDetail("ActuatorStartUpJobClass","ActuatorStartUpJob",ActuatorStartUpJob.class);
		
		//Associate Trigger to the Job
		CronTrigger trigger=new CronTrigger("cronTrigger","ActuatorStartUpJob",prop.getProperty("cron"));
		
		//Pass JobDetail and trigger dependencies to schedular
		scheduler.scheduleJob(jobDetail,trigger);
		
		//Start schedular
		scheduler.start();
		
		} catch (SchedulerException | ParseException e) {
			LOGGER.warn("Failure message : " + e.getMessage());
			LOGGER.warn("Failure cause : " + e.getCause());
			e.printStackTrace();
		}catch (IOException e) {
			LOGGER.warn("Failure message : " + e.getMessage());
			LOGGER.warn("Failure cause : " + e.getCause());
			e.printStackTrace();
		}
	}
}
