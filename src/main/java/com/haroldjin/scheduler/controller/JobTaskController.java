package com.haroldjin.scheduler.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haroldjin.scheduler.domain.RetObj;
import com.haroldjin.scheduler.domain.ScheduleJob;
import com.haroldjin.scheduler.service.JobTaskService;
import com.haroldjin.scheduler.utils.SpringUtils;


@Controller
@RequestMapping("/task")
public class JobTaskController {
	private static final String JOBS_PACKAGE = "com.haroldjin.scheduler.jobs.";
	public final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private JobTaskService taskService;

	@RequestMapping("taskList")
	public String taskList(HttpServletRequest request, Model model) {
		List<ScheduleJob> taskList = taskService.getAllTask();
		String path = request.getRequestURL().toString();
		path = path.substring(0, path.lastIndexOf("/"));
		model.addAttribute("path", path);
		model.addAttribute("taskList", taskList);
		return "task/taskList";
	}

	@RequestMapping("add")
	@ResponseBody
	public RetObj taskList(HttpServletRequest request, ScheduleJob scheduleJob) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		scheduleJob.setBeanClass(JOBS_PACKAGE + scheduleJob.getBeanClass());
		try {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
		} catch (Exception e) {
			retObj.setMsg("cron expression error！");
			return retObj;
		}
		Object obj = null;
		try {
			if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
				obj = SpringUtils.getBean(scheduleJob.getSpringId());
			} else {
				Class clazz = Class.forName(scheduleJob.getBeanClass());
				obj = clazz.newInstance();
			}
		} catch (Exception e) {
		}
		if (obj == null) {
			retObj.setMsg("Unable to find the class！");
			return retObj;
		} else {
			Class clazz = obj.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(scheduleJob.getMethodName(), null);
			} catch (Exception e) {
			}
			if (method == null) {
				retObj.setMsg("Unable to find method！");
				return retObj;
			}
		}
		try {
			taskService.addTask(scheduleJob);
		} catch (Exception e) {
			e.printStackTrace();
			retObj.setFlag(false);
			retObj.setMsg("Unable to save, check name group duplication！");
			return retObj;
		}

		retObj.setFlag(true);
		return retObj;
	}

	@RequestMapping("changeJobStatus")
	@ResponseBody
	public RetObj changeJobStatus(HttpServletRequest request, Long jobId, String cmd) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			taskService.changeStatus(jobId, cmd);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			retObj.setMsg("Unable to change job status！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}

	@RequestMapping("updateCron")
	@ResponseBody
	public RetObj updateCron(HttpServletRequest request, Long jobId, String cron) {
		RetObj retObj = new RetObj();
		retObj.setFlag(false);
		try {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
		} catch (Exception e) {
			retObj.setMsg("cron expression error！");
			return retObj;
		}
		try {
			taskService.updateCron(jobId, cron);
		} catch (SchedulerException e) {
			retObj.setMsg("cron update unsuccessful！");
			return retObj;
		}
		retObj.setFlag(true);
		return retObj;
	}
}
