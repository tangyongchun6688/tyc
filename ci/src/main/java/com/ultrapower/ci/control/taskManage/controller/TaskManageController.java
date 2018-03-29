package com.ultrapower.ci.control.taskManage.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.common.mail.SendEmailTool;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.common.utils.JenkinsUtils;
import com.ultrapower.ci.common.utils.RequestUtils;
import com.ultrapower.ci.control.autoTestManage.service.IAutoTestService;
import com.ultrapower.ci.control.buildManage.service.IBuildService;
import com.ultrapower.ci.control.deployManage.service.IDeployService;
import com.ultrapower.ci.control.taskManage.service.ITaskManageService;

@Controller
@RequestMapping("taskManage")
public class TaskManageController extends BaseController{

	private static final Logger logger = Logger.getLogger(TaskManageController.class);
	
	@Resource
	private ITaskManageService taskManageService;
	@Resource
	private IBuildService buildService;
	@Resource
	private IDeployService deployService;
	@Resource
	private IAutoTestService autoTestService;
	/**
	 * 查询流水线列表
	 * @param req
	 * @param res
	 */
	@RequestMapping("getPipliningList")
	public void getPipliningList(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,taskManageService.getPipliningList(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @time 2017-12-07
	 * @author tangyongchun
	 * @description 存储流水线记录信息接口
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="savePipHistory",method=RequestMethod.POST)
	public void savePipHistory(HttpServletRequest request,HttpServletResponse response){
		try {
			// 获取参数
			Map<String, Object> maps = RequestUtils.getParameters(request);
			
			writeUTFJson(response, taskManageService.savePipHistory(maps));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @time 2017-12-08
	 * @author tangyongchun
	 * @description 执行单个job接口
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="executeSingleJob",method=RequestMethod.POST)
	public void executeSingleJob(HttpServletRequest request, HttpServletResponse response){
		System.out.println("executeSingleJob.................................................开始");
		try {
			boolean flag = false; // 校验操作
			// 获取参数
			Map<String, Object> maps = RequestUtils.getParameters(request);
			
			String usernoRand = (String) maps.get("usernoRand");
			maps.put("projectName", request.getContextPath());
			// 任务类型
			String taskType = "";
			if (maps.get("taskType") != null && !"".equals(maps.get("taskType"))) {
				taskType= maps.get("taskType").toString();
			} 
			/*if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {// 判断是否为部署操作
				// 清空节点表中的ip及port
				taskManageService.emptyNodeInfo(maps.get("taskId").toString());
			}*/
			// 执行job及操作数据库
			Map<String, Object> map = taskManageService.updateSingleJobHistory(maps);
			System.out.println("executeSingleJob.................................................结束");
			// 自动化测试操作
			if (TaskConstant.TASK_TYPE_ITEM3.equals(taskType)) {
				String buildStartTime = maps.get("buildStartTime") !=null ?maps.get("buildStartTime").toString() : "";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("pipStepTaskId", maps.get("pipStepTaskId"));
				params.put("pipVersion", maps.get("pipVersion"));
				if (map != null && map.get("status") != null) {
					String status = (String) map.get("status");
					if (status.equals("success")){
						flag = (boolean) map.get("flag");
						if (flag) {
							Map<String, Object> autoTestMap = new HashMap<String, Object>();
							// 循环查询自动化测试平台是否已经回调过了
							for (int i = 0; i < 10000; i++) {
								// 查询自动化测试历史
								autoTestMap = taskManageService.getAutoTestHistory(params);
								if (autoTestMap != null) {
									String buildStatus = autoTestMap.get("BUILD_STATUS") != null ? (String)autoTestMap.get("BUILD_STATUS") : "";
									long buildTotalTime = autoTestMap.get("BUILD_TOTAL_TIME") != null ? Long.parseLong((String) autoTestMap.get("BUILD_TOTAL_TIME")): 0;
									String applyUrl = autoTestMap.get("APPLY_URL") != null ?  (String)autoTestMap.get("APPLY_URL"): "";
									String buildEndTime = autoTestMap.get("BUILD_END_TIME") != null ? (String)autoTestMap.get("BUILD_END_TIME") : "";
									map.put("buildStatus", buildStatus);
									map.put("buildTotalTime", buildTotalTime);
									map.put("taskVersion", autoTestMap.get("TASK_VERSION") != null ? Integer.parseInt((String)autoTestMap.get("TASK_VERSION")): 0);
									map.put("applyUrl", applyUrl);
									map.put("buildEndTime", buildEndTime);
									if(buildStatus.equals("2")){
										map.put("message", "执行成功！");
									}else{
										map.put("message", "执行失败！");
									}
									String jsonInfo = "{\"pipeStatus\":\"3\",\"pipId\":\""+maps.get("pipId")+"\",\"taskId\":\""+maps.get("taskId")+"\",\"buildStartTime\":\""+buildStartTime+"\",\"buildStatus\":\"" +buildStatus+"\",\"buildEndTime\":\"" +buildEndTime+"\",\"status\":\""+status+"\",\"buildTotalTime\":\""+ buildTotalTime+"\",\"wsKey\":\"自动化测试完成\",\"wsValue\":\"\"}";
									WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									break;
								}
								Thread.sleep(1000);
							}
						}
					}else {
						String jsonInfo = "{\"pipeStatus\" : \"3\",\"pipId\":\""+maps.get("pipId")+"\",\"taskId\":\""+maps.get("taskId")+"\",\"buildStartTime\":\""+buildStartTime+"\",\"status\":\""+status+"\",\"wsKey\":\"自动化测试失败\",\"wsValue\":\"\"}";
						WebSocketServer.sendAllClient(jsonInfo, usernoRand);
					}
				}
			}
			writeUTFJson(response, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新建流水线
	 * @param req
	 * @param res
	 */
	@RequestMapping("addPipelining")
	public void addPipelining(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, taskManageService.addPipelining(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改流水线
	 * @param req
	 * @param res
	 */
	@RequestMapping("updatePipelining")
	public void updatePipelining(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, taskManageService.updatePipelining(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 修改流水线信息
	 * @param req
	 * @param res
	 */
	@RequestMapping("updatePipeliningInfo")
	public void updatePipeliningInfo(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, taskManageService.updatePipeliningInfo(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询自动化测试任务
	 * @param req
	 * @param res
	 */
	@RequestMapping("queryAutomatedTask")
	public void queryAutomatedTask(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson2(response, taskManageService.queryAutomatedTask(request));
//			taskManageService.queryAutomatedTask(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 添加Tomcat信息
	 * @param req
	 */
	@RequestMapping("addTomcatServer")
	public void addTomcatServer(HttpServletRequest req,HttpServletResponse res){
		writeUTFJson(res, taskManageService.addTomcatServer(req));
	}
	/**
	 * 查询流水线执行历史
	 * @param request
	 * @param response
	 */
	@RequestMapping("queryPipHistoryList")
	public void queryPipHistoryList(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res, taskManageService.queryPipHistoryList(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryTaskLoginfo")
	public String queryTaskLoginfo(HttpServletRequest req,HttpServletResponse res){
		String logInfo = taskManageService.queryTaskLoginfo(req);
		req.setAttribute("logInfo", logInfo);
		return "taskManage/pipelining/pipelining_log";
	}
	/**
	 * 定时执行流水线
	 * @param req
	 * @param res
	 */
	@RequestMapping("timerExecution")
	public void timerExecution(HttpServletRequest req,HttpServletResponse res){
		try {
			// 获取参数
			Map<String, Object> pmap = RequestUtils.getParameters(req);
			writeUTFJson(res, taskManageService.timerExecution(pmap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除流水线
	 * @param req
	 * @param res
	 */
	@RequestMapping("deletePipeliningById")
	public void deletePipeliningById(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, taskManageService.deletePipeliningById(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @time 2017-12-20
	 * @author tangyongchun
	 * @description 新增自动化测试历史记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="saveAutoTestHistory", method=RequestMethod.POST)
	public void saveAutoTestHistory(HttpServletRequest request, HttpServletResponse response){
		try {
			writeUTFJson(response, taskManageService.saveAutoTestHistory(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="sendEmail", method=RequestMethod.POST)
	public void sendEmail(HttpServletRequest request, HttpServletResponse response){
//		Map<String, Object> maps = new HashMap<String, Object>();
//		String pathHeader = TaskManageController.class.getResource("/").getPath();
//		int Q = pathHeader.indexOf("/");// 找到第一个位置
//		int E = pathHeader.indexOf("/WEB-INF/classes");// 找到最后一个位置
//		pathHeader = pathHeader.substring(Q+1, E);
//		String path = pathHeader + "/config/";
//		
//		maps.put("pipId", "d1b670d80f2b42fdbb4644722f74e182");
//		maps.put("pipVersion", "2");
//		StringBuffer pipContents = new StringBuffer(); // 流水线邮件内容显示
//		StringBuffer buildContents = new StringBuffer(); // 构建任务邮件内容显示
//		StringBuffer deployContents = new StringBuffer(); // 部署任务邮件内容显示
//		List<Map<String, Object>> pipList = taskManageService.getPipliningListInfo(maps);
//		String host_ip = ApplyConstant.LOCATION_TOMCAT_IP;
//		String host_port = ApplyConstant.LOCATION_TOMCAT_PORT;
//		String localhost = host_ip +":"+ host_port + request.getContextPath();
//		if (pipList != null && pipList.size() > 0) {
//			for (int i = 0; i < pipList.size(); i++) {
//				Map<String, Object> pipMap = pipList.get(i);
//				String buildStartTime = pipMap.get("BUILD_START_TIME") != null ? pipMap.get("BUILD_START_TIME").toString() : "";
//				String buildEndTime = pipMap.get("BUILD_END_TIME") != null ? pipMap.get("BUILD_END_TIME").toString() : "";
//				String buildStatus = pipMap.get("BUILD_STATUS") != null ? pipMap.get("BUILD_STATUS").toString() : "";
//				String buildTotalTime = pipMap.get("BUILD_TOTAL_TIME") != null ? pipMap.get("BUILD_TOTAL_TIME").toString() : "";
//				String applyUrl = pipMap.get("APPLY_URL") != null ? pipMap.get("APPLY_URL").toString() : "";
//				String logUrl = localhost +"/taskManage/queryTaskLoginfo.do?task_version=1&amp;task_name=6b49648712da4caf88cc99ebcdf4eb32_deploy";
//				String warUlr = localhost +"/taskManage/downJobWar.do?jobName=ci_build&buildNumber=11";
//				if (TaskConstant.JOB_RESULTS_ITEM2.equals(buildStatus)) {
//					buildStatus = TaskConstant.JOB_BUILD_STATUS_SUCCESS;
//				}else {
//					buildStatus = TaskConstant.JOB_BUILD_STATUS_FAILURE;
//				}
//				if (pipMap.get("TASK_TYPE") != null) {
//					String taskType = pipMap.get("TASK_TYPE").toString();
//					if (taskType.equals("1")) {
//						buildContents.append("<tr><td>构建任务信息</td></tr>")
//						.append("<tr><td>开始时间："+buildStartTime+"</td></tr>")
//						.append("<tr><td>结束时间："+buildEndTime+"</td></tr>")
//						.append("<tr><td>耗时："+buildTotalTime+"</td></tr>")
//						.append("<tr><td>状态："+buildStatus+"</td></tr>")
//						.append("<tr><td>日志：<a href="+logUrl+" target=_blank>执行日志</a></td></tr>")
//						.append("<tr><td>war包：<a href="+warUlr+">ci.war</a></td></tr>");
//					}else if (taskType.equals("2")) {
//						deployContents.append("<tr><td>部署任务信息</td></tr>")
//						.append("<tr><td>开始时间："+buildStartTime+"</td></tr>")
//						.append("<tr><td>结束时间："+buildEndTime+"</td></tr>")
//						.append("<tr><td>耗时："+buildTotalTime+"</td></tr>")
//						.append("<tr><td>状态："+buildStatus+"</td></tr>")
//						.append("<tr><td>应用url：<a href ="+applyUrl+">"+applyUrl+"</a></td></tr>")
//						.append("<tr><td>日志：<a href="+logUrl+" target=_blank>执行日志</a></td></tr>");
//					}
//				}else {
//					pipContents.append("<tr><td>流水线信息</td></tr>")
//					.append("<tr><td>开始时间："+buildStartTime+"</td></tr>")
//					.append("<tr><td>结束时间："+buildEndTime+"</td></tr>")
//					.append("<tr><td>耗时："+buildTotalTime+"</td></tr>")
//					.append("<tr><td>状态："+buildStatus+"</td></tr>");
//				}
//			}
//		}
		
//		maps.put("path", path);
//		maps.put("pipContents", pipContents.toString());
//		maps.put("buildContents", buildContents.toString());
//		maps.put("deployContents", deployContents.toString());
//		SendEmailTool.sendEmail(maps,"test8888888@163.com");
	}
	/**
	 * 下载job构建后的war包
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downJobWar")
	public void downJobWar(HttpServletRequest request,HttpServletResponse response){
		String jobName = ""; // job名称
		int buildNumber = 0; // job构建次数
		Map<String, Object> maps = RequestUtils.getParameters(request);
		try {
			if (maps.get("jobName") != null && !"".equals(maps.get("jobName").toString()) && maps.get("buildNumber") != null && !"".equals(maps.get("buildNumber").toString())) {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-download;");
				
				jobName = maps.get("jobName").toString();
				buildNumber = Integer.parseInt(maps.get("buildNumber").toString());
				
				String warName = JenkinsUtils.getWarName(jobName) + ".war"; // job的war名称
				warName = URLEncoder.encode(warName, "UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename=" + warName);
				
				InputStream in = JenkinsUtils.downJobWar(jobName,buildNumber);
				OutputStream out = response.getOutputStream();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len=in.read(buffer))!=-1){
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据项目主键，获取项目的流水线列表
	 * @param req
	 * @param res
	 */
	@RequestMapping("getPipliningListByProjectId")
	public void getPipliningListByProjectId(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,taskManageService.getPipliningListByProjectId(req));
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
		
		try {
			String pipId = req.getParameter("pipId");
			String pipName = req.getParameter("pipName");
			String projectName = req.getContextPath();
			String usernoRand = req.getParameter("usernoRand");
					
			//1、获取流水线下最大版本的任务
			List<Map<String,String>> taskList = new ArrayList<Map<String,String>>();
			String pipVersion = "0";
			Map<String,String> vmap = taskManageService.getMaxPipVersionByPipId(pipId);
			if(vmap!= null && vmap.get("MAX_PIP_VERSION") != null){//存在最大版本号，表示有构建历史
				pipVersion = String.valueOf(vmap.get("MAX_PIP_VERSION"));
				Map<String,Object> paraMap = new HashMap<String,Object>();
				paraMap.put("pipId", pipId);
				paraMap.put("pip_version", pipVersion);
				//查询流水线最大历史版本信息
				taskList = taskManageService.queryBuildHistoryByVersion(paraMap);
			}else{//不存在构建历史时，查询流水线原有信息
				taskList = taskManageService.queryTaskListByPipId(pipId);
			}
			//2、在构建历史插入流水线信息
			Map<String, Object> pmap1 = this.savePipHis(pipId, pipVersion,usernoRand,"insertPip");
			if (pmap1 != null && pmap1.get("status") != null) {
				if (pmap1.get("status").equals("success")) {
					pipVersion = (String) pmap1.get("pipVersion");
					// 3、循环执行流水线的各个任务
					Map<String, Object> pmap2 = this.batchExecutionTask(pipId, pipVersion, projectName, taskList,usernoRand);
					if (pmap2 != null && pmap2.get("status") != null) {
						result = "true";
						if (pmap2.get("status").equals("success")) {
							// 4、修改构建历史表流水线执行状态
							Map<String, Object> pmap3 = this.savePipHis(pipId, pipVersion,usernoRand,"updatePip");
							if(pmap3 != null && pmap3.get("status").equals("success")){
								result = "true";
							}else{
								result = "false";
							}
						}
					}else{
						result = "false";
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("result", result);
		writeUTFJson(res,map);
	}
	/**
	 * @param pipId
	 * @param pipVersion
	 * @param operationFlag{
	 *            "insertPip" : 在构建历史插入流水线信息,"updatePip" 修改构建历史表流水线执行状态}
	 * @return
	 */
	private Map<String, Object> savePipHis(String pipId,String pipVersion,String usernoRand,String operationFlag){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pipId", pipId);
		map.put("pipVersion", pipVersion);
		map.put("operationFlag", operationFlag);
		String currentTime = DateTimeUtils.getFormatCurrentTime();
		map.put("buildStartTime", currentTime);
		map.put("usernoRand", usernoRand);
		Map<String, Object> rMap = taskManageService.savePipHistory(map);
		return rMap;
	}
	/**
	 * 循环执行流水线下的各个任务
	 * @param pipId
	 * @param pipVersion
	 * @param projectName
	 * @param taskList
	 * @return
	 */
	private Map<String, Object> batchExecutionTask(String pipId, String pipVersion, String projectName,
			List<Map<String, String>> taskList,String usernoRand) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		try {
			if(taskList != null && taskList.size() > 0){
				for (int i = 0; i < taskList.size(); i++) {
					Map<String,String> itemMap = taskList.get(i);
					String pipName = itemMap.get("PIP_NAME");
//					String jobName = itemMap.get("TASK_NAME");
					String taskType = itemMap.get("TASK_TYPE");
					String taskId = itemMap.get("TASK_ID");
					String pipStepTaskId = itemMap.get("PST_ID");
					String jobName = "";
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("pipId", pipId);
					map.put("pipName", pipName);
//					map.put("jobName", jobName);
					map.put("taskType", taskType);
					map.put("taskId", taskId);
					map.put("pipStepTaskId", pipStepTaskId);
					String currentTime = DateTimeUtils.getFormatCurrentTime();
					map.put("buildStartTime", currentTime);
					map.put("pipVersion", pipVersion);
					map.put("projectName", projectName);
					map.put("usernoRand", usernoRand);
					
					if (TaskConstant.TASK_TYPE_ITEM1.equals(taskType)) {// 判断是否为构建操作
						Map<String,String> buildMap = buildService.getBuildById(taskId);
						jobName = buildMap.get("BUILD_NAME");
					}else if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {// 判断是否为部署操作
						Map<String,String> deployMap = deployService.getDeployById(taskId);
						jobName = deployMap.get("DEPLOY_NAME");
						// 清空节点表中的ip及port
						taskManageService.emptyNodeInfo(taskId);
					}else if(TaskConstant.TASK_TYPE_ITEM3.equals(taskType)){//判断是否为自动化测试操作
						Map<String,String> autoTestMap = autoTestService.getAutoTestById(taskId);
						jobName = autoTestMap.get("TEST_NAME");
						String autoTestId = autoTestMap.get("AUTOTEST_ID");
						map.put("autoTestId", autoTestId);
					}
					map.put("jobName", jobName);
					Map<String, Object> resultMap = taskManageService.updateSingleJobHistory(map);
					
					if(TaskConstant.TASK_TYPE_ITEM3.equals(taskType)){//判断是否为自动化测试操作
						if (resultMap != null && resultMap.get("status") != null) {
							String status = (String) resultMap.get("status");
							String buildTotalTime = "";
							if (status.equals("success")){
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("pipStepTaskId", pipStepTaskId);
								params.put("pipVersion", pipVersion);
								
								boolean flag = (boolean) resultMap.get("flag");
								if (flag) {
									Map<String, Object> autoTestMap = new HashMap<String, Object>();
									// 循环查询自动化测试平台是否已经回调过了
									for (int j = 0; j < 10000; j++) {
										// 查询自动化测试历史
										autoTestMap = taskManageService.getAutoTestHistory(params);
										if (autoTestMap != null) {
											String buildStatus = autoTestMap.get("BUILD_STATUS") != null ? (String)autoTestMap.get("BUILD_STATUS") : "";
											buildTotalTime = String.valueOf(autoTestMap.get("BUILD_TOTAL_TIME") != null ? Long.parseLong((String) autoTestMap.get("BUILD_TOTAL_TIME")): 0);
											String applyUrl = autoTestMap.get("APPLY_URL") != null ?  (String)autoTestMap.get("APPLY_URL"): "";
											rMap.put("buildStatus", buildStatus);
											rMap.put("buildTotalTime", buildTotalTime);
											rMap.put("taskVersion", autoTestMap.get("TASK_VERSION") != null ? Integer.parseInt((String)autoTestMap.get("TASK_VERSION")): 0);
											rMap.put("applyUrl", applyUrl);
											if(buildStatus.equals("2")){
												rMap.put("message", "执行成功！");
												rMap.put("status", status);
											}else{
												rMap.put("message", "执行成功！");
											}
											String jsonInfo = "{\"pipeStatus\":\"3\",\"taskType\" : \"" + taskType + "\",\"pipId\":\""+pipId+"\",\"taskId\":\""+taskId+"\",\"applyUrl\" : \"" + applyUrl + "\",\"buildStartTime\" : \"" + currentTime + "\",\"buildStatus\":\"" +buildStatus+"\",\"status\":\""+status+"\",\"buildTotalTime\":\""+ buildTotalTime+"\",\"wsKey\":\"自动化测试完成\",\"wsValue\":\"\"}";
											WebSocketServer.sendAllClient(jsonInfo, usernoRand);
											break ;
										}
										Thread.sleep(1000);
									}
									return rMap;
								}
							}else {
								String jsonInfo = "{\"pipeStatus\":\"3\",\"taskType\" : \"" + taskType + "\",\"pipId\":\""+pipId+"\",\"taskId\":\""+taskId+"\",\"buildStartTime\" : \"" + currentTime + "\",\"buildTotalTime\":\""+ buildTotalTime+"\",\"status\":\""+status+"\",\"wsKey\":\"自动化测试完成\",\"wsValue\":\"\"}";
								WebSocketServer.sendAllClient(jsonInfo, usernoRand);
								return resultMap;
							}
						}
					}
					
					if (resultMap != null && resultMap.get("status") != null) {
						String status = (String) resultMap.get("status");
						String buildStatus = (String) resultMap.get("buildStatus");
						if (!(status.equals("success")&&buildStatus.equals("2"))) {
							return resultMap;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rMap;
	}
	
	/**
     * 通过webSocket获取自动化测试的日志（提供给测试平台）
     * @param req
     * @return
     */
	@RequestMapping("/autoTestLogByWs")
	public void autoTestLogByWs(@RequestBody String json,HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res, taskManageService.autoTestLogByWs(req,json));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 新建流水线组，查询流水线列表
	 * @param req
	 * @param res
	 * @author mxx
	 */
	@RequestMapping("queryPipList")
	public void queryPipList(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,taskManageService.queryPipList(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 新建流水线组，选中某条流水线的信息查询
	 * @param req
	 * @param res
	 * @author mxx
	 */
	@RequestMapping("queryPipInfo")
	public void queryPipInfo(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,taskManageService.queryPipInfo(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
