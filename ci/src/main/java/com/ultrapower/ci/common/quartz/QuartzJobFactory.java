package com.ultrapower.ci.common.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.ultrapower.ci.control.taskManage.dao.TaskManageMapper;
import com.ultrapower.ci.control.taskManage.service.ITaskManageService;
public class QuartzJobFactory implements Job{
	
	@Resource
	private ITaskManageService taskManageService;
	@Autowired
	private TaskManageMapper taskManageMapper;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date())
				+ "★★★★★★★★★★★"
				+ context.getJobDetail().getJobDataMap()
						.get("endTime"));
				
				String endTime = context.getJobDetail().getJobDataMap().get("endTime")
						.toString();
				
				Map<String,String> vmap = taskManageMapper.getMaxPipVersionByPipId("bbefad253e3c4ee584af876d9375993e");
				if(vmap!= null && vmap.get("MAX_PIP_VERSION") != null){
					System.out.println(vmap.get("MAX_PIP_VERSION"));
				}
	}
	
	public static boolean startPipeByQuartz(){
		 boolean flag = false;
		try {
			SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
			Scheduler sche = gSchedulerFactory.getScheduler();
			String job_name = "111";
			String jobTimeCron = "0/10 * * * * ? *";
			JobDataMap paramsMap = new JobDataMap();
			paramsMap.put("endTime", "12323");
			QuartzManager.addJob(sche, job_name, QuartzJobFactory.class,
					jobTimeCron, paramsMap);
			flag = true;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
