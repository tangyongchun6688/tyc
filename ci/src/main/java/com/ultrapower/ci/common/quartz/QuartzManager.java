package com.ultrapower.ci.common.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;

/**
 * Quartz调度管理器
 * 
 * @author yangbin6
 * @date 2017-10-18
 */
public class QuartzManager {

	public static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param sched
	 *            调度器
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @return void
	 * @throws
	 * 
	 */
	public static boolean addJob(Scheduler sched, String jobName,
			@SuppressWarnings("rawtypes") Class cls, String time,
			JobDataMap paramsMap) {
		if(jobName == null){
			return false;
		}
		try {
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类
			jobDetail.setJobDataMap(paramsMap);
			// 触发器
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
			trigger.setCronExpression(time);// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
//			throw new RuntimeException(e);
		}
		return true;
	}

	/**
	 * 
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * @param @param sched
	 * @param @param jobName
	 * @return void
	 * @throws
	 * 
	 */
	public static void removeJob(Scheduler sched, String jobName) {
		try {
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
			sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param @param sched
	 * @param @param jobName
	 * @param @param time
	 * @return void
	 * @throws
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyJobTime(Scheduler sched, String jobName,
			String time,JobDataMap paramsMap) {
		try {
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName,
					TRIGGER_GROUP_NAME);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName,
						JOB_GROUP_NAME);
				Class objJobClass = jobDetail.getJobClass();
				removeJob(sched, jobName);
				addJob(sched, jobName, objJobClass, time,paramsMap);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 启动所有定时任务
	 * 
	 * @param @param sched
	 * @return void
	 * @throws
	 * 
	 */
	public static void startJobs(Scheduler sched) {
		try {
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 * 
	 * @param @param sched
	 * @return void
	 * @throws
	 * 
	 */
	public static void shutdownJobs(Scheduler sched) {
		try {
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
