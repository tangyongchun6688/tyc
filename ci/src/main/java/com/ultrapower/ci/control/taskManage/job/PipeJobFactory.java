package com.ultrapower.ci.control.taskManage.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.common.quartz.QuartzManager;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.control.autoTestManage.dao.AutoTestMapper;
import com.ultrapower.ci.control.buildManage.dao.BuildMapper;
import com.ultrapower.ci.control.deployManage.dao.DeployMapper;
import com.ultrapower.ci.control.taskManage.dao.TaskManageMapper;
import com.ultrapower.ci.control.taskManage.service.ITaskManageService;
/**
 * 
 * @author yangbin6
 * Description : 动态添加流水线定时执行任务
 * 2017年12月19日
 */
public class PipeJobFactory implements Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		@SuppressWarnings("resource")
		BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/spring-*.xml"); 
		ITaskManageService taskManageService = factory.getBean(ITaskManageService.class);
		TaskManageMapper taskManageMapper = factory.getBean(TaskManageMapper.class);
		BuildMapper buildMapper = factory.getBean(BuildMapper.class);
		DeployMapper deployMapper = factory.getBean(DeployMapper.class);
		AutoTestMapper autoTestMapper = factory.getBean(AutoTestMapper.class);
		
		String pipId = context.getJobDetail().getJobDataMap().get("pipId")
				.toString();
		
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String projectName = servletContext.getContextPath();
		
		//1、获取流水线下最大版本的任务
		List<Map<String,String>> taskList = new ArrayList<Map<String,String>>();
		String pipVersion = "0";
		Map<String,String> vmap = taskManageMapper.getMaxPipVersionByPipId(pipId);
		if(vmap!= null && vmap.get("MAX_PIP_VERSION") != null){//存在最大版本号，表示有构建历史
			pipVersion = String.valueOf(vmap.get("MAX_PIP_VERSION"));
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("pipId", pipId);
			paraMap.put("pip_version", pipVersion);
			//查询流水线最大历史版本信息
			taskList = taskManageMapper.queryBuildHistoryByVersion(paraMap);
		}else{//不存在构建历史时，查询流水线原有信息
			taskList = taskManageMapper.queryTaskListByPipId(pipId);
		}
		//2、在构建历史插入流水线信息
		Map<String, Object> pmap1 = this.savePipHistory(pipId, pipVersion, "insertPip",taskManageService);
		if (pmap1 != null && pmap1.get("status") != null) {
			if (pmap1.get("status").equals("success")) {
				pipVersion = (String) pmap1.get("pipVersion");
				// 3、循环执行流水线的各个任务
				Map<String, Object> pmap2 = this.batchExecutionTask(pipId, pipVersion, projectName, taskList,
						taskManageService, buildMapper,deployMapper,autoTestMapper);
				if (pmap2 != null && pmap2.get("status") != null) {
					if (pmap2.get("status").equals("success")) {
						// 4、修改构建历史表流水线执行状态
						Map<String, Object> pmap3 = this.savePipHistory(pipId, pipVersion, "updatePip",
								taskManageService);
					}
				}
			}
		}
		// TODO Auto-generated method stub
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date())
				+ "★★★★★★★★★★★定时执行流水线"
				+ pipId);
	}
	
	
	/**
	 * @param pipId
	 * @param pipVersion
	 * @param operationFlag{
	 *            "insertPip" : 在构建历史插入流水线信息,"updatePip" 修改构建历史表流水线执行状态}
	 * @return
	 */
	private Map<String, Object> savePipHistory(String pipId,String pipVersion,String operationFlag,ITaskManageService taskManageService){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pipId", pipId);
		map.put("pipVersion", pipVersion);
		map.put("operationFlag", operationFlag);
		String currentTime = DateTimeUtils.getFormatCurrentTime();
		map.put("buildStartTime", currentTime);
		
		Map<String, Object> rMap = taskManageService.savePipHistory(map);
		return rMap;
	}
	/**
	 * 循环执行流水线下的各个任务
	 * @param pipId  流水线主键
	 * @param pipVersion 流水线版本
	 * @param projectName 项目名称
	 * @param taskList  流水线下各个任务集合
	 * @param taskManageService
	 * @return
	 */
	private Map<String, Object> batchExecutionTask(String pipId, String pipVersion, String projectName,
			List<Map<String, String>> taskList, ITaskManageService taskManageService, BuildMapper buildMapper,
			DeployMapper deployMapper,AutoTestMapper autoTestMapper) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		try {
			if(taskList != null && taskList.size() > 0){
				for (int i = 0; i < taskList.size(); i++) {
					Map<String,String> itemMap = taskList.get(i);
//					String jobName = itemMap.get("TASK_NAME");
					String taskType = itemMap.get("TASK_TYPE");
					String taskId = itemMap.get("TASK_ID");
					String pipStepTaskId = itemMap.get("PST_ID");
					String jobName = "";
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("pipId", pipId);
//					map.put("jobName", jobName);
					map.put("taskType", taskType);
					map.put("taskId", taskId);
					map.put("pipStepTaskId", pipStepTaskId);
					String currentTime = DateTimeUtils.getFormatCurrentTime();
					map.put("buildStartTime", currentTime);
					map.put("pipVersion", pipVersion);
					map.put("projectName", projectName);
					if (TaskConstant.TASK_TYPE_ITEM1.equals(taskType)) {// 判断是否为构建操作
						Map<String,String> buildMap = buildMapper.getBuildById(taskId);
						jobName = buildMap.get("BUILD_NAME");
					}else if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {// 判断是否为部署操作
						Map<String,String> deployMap = deployMapper.getDeployById(taskId);
						jobName = deployMap.get("DEPLOY_NAME");
						// 清空节点表中的ip及port
						taskManageService.emptyNodeInfo(taskId);
					}else if(TaskConstant.TASK_TYPE_ITEM3.equals(taskType)){//判断是否为自动化测试操作
						Map<String,String> autoTestMap = autoTestMapper.getAutoTestById(taskId);
						jobName = autoTestMap.get("TEST_NAME");
					}
					map.put("jobName", jobName);
					rMap = taskManageService.updateSingleJobHistory(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rMap;
	}
	
	/**
	 * 添加流水线定时执行任务
	 * @param pipId   流水线
	 * @param jobTimeCron 定时执行cron表达式
	 * @return
	 */
	public static boolean startPipeByQuartz(String pipId,String jobTimeCron){
		 boolean flag = false;
		 
//		 pipId = "bbefad253e3c4ee584af876d9375993e";
//		 jobTimeCron = "0 58 10 25 12 ? ";
		try {
			SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
			Scheduler sche = gSchedulerFactory.getScheduler();
			//获取所有定时任务的触发器名称
			String[] getJobNames = sche.getJobNames(QuartzManager.JOB_GROUP_NAME);
			String job_name = "pipeJobName_" + pipId;
			//判断该名称是否已经被使用
			boolean is_exist = Arrays.asList(getJobNames).contains(job_name);
			//没有使用时才能新建定时任务
			if(!is_exist){
				JobDataMap paramsMap = new JobDataMap();
				paramsMap.put("pipId", pipId);
				QuartzManager.addJob(sche, job_name, PipeJobFactory.class,
						jobTimeCron, paramsMap);
			}			
			flag = true;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
