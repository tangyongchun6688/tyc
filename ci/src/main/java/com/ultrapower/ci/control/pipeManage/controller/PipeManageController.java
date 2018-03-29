package com.ultrapower.ci.control.pipeManage.controller;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.common.mail.SendEmailTool;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.common.utils.HttpClientUtils;
import com.ultrapower.ci.common.utils.IDFactory;
import com.ultrapower.ci.common.utils.JenkinsUtils;
import com.ultrapower.ci.common.utils.RandomSix;
import com.ultrapower.ci.control.pipeManage.service.IPipeManageService;
import com.ultrapower.ci.control.taskManage.controller.WebSocketServer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("pipeManage")
public class PipeManageController extends BaseController{

	@Resource
	private IPipeManageService pipeManageService;
	
	/**
	 * 根据项目主键，获取项目的流水线列表
	 * @param req
	 * @param res
	 */
	@RequestMapping("getPipliningListByProjectId")
	public void getPipliningListByProjectId(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,pipeManageService.getPipliningListByProjectId(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	/**
	 * 查询流水线执行历史
	 * @param request
	 * @param response
	 */
	@RequestMapping("queryPipHistoryList")
	public void queryPipHistoryList(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res, pipeManageService.queryPipHistoryList(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 立即执行流水线（循环执行流水线上所有的job）
	 * @param req
	 * @param res
	 */
	@RequestMapping("batchExecution")
	public void batchExecution(HttpServletRequest req,HttpServletResponse res){
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "false";
		String pipId = req.getParameter("pipId");
		String pipName = req.getParameter("pipName");
		String pipVersion = req.getParameter("pipVersion");
		String mail_address = req.getParameter("mail_address");
		pipVersion = String.valueOf(Integer.parseInt(pipVersion) + 1);
		String usernoRand = req.getParameter("usernoRand");
		String projectName = req.getContextPath();
		//流水线开始执行，历史表添加记录，并返回客户端
		String buildHistoryId = IDFactory.getIDStr();
		String buildStartTime = DateTimeUtils.getFormatCurrentTime();
		boolean flag = this.addBuildHistory(buildHistoryId, pipId,  null, null, null, null, null, pipVersion, usernoRand,
				buildStartTime);
		if(!flag){
			return;
		}
		flag = this.runChildPipe(projectName,pipId,null,null, pipName, pipVersion, usernoRand);
		//在构建历史表，修改主流水线的执行情况
		Map<String,String> bhMap = new HashMap<String,String>();
		bhMap.put("buildHistoryId", buildHistoryId);
		bhMap.put("usernoRand", usernoRand);
		bhMap.put("pipId", pipId);
		bhMap.put("pipName", pipName);
		bhMap.put("pipeStatus","4");
		bhMap.put("pipVersion",pipVersion);
		bhMap.put("toAddress",mail_address);
		bhMap.put("buildStartTime", buildStartTime);
		if(flag){//流水线执行成功
			bhMap.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM2);
		}else{//流水线执行失败
			bhMap.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM3);
		}
		this.updateBuildHistory(bhMap);
		
		map.put("result", result);
		writeUTFJson(res,map);
	}
	/**
	 * 循环执行流水线下各个子任务
	 * @param pipId  	流水线主键
	 * @return
	 */
	public boolean runChildPipe(String projectName,String pipId, String parentPipId,String parentPiptask_id,String pipName,String pipVersion,String usernoRand){
		//查询流水线下任务列表
		List<Map<String,String>> taskList = this.getPipeTaskList(pipId);
		boolean flag = false;
		if(taskList != null && taskList.size() > 0){
			for (int i = 0; i < taskList.size(); i++) {
				Map<String,String> taskItem = taskList.get(i);
				String piptask_id = taskItem.get("PIPTASK_ID");
				String task_id = taskItem.get("TASK_ID");
				String task_type = taskItem.get("TASK_TYPE");
				//开始执行子任务，历史表添加记录
				Map<String, String> pipInfo = new HashMap<String, String>();
				String childBuildHistoryId = IDFactory.getIDStr();
				String childBuildStartTime = DateTimeUtils.getFormatCurrentTime();
				flag = this.addBuildHistory(childBuildHistoryId, pipId,parentPipId,parentPiptask_id, piptask_id, task_id, task_type, pipVersion,
						usernoRand,childBuildStartTime);
				if(!flag){
					break;
				}
				if(task_type.equals(TaskConstant.TASK_TYPE_ITEM0)){//子流水线
					String childPipName = "";
					Map<String,String> childPipMap= pipeManageService.getPipeByPipId(pipId);
					if(childPipMap != null){
						childPipName = childPipMap.get("PIP_NAME");
					}
					flag = this.runChildPipe(projectName,task_id,pipId,piptask_id,childPipName, pipVersion, usernoRand);
				}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
					// 执行构建任务
					Map<String, Object> jobInfo = this.runTaskJob(task_id, task_type, usernoRand, pipId, flag,
							parentPipId, parentPiptask_id, piptask_id);
					if(jobInfo != null){
						pipInfo.put("taskVersion", (String)jobInfo.get("taskVersion"));
						pipInfo.put("jobName", (String)jobInfo.get("jobName"));
						if (jobInfo.get("flag").equals(TaskConstant.JOB_BUILD_STATUS_SUCCESS)) {
							pipInfo.put("warName", (String)jobInfo.get("warName"));
						} else {
							flag = false;
						}
					}else{
						flag = false;
					}
					// 存储构建后的产出物
					Map<String, Object> buildInfo = new HashMap<String, Object>();
					buildInfo.put("warName", (String)jobInfo.get("warName"));
					buildInfo.put("projectName", projectName);
					String params[] = {"warName","projectName"}; // 产出物的key
					this.saveBuildHistoryProducts(buildInfo,piptask_id,pipVersion,pipId,params,childBuildHistoryId);
				}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
					flag = true;
					//执行部署任务
					Map<String, Object> jobInfo = this.runDeployJob(piptask_id,task_id,pipVersion,childBuildHistoryId,projectName,pipName,task_type,usernoRand,pipId,parentPipId,parentPiptask_id);
					if(jobInfo != null){
						pipInfo.put("taskVersion", (String)jobInfo.get("taskVersion"));
						pipInfo.put("jobName", (String)jobInfo.get("jobName"));
						if (jobInfo.get("flag").equals(TaskConstant.JOB_BUILD_STATUS_SUCCESS)) {
							flag = true;
							pipInfo.put("deploy_ip", (String)jobInfo.get("deploy_ip"));
							pipInfo.put("deploy_port", (String)jobInfo.get("deploy_port"));
							pipInfo.put("applyUrl", (String)jobInfo.get("applyUrl"));
						} else {
							flag = false;
						}
					}else{
						flag = false;
					}
					
				}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
//					flag = false;
					// 执行自动化任务
					this.runAutoTestTask(piptask_id, task_id, pipVersion, childBuildHistoryId, projectName, pipName,
							task_type, usernoRand, pipId, parentPipId, parentPiptask_id,childBuildStartTime);
				}
				
				pipInfo.put("buildHistoryId", childBuildHistoryId);
				pipInfo.put("pipId", pipId);
				pipInfo.put("parentPipId", parentPipId);
				pipInfo.put("parentPiptask_id", parentPiptask_id);
				pipInfo.put("piptask_id", piptask_id);
				pipInfo.put("pipVersion", pipVersion);
				pipInfo.put("task_type", task_type);
				pipInfo.put("task_id", task_id);
				pipInfo.put("usernoRand", usernoRand);
				pipInfo.put("buildStartTime", childBuildStartTime);
				if(!task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试构建历史信息，有测试平台回调时修改
					if(flag){//流水线任务执行成功
						pipInfo.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM2);
						this.updateBuildHistory(pipInfo);
					}else{//流水线任务执行失败
						pipInfo.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM3);
						this.updateBuildHistory(pipInfo);
						break;
					}
				}
				
			}
		}
		return flag;
	}

	/**
	 * @time 2018-03-014
	 * @author tangyongchun
	 * @description 存储构建后的产出物
	 * @param jobInfo
	 * @param piptask_id
	 * @param pipVersion
	 * @param pipId
	 * @param params
	 * @param childBuildHistoryId 
	 * @return
	 */
	private boolean saveBuildHistoryProducts(Map<String, Object> jobInfo, String piptask_id, String pipVersion,
			String pipId, String[] params, String childBuildHistoryId) {
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("piptaskId", piptask_id);
		paramMap.put("pipVersion", pipVersion);
		paramMap.put("pipId", pipId);
		paramMap.put("childBuildHistoryId", childBuildHistoryId);
		Iterator<String> it = jobInfo.keySet().iterator(); // 获取key值
		int i = 0;
		while (it.hasNext()) {
			String key = it.next();
			paramMap.put("id", IDFactory.getIDStr());
			if (key.equals(params[i])) {
				paramMap.put("paramKey", key);
				paramMap.put("valueKey", jobInfo.get(key));
				flag = pipeManageService.saveBuildHistoryByProducts(paramMap);

				i++;
			}

		}
		return flag;
	}
	
	/**
	 * 获取流水线下子任务列表
	 * @param pipId
	 * @return
	 */
	private List<Map<String,String>> getPipeTaskList(String pipId){
		List<Map<String,String>> list = pipeManageService.getPipeTaskList(pipId);
		return list;
	}
	/**
	 * 流水线（子流水线或任务）开始执行，历史表添加记录，并返回客户端
	 * @param buildHistoryId   	构建历史主键
	 * @param pipId				流水线主键
	 * @param parentPipId		父流水线主键
	 * @param piptask_id		流水线关联任务映射表主键（null表示没有关联任务，为主流水线本身）
	 * @param task_id			任务主键
	 * @param task_type			任务类型
	 * @param pipVersion		当前流水线版本号
	 * @param usernoRand		客户端唯一标识
	 * @return
	 */
	private boolean addBuildHistory(String buildHistoryId, String pipId, String parentPipId, String parentPiptask_id,
			String piptask_id, String task_id, String task_type, String pipVersion, String usernoRand,
			String buildStartTime) {
		boolean flag = false;
//		String buildStartTime = DateTimeUtils.getFormatCurrentTime();
		Map<String,String> pmap = new HashMap<String,String>();
		pmap.put("id", buildHistoryId);
		pmap.put("pipId", pipId);
		pmap.put("piptask_id", piptask_id);
		pmap.put("pipVersion", pipVersion);
		pmap.put("buildStartTime", buildStartTime);
		pmap.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM0);
		pmap.put("buildUser", "admin");
		pmap.put("parentPipId", parentPipId);
		int k = pipeManageService.addBuildHistory(pmap);
		if(k > 0){
			flag = true;
			String jsonInfo = "";
			if(piptask_id == null){//主流水线开始执行
				jsonInfo = "{\"pipeStatus\" : \"0\",\"pipId\":\"" + pipId + "\",\"buildStartTime\" : \"" + buildStartTime + "\"}";
			}else{//子任务开始执行
				if(parentPipId != null){//该任务为子流水线
					jsonInfo = "{\"pipeStatus\" : \"1\",\"pipId\":\"" + parentPipId + "\",\"piptask_id\" : \""
							+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \"" + TaskConstant.TASK_TYPE_ITEM0
							+ "\",\"buildStartTime\" : \"" + buildStartTime + "\"}";
				}else{//该任务不是子流水线
					jsonInfo = "{\"pipeStatus\" : \"1\",\"pipId\":\"" + pipId + "\",\"piptask_id\" : \""
							+ piptask_id + "\",\"task_id\" : \"" + task_id + "\",\"task_type\" : \"" + task_type
							+ "\",\"buildStartTime\" : \"" + buildStartTime + "\"}";
				}
			}
			WebSocketServer.sendAllClient(jsonInfo, usernoRand);
		}
		return flag;
	}
	
	/**
	 * 修改构建历史表流水线（任务）执行状态
	 * @param bhMap
	 * @return
	 */
	public boolean updateBuildHistory(Map<String,String> bhMap){
		boolean flag = false;
		String buildStartTime = bhMap.get("buildStartTime");//流水线任务开始时间
		String buildEndTime = DateTimeUtils.getFormatCurrentTime();//流水线任务结束时间
		String buildTotalTime = "";
		if (buildStartTime != null && !buildStartTime.equals("")) {
			buildTotalTime = String.valueOf(
					DateTimeUtils.stringToTimeStamp(buildEndTime) - DateTimeUtils.stringToTimeStamp(buildStartTime));
		}
		bhMap.put("buildEndTime", buildEndTime);
		bhMap.put("buildTotalTime", buildTotalTime);
		int k = pipeManageService.updateBuildHistory(bhMap);
		buildTotalTime = DateTimeUtils.timeStampToSecond(Long.valueOf(buildTotalTime));
		if(k > 0){
			flag = true;
			String buildStatus = "";
			if(bhMap.get("buildStatus").equals(TaskConstant.JOB_RESULTS_ITEM2)){//任务执行成功
				buildStatus = "2";
			}else if(bhMap.get("buildStatus").equals(TaskConstant.JOB_RESULTS_ITEM3)){//任务执行失败
				buildStatus = "3";
			}
			String resultJson = "";
			bhMap.put("buildStatus", buildStatus);
			String taskVersion = bhMap.get("taskVersion");
			String jobName = bhMap.get("jobName");
			String warName = bhMap.get("warName");
			
			String deploy_ip = bhMap.get("deploy_ip");
			String deploy_port = bhMap.get("deploy_port");
			String applyUrl = bhMap.get("applyUrl");
			
			if(bhMap.get("pipeStatus") != null && bhMap.get("pipeStatus").equals("4")){//主流水线执行结束
				//邮件推送
				boolean sendEmailFlag = this.sendEmail(bhMap);
				String mail_address = bhMap.get("toAddress");
				if(!sendEmailFlag){
					mail_address = mail_address + "（发送失败！）";
				}
				resultJson = "{\"pipeStatus\" : \"4\",\"pipId\":\"" + bhMap.get("pipId") 
				+ "\",\"buildStartTime\" : \"" + buildStartTime + "\",\"buildEndTime\" : \"" + buildEndTime 
				+ "\",\"buildTotalTime\" : \"" + buildTotalTime + "\",\"pipVersion\" : \"" + bhMap.get("pipVersion") 
				+ "\",\"mail_address\" : \"" + mail_address + "\"}";
			}else{
				if(bhMap.get("parentPipId") != null){//该任务为子流水线
					resultJson = "{\"pipeStatus\" : \"3\",\"pipId\" : \"" + bhMap.get("parentPipId") 
					+ "\",\"piptask_id\" : \"" + bhMap.get("parentPiptask_id") + 
					"\",\"childPiptask_id\" : \"" + bhMap.get("piptask_id") + "\",\"task_type\" : \"" 
					+ TaskConstant.TASK_TYPE_ITEM0 + "\",\"childTaskType\" : \"" + bhMap.get("task_type") + "\",\"buildStatus\" : \"" + buildStatus + 
					"\",\"buildStartTime\" : \"" + buildStartTime + "\",\"buildEndTime\" : \"" + buildEndTime 
					+ "\",\"buildTotalTime\" : \"" + buildTotalTime + "\",\"pipVersion\" : \"" + bhMap.get("pipVersion") 
					+ "\",\"taskVersion\" : \"" + taskVersion + "\",\"jobName\" : \"" + jobName + "\",\"warName\" : \"" + warName 
					+ "\",\"deploy_ip\" : \"" + deploy_ip + "\",\"deploy_port\" : \"" + deploy_port + "\",\"applyUrl\" : \"" + applyUrl + "\"}";
				}else{//该任务不是子流水线
					resultJson = "{\"pipeStatus\" : \"3\",\"pipId\" : \"" + bhMap.get("pipId") 
					+ "\",\"piptask_id\" : \"" + bhMap.get("piptask_id") + 
					"\",\"task_id\" : \"" + bhMap.get("task_id") + "\",\"task_type\" : \"" 
					+ bhMap.get("task_type") + "\",\"buildStatus\" : \"" + buildStatus + 
					"\",\"buildStartTime\" : \"" + buildStartTime + "\",\"buildEndTime\" : \"" + buildEndTime 
					+ "\",\"buildTotalTime\" : \"" + buildTotalTime + "\",\"pipVersion\" : \"" + bhMap.get("pipVersion") 
					+ "\",\"taskVersion\" : \"" + taskVersion + "\",\"jobName\" : \"" + jobName + "\",\"warName\" : \"" + warName 
					+ "\",\"deploy_ip\" : \"" + deploy_ip + "\",\"deploy_port\" : \"" + deploy_port + "\",\"applyUrl\" : \"" + applyUrl + "\"}";
				}
			}
			WebSocketServer.sendAllClient(resultJson, bhMap.get("usernoRand"));
		}
		return flag;
	}
	
	/**
	 * @time 2018-03-12
	 * @author tangyongchun
	 * @param usernoRand 
	 * @param flag 
	 * @param pipId 
	 * @param piptask_id 
	 * @param parentPiptask_id 
	 * @param parentPipId 
	 * @description 调用jenkins的api执行job 
	 * @return
	 */
	private Map<String, Object> runTaskJob(String task_id,String task_type, String usernoRand, String pipId, boolean flag, String parentPipId, String parentPiptask_id, String piptask_id) {
		Map<String, Object> jobInfo = null;
		try {
			String jobName = this.jobName(task_id, task_type);// 获取任务名称
			jobInfo = JenkinsUtils.runJob(jobName);// 执行job
			if (!jobInfo.isEmpty()) {
				String taskVersion = jobInfo.get("buildNumber").toString();// 获取版本号
				StringBuffer sb = new StringBuffer();
				sb.append(ApplyConstant.JOB_LOG_PATH).append(jobName).append("/builds/").append(taskVersion).append("/log");
				File logfile = new File(sb.toString()); 
				while(!logfile.exists()) {
					
				}
				// 获取job日志信息
				Map<String, String> logInfo= this.getJenkinsLogs(logfile,usernoRand,task_type,pipId,flag,task_id,jobName,parentPipId,parentPiptask_id,piptask_id);// 获取jenkins实时的日志
				if (!logInfo.isEmpty()) {
					jobInfo.put("testResult", logInfo.get("testResult"));
					jobInfo.put("warName", logInfo.get("warName"));
				}
				
				jobInfo.put("taskVersion", taskVersion);
				jobInfo.put("jobName", jobName);
				// 获取执行后的job信息
				Map<String, Object> maps= JenkinsUtils.jobInfo(jobName, Integer.parseInt(taskVersion));
				jobInfo.put("flag", maps.get("flag"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobInfo;
	}
	
	/**
	 * @time 2018-03-13
	 * @author tangyongchun
	 * @description 执行部署任务
	 * @param task_id
	 * @param pipVersion
	 * @param childBuildHistoryId
	 * @param projectName
	 * @param pipName
	 * @param pipId 
	 * @param usernoRand 
	 * @param task_type 
	 * @return
	 */
	private Map<String, Object> runDeployJob(String piptask_id,String task_id, String pipVersion, String childBuildHistoryId,
			String projectName, String pipName, String task_type, String usernoRand, String pipId, String parentPipId,
			String parentPiptask_id) {
		// 调cass平台接口申请机器
		boolean flag = this.applicationMachine(task_id,pipVersion,childBuildHistoryId,projectName,pipName,pipId);
		String jsonInfo = ""; // 实时向前台打印的json字符串
		String applyUrl = ""; // 部署应用的url
		Map<String, Object> jobInfo = new HashMap<String, Object>();
		if (flag) {
			if (parentPipId != null) {// 该流水线为子流水线
				jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + parentPipId + "\",\"piptask_id\" : \""
						+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \""
						+ TaskConstant.TASK_TYPE_ITEM0 + "\",\"wsKey\":\"申请环境成功\",\"wsValue\":\"\"}";
				WebSocketServer.sendAllClient(jsonInfo, usernoRand);
			} else {
				jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + pipId + "\",\"piptask_id\" : \""
						+ piptask_id + "\",\"taskId\":\"" + task_id + "\",\"task_type\" : \"" + task_type
						+ "\",\"wsKey\":\"申请环境成功\",\"wsValue\":\"\"}";
				WebSocketServer.sendAllClient(jsonInfo, usernoRand);
			}
			// 获取访问应用的url
			applyUrl = this.getApplyUrl(task_id,childBuildHistoryId,task_type);
			// 存储构建历史的产出物
			String params[] = {"deploy_ip","deploy_port","applyUrl"}; // 产出物的key
			String ipAndPort = applyUrl.substring(applyUrl.indexOf("://")+3);
			String ip=ipAndPort.substring(0, ipAndPort.indexOf(":"));
			String port = ipAndPort.substring(ipAndPort.indexOf(":") + 1, ipAndPort.indexOf("/"));
			jobInfo.put("deploy_ip", ip);
			jobInfo.put("deploy_port", port);
			jobInfo.put("applyUrl", applyUrl);
			flag = this.saveBuildHistoryProducts(jobInfo,piptask_id,pipVersion,pipId,params,childBuildHistoryId);
			// 执行任务
			Map<String , Object> deployInfo= this.runTaskJob(task_id,task_type,usernoRand,pipId,flag,parentPipId,parentPiptask_id,piptask_id);
			jobInfo.put("jobName", deployInfo.get("jobName"));
			jobInfo.put("flag", deployInfo.get("flag"));
			jobInfo.put("buildNumber", deployInfo.get("buildNumber"));
			jobInfo.put("taskVersion", deployInfo.get("taskVersion"));
		}else {
			if (parentPipId != null) {// 该流水线为子流水线
				jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + parentPipId + "\",\"piptask_id\" : \""
						+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \"" + TaskConstant.TASK_TYPE_ITEM0
						+ "\",\"childTaskType\" : \"" + task_type + "\",\"buildStartTime\":\""+""+"\",\"status\" : \"failure\",\"message\" : \"申请环境失败\"}";
			}else{
				jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + pipId + "\",\"piptask_id\" : \""
						+ piptask_id + "\",\"taskId\":\""+ task_id + "\",\"task_type\" : \"" + task_type
						+ "\",\"buildStartTime\":\""+""+"\",\"status\" : \"failure\",\"message\" : \"申请环境失败\"}";
			}
			WebSocketServer.sendAllClient(jsonInfo , usernoRand);
		}
		jobInfo.put("applyUrl", applyUrl);
		return jobInfo;
	}
	
	/**
	 * @time 2018-03-13
	 * @author tangyongchun
	 * @description 执行自动化测试
	 * @param piptask_id
	 * @param task_id
	 * @param pipVersion
	 * @param childBuildHistoryId
	 * @param projectName
	 * @param pipName
	 * @param task_type
	 * @param usernoRand
	 * @param pipId
	 * @param parentPipId
	 * @param parentPiptask_id
	 */
	private void runAutoTestTask(String piptask_id, String task_id, String pipVersion, String childBuildHistoryId,
			String projectName, String pipName, String task_type, String usernoRand, String pipId, String parentPipId,
			String parentPiptask_id,String buildStartTime) {
		try {
			String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + pipId + "\",\"taskId\":\"" + task_id
					+ "\",\"wsKey\":\"开始自动化测试\",\"wsValue\":\"\"}";
			WebSocketServer.sendAllClient(jsonInfo, usernoRand);

			boolean flag = runAutoTest(projectName, task_id, pipName, pipVersion, childBuildHistoryId, pipId,
					usernoRand, task_type);
			if (flag) {
				Map<String, Object> autoTestMap = new HashMap<String, Object>();
				// 循环查询自动化测试平台是否已经回调过了
				for (int i = 0; i < 10000; i++) {
					// 查询自动化测试历史
					autoTestMap = pipeManageService.getAutoTestHistory(piptask_id, pipVersion);
					if (autoTestMap != null) {
						if (TaskConstant.JOB_RESULTS_ITEM2.equals((String)autoTestMap.get("BUILD_STATUS"))) {
							String buildStatus = autoTestMap.get("BUILD_STATUS") != null ? (String) autoTestMap.get("BUILD_STATUS") : "";
							long buildTotalTime = autoTestMap.get("BUILD_TOTAL_TIME") != null ? Long.parseLong((String) autoTestMap.get("BUILD_TOTAL_TIME")) : 0;
							String applyUrl = autoTestMap.get("APPLY_URL") != null ? (String) autoTestMap.get("APPLY_URL") : "";
							String buildEndTime = autoTestMap.get("BUILD_END_TIME") != null ? (String) autoTestMap.get("BUILD_END_TIME") : "";
							String buildTotalTimeStr = DateTimeUtils.timeStampToSecond(Long.valueOf(buildTotalTime));
							if (parentPipId != null) {
								jsonInfo = "{\"pipeStatus\":\"3\",\"pipId\":\"" + parentPipId + "\",\"taskId\":\"" + task_id
										+ "\",\"piptask_id\" : \""+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \"" + TaskConstant.TASK_TYPE_ITEM0
										+ "\",\"buildStartTime\":\"" + buildStartTime + "\",\"buildStatus\":\"" + buildStatus
										+ "\",\"buildEndTime\":\"" + buildEndTime + "\",\"applyUrl\":\"" + applyUrl
										+ "\",\"status\":\"success\",\"buildTotalTime\":\"" + buildTotalTimeStr
										+ "\",\"wsKey\":\"自动化测试完成\",\"wsValue\":\"\"}";
							}else {
								jsonInfo = "{\"pipeStatus\":\"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\"" + task_id
										+ "\",\"piptask_id\" : \""+ piptask_id + "\",\"task_type\" : \"" + task_type
										+ "\",\"buildStartTime\":\"" + buildStartTime + "\",\"buildStatus\":\"" + buildStatus
										+ "\",\"buildEndTime\":\"" + buildEndTime + "\",\"applyUrl\":\"" + applyUrl
										+ "\",\"status\":\"success\",\"buildTotalTime\":\"" + buildTotalTimeStr
										+ "\",\"wsKey\":\"自动化测试完成\",\"wsValue\":\"\"}";
							}
							WebSocketServer.sendAllClient(jsonInfo, usernoRand);
							break;
						}
					}
					Thread.sleep(1000);
				}
			} else {
				if (parentPipId != null) {
					jsonInfo = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + parentPipId + "\",\"taskId\":\"" + task_id
							+ "\",\"piptask_id\" : \""+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \"" + TaskConstant.TASK_TYPE_ITEM0
							+ "\",\"buildStartTime\":\"" + buildStartTime
							+ "\",\"status\":\"failure\",\"wsKey\":\"自动化测试失败\",\"wsValue\":\"\"}";
				}else {
					jsonInfo = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\"" + task_id
							+ "\",\"piptask_id\" : \""+ piptask_id + "\",\"task_type\" : \"" + task_type
							+ "\",\"buildStartTime\" : \"" + buildStartTime + "\",\"status\":\"failure\",\"wsKey\":\"自动化测试失败\",\"wsValue\":\"\"}";
				}
				WebSocketServer.sendAllClient(jsonInfo, usernoRand);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @time 2018-03-12
	 * @author tangyongchun
	 * @descriptiion 查询job名称
	 * @return
	 */
	private String jobName(String task_id, String task_type) {

		return pipeManageService.getTaskInfo(task_id, task_type);
	}
	
	/**
	 * @time 2018-03-12
	 * @author tangyongchun
	 * @description 获取jenkins日志
	 * @param logfile
	 * @param usernoRand
	 * @param flag 
	 * @param pipId 
	 * @param taskId 
	 * @param jobName 
	 * @param piptask_id 
	 * @param parentPiptask_id 
	 * @param parentPipId 
	 */
	private Map<String, String> getJenkinsLogs(File logfile, String usernoRand,String taskType, String pipId, boolean flag, String taskId, String jobName, String parentPipId, String parentPiptask_id, String piptask_id) {
		Map<String, String> logInfo = new HashMap<String, String>();
		try {
			String testResult = "";
			String warName = "";
			String wsKey = "";
			long filePointer = logfile.length();
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			while (flag) {  
				long fileLength = logfile.length();
				if (fileLength < filePointer) {
					file = new RandomAccessFile(logfile, "r");
					filePointer = 0;
				}
				if (fileLength > filePointer) {
					file.seek(filePointer);
					String line = new String(file.readLine().getBytes("iso8859-1"), "utf-8");
					while (line != null) {
						
						if (TaskConstant.TASK_TYPE_ITEM1.equals(taskType)) {
							if (line.indexOf("Started by user") != -1) {
								wsKey = "开始下载源码";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("ERROR: Subversion") != -1 || line.indexOf("ERROR: Failed") != -1) {
								wsKey = "源码下载失败";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								flag = false;
							}
							if (line.indexOf("clean compile") != -1) {
								wsKey = "源码下载成功";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								Thread.sleep(1000);// 用于前台显示日志
								
								wsKey = "开始编译源码";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("clean package") != -1) {
								wsKey = "源码编译成功";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("COMPILATION ERROR") != -1) {
								wsKey = "源码编译失败";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("T E S T S") != -1) {
								wsKey = "开始单元测试";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("Results :") != -1) {
								wsKey = "单元测试完成";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("Failures") != -1 && line.indexOf("Errors") != -1 && line.indexOf("Skipped") != -1 && line.indexOf("Time elapsed") == -1) {
								
								if (line.indexOf("Failures: 0") != -1 && line.indexOf("Errors: 0") != -1) {
									wsKey = "单元测试：";
									testResult = "成功";
									this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,testResult,"");
								}else {
									wsKey = "单元测试：";
									testResult =line.substring(line.indexOf("Failures"), line.indexOf(", Skipped"));
									this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,testResult,"");
								}
								
							}
							if (line.indexOf("test failures") != -1) {
								wsKey = "单元测试：";
								testResult = "单元测试失败";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,testResult,"");
								flag = false;
							}
							if (line.indexOf("Packaging webapp") != -1) {
								wsKey = "开始打war包";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("Finished: SUCCESS") != -1) {
								wsKey = "打war包成功";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								
								warName = JenkinsUtils.getWarName(jobName);
								logInfo.put("projectName", warName); // 应用名称
								warName = warName +".war";
								flag = false;
							}
							if (line.indexOf("Finished: FAILURE") != -1) {
								wsKey = "构建失败";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								flag = false;
							}
							
							logInfo.put("testResult", testResult);
							logInfo.put("warName", warName);// war名称
						}
						
						if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {
							if (line.indexOf("Started by user") != -1) {
								wsKey = "开始部署war包";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
							}
							if (line.indexOf("Finished: SUCCESS") != -1) {
								wsKey = "部署war包完成";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								flag = false;
							}
							if (line.indexOf("Finished: FAILURE") != -1) {
								wsKey = "部署war包失败";
								this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"","");
								flag = false;
							}
						}
						// 实时向前台返job执行日志
//						this.sendJsonInfo(pipId,taskId,wsKey,usernoRand,parentPipId,parentPiptask_id,piptask_id,"",line);
						line = null;  
						filePointer = file.getFilePointer();  
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logInfo;
	}
	
	/**
	 * @time 2018-03-13
	 * @author tangyongchun
	 * @description 使用webSocket向前端发送信息
	 * @param pipId
	 * @param taskId
	 * @param wsKey
	 * @param usernoRand
	 * @param piptask_id 
	 * @param parentPiptask_id 
	 * @param parentPipId 
	 * @param logInfo 
	 * @param wsValue 
	 */
	private void sendJsonInfo(String pipId, String task_id, String wsKey, String usernoRand, String parentPipId, String parentPiptask_id, String piptask_id, String wsValue, String logInfo) {
		String jsonInfo = "";
		if (parentPipId != null) {// 该流水线为子流水线
			jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + parentPipId + "\",\"piptask_id\" : \""
					+ parentPiptask_id + "\",\"childPiptask_id\" : \"" + piptask_id + "\",\"task_type\" : \"" + TaskConstant.TASK_TYPE_ITEM0
					+ "\",\"wsKey\" : \""+wsKey+"\",\"wsValue\":\"\"}";
		}else{
			jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + pipId + "\",\"piptask_id\" : \""
					+ piptask_id + "\",\"taskId\":\""+ task_id + "\",\"task_type\" : \"" + ""
					+ "\",\"wsKey\" : \""+wsKey+"\",\"wsValue\":\""+wsValue+"\",\"logInfo\":\""+logInfo+"\"}";
		}
		WebSocketServer.sendAllClient(jsonInfo , usernoRand);
	}
	
	/**
	 * @time 2018-03-12
	 * @author tangyongchun
	 * @description 调用cass平台申请机器
	 * @param task_id
	 * @param pipVersion
	 * @param pipName 
	 * @param projectName 
	 * @param pipId 
	 * @param childBuildHistoryId
	 * @return
	 */
	private boolean applicationMachine(String taskId, String pipVersion, String jobHistoryId, String projectName, String pipName, String pipId) {
		boolean flag = false;
		try {
			String localhostPath = ApplyConstant.LOCATION_TOMCAT_IP +":"+ ApplyConstant.LOCATION_TOMCAT_PORT;
			String callbackUrl = localhostPath + projectName +"/taskManage/addTomcatServer.do";
			String caas_ip = ApplyConstant.CAAS_IP;
			String caas_port = ApplyConstant.CAAS_PORT;
//			String url = caas_ip + ":" + caas_port + "/szty-web/v2/app/createContainer.do";
			String url = caas_ip + ":" + caas_port + "/szty-web/v2/api/app/createContainer.do";
			Map<String, Object> temMap=new HashMap<String, Object>();
			temMap.put("appName",pipName);
			temMap.put("appVersion",pipVersion);
			temMap.put("evnId","appdemo");
			temMap.put("appDescription","应用描述");
			
			// 容器端口信息
			List<Map<String, Object>> ports = new ArrayList<Map<String, Object>>();
			Map<String, Object> portsInfo = new HashMap<String, Object>();
			portsInfo.put("mainPort", 0);
			portsInfo.put("containerPort", 8080);
			portsInfo.put("protocol", "TCP");
			ports.add(portsInfo);
			
			// 获取产出物
//			List<Map<String, Object>> listEvn = this.getBuildHistoryProducts(pipId,pipVersion);
			List<Map<String, Object>> listEvn = new ArrayList<Map<String, Object>>();
			Map<String, Object> evnMap = new HashMap<String, Object>();
			evnMap.put("envName", "JENKINS_HOST");
			evnMap.put("envVal", "192.168.26.93");
			Map<String, Object> evnMap1 = new HashMap<String, Object>();
			evnMap1.put("envName", "JENKINS_PORT");
			evnMap1.put("envVal", "8888");
			listEvn.add(evnMap);
			listEvn.add(evnMap1);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ports", JSONArray.fromObject(ports));
			params.put("envs", JSONArray.fromObject(listEvn));
			params.put("name", RandomSix.randomSix());// 生成六位随机数的名字
			params.put("esName", "devops");
			params.put("proName", "tomcat");
			params.put("projectName", "tomcat公共仓库");
			params.put("esEdition", "1.5.1");
			params.put("containerNum", "1");
			params.put("imageName", "");
			params.put("harborPath", "");
			temMap.put("services",JSONArray.fromObject(params));
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("appKey", "");
			map.put("random", taskId +";"+ jobHistoryId);
			map.put("jsonData", JSONObject.fromObject(temMap).toString());
			map.put("callbackUrl", callbackUrl);
			map.put("sig", "");
			flag = HttpClientUtils.doPost(url, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @time 2018-03-13
	 * @author tangyongchun
	 * @description 调用自动化测试接口并执行
	 * @param projectName
	 * @param task_id
	 * @param pipName
	 * @param pipVersion
	 * @param childBuildHistoryId
	 * @param usernoRand 
	 * @param pipId 
	 * @param taskType 
	 * @return
	 */
	private boolean runAutoTest(String projectName, String taskId, String pipName, String pipVersion,String childBuildHistoryId, String pipId, String usernoRand, String taskType) {
		boolean flag = false;
		try {
			String localhostPath = ApplyConstant.LOCATION_TOMCAT_IP +":"+ ApplyConstant.LOCATION_TOMCAT_PORT;
			String callbackUrl = localhostPath + projectName +"/taskManage/saveAutoTestHistory.do";
			// 回调获取自动化测试日志
			String callbackUrlLog = localhostPath + projectName + "/taskManage/autoTestLogByWs.do";
			String autoTest_ip = ApplyConstant.AUTOTEST_IP;
			String autoTest_port = ApplyConstant.AUTOTEST_PORT;
			String url = autoTest_ip + ":" + autoTest_port + "/atp/service/rest/v1/tasks/operate";
			
			//查询该流水线历史版本中部署任务关联的节点IP和端口
			Map<String, Object> taskMap = pipeManageService.getDeployIpAndPortByUrl(pipId,pipVersion,TaskConstant.TASK_TYPE_ITEM2);
			if (taskMap != null) {
				String nodeIp = (String)taskMap.get("NODE_IP");
				String nodePort = (String)taskMap.get("NODE_PORT");
				String autoTestId = this.jobName(taskId, taskType);// 自动化测试任务id
				
				JSONObject paramJson = new JSONObject();
				String[] autoTestTaskId = {autoTestId}; 
				paramJson.put("userName", "niewei1");
				paramJson.put("taskId", autoTestTaskId);
				paramJson.put("applyUrl", nodeIp +":"+ nodePort);
				paramJson.put("operate", "execute");
				paramJson.put("callbackUrl", callbackUrl);
				JSONObject callbackParam = new JSONObject();
				callbackParam.put("id", childBuildHistoryId);
				callbackParam.put("pipId", pipId);
				callbackParam.put("pipName", pipName);
				callbackParam.put("usernoRand",usernoRand);
				callbackParam.put("callbackUrlLog", callbackUrlLog);
				paramJson.put("callbackParam", callbackParam.toString());
	 			
				String responseParam = HttpClientUtils.doPostJson(url,paramJson.toString());
				paramJson.clear();
				paramJson =JSONObject.fromObject(responseParam);
				// 获取请求成功的状态
				String code = (String)paramJson.get("code");
				if ("200".equals(code)) {
					flag = true;
				}else {
					flag = false;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @time 2018-03-13
	 * @author tangyongchun
	 * @description 获取访问应用的url
	 * @param task_id
	 * @param childBuildHistoryId
	 * @param taskType 
	 * @return
	 */
	private String getApplyUrl(String taskId, String childBuildHistoryId, String taskType) {
		StringBuffer url = new StringBuffer();
		String warName = "";// war名称
		try {
			// 查询构建job的名称
			String buildName = pipeManageService.getBuildByJobName(taskId);
			if (!"".equals(buildName)) {
				// 获取war包名称
				warName = JenkinsUtils.getWarName(buildName);
			}
			if (!"".equals(warName)) {
				String node_ip = ""; // 节点ip
				String node_port = "";// 节点端口
				// 获取部署的节点环境
				List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
				boolean flag = false;
				for (int i = 0; i < 600; i++) {// 查询节点信息直到有跳出循环
					nodeList = pipeManageService.getNodeInfo(childBuildHistoryId);
					if (nodeList != null && nodeList.size() > 0) {
						for (int k = 0; k < nodeList.size(); k++) {
							Map<String, Object> nodes = nodeList.get(k);
							if (nodes != null && nodes.get("NODE_IP") != null && nodes.get("NODE_PORT") != null) {
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						break;
					}
					Thread.sleep(1000);
				}
				if (nodeList != null && nodeList.size() > 0) {
					for (int j = 0; j < nodeList.size(); j++) {
						Map<String, Object> nodes = nodeList.get(j);
						if (nodes.get("NODE_IP") != null && nodes.get("NODE_PORT") != null) {
							node_ip = nodes.get("NODE_IP").toString();
							node_port = nodes.get("NODE_PORT").toString();
							url.append("http://");
							url.append(node_ip);
							url.append(":");
							url.append(node_port);
							url.append("/");
							url.append(warName);
							url.append(";");
						}
					}
					if (url.length() > 0) {
						url.deleteCharAt(url.length() - 1);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return url.toString();
	}
	/**
	 * 新建流水线组
	 * @param req
	 * @param res
	 * @author mxx
	 */
	@RequestMapping("addPipeliningGroup")
	public void addPipelining(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, pipeManageService.addPipeliningGroup(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @time 2018-03-15
	 * @author tangyongchun
	 * @description 查询构建后的产出物
	 * @param pipId
	 * @param pipVersion
	 * @return
	 */
	private List<Map<String, Object>> getBuildHistoryProducts(String pipId, String pipVersion) {
		
		return pipeManageService.getBuildHistoryProducts(pipId,pipVersion);
	}
	/**
	 * @author mxx
	 * @description 根据流水线id查询它是否有构建任务或部署任务
	 * @return
	 */
	@RequestMapping("queryProduct")
	public void queryProduct(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,pipeManageService.queryProduct(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询流水线组信息
	 * @author mxx
	 * @param req
	 * @param res
	 */
	@RequestMapping("pipGroupInfo")
	public void pipGroupInfo(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, pipeManageService.pipGroupInfo(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改流水线组
	 * @author mxx
	 * @param req
	 * @param res
	 */
	@RequestMapping("updatePipeliningGroup")
	public void updatePipeliningGroup(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, pipeManageService.updatePipeliningGroup(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean sendEmail(Map<String,String> map){
		StringBuffer pipContents = new StringBuffer();
		StringBuffer buildContents = new StringBuffer();
		StringBuffer deployContents = new StringBuffer();
		String buildTotalTime = map.get("buildTotalTime");
		buildTotalTime = DateTimeUtils.timeStampToSecond(Long.valueOf(buildTotalTime));
		pipContents.append("<tr><td>流水线信息</td></tr>")
		.append("<tr><td>开始时间："+map.get("buildStartTime")+"</td></tr>")
		.append("<tr><td>结束时间："+map.get("buildEndTime")+"</td></tr>")
		.append("<tr><td>耗时："+ buildTotalTime +"</td></tr>")
		.append("<tr><td>状态："+map.get("buildStatus")+"</td></tr>");
		buildContents.append("<tr><td>构建任务信息</td></tr>");
		
		deployContents.append("<tr><td>部署任务信息</td></tr>");
		map.put("pipContents", pipContents.toString());
		map.put("buildContents", buildContents.toString());
		map.put("deployContents", deployContents.toString());
		
		String pathHeader = PipeManageController.class.getResource("/").getPath();
		int Q = pathHeader.indexOf("/");// 找到第一个位置
		int E = pathHeader.indexOf("/WEB-INF/classes");// 找到最后一个位置
		pathHeader = pathHeader.substring(Q+1, E);
		String path = pathHeader + "/config/";
		map.put("path", path);
		
		boolean flag = SendEmailTool.sendEmail(map);
		return flag;
	};
}
