package com.haroldjin.scheduler.task;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.haroldjin.scheduler.domain.ScheduleJob;
import com.haroldjin.scheduler.utils.TaskUtils;

public class QuartzJobFactory implements Job {
	public final Logger log = Logger.getLogger(getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		TaskUtils.invokMethod(scheduleJob);
	}
}