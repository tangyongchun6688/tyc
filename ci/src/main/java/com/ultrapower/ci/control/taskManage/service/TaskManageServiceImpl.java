package com.ultrapower.ci.control.taskManage.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNException;

import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.common.quartz.QuartzManager;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.common.utils.HttpClientUtils;
import com.ultrapower.ci.common.utils.IDFactory;
import com.ultrapower.ci.common.utils.JenkinsUtils;
import com.ultrapower.ci.common.utils.PackJobUtils;
import com.ultrapower.ci.common.utils.RequestUtils;
import com.ultrapower.ci.common.utils.SvnUtil;
import com.ultrapower.ci.common.utils.cronUtils;
import com.ultrapower.ci.control.autoTestManage.dao.AutoTestMapper;
import com.ultrapower.ci.control.autoTestManage.service.IAutoTestService;
import com.ultrapower.ci.control.buildManage.dao.BuildMapper;
import com.ultrapower.ci.control.deployManage.dao.DeployMapper;
import com.ultrapower.ci.control.pipeManage.service.IPipeManageService;
import com.ultrapower.ci.control.taskManage.controller.WebSocketServer;
import com.ultrapower.ci.control.taskManage.dao.TaskManageMapper;
import com.ultrapower.ci.control.taskManage.job.PipeJobFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class TaskManageServiceImpl implements ITaskManageService {
	private static final Logger logger = Logger.getLogger(TaskManageServiceImpl.class);

	@Autowired
	private TaskManageMapper taskManageMapper;
	@Autowired
	private BuildMapper buildMapper;
	@Autowired
	private DeployMapper deployMapper;
	@Autowired
	private AutoTestMapper autoTestMapper;
	@Resource
	private IPipeManageService pipeManageService;
	@Override
	public Map<String, Object> addPipelining(HttpServletRequest req) {
		logger.info("xinjiankaishi***************1");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		int steps = 1;
		String id = IDFactory.getIDStr();// 流水线Id
		String time = (String) pmap.get("time");// 定时执行时间
		logger.info("dinshishijian***************2"+time);
		String day = null;
		String week = null;
		String hour = time.substring(0,2);
		String minute = time.substring(3,5);
		logger.info("cronhour***************3"+hour+"minute"+minute);
		String cron = cronUtils.cronExpression(day, week, hour, minute);
		String executionMode = req.getParameter("executionMode");//执行方式
		String templetMode = "";
		pmap.put("cron", cron);
		pmap.put("executionMode", executionMode);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date()); // 创建时间
		logger.info("changjianshijian***************4"+datetime);
		String pip_name = ((String) pmap.get("pip_name")).trim(); // 流水线名称
		String email = (String) pmap.get("email");
		String remark = (String) pmap.get("remark");// 备注
		String creat_user = "test"; // 创建人
		String project_id = ""; // 项目id
		project_id = req.getParameter("projectId");
		String svn_url = (String) pmap.get("svn_url");// 代码仓库url
		String requestUrl = "";
		String resposeUrl = "";
		String automatedtaskInfo = "";//自动化测试信息
		pmap.put("requestUrl", requestUrl);
		pmap.put("resposeUrl", resposeUrl);//自动化测试回调url
		
		// 流水线表
		pmap.put("id", id);
		pmap.put("pip_name", pip_name);
		pmap.put("pip_create_time", datetime);
		pmap.put("pip_create_user", creat_user);
		pmap.put("project_id", project_id);
		logger.info("liushuixianbiao***************5");
		int res1 = taskManageMapper.insertPip(pmap);
		logger.info("liushuixianbiao***************6");
		// 流水线步骤表
		// 步骤任务关联表
		// 构建任务表
		// 部署任务表
//		String build_environment = (String) pmap.get("environment");// 构建环境类型
//		String build_type = (String) pmap.get("buildType");// 构建类型
//		构建环境和类型的传值方式改变
		String build_environment = (String) pmap.get("environmentHid");// 构建环境类型
		String build_type = (String) pmap.get("typeHid");// 构建类型
		String deployHid = (String) pmap.get("deployHid");// 部署任务开关
		String autoHid = (String) pmap.get("autoHid");// 自动化测试开关
		if(deployHid.equals("1")){
			 templetMode = req.getParameter("templetMode");//模板
		}
		if(autoHid.equals("1")){
			automatedtaskInfo = (String) pmap.get("automatedtask");//自动化测试信息
		}
		
//		String mould_id = "";// 模板id
//		String node_id = "";// 节点id;
		pmap.put("build_environment", build_environment);
		pmap.put("build_type", build_type);
		pmap.put("mould_id", templetMode);
//		pmap.put("node_id", node_id);
		String repId = IDFactory.getIDStr();// 代码仓库id
		String repName = "仓库名称";// 代码仓库名称
		String repDes = "仓库描述";// 代码仓库描述
		String repVersion = "2";// 代码仓库版本类型
		String repAccount = (String) pmap.get("svn_account");// 代码仓库账号
		String repPassword = (String) pmap.get("svn_password");// 代码仓库密码
		String taskBuildId = IDFactory.getIDStr();// 构建任务id
		String taskDeployId = IDFactory.getIDStr();// 部署任务id
		String taskAutoId = IDFactory.getIDStr();// 自动化测试任务id
		String real_build_name = taskBuildId + "_build";// 用于创建构建任务的name
		String real_deploy_name = taskDeployId + "_deploy";// 用于创建部署任务的name
//		node_id = IDFactory.getIDStr();// 节点id
		pmap.put("repId", repId);// 代码仓库id
		pmap.put("repName", repName);// 代码仓库名称
		pmap.put("repDes", repDes);// 代码仓库描述
		pmap.put("repVersion", repVersion);// 代码仓库版本库类型
		pmap.put("repAccount", repAccount);// 代码仓库账号
		pmap.put("repPassword", repPassword);// 代码仓库密码
		pmap.put("taskBuildId", taskBuildId);// 构建任务id
		pmap.put("taskAutoId", taskAutoId);// 自动化测试任务id
		pmap.put("build_name", pip_name + "_build");// 构建任务名称
		pmap.put("real_build_name", real_build_name);// 创建job的构建任务名称
		pmap.put("taskDeployId", taskDeployId);// 部署任务id
		pmap.put("deploy_name", pip_name + "_deploy");// 部署任务名称
		pmap.put("real_deploy_name", real_deploy_name);// 创建job的部署任务名称
//		pmap.put("node_id", node_id);// 节点id
		pmap.put("tomcat_account", "admin");// 节点id
		pmap.put("tomcat_password", "admin");// 节点id
		pmap.put("build_mode", "1");// 部署任务表中部署方式 changeAt 01.29
		String param = (String) pmap.get("param");
//		Map maps = (Map)JSON.parse(param);

		List<Map> list2 = new ArrayList<Map>(); 
		JSONArray jsonArray = JSONArray.fromObject(param);//把String转换为json 
		list2 = JSONArray.toList(jsonArray,Map.class);//这里的t是Class<T> 
//		System.out.println(list2.get(0).get("param_name"));
		
		List<Map> list = new ArrayList<Map>();//批量插入的list
		for(int i=0;i<list2.size();i++){
			Map<String, Object> tempmapMap = new HashMap<String, Object>();
			String taskParameterId = IDFactory.getIDStr();//任务参数配置表id
			tempmapMap.put("id", taskParameterId);
			tempmapMap.put("task_id", taskDeployId);
			tempmapMap.put("task_type", "");
			tempmapMap.put("param_name", list2.get(i).get("param_name"));
			tempmapMap.put("param_type", "参数类型");
			tempmapMap.put("param_default", list2.get(i).get("param_default"));
			tempmapMap.put("param_describe", "描述");
			tempmapMap.put("param_sort", list2.get(i).get("param_sort"));
			list.add(tempmapMap);
		}
		System.out.println(list);
		
		logger.info("***********************7"+templetMode != null && !templetMode.equals(""));
		if(templetMode != null && !templetMode.equals("")){
			if(automatedtaskInfo != null && !automatedtaskInfo.equals("")){
				List<String> result = Arrays.asList(automatedtaskInfo.split(","));
				String automatedtaskId = result.get(0);//自动化测试id（接口提供的id）
				String automatedtaskName = result.get(1);//自动化测试名称
				String automatedtaskType = result.get(2);//自动化测试类型
				logger.info("zidonghuaid***********************8"+automatedtaskId);
				logger.info("zidonghua***********************9"+automatedtaskName);
				pmap.put("automatedtaskId", automatedtaskId);
				pmap.put("automatedtaskName", automatedtaskName);
				pmap.put("automatedtaskType", automatedtaskType);
				steps = 3;
				logger.info("***********************10");
				taskManageMapper.insertCodeRep(pmap);// 代码仓库表插入数据
				logger.info("***********************11");
				taskManageMapper.insertTaskBuild(pmap);// 构建任务表插入数据
				logger.info("***********************12");
				taskManageMapper.insertTaskDeploy(pmap);// 部署任务表插入数据
				if(list.size() != 0){
					taskManageMapper.insertTaskParameter(list);//参数表插入数据
				}
				logger.info("***********************13");
//				taskManageMapper.insertNodeInfo(pmap);// 节点表插入数据 changeAt 01.29
//				logger.info("***********************14");
				taskManageMapper.insertTaskAuto(pmap);// 自动化测试表插入数据
				logger.info("***********************15");
			}else{
				steps = 2;
				taskManageMapper.insertCodeRep(pmap);// 代码仓库表插入数据
				taskManageMapper.insertTaskBuild(pmap);// 构建任务表插入数据
				taskManageMapper.insertTaskDeploy(pmap);// 部署任务表插入数据
				if(list.size() != 0){
					taskManageMapper.insertTaskParameter(list);//参数表插入数据
				}
//				taskManageMapper.insertNodeInfo(pmap);// 节点表插入数据 changeAt 01.29
				logger.info("***********************16");
			}
		}else{
			steps = 1;
			taskManageMapper.insertCodeRep(pmap);// 代码仓库表插入数据
			taskManageMapper.insertTaskBuild(pmap);// 构建任务表插入数据
			logger.info("***********************17");
		}
		
		for (int i = 1; i <= steps; i++) {
			if (i == 1) {
				pmap.put("taskID", taskBuildId);
			} else if(i == 2) {
				pmap.put("taskID", taskDeployId);
			}else{
				pmap.put("taskID", taskAutoId);
			}
//			pmap.put("pipStepId", IDFactory.getIDStr());// 流水线步骤表主键  3.8
			pmap.put("pipStepTaskId", IDFactory.getIDStr());// 步骤任务关联表主键
			pmap.put("pip_steps_sort", i);
			pmap.put("pip_steps_name", "step" + i);
//			taskManageMapper.insertPipSteps(pmap);   3.8
			taskManageMapper.insertPipStepsTask(pmap);
			logger.info("***********************18");
		}
		String tomcat_url = "";// 申请成功之后直接拼url或者存到库中再取
		String pathHeader = TaskManageServiceImpl.class.getResource("/").toString();
		int Q = pathHeader.indexOf("/");// 找到第一个位置
		int E = pathHeader.indexOf("/WEB-INF/classes");// 找到最后一个位置
		pathHeader = pathHeader.substring(Q, E);
		String xmlPath1 = pathHeader + "/config/buildConfig.xml";
		String xmlPath2 = pathHeader + "/config/deployConfig.xml";
		String svn_account = (String) pmap.get("svn_account");
		String svn_password = (String) pmap.get("svn_password");
		String tomcat_account = (String) pmap.get("tomcat_account");
		String tomcat_password = (String) pmap.get("tomcat_password");
		String emailSubject = pip_name + "-构建任务执行情况";
		String emailContext = "邮件内容";
		String svncredentialID = pip_name + svn_account + IDFactory.getIDStr();// 造svn账号的唯一标识
		String tomcatcredentialID = "1";// 生成tomcat账号的唯一标识 改为用节点id changeAt 01.29
		//生成账号之前调接口查询
		String x = pipeManageService.getJenkinsCredential(svn_account, svn_password);
		if(x == null || x.equals("")){
			JenkinsUtils.createCredential(svncredentialID, svn_account, svn_password);// 生成svn账号
			pipeManageService.addJenkinsCredential(svncredentialID, svn_account, svn_password);
		}else{
			svncredentialID = x;
		}
		//在此调用唐接口 改变svn_url 和命令
		Map<String, String> svnInfo =new HashMap<String, String>();
		try {
			svnInfo = SvnUtil.handleSvnAddress(svn_url, svn_account, svn_password);
		} catch (SVNException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		svn_url = svnInfo.get("svnUrl");
		String mvnCmd = svnInfo.get("mvnCmd");
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("svn_url", svn_url);
		map1.put("svncredentialID", svncredentialID);
		map1.put("remark", remark);
		map1.put("xmlPath1", xmlPath1);
		map1.put("emailUser", email);
		map1.put("emailSubject", emailSubject);
		map1.put("emailContext", emailContext);
		map1.put("mvnCmd", mvnCmd);
		String job1Xml = PackJobUtils.Packjob(map1);
		logger.info("job1xml***********************19"+job1Xml);
		JenkinsUtils.createJob(real_build_name, job1Xml);// 生成job1
		logger.info("END***********************20");

		if(templetMode != null && !templetMode.equals("")){
//			JenkinsUtils.createCredential(tomcatcredentialID, tomcat_account, tomcat_password);// 生成tomcat账号 changeAt 01.29
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("job1Name", real_build_name);
			map2.put("tomcat_url", tomcat_url);
			map2.put("tomcat_username", tomcat_account);
			map2.put("tomcat_password", tomcat_password);
			map2.put("tomcatcredentialID", tomcatcredentialID);
			map2.put("templatePath", xmlPath2);
			map2.put("xmlPath2", xmlPath2);
			map2.put("emailUser", email);
			map2.put("emailSubject", emailSubject);
			map2.put("emailContext", emailContext);
			// 获取config.xml
			String job2Xml = PackJobUtils.changeXml(map2);
			logger.info("job2xml***********************21"+job1Xml);
			// 创建job
			JenkinsUtils.createJob(real_deploy_name, job2Xml);// 生成job2
			logger.info("END***********************22");
		}
		Map<String, Object> timerMap = new HashMap<String, Object>();
		timerMap.put("pipId", id);
		timerMap.put("executionMode", executionMode);
		timerMap.put("timed_cron", cron);
		timerExecution(timerMap);
		logger.info("***********************23");
		map.put("result", "true");
		map.put("count", res1);
		return map;
	}
	private String addPipParam(Map<String, Object> map){
		String param = (String) map.get("param");
		String taskDeployId = (String) map.get("taskDeployId");
		List<Map> list2 = new ArrayList<Map>(); 
		JSONArray jsonArray = JSONArray.fromObject(param);//把String转换为json 
		list2 = JSONArray.toList(jsonArray,Map.class);//这里的t是Class<T> 
		List<Map> list = new ArrayList<Map>();//批量插入的list
		for(int i=0;i<list2.size();i++){
			Map<String, Object> tempmapMap = new HashMap<String, Object>();
			String taskParameterId = IDFactory.getIDStr();//任务参数配置表id
			tempmapMap.put("id", taskParameterId);
			tempmapMap.put("task_id", taskDeployId);
			tempmapMap.put("task_type", "");
			tempmapMap.put("param_name", list2.get(i).get("param_name"));
			tempmapMap.put("param_type", "参数类型");
			tempmapMap.put("param_default", list2.get(i).get("param_default"));
			tempmapMap.put("param_describe", "描述");
			tempmapMap.put("param_sort", list2.get(i).get("param_sort"));
			list.add(tempmapMap);
		}									
		if(list.size() != 0){
					taskManageMapper.deleteTaskParameter(taskDeployId);//根据task_id删除现有的参数
					taskManageMapper.insertTaskParameter(list);//参数表插入数据
				}
		return param;
		
	}

	@Override
	public Map<String, Object> updatePipeliningInfo(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		logger.info("更新流水线相关表");
		int steps = 1;
		String id = req.getParameter("id");
		String time = (String) pmap.get("time");// 定时执行时间
		String day = null;
		String week = null;
		String hour = time.substring(0,2);
		String minute = time.substring(3,5);
		String cron = cronUtils.cronExpression(day, week, hour, minute);
		String executionMode = req.getParameter("executionMode");//执行方式
		String templetMode = "";//模板
		
		pmap.put("cron", cron);
		pmap.put("executionMode", executionMode);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date()); // 创建时间
		String creat_user = "test"; // 创建人
		pmap.put("pip_create_time", datetime);
		pmap.put("pip_create_user", creat_user);
		String pip_name = ((String) pmap.get("pip_name")).trim(); // 流水线名称
		pmap.put("build_name", pip_name + "_build");// 构建任务名称
		pmap.put("deploy_name", pip_name + "_deploy");// 构建任务名称
		pmap.put("tomcat_account", "admin");// 节点id
		pmap.put("tomcat_password", "admin");// 节点id
		pmap.put("build_mode", "1");
		String automatedtaskInfo = "";//自动化测试信息
		String param = (String) pmap.get("param");// 参数信息
		String build_environment = (String) pmap.get("environmentHid");// 构建环境类型
		String build_type = (String) pmap.get("typeHid");// 构建类型
		pmap.put("build_environment", build_environment);
		pmap.put("build_type", build_type);
		String deployHid = (String) pmap.get("deployHid");// 部署任务开关
		String autoHid = (String) pmap.get("autoHid");// 自动化测试开关
		if(deployHid.equals("1")){
			 templetMode = req.getParameter("templetMode");//模板
		}
		if(autoHid.equals("1")){
			automatedtaskInfo = (String) pmap.get("automatedtask");//自动化测试信息
		}
		pmap.put("mould_id", templetMode);
		String requestUrl = "";
		String resposeUrl = "";
		pmap.put("requestUrl", requestUrl);
		pmap.put("resposeUrl", resposeUrl);//自动化测试回调url
		String real_build_name = taskManageMapper.queryJenkinsBuildName(id);//根据流水线id查询jenkins构建任务的name
		String real_deploy_name = taskManageMapper.queryJenkinsDeployName(id);//根据流水线id查询jenkins部署任务的name
		String taskAutoId = taskManageMapper.queryTaskAutoId(id);//根据流水线id查询自动化测试任务id
		taskManageMapper.updatePip(pmap);// 修改流水线表
		if(templetMode != null && !templetMode.equals("")){
			if (automatedtaskInfo != null && !automatedtaskInfo.equals("")) {// 现在有三个步骤
				List<String> result = Arrays.asList(automatedtaskInfo.split(","));
				String automatedtaskId = result.get(0);// 自动化测试id（接口提供的id）
				String automatedtaskName = result.get(1);// 自动化测试名称
				String automatedtaskType = result.get(2);// 自动化测试类型
				pmap.put("automatedtaskId", automatedtaskId);
				pmap.put("automatedtaskName", automatedtaskName);
				pmap.put("automatedtaskType", automatedtaskType);
				steps = 3;
				if (real_deploy_name != null && taskAutoId != null) {// 原来有3个步骤
					taskManageMapper.updateTaskBuild(pmap);// 修改构建任务表
					taskManageMapper.updateCodeRep(pmap);// 修改代码仓库表
					taskManageMapper.updateTaskDeploy(pmap);// 修改部署任务表
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String taskDeployId = real_deploy_name.substring(0, real_deploy_name.length()-7);
					tempmap.put("taskDeployId", taskDeployId);
					tempmap.put("param", param);
					this.addPipParam(tempmap);// 参数表中插入数据
					taskManageMapper.updateTaskAutoInfo(pmap);// 修改自动化测试表
				} else if (real_deploy_name != null && taskAutoId == null) {// 原来有2个步骤
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String taskDeployId = real_deploy_name.substring(0, real_deploy_name.length()-7);
					tempmap.put("taskDeployId", taskDeployId);
					tempmap.put("param", param);
					this.addPipParam(tempmap);// 参数表中插入数据
					// 新建自动化测试任务
					taskAutoId = IDFactory.getIDStr();// 自动化测试任务id
					pmap.put("taskID", taskAutoId);
					pmap.put("taskAutoId", taskAutoId);
					pmap.put("pipStepTaskId", IDFactory.getIDStr());// 流水线任务关联表主键
					pmap.put("pip_steps_sort", "3");
					pmap.put("pip_steps_name", "step3");
					taskManageMapper.insertPipStepsTask(pmap);
					taskManageMapper.insertTaskAuto(pmap);// 自动化测试表插入数据
				} else {// 原来有一个步骤
						// 新建部署任务与自动化测试任务
					String taskDeployId = IDFactory.getIDStr();// 部署任务id
					Map<String, Object> tempmap = new HashMap<String, Object>();
					tempmap.put("taskDeployId", taskDeployId);
					tempmap.put("param", param);
					this.addPipParam(tempmap);// 参数表中插入数据
					taskAutoId = IDFactory.getIDStr();// 自动化测试任务id
					real_deploy_name = taskDeployId + "_deploy";
					pmap.put("taskDeployId", taskDeployId);
					pmap.put("taskAutoId", taskAutoId);
					pmap.put("real_deploy_name", real_deploy_name);
					for (int i = 2; i <= steps; i++) {
						if (i == 2) {
							pmap.put("taskID", taskDeployId);
						} else {
							pmap.put("taskID", taskAutoId);
						}
						pmap.put("pipStepTaskId", IDFactory.getIDStr());// 流水线任务关联表主键
						pmap.put("pip_steps_sort", i);
						pmap.put("pip_steps_name", "step" + i);
						taskManageMapper.insertPipStepsTask(pmap);
					}
					taskManageMapper.insertTaskDeploy(pmap);// 部署任务表插入数据
					taskManageMapper.insertTaskAuto(pmap);// 自动化测试表插入数据
				}
			}else{// 自动化测试id为空但模板id不为空  代表现在有构建和部署两个步骤
				steps = 2;
				if(real_deploy_name != null && taskAutoId == null){//原来有2步骤
					taskManageMapper.updateTaskBuild(pmap);// 修改构建任务表
					taskManageMapper.updateCodeRep(pmap);// 修改代码仓库表
					taskManageMapper.updateTaskDeploy(pmap);// 修改部署任务表
					Map<String, Object> tempmap = new HashMap<String, Object>();
					String taskDeployId = real_deploy_name.substring(0, real_deploy_name.length()-7);
					tempmap.put("taskDeployId", taskDeployId);
					tempmap.put("param", param);
					this.addPipParam(tempmap);// 参数表中插入数据
				}else if(real_deploy_name == null && taskAutoId == null){//原来有1步骤
					//新建部署job 部署任务表
					String taskDeployId = IDFactory.getIDStr();// 部署任务id
					real_deploy_name = taskDeployId + "_deploy";
					String taskBuildId = real_build_name.substring(0, real_build_name.length()-6);
					pmap.put("taskDeployId", taskDeployId);
					pmap.put("taskID", taskDeployId);
					pmap.put("pipStepTaskId", IDFactory.getIDStr());// 流水线任务关联表主键
					pmap.put("pip_steps_sort", "2");
					pmap.put("real_deploy_name", real_deploy_name);
					pmap.put("taskBuildId", taskBuildId);
					taskManageMapper.insertPipStepsTask(pmap);
					taskManageMapper.insertTaskDeploy(pmap);// 部署任务表插入数据
					Map<String, Object> tempmap = new HashMap<String, Object>();
					tempmap.put("taskDeployId", taskDeployId);
					tempmap.put("param", param);
					this.addPipParam(tempmap);// 参数表中插入数据
				}else{
					//删除自动化测试表 删除步骤表及步骤任务关联表
					taskManageMapper.deleteTaskAutoTest(id);//删除自动化测试表信息
					String step = "3";
					Map<String, Object> delMap = new HashMap<String, Object>();
					delMap.put("id", id);
					delMap.put("task_type", step);
					taskManageMapper.deletePipelining_task2(delMap);//删除流水线任务关联表2
				}
			}
		}else{	//模板id为空  代表现在只有一个构建步骤	
			steps = 1;
			if(real_deploy_name == null && taskAutoId == null){//原来有1个步骤
				taskManageMapper.updateTaskBuild(pmap);// 修改构建任务表
				taskManageMapper.updateCodeRep(pmap);// 修改代码仓库表
			}else if(real_deploy_name != null && taskAutoId == null){//原来有2个步骤
				//删除部署job 删除部署任务表及节点表  删除部署步骤表及步骤任务关联表
				String step = "2";
				Map<String, Object> delMap = new HashMap<String, Object>();
				delMap.put("id", id);
				delMap.put("task_type", step);
				String taskDeployId = real_deploy_name.substring(0, real_deploy_name.length()-7);
				JenkinsUtils.deleteJobInfo(real_deploy_name);//根据jobName删除job
				taskManageMapper.deleteTask_deploy(id);// 删除部署任务表
				taskManageMapper.deletePipelining_task2(delMap);//删除流水线任务关联表2
				taskManageMapper.deleteTaskParameter(taskDeployId);
			}else{//原来有3步骤
				//删除部署job 删除部署任务表及节点表  删除部署步骤表及步骤任务关联表
				JenkinsUtils.deleteJobInfo(real_deploy_name);//根据jobName删除job
				taskManageMapper.deleteTaskAutoTest(id);//删除自动化测试表信息
				taskManageMapper.deleteTask_deploy(id);// 删除部署任务表
				String taskDeployId = real_deploy_name.substring(0, real_deploy_name.length()-7);
				taskManageMapper.deleteTaskParameter(taskDeployId);//删除参数表
				Map<String, Object> delMap = new HashMap<String, Object>();
				delMap.put("id", id);
				for(int i=2; i<=3 ; i++){
					String step = String(i);
					delMap.put("task_type", step);
					taskManageMapper.deletePipelining_task2(delMap);//删除流水线任务关联表2
				}
			}
		}
//		String node_id = "";// 节点id;
//		pmap.put("node_id", node_id);
		String svn_url = (String) pmap.get("svn_url");// 代码仓库url
		String remark = (String) pmap.get("remark");// 备注
		String email = (String) pmap.get("email");
		String tomcat_url = "";
		String pathHeader = TaskManageServiceImpl.class.getResource("/").toString();
		int Q = pathHeader.indexOf("/");// 找到第一个位置
		int E = pathHeader.indexOf("/WEB-INF/classes");// 找到最后一个位置
		pathHeader = pathHeader.substring(Q, E);
		String xmlPath1 = pathHeader + "/config/buildConfig.xml";
		String xmlPath2 = pathHeader + "/config/deployConfig.xml";
		String svn_account = (String) pmap.get("svn_account");
		String svn_password = (String) pmap.get("svn_password");
		String tomcat_account = (String) pmap.get("tomcat_account");
		String tomcat_password = (String) pmap.get("tomcat_password");
		String emailSubject = pip_name + "-构建任务执行情况";
		String emailSubject2 = pip_name + "-部署任务执行情况";
		String emailContext = "邮件内容";
		String svncredentialID = pip_name + svn_account + IDFactory.getIDStr();// 生成svn账号的唯一标识
		String tomcatcredentialID = "";// 生成tomcat账号的唯一标识 改为用节点id
		//生成账号之前调接口查询
		String x = pipeManageService.getJenkinsCredential(svn_account, svn_password);
		if(x == null || x.equals("")){
			JenkinsUtils.createCredential(svncredentialID, svn_account, svn_password);// 生成svn账号
			pipeManageService.addJenkinsCredential(svncredentialID, svn_account, svn_password);
		}else{
			svncredentialID = x;
		}
		//在此调用唐接口 改变svn_url 和命令
				Map<String, String> svnInfo =new HashMap<String, String>();
				try {
					svnInfo = SvnUtil.handleSvnAddress(svn_url, svn_account, svn_password);
				} catch (SVNException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				svn_url = svnInfo.get("svnUrl");
				String mvnCmd = svnInfo.get("mvnCmd");
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("svn_url", svn_url);
		map1.put("mvnCmd", mvnCmd);
		map1.put("svncredentialID", svncredentialID);
		map1.put("remark", remark);
		map1.put("xmlPath1", xmlPath1);
		map1.put("emailUser", email);
		map1.put("emailSubject", emailSubject);
		map1.put("emailContext", emailContext);
		String job1Xml = PackJobUtils.Packjob(map1);
		JenkinsUtils.updateJob(real_build_name, job1Xml);// 生成job1
		if(taskAutoId != null && !taskAutoId.equals("")){//原来有三步
			if(steps > 1){
//				JenkinsUtils.createCredential(tomcatcredentialID, tomcat_account, tomcat_password);// 生成tomcat账号
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("job1Name", real_build_name);
				map2.put("tomcat_url", tomcat_url);
				map2.put("tomcat_username", tomcat_account);
				map2.put("tomcat_password", tomcat_password);
				map2.put("tomcatcredentialID", tomcatcredentialID);
				map2.put("templatePath", xmlPath2);
				map2.put("xmlPath2", xmlPath2);
				map2.put("emailUser", email);
				map2.put("emailSubject", emailSubject2);
				map2.put("emailContext", emailContext);
				// 获取config.xml
				String job2Xml = PackJobUtils.changeXml(map2);
				// 修改job
				boolean flag = JenkinsUtils.exist(real_deploy_name);
				if(flag){
					JenkinsUtils.updateJob(real_deploy_name, job2Xml);// 修改job2
				}else{
					JenkinsUtils.createJob(real_deploy_name, job2Xml);// 新建job2
				}
			}else{
				JenkinsUtils.deleteJobInfo(real_deploy_name);// 删除job2
			}
		}else if(real_deploy_name != null && !real_deploy_name.equals("")){//原来有两步
			if(steps > 1){
//				JenkinsUtils.createCredential(tomcatcredentialID, tomcat_account, tomcat_password);// 生成tomcat账号
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("job1Name", real_build_name);
				map2.put("tomcat_url", tomcat_url);
				map2.put("tomcat_username", tomcat_account);
				map2.put("tomcat_password", tomcat_password);
				map2.put("tomcatcredentialID", tomcatcredentialID);
				map2.put("templatePath", xmlPath2);
				map2.put("xmlPath2", xmlPath2);
				map2.put("emailUser", email);
				map2.put("emailSubject", emailSubject2);
				map2.put("emailContext", emailContext);
				// 获取config.xml
				String job2Xml = PackJobUtils.changeXml(map2);
				// 修改job
//				JenkinsUtils.updateJob(real_deploy_name, job2Xml);// 修改job2
				boolean flag = JenkinsUtils.exist(real_deploy_name);
				if(flag){
					JenkinsUtils.updateJob(real_deploy_name, job2Xml);// 修改job2
				}else{
					JenkinsUtils.createJob(real_deploy_name, job2Xml);// 新建job2
				}
			}else{
				JenkinsUtils.deleteJobInfo(real_deploy_name);// 删除job2
			}
		}else{//原来一步
			if(steps == 2){
				System.out.println("新增部署job");
//				JenkinsUtils.createCredential(tomcatcredentialID, tomcat_account, tomcat_password);// 生成tomcat账号
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("job1Name", real_build_name);
				map2.put("tomcat_url", tomcat_url);
				map2.put("tomcat_username", tomcat_account);
				map2.put("tomcat_password", tomcat_password);
				map2.put("tomcatcredentialID", tomcatcredentialID);
				map2.put("templatePath", xmlPath2);
				map2.put("xmlPath2", xmlPath2);
				map2.put("emailUser", email);
				map2.put("emailSubject", emailSubject2);
				map2.put("emailContext", emailContext);
				// 获取config.xml
				String job2Xml = PackJobUtils.changeXml(map2);
				JenkinsUtils.createJob(real_deploy_name, job2Xml);// 新增job2
			}else if(steps == 3){
				System.out.println("新增部署job");
//				JenkinsUtils.createCredential(tomcatcredentialID, tomcat_account, tomcat_password);// 生成tomcat账号
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("job1Name", real_build_name);
				map2.put("tomcat_url", tomcat_url);
				map2.put("tomcat_username", tomcat_account);
				map2.put("tomcat_password", tomcat_password);
				map2.put("tomcatcredentialID", tomcatcredentialID);
				map2.put("templatePath", xmlPath2);
				map2.put("xmlPath2", xmlPath2);
				map2.put("emailUser", email);
				map2.put("emailSubject", emailSubject2);
				map2.put("emailContext", emailContext);
				// 获取config.xml
				String job2Xml = PackJobUtils.changeXml(map2);
				JenkinsUtils.createJob(real_deploy_name, job2Xml);// 新增job2
				System.out.println("新增部署和自动化测试");
			}else{
				System.out.println("现在只有一步");
			}
		}
		Map<String, Object> timerMap = new HashMap<String, Object>();
		timerMap.put("pipId", id);
		timerMap.put("executionMode", executionMode);
		timerMap.put("timed_cron", cron);
		timerExecution(timerMap);
		
		map.put("result", "true");
		return map;
	}

	private String String(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String queryAutomatedTask(HttpServletRequest req) {
		logger.info("自动化测试任务接口调用");
		String autoIp = getPropertyValue("autoTest_ip");
		String autoPort = getPropertyValue("autoTest_port");
		String URL = autoIp + ":" + autoPort + "/atp/service/rest/v1/tasks";
//		String URL = "http://192.168.120.102:8888/atp/service/rest/v1/tasks";
		JSONObject jsobj1 = new JSONObject();
		jsobj1.put("userName", "niewei1");
        jsobj1.put("classifyName", "ATP-1.0");
        String json = jsobj1.toString();
        String result = HttpClientUtils.doPostJson(URL,json);
        return result;
	}

	@Override
	public Map<String, Object> updatePipelining(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		String id = req.getParameter("id");
		// String id = "afc0d991698641e8ad5914d20692f714";
		Map<String, Object> info = taskManageMapper.queryPipName(id);
		if (info != null && info.size() > 0) {
				String pipName = (String) info.get("PIP_NAME");
				mapInfo.put("pipName", pipName);
				String mailAddress = (String) info.get("MAIL_ADDRESS");
				mapInfo.put("mailAddress", mailAddress);
				String momo = (String) info.get("EXECUTION_MODE");
				mapInfo.put("momo", momo);
				String cron = (String) info.get("TIMED_CRON");
				cron = cron.substring(5,7)+ ":00";
				mapInfo.put("cron", cron);
		}
		Map<String, Object> deployInfo = taskManageMapper.queryDeployTaskInfo(id);
		if (deployInfo != null && deployInfo.size() > 0) {
				String templetId = (String) deployInfo.get("MOULD_ID");
				mapInfo.put("templetId", templetId);
				
		}
		List<Map<String, String>> svnList = taskManageMapper.querySvnInfo(id);
		if (svnList != null && svnList.size() > 0) {
			for (int i = 0; i < svnList.size(); i++) {
				String svnAccount = svnList.get(i).get("REP_ACCOUNT_NUMBER");
				mapInfo.put("svnAccount", svnAccount);
				String svnPassword = svnList.get(i).get("REP_PASSWORD");
				mapInfo.put("svnPassword", svnPassword);
				String svnUrl = svnList.get(i).get("REP_URL");
				mapInfo.put("svnUrl", svnUrl);
			}
		}
		List<Map<String, String>> tomcatList = taskManageMapper.queryTomcatInfo(id);
		if (tomcatList != null && tomcatList.size() > 0) {
			for (int i = 0; i < tomcatList.size(); i++) {
				String tomcatAccount = tomcatList.get(i).get("NODE_ACCOUNT_NUMBER");
				mapInfo.put("tomcatAccount", tomcatAccount);
				String tomcatPassword = tomcatList.get(i).get("NODE_PASSWORD");
				mapInfo.put("tomcatPassword", tomcatPassword);
			}
		}
		List<Map<String, String>> taskList = taskManageMapper.querytaskInfo(id);
		if (taskList != null && taskList.size() > 0) {
			for (int i = 0; i < taskList.size(); i++) {
				String build_environment = taskList.get(i).get("BUILD_ENVIRONMENT");
				mapInfo.put("build_environment", build_environment);
				String build_type = taskList.get(i).get("BUILD_TYPE");
				mapInfo.put("build_type", build_type);
				String build_environment_id = taskList.get(i).get("DIC_ID");
				mapInfo.put("build_environment_id", build_environment_id);
			}
		}
		List<Map<String, String>> paramList = taskManageMapper.queryparamInfo(id);
		List<Map<String, Object>> paramInfo = new ArrayList<Map<String,Object>>();
		if (paramList != null && paramList.size() > 0) {
			for (int i = 0; i < paramList.size(); i++) {
				Map<String, Object> tempInfo = new HashMap<String, Object>();
				String paramName = paramList.get(i).get("PARAM_NAME");
				tempInfo.put("paramName", paramName);
				String paramDefault = paramList.get(i).get("PARAM_DEFAULT");
				tempInfo.put("paramDefault", paramDefault);
				paramInfo.add(tempInfo);
			}
		}
		mapInfo.put("paramInfo", paramInfo);
		map.put("result", "success");
		map.put("info", mapInfo);
		return map;
	}

	@Override
	public Map<String, Object> getPipliningList(HttpServletRequest req) {

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		List<Map<String, String>> pipList = taskManageMapper.queryPipeliningList(pmap);
		if (pipList != null && pipList.size() > 0) {
			for (int i = 0; i < pipList.size(); i++) {
				String pipId = pipList.get(i).get("ID");
				Map<String, String> vmap = taskManageMapper.getMaxPipVersionByPipId(pipId);
				if (vmap != null && vmap.get("MAX_PIP_VERSION") != null) {// 存在最大版本号，表示有构建历史
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("pipId", pipId);
					paraMap.put("pip_version", String.valueOf(vmap.get("MAX_PIP_VERSION")));
					// 查询流水线最大历史版本信息
					List<Map<String, String>> taskList = taskManageMapper.queryBuildHistoryByVersion(paraMap);
					map.put("pipId_" + pipId, taskList);
					map.put("pip_version_" + pipId, String.valueOf(vmap.get("MAX_PIP_VERSION")));
				} else {// 不存在构建历史时，查询流水线原有信息
					List<Map<String, String>> taskList = taskManageMapper.queryTaskListByPipId(pipId);
					map.put("pipId_" + pipId, taskList);
					map.put("pip_version_" + pipId, "0");
				}
			}
		}
		map.put("status", "success");
		map.put("pipList", pipList);
		return map;
	}

	@Override
	public Map<String, Object> savePipHistory(Map<String, Object> maps) {
		// 封装结果集
		Map<String, Object> info = new HashMap<String, Object>();
		// 封装存储数据
		Map<String, Object> params = new HashMap<String, Object>();

		String pipVersion = "0"; // 流水线版本号
		String pipId = "";// 流水线Id
		String buildStartTime = ""; // 构建开始时间
		String operationFlag = "";
		String buildStatus = ""; // 构建状态
		String usernoRand = ""; // webSockect唯一标识
		String jsonInfo = "";

		boolean flag = false; // 参数校验标识
		if (maps.get("operationFlag") != null && !"".equals(maps.get("operationFlag"))) {
			operationFlag = maps.get("operationFlag").toString();
			flag = true;
		} else {
			flag = false;
		}
		if (flag) {
			if (maps.get("pipId") != null && !"".equals(maps.get("pipId"))) {
				pipId = maps.get("pipId").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("buildStartTime") != null && !"".equals(maps.get("buildStartTime"))) {
				buildStartTime = maps.get("buildStartTime").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("usernoRand") != null && !"".equals(maps.get("usernoRand"))) {
				usernoRand = maps.get("usernoRand").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (maps.get("pipVersion") != null && !"".equals(maps.get("pipVersion"))) {
			pipVersion = maps.get("pipVersion").toString();
		}
		
		if (flag) {
			params.put("pipId", pipId);
			if (operationFlag.equals("insertPip")) {// 插入流水线记录
				// 查询流水线最大版本号
				Map<String, String> mapVersion = taskManageMapper.getMaxPipVersionByPipId(pipId);
				if (mapVersion.get("MAX_PIP_VERSION") != null) {
					pipVersion =  String.valueOf(mapVersion.get("MAX_PIP_VERSION"));
					pipVersion = String.valueOf(Integer.parseInt(pipVersion) + 1);
				}else {
					pipVersion = "1";
				}
				params.put("id", IDFactory.getIDStr()); // 生成主键ID
				params.put("buildStartTime", buildStartTime);
				params.put("buildUser", "root");
				params.put("pipVersion", pipVersion);
				
				jsonInfo = "{\"pipeStatus\":\"0\",\"pipId\":\""+pipId+"\",\"buildStartTime\":\""+buildStartTime+"\",\"wsKey\":\"\",\"wsValue\":\"\"}";
				
				flag = taskManageMapper.insertPipHistory(params);
			}
			if (operationFlag.equals("updatePip")) { // 更新流水线历史记录
				params.put("buildEndTime", DateTimeUtils.getFormatCurrentTime());
				buildStatus = TaskConstant.TASK_TYPE_ITEM2; // 成功
				params.put("buildStatus", buildStatus);
				params.put("pipVersion", pipVersion);
				
				flag = taskManageMapper.updatePipHistory(params);
				
				// 根据流水线id查询流水线的结束时间、耗时、邮件地址
				List<Map<String, Object>> listPip = taskManageMapper.getPiplineById(params);
				String buildEndTime = "";
				String buildTotalTime = ""; 
				String mailAddress = "";
				if (listPip != null && listPip.size() > 0) {
					Map<String, Object> pipMap = listPip.get(0);
					buildEndTime = pipMap.get("BUILD_END_TIME") != null ? pipMap.get("BUILD_END_TIME").toString() : "";
					buildTotalTime = pipMap.get("BUILD_TOTAL_TIME") != null ? pipMap.get("BUILD_TOTAL_TIME").toString() : "";
					mailAddress = pipMap.get("MAIL_ADDRESS") != null ? pipMap.get("MAIL_ADDRESS").toString() : "";
				}
				jsonInfo = "{\"pipeStatus\":\"4\",\"pipId\":\""+pipId+"\",\"buildEndTime\":\"" +buildEndTime+"\",\"buildTotalTime\":\""+buildTotalTime+"\",\"mailAddress\":\""+ mailAddress +"\",\"wsKey\":\"\",\"wsValue\":\"\"}";
			}
			
			WebSocketServer.sendAllClient(jsonInfo, usernoRand);
			
			if (flag) {
				info.put("status", "success");
				info.put("pipVersion", pipVersion);
				info.put("message", "执行成功！");
				info.put("buildStatus", buildStatus);
			} else {
				info.put("status", "failure");
				info.put("pipVersion", pipVersion);
				info.put("message", "执行失败！");
				info.put("buildStatus", buildStatus);
			}
		} else {
			info.put("status", "failure");
			info.put("message", "执行失败！");
		}
		return info;
	}

	@Override
	public Map<String, Object> updateSingleJobHistory(Map<String, Object> maps) throws InterruptedException, DeploymentException, IOException, URISyntaxException, EncodeException {
		logger.info("updateSingleJobHistory....................................................................");
		String usernoRand = "";
		if (maps.get("usernoRand") != null && !"".equals(maps.get("usernoRand"))) {
			// websocket相关配置
			usernoRand = (String) maps.get("usernoRand");
			String jsonInfo = "{\"pipeStatus\" : \"1\",\"pipId\":\"" + maps.get("pipId") + "\",\"taskId\":\""
					+ maps.get("taskId") + "\",\"buildStartTime\" : \"" + maps.get("buildStartTime") + "\"}";
			WebSocketServer.sendAllClient(jsonInfo, usernoRand);
		}
		
		String jobName = ""; // 任务名称
		String pipStepTaskId = ""; // 步骤任务关联id
		String pipVersion = ""; // 流水线版本号
		String pipId = "";// 流水线Id
		String buildStartTime = ""; // 构建开始时间
		String buildEndTime = ""; // 构建结束时间
		long buildTotalTime = 0; // 构建job的耗时
		String buildStatus = ""; // 构建状态
		String applyUrl = ""; // 部署应用url
		String taskType = ""; // 任务类型
		int taskVersion = 1; // 任务版本号
		String warName = "";
		String testResult = "";
		// 封装结果集
		Map<String, Object> info = new HashMap<String, Object>();
		// 封装存储数据
		Map<String, Object> params = new HashMap<String, Object>();
		// 存储job信息
		List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();
		boolean flag = false; // 参数校验标识
		if (maps.get("pipId") != null && !"".equals(maps.get("pipId"))) {
			pipId = maps.get("pipId").toString();
			flag = true;
		} else {
			flag = false;
		}
		if (flag) {
			if (maps.get("buildStartTime") != null && !"".equals(maps.get("buildStartTime"))) {
				buildStartTime = maps.get("buildStartTime").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("pipVersion") != null && !"".equals(maps.get("pipVersion"))) {
				pipVersion = maps.get("pipVersion").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("jobName") != null && !"".equals(maps.get("jobName"))) {
				jobName = maps.get("jobName").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("pipStepTaskId") != null && !"".equals(maps.get("pipStepTaskId"))) {
				pipStepTaskId = maps.get("pipStepTaskId").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {
			if (maps.get("taskType") != null && !"".equals(maps.get("taskType"))) {
				taskType = maps.get("taskType").toString();
				flag = true;
			} else {
				flag = false;
			}
		}
		if (flag) {

			params.put("pipStepTaskId", pipStepTaskId);
			params.put("pipVersion", pipVersion);
			String jobHistoryId= IDFactory.getIDStr();// 生成主键ID
			params.put("id", jobHistoryId); 
			// 查询单个job信息是否插入
			jobList = taskManageMapper.getSingleJobHistory(params);
			
			if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {// 判断是否为部署操作
				maps.put("jobHistoryId", jobHistoryId);
				String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"向容器平台申请环境\",\"wsValue\":\"\"}";
				WebSocketServer.sendAllClient(jsonInfo, usernoRand);
				if (jobList != null && jobList.size() > 0) {
					if (flag) {
						Map<String, Object> deployJob = jobList.get(0);
						if (deployJob.get("BUILD_STATUS") != null && !"".equals(deployJob.get("BUILD_STATUS"))) {
							buildStatus = deployJob.get("BUILD_STATUS").toString();
							if (buildStatus.equals(TaskConstant.JOB_RESULTS_ITEM3)) {
								// 当部署失败时调用申请机器的方法
								flag = this.applicationMachine(maps);
							}
						}
					}
				} else {
					// 当没有部署时先调用申请机器的方法
					flag = this.applicationMachine(maps);
				}
				if (!flag) {
					info.put("status", "failure");
					info.put("message", "申请tomcat机器失败！");
					if (usernoRand != "") {
						String resultJson = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\""
								+ maps.get("taskId") + "\",\"buildStartTime\":\""+buildStartTime+"\",\"status\" : \"failure\",\"message\" : \"申请环境失败\"}";
						WebSocketServer.sendAllClient(resultJson, usernoRand); 
					}
//					params.put("id", IDFactory.getIDStr()); // 生成主键ID
					params.put("pipId", pipId);
					params.put("buildStartTime", buildStartTime);
					buildEndTime = DateTimeUtils.getFormatCurrentTime();// 获取结束时间
					params.put("buildEndTime", buildEndTime);
					params.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM3);
					params.put("buildUser", "root");
					buildTotalTime= DateTimeUtils.stringToTimeStamp(buildEndTime) - DateTimeUtils.stringToTimeStamp(buildStartTime);
					params.put("buildTotalTime", buildTotalTime);
					params.put("taskVersion", 0);
					params.put("applyUrl", "");
					if (jobList != null && jobList.size() > 0) { // 修改构建的job记录
						flag = taskManageMapper.updateSingleJobHistory(params);
					} else {// 插入构建的job记录
						flag = taskManageMapper.insertJobHistory(params);
					}
					return info;
				}else {
					jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"申请环境成功\",\"wsValue\":\"\"}";
					WebSocketServer.sendAllClient(jsonInfo, usernoRand);
				}
				// 获取访问应用的url
				applyUrl = this.getApplyUrl(maps);
				params.put("applyUrl", applyUrl);
			} else {
				params.put("applyUrl", applyUrl);
			}
			System.out.println("=======================单个执行任务开始执行job===========");
			Map<String, Object> jobInfo = JenkinsUtils.runJob(jobName);
			if (usernoRand != "") {
				if (!jobInfo.isEmpty()) { // 实时获取job日志
					
					taskVersion = (int) jobInfo.get("buildNumber"); // 任务版本号
					logger.info("updateSingleJobHistory...................................................................."+info);
					StringBuffer sb = new StringBuffer();
					sb.append(ApplyConstant.JOB_LOG_PATH).append(jobName).append("/builds/").append(taskVersion).append("/log");
					File logfile = new File(sb.toString()); 
					
					while(!logfile.exists()) {
						
					}
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
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始下载源码\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand); 
									}
									if (line.indexOf("ERROR: Subversion") != -1 || line.indexOf("ERROR: Failed") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"源码下载失败\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										flag = false;
									}
									if (line.indexOf("clean compile") != -1) {
										String jsonInfo1 = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"源码下载成功\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo1, usernoRand);
										Thread.sleep(1000);// 用于前台显示日志
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始编译源码\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("clean package") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"源码编译成功\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("COMPILATION ERROR") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"源码编译失败\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("T E S T S") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始单元测试\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("Results :") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"单元测试完成\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("Failures") != -1 && line.indexOf("Errors") != -1 && line.indexOf("Skipped") != -1 && line.indexOf("Time elapsed") == -1) {
										
										if (line.indexOf("Failures: 0") != -1 && line.indexOf("Errors: 0") != -1) {
											testResult = "成功";
											String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"单元测试：\",\"wsValue\":\""+testResult+"\"}";
											WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										}else {
											testResult =line.substring(line.indexOf("Failures"), line.indexOf(", Skipped"));
											String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"单元测试：\",\"wsValue\":\""+testResult+"\"}";
											WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										}
										
									}
									if (line.indexOf("test failures") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"单元测试：\",\"wsValue\":\"单元测试失败\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										flag = false;
									}
									if (line.indexOf("Packaging webapp") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始打war包\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("Finished: SUCCESS") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"打war包成功\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										
										warName = JenkinsUtils.getWarName(jobName);
										warName = warName +".war";
										flag = false;
									}
									if (line.indexOf("Finished: FAILURE") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"构建失败\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										flag = false;
									}
								}
								
								if (TaskConstant.TASK_TYPE_ITEM2.equals(taskType)) {
									if (line.indexOf("Started by user") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始部署war包\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
									}
									if (line.indexOf("Finished: SUCCESS") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"部署war包完成\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										flag = false;
									}
									if (line.indexOf("Finished: FAILURE") != -1) {
										String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"部署war包失败\",\"wsValue\":\"\"}";
										WebSocketServer.sendAllClient(jsonInfo, usernoRand);
										flag = false;
									}
								}
								// 实时向前台返job执行日志
								String jobLog = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"taskType\" : \""+taskType+"\",\"logInfo\":\""+line+"\"}";
								WebSocketServer.sendAllClient(jobLog, usernoRand);								
								line = null;  
								filePointer = file.getFilePointer();  
							}  
						}  
					}
				}
				// 自动化测试
				if (TaskConstant.TASK_TYPE_ITEM3.equals(taskType)) {
					String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\""+pipId+"\",\"taskId\":\""+maps.get("taskId")+"\",\"wsKey\":\"开始自动化测试\",\"wsValue\":\"\"}";
					WebSocketServer.sendAllClient(jsonInfo, usernoRand);
				}
			}
			
            params.put("pipId", pipId);
			params.put("buildStartTime", buildStartTime);
			// 自动化测试
			if (TaskConstant.TASK_TYPE_ITEM3.equals(taskType)) {
				params.put("jobName", jobName);
				if (jobList != null && jobList.size() > 0) {
					Map<String, Object> atMap = jobList.get(0);
					String autoTestHistoryId = (String) atMap.get("ID");
					maps.put("id", autoTestHistoryId);
					if (atMap.get("BUILD_STATUS") != null && !"".equals(atMap.get("BUILD_STATUS"))) {
						// 清空自动化测试历史记录中已有的状态
						flag = taskManageMapper.cleanAutoTestByStatus(params);
						if (flag) {
							flag = this.executeOuteTest(maps);// 执行自动化测试接口
						}
					} else {
						flag = this.executeOuteTest(maps);// 执行自动化测试接口
					}
				} else {
//					String id = IDFactory.getIDStr();// 自动化测试历史记录id
//					params.put("id", id);
					params.put("pipId", pipId);
					params.put("pipStepTaskId", pipStepTaskId);
					params.put("buildStartTime", buildStartTime);
					params.put("buildEndTime", "");
					params.put("buildStatus", "");
					params.put("buildUser", "root");
					params.put("buildTotalTime", "");
					params.put("pipVersion", pipVersion);
					params.put("taskVersion", "");
					params.put("applyUrl", "");
					// 插入自动化测试历史记录
					flag = taskManageMapper.insertJobHistory(params);
					if (flag) {
						maps.put("id", jobHistoryId);
						flag = this.executeOuteTest(maps);// 执行自动化测试接口
					}
				}
				if (!flag) {
					info.put("status", "failure");
					info.put("message", "执行自动化测试失败！");
					return info;
				}
				info.put("flag", flag);
			}
			
			// 获取构建后的job信息
			jobInfo= JenkinsUtils.jobInfo(jobName, taskVersion);
			
			if (!jobInfo.isEmpty()) {
//				params.put("id", IDFactory.getIDStr()); // 生成主键ID
				buildEndTime = DateTimeUtils.getFormatCurrentTime();
				params.put("buildEndTime", buildEndTime);
				String status = (String) jobInfo.get("flag"); // 获取job执行成功或失败的状态
				if (TaskConstant.JOB_BUILD_STATUS_SUCCESS.equals(status)) {
					buildStatus = TaskConstant.JOB_RESULTS_ITEM2;// 成功
					params.put("buildStatus", buildStatus);
				} else {
					buildStatus = TaskConstant.JOB_RESULTS_ITEM3;// 失败
					params.put("buildStatus", buildStatus);
				}

				params.put("buildUser", "root");
//				buildTotalTime = (long) jobInfo.get("duration"); // 耗时
				buildTotalTime= DateTimeUtils.stringToTimeStamp(buildEndTime) - DateTimeUtils.stringToTimeStamp(buildStartTime);
				params.put("buildTotalTime", buildTotalTime);
				params.put("taskVersion", taskVersion);
				if (jobList != null && jobList.size() > 0) { // 修改构建的job记录
					flag = taskManageMapper.updateSingleJobHistory(params);
				} else {// 插入构建的job记录
					flag = taskManageMapper.insertJobHistory(params);
				}

				// 查询构建job时处于失败状态的
				List<Map<String, Object>> failureInfo = taskManageMapper.getBuildJobFailure(params);
				if (failureInfo != null && failureInfo.size() > 0) {
					Map<String, Object> map = failureInfo.get(0);
					String stepscount = map.get("STEPSCOUNT").toString(); // 流水线步骤数量
					String buildcount = map.get("BUILDCOUNT").toString(); // 流水线构建任务数量
					// 当流水线步骤数量等于流水线构建任务数量时，说明当前流水线上的任务全部已执行成功
					if (stepscount.equals(buildcount)) {
						params.put("buildEndTime", DateTimeUtils.getFormatCurrentTime());
						params.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM2);
						// 修改流水线的基本信息
						flag = taskManageMapper.updatePipHistory(params);
					}
				}
				// 单个job执行失败时更新流水线的信息
				if (TaskConstant.JOB_RESULTS_ITEM3.equals(buildStatus)) {
					params.put("buildEndTime", DateTimeUtils.getFormatCurrentTime());
					params.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM3);
					// 修改流水线的基本信息
					flag = taskManageMapper.updatePipHistory(params);
				}
			}
			String status = "";
			String message = "";
			if (flag) {
				status = "success";
				if(buildStatus.equals("2")){
					message = "执行成功！";
				}else{
					message = "执行失败！";
				}
//				info.put("status", "success");
//				info.put("message", "执行成功！");
//				info.put("buildStatus", buildStatus);
//				info.put("pipVersion", pipVersion);
//				info.put("buildTotalTime", buildTotalTime);
//				info.put("applyUrl", applyUrl);
//				info.put("taskVersion", taskVersion);
//				info.put("warName", warName);
//				info.put("testResult", testResult);
			} else {
				status = "failure";
				message = "执行失败！";
//				info.put("status", "failure");
//				info.put("message", "执行失败！");
//				info.put("buildStatus", buildStatus);
//				info.put("pipVersion", pipVersion);
//				info.put("buildTotalTime", buildTotalTime);
//				info.put("applyUrl", applyUrl);
//				info.put("taskVersion", taskVersion);
//				info.put("warName", warName);
//				info.put("testResult", testResult);
			}
			info.put("status", status);
			info.put("message", message);
			info.put("buildStatus", buildStatus);
			info.put("pipVersion", pipVersion);
			info.put("buildTotalTime", buildTotalTime);
			info.put("applyUrl", applyUrl);
			info.put("taskVersion", taskVersion);
			info.put("warName", warName);
			info.put("testResult", testResult);
			
			if (usernoRand != "") {
				if (buildStatus != "") {
					String resultJson = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\""
							+ maps.get("taskId") + "\",\"taskType\" : \"" + taskType + "\",\"status\" : \"" + status
							+ "\",\"message\" : \"" + message + "\",\"buildStatus\" : \"" + buildStatus + "\",\"buildStartTime\" : \""+buildStartTime
							+ "\",\"pipVersion\" : \"" + pipVersion + "\",\"buildTotalTime\" : \"" + buildTotalTime + "\",\"buildEndTime\" : \"" + buildEndTime 
							+ "\",\"applyUrl\" : \"" + applyUrl + "\",\"taskVersion\" : \"" + taskVersion
							+ "\",\"warName\" : \"" + warName + "\",\"jobName\" : \"" + jobName + "\",\"testResult\" : \""
							+ testResult + "\"}";
					WebSocketServer.sendAllClient(resultJson, usernoRand);
				}else {
					if(!TaskConstant.TASK_TYPE_ITEM3.equals(taskType)){
						buildEndTime = DateTimeUtils.getFormatCurrentTime();
						String resultJson = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\""+ maps.get("taskId") + "\",\"buildStartTime\":\""+buildStartTime+"\",\"buildEndTime\" : \""+buildEndTime+"\",\"status\" : \"failure\",\"message\" : \"执行任务不存在\"}";
						WebSocketServer.sendAllClient(resultJson, usernoRand);
					}
					
				}
			}
		} else {
			info.put("status", "failure");
			info.put("message", "执行失败！");
			if (usernoRand != "") {
				String resultJson = "{\"pipeStatus\" : \"3\",\"pipId\":\"" + pipId + "\",\"taskId\":\""
						+ maps.get("taskId") + "\",\"buildStartTime\":\""+buildStartTime+"\",\"status\" : \"failure\",\"message\" : \"执行失败\"}";
				WebSocketServer.sendAllClient(resultJson, usernoRand);
			}
		}
		return info;
	}

	@Override
	public Map<String, Object> addTomcatServer(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, String> mapDeploy = new HashMap<String, String>();
		boolean flag = false;
		String random = req.getParameter("random");
		String jobId = "";
		String historyId = "";
		if(random != null && !"".equals(random)){
			 jobId = random.substring(0, random.indexOf(";"));
			 historyId = random.substring(random.indexOf(";")+1,random.length());
		}
		String appId = req.getParameter("appId");//changeAt 01.29
//		params.put("id", jobId); changeAt 01.29
		String responseData = req.getParameter("responseData");
		if (responseData != null && responseData.length() != 0) {
			responseData = responseData.substring(1, responseData.length() - 1);
			String tomcat_ip = "";
			String tomcat_port = "";
			String node_account_number = "admin";//changeAt 01.29
			String node_password = "admin";//暂时写死  将来caas提供
			JSONObject responseJson = JSONObject.fromObject(responseData);
			Map<?, ?> m = responseJson;
			Object info = m.get("urlArray");
			JSONArray info2 = (JSONArray) info;
			tomcat_ip = info2.getJSONObject(0).getString("nodeIp");
			tomcat_port = info2.getJSONObject(0).getString("nodePort"); // port
																		// nodePort
			String tomcatUrl = "http://" + tomcat_ip + ":" + tomcat_port;
			params.put("tomcat_ip", tomcat_ip);
			params.put("tomcat_port", tomcat_port);

			// //将tomcatUrl插入到部署job的config.xml中
			String pathHeader = TaskManageServiceImpl.class.getResource("/").toString();
			int Q = pathHeader.indexOf("/");// 找到第一个位置
			int E = pathHeader.indexOf("/WEB-INF/classes");// 找到最后一个位置
			pathHeader = pathHeader.substring(Q, E);
			String xmlPath2 = pathHeader + "/config/deployConfig.xml";
			String deploy_name = taskManageMapper.queryDeployName(jobId);// 根据部署任务的id查询部署任务的name
			String real_deploy_name = taskManageMapper.queryRealDeployName(jobId);// 根据部署任务的id查询部署任务的name(显示的name)
			String pip_name = real_deploy_name.substring(0, real_deploy_name.length() - 7);// 流水线名称
			String build_name = taskManageMapper.queryBuildName(jobId);// 根据部署任务的id查询构建任务的name
			String tomcatcredentialID = historyId; //taskManageMapper.queryNodeId(jobId);// 唯一标识 changeAt 01.29
			JenkinsUtils.createCredential(tomcatcredentialID, node_account_number, node_password);//生成tomcat账号密码 changeAt 01.29
			// List<Map<String, String>> tomcatList =
			// taskManageMapper.queryTomcatInfo2(jobId);//查询tomcat账号，密码
			String tomcat_account = node_account_number;// tomcat账号changeAt 01.29
			String tomcat_password = node_password;// tomcat密码changeAt 01.29
			String email = taskManageMapper.queryEmail(jobId);// 查询邮箱
			// String email = "123@ultrapower.com.cn";
			String emailSubject = pip_name + "-部署任务执行情况";
			String emailContext = "邮件内容";
			// String pName = deploy_name.substring(0,deploy_name.length()-7);
			mapDeploy.put("job1Name", build_name);
			mapDeploy.put("tomcat_url", tomcatUrl);
			mapDeploy.put("tomcat_username", tomcat_account);
			mapDeploy.put("tomcat_password", tomcat_password);
			mapDeploy.put("tomcatcredentialID", tomcatcredentialID);
			mapDeploy.put("templatePath", xmlPath2);
			mapDeploy.put("xmlPath2", xmlPath2);
			mapDeploy.put("emailUser", email);
			mapDeploy.put("emailSubject", emailSubject);
			mapDeploy.put("emailContext", emailContext);
			String job2Xml = PackJobUtils.changeXml(mapDeploy);
			// 创建job2
			JenkinsUtils.updateJob(deploy_name, job2Xml);// 修改job2
			// 更改job之后再入库
//			boolean flag2 = taskManageMapper.updateNodeInfo(params);
//			logger.info("--更行ip及port---"+flag2);
			String nodeInfoId = IDFactory.getIDStr();
			params.put("nodeInfoId", nodeInfoId);
			params.put("historyId", historyId);
			params.put("appId", appId);
			params.put("tomcat_account", node_account_number);
			params.put("tomcat_password", node_password);
			taskManageMapper.insertNodeInfo(params);
			flag = true;
		} else {
			logger.info("回调函数返回结果为空");
			flag = false;
		}
		//
		// }
		// }
		if (flag) {
			map.put("result", "success");
		} else {
			map.put("result", "failure");
		}
		return map;
	}

	@Override
	public Map<String, Object> queryPipHistoryList(HttpServletRequest req) {
		String pipId = req.getParameter("pip_id");
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询流水线所有的执行历史版本
		List<Map<String, String>> vList = taskManageMapper.queryPipVersionList(pipId);
		if (vList != null && vList.size() > 0) {
			for (int i = 0; i < vList.size(); i++) {
				Map<String, String> vmap = vList.get(i);
				String pip_version = vmap.get("PIP_VERSION");

				Map<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put("pipId", pipId);
				paraMap.put("pip_version", pip_version);
				// 查询流水线历史版本信息
				List<Map<String, String>> taskList = taskManageMapper.queryRealHistoryByVersion(paraMap);
				if(taskList != null && taskList.size() > 0){
					for (int j = 0; j < taskList.size(); j++) {
						Map<String, String> itemMap = taskList.get(j);
						itemMap.put("PIP_VERSION", pip_version);
						String task_id = itemMap.get("TASK_ID");
						String task_type = itemMap.get("TASK_TYPE");
						if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
							//查询构建任务信息
							Map<String, String> buildItem = buildMapper.getBuildById(task_id);
							if(buildItem != null){
								itemMap.put("BUILD_ENVIRONMENT", buildItem.get("BUILD_ENVIRONMENT"));
								itemMap.put("BUILD_TYPE", buildItem.get("BUILD_TYPE"));
								itemMap.put("BUILD_NAME", buildItem.get("BUILD_NAME"));
								itemMap.put("REAL_BUILD_NAME", buildItem.get("REAL_BUILD_NAME"));
//								String warName = JenkinsUtils.getWarName(buildItem.get("REAL_BUILD_NAME"));
								itemMap.put("WARNAME", "ci.war");
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
							//查询 部署任务信息
							Map<String, String> deployItem = deployMapper.getDeployById(task_id);
							if(deployItem != null){
								itemMap.put("DEPLOY_NAME", deployItem.get("DEPLOY_NAME"));
								itemMap.put("MOULD_ID", deployItem.get("MOULD_ID"));
								itemMap.put("REAL_DEPLOY_NAME", deployItem.get("REAL_DEPLOY_NAME"));
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
							//查询自动化测试信息
							Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
							if(autoTestItem != null){
								itemMap.put("TEST_NAME", autoTestItem.get("TEST_NAME"));
								itemMap.put("TEST_TYPE", autoTestItem.get("TEST_TYPE"));
							}
						}
					}
				}
				map.put("pip_version" + pip_version, taskList);
			}
		}
		map.put("status", "success");
		map.put("vList", vList);
		return map;
	}

	/**
	 * 获取部署之后的应用url
	 * 
	 * @param pipId
	 * @param pipVersion
	 * @param pipStepTaskId
	 * @return
	 */
	private String getApplyUrl(Map<String, Object> params) {
		StringBuffer url = new StringBuffer();
		String buildName = "";// 构建名称
		String warName = "";// war名称
		try {
			if (params.get("taskId") != null && !"".equals(params.get("taskId"))) {
				// 查询构建job的名称
				List<Map<String, Object>> buildInfo = deployMapper.getBuildByJobName(params);
				if (buildInfo != null && buildInfo.size() > 0) {
					Map<String, Object> buildMap = buildInfo.get(0);
					if (buildMap.get("BUILD_NAME") != null) {
						buildName = buildMap.get("BUILD_NAME").toString();
					}
				}
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
						nodeList = deployMapper.getNodeInfo(params);
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
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return url.toString();
	}

	/**
	 * 申请机器
	 * 
	 * @param request
	 * @return
	 */
	private boolean applicationMachine(Map<String, Object> params) {
		boolean flag = false;
		String taskId = ""; // 部署id
		String appName = "";//流水线名称
		String appVersion = "";//版本号
		String jobHistoryId = ""; // 任务历史id
		try {
			if (params.get("taskId") != null && !"".equals(params.get("taskId")) && params.get("jobHistoryId") != null && !"".equals(params.get("jobHistoryId"))) {
				taskId = params.get("taskId").toString();
				appName = params.get("pipName").toString();
			    appVersion  = params.get("pipVersion").toString();
				jobHistoryId = params.get("jobHistoryId").toString();
				String localhostPath = ApplyConstant.LOCATION_TOMCAT_IP +":"+ ApplyConstant.LOCATION_TOMCAT_PORT;
				String projectName = (String)params.get("projectName");
				String callbackUrl = localhostPath + projectName +"/taskManage/addTomcatServer.do";
				String caas_ip = ApplyConstant.CAAS_IP;
				String caas_port = ApplyConstant.CAAS_PORT;
				String url = caas_ip + ":" + caas_port + "/szty-web/v2/app/createContainer.do";
				Map<String, Object> temMap=new HashMap<String, Object>();
				JSONArray jsonarray=new JSONArray ();
				temMap.put("appName",appName);
				temMap.put("appVersion",appVersion);
				temMap.put("evnId","appdemo");
				temMap.put("appDescription","应用描述");
				temMap.put("serviceArray",jsonarray);
				JSONObject mapObject=JSONObject.fromObject(temMap);
				String jsonData = mapObject.toString();
				Map<String, String> map = new HashMap<String, String>();
				map.put("appKey", "");
				map.put("random", taskId +";"+ jobHistoryId);
				map.put("jsonData", jsonData);
				map.put("callbackUrl", callbackUrl);
				map.put("sig", "");
				flag = HttpClientUtils.doPost(url, map);
			} else {
				logger.info("任务ID为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @time 2017-12-19
	 * @author tangyongchun
	 * @description 执行自动化测试
	 * @param request
	 * @param params2 
	 * @return
	 */
	private boolean executeOuteTest(Map<String, Object> maps) {
		boolean flag = false;
		String taskId = ""; // 自动测试表主键id
		try {
			String localhostPath = ApplyConstant.LOCATION_TOMCAT_IP +":"+ ApplyConstant.LOCATION_TOMCAT_PORT;
			String projectName = (String)maps.get("projectName"); // 应用名称
			String callbackUrl = localhostPath + projectName +"/taskManage/saveAutoTestHistory.do";
			// 回调获取自动化测试日志
			String callbackUrlLog = localhostPath + projectName + "/taskManage/autoTestLogByWs.do";
			String autoTest_ip = ApplyConstant.AUTOTEST_IP;
			String autoTest_port = ApplyConstant.AUTOTEST_PORT;
			String url = autoTest_ip + ":" + autoTest_port + "/atp/service/rest/v1/tasks/operate";
		
			if (maps.get("taskId") != null && !"".equals(maps.get("taskId"))) {
				taskId = maps.get("taskId").toString();
//				Map<String, Object> taskMap = taskManageMapper.getDeployHistroyByUrl(taskId);
				Map<String,Object> pmap = new HashMap<String,Object>();
				pmap.put("pipId", maps.get("pipId"));
				pmap.put("pipVersion", maps.get("pipVersion"));
				pmap.put("taskType", TaskConstant.TASK_TYPE_ITEM2);
				//查询改流水线历史版本中部署任务关联的节点IP和端口
				Map<String, Object> taskMap = taskManageMapper.getDeployIpAndPortByUrl(pmap);
				if (taskMap != null) {
					String nodeIp = (String)taskMap.get("NODE_IP");
					String nodePort = (String)taskMap.get("NODE_PORT");
					String autoTestId = (String) maps.get("autoTestId");
					
					JSONObject paramJson = new JSONObject();
					String[] autoTestTaskId = {autoTestId}; 
					paramJson.put("userName", "niewei1");
					paramJson.put("taskId", autoTestTaskId);
					paramJson.put("applyUrl", nodeIp +":"+ nodePort);
					paramJson.put("operate", "execute");
					paramJson.put("callbackUrl", callbackUrl);
					JSONObject callbackParam = new JSONObject();
					callbackParam.put("id", maps.get("id").toString());
					callbackParam.put("pipId", maps.get("pipId").toString());
					callbackParam.put("pipName", maps.get("pipName").toString());
					callbackParam.put("usernoRand", maps.get("usernoRand"));
					callbackParam.put("callbackUrlLog", callbackUrlLog);
					paramJson.put("callbackParam", callbackParam.toString());
		 			
					logger.info("---请求参数---"+paramJson.toString());
					String responseParam = HttpClientUtils.doPostJson(url,paramJson.toString());
					logger.info("---响应参数---"+paramJson.toString());
					paramJson.clear();
					paramJson =JSONObject.fromObject(responseParam);
					// 获取请求成功的状态
					String code = (String)paramJson.get("code");
					if ("200".equals(code)) {
						flag = true;
					}else {
						flag = false;
					}
					logger.info("执行自动化平台接口成功！");
				}else {
					logger.info("---应用url为空---");
				}
			}else {
				logger.info("任务taskId为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@Override
	public String queryTaskLoginfo(HttpServletRequest req) {
		String task_version = req.getParameter("task_version");
		String task_name = req.getParameter("task_name");
		String result = JenkinsUtils.getJobBuildLogs(task_name, Integer.parseInt(task_version));
		return result;
	}

	/**
	 * 读取配置文件
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName) {
		Properties prop = new Properties();
		InputStream in;
		try {
			in = JenkinsUtils.class.getResourceAsStream("/sysConfig.properties");
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prop.getProperty(propertyName).trim();
	}

	@Override
	public Map<String, Object> timerExecution(Map<String,Object> pmap) {
		Map<String, Object> map = new HashMap<String, Object>();
		String status = "false";
		String pipId = pmap.get("pipId").toString();
		String executionMode = pmap.get("executionMode").toString();
		//1、将原有定时任务删除（添加定时任务时，也要先删除原有定时任务）
		SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
		Scheduler sche = null;
		try {
			sche = gSchedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String jobName = "pipeJobName_" + pipId;
		QuartzManager.removeJob(sche, jobName);
		//2、添加定时任务
		if(executionMode.equals("1")){//只删除定时任务
			status = "true";
		}else{//添加定时任务
			String timed_cron = pmap.get("timed_cron").toString();
			if(timed_cron != null){
				PipeJobFactory.startPipeByQuartz(pipId,timed_cron);
				status = "true";
			}
		}
		//3、修改流水线执行方式executionMode
		int k = taskManageMapper.updateExecutionModeById(pmap);
		if(k > 0){
			status = "true";
		}
		map.put("status", status);
		return map;
	}

	@Override
	public Map<String, Object> deletePipeliningById(HttpServletRequest req) {
		logger.info("delete***********************24");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		String pipId = req.getParameter("pipId");
		String id = pipId;
		//先删除jenkins中的job
		String buildName = taskManageMapper.queryJenkinsBuildName(id);
		String deployName = taskManageMapper.queryJenkinsDeployName(id);
		logger.info("deleteName***********************25");
		boolean flagBuild = true;
		boolean flagDeploy = true;
		if(buildName != null){
			logger.info("***********************26");
			flagBuild = JenkinsUtils.deleteJobInfo(buildName);//根据jobName删除job
			logger.info("***********************27");
		}
		if(deployName != null){
			logger.info("***********************28");
			flagDeploy = JenkinsUtils.deleteJobInfo(deployName);//根据jobName删除job
			logger.info("***********************29");
			}
		logger.info("jenkinsJob删除情况buildJob:"+flagBuild+"   deployJob:"+flagDeploy);
		//调用caas接口删除应用
		//根据流水线id查询所有节点唯一标识
//		List<Map<String, String>> appIdList = taskManageMapper.selectTaskId(id);
//		根据流水线id查询所有random 即部署任务的id
		List<Map<String, String>> appIdList = taskManageMapper.selectTaskId2(id);
		for(int i = 0;i<appIdList.size();i++){
			String random = appIdList.get(i).get("TASK_ID")+ ";" + appIdList.get(i).get("ID");
			Map<String, String> deleteMap = new HashMap<String, String>();
			deleteMap.put("appKey", "");
			deleteMap.put("random", random);
			deleteMap.put("sig", "");
			String caas_ip = ApplyConstant.CAAS_IP;
			String caas_port = ApplyConstant.CAAS_PORT;
			String url = caas_ip + ":" + caas_port + "/szty-web/v2/app/deleteAppByRandom.do";
			boolean flag = HttpClientUtils.doPost(url, deleteMap);
		}
		//end
		logger.info("***********************30");
		if(flagBuild && flagDeploy){
			taskManageMapper.deleteTaskAutoTest(id);//删除自动化测试表信息
			taskManageMapper.deleteNode_info(pipId);// 删除节点表
			taskManageMapper.deletecode_repositories(pipId);// 删除代码仓库表
			taskManageMapper.deleteTask_build(pipId);// 删除构建任务表
			taskManageMapper.deleteTask_deploy(pipId);// 删除部署任务表
//			taskManageMapper.deletePipelining_steps_task(pipId);// 删除步骤任务关联表
//			taskManageMapper.deletePipelining_steps(pipId);// 删除流水线步骤表
			taskManageMapper.deletePipelining_task(pipId);//删除流水线任务关联表
			taskManageMapper.deletePipelining_info(pipId);// 删除流水线表
			taskManageMapper.deleteBuildHistory(pipId);// 删除流水线历史表
			logger.info("***********************31");
			//删除流水线定时任务 
			Map<String, Object> emap = new HashMap<String, Object>();
			emap.put("pipId", pipId);
			emap.put("executionMode", "1");
			this.timerExecution(emap);
			map.put("result", "success");
		}else{
			map.put("result", "failure");
		}
		logger.info("***********************32");
		return map;
	}
	@Override
	public Map<String, Object> deletePipeliningByPipId(String pipId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = pipId;
		//先删除jenkins中的job
		String buildName = taskManageMapper.queryJenkinsBuildName(id);
		String deployName = taskManageMapper.queryJenkinsDeployName(id);
		if(buildName != null){
			JenkinsUtils.deleteJobInfo(buildName);//根据jobName删除job
		}
		if(deployName != null){
			JenkinsUtils.deleteJobInfo(deployName);//根据jobName删除job
			}
		
//		根据流水线id查询所有random 即部署任务的id
		List<Map<String, String>> appIdList = taskManageMapper.selectTaskId2(id);
		for(int i = 0;i<appIdList.size();i++){
			String random = appIdList.get(i).get("TASK_ID")+ ";" + appIdList.get(i).get("ID");
			Map<String, String> deleteMap = new HashMap<String, String>();
			deleteMap.put("appKey", "");
			deleteMap.put("random", random);
			deleteMap.put("sig", "");
			String caas_ip = ApplyConstant.CAAS_IP;
			String caas_port = ApplyConstant.CAAS_PORT;
			String url = caas_ip + ":" + caas_port + "/szty-web/v2/app/deleteAppByRandom.do";
			boolean flag = HttpClientUtils.doPost(url, deleteMap);
		}
		//end
		
		taskManageMapper.deleteTaskAutoTest(id);//删除自动化测试表信息
		taskManageMapper.deleteNode_info(pipId);// 删除节点表
		taskManageMapper.deletecode_repositories(pipId);// 删除代码仓库表
		taskManageMapper.deleteTask_build(pipId);// 删除构建任务表
		taskManageMapper.deleteTask_deploy(pipId);// 删除部署任务表
//		taskManageMapper.deletePipelining_steps_task(pipId);// 删除步骤任务关联表
//		taskManageMapper.deletePipelining_steps(pipId);// 删除流水线步骤表
		taskManageMapper.deletePipelining_task(pipId);//删除流水线任务关联表
		taskManageMapper.deletePipelining_info(pipId);// 删除流水线表
		taskManageMapper.deleteBuildHistory(pipId);// 删除流水线历史表
		map.put("result", "success");
		return map;
	}

	@Override
	public Map<String, Object> saveAutoTestHistory(HttpServletRequest request) {
		// 封装响应结果集
		Map<String, Object> info = new HashMap<String, Object>();
		// 封装存储参数
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取请求参数
		Map<String, Object> maps = RequestUtils.getParameters(request);
		
		String paramJson = ""; //请求参数
		JSONObject jsonParam = null;
		String id = ""; // 自动化测试历史记录id
		String buildStatus = ""; // 测试状态
		long buildTotalTime = 0; // 耗时
		String taskVersion = ""; // 版本号
		String resultUrl = ""; // 测试日志url
		
		boolean flag = false;
		if (maps.get("paramJson") != null && !"".equals(maps.get("paramJson").toString())) {
			paramJson = maps.get("paramJson").toString();
			jsonParam = JSONObject.fromObject(paramJson);
			flag = true;
		}else {
			flag = false;
		}
		if (flag) {
			try {
				if (flag) {// 校验参数
					if (jsonParam.get("id") != "" && !"null".equals(jsonParam.get("id"))) {
						id = jsonParam.get("id").toString();
						flag = true;
					}else {
						flag = false;
					}
					if (flag) {
						if (jsonParam.get("buildStatus") != "" && !"null".equals(jsonParam.get("buildStatus"))) {
							buildStatus = jsonParam.get("buildStatus").toString();
							flag = true;
						}else {
							flag = false;
						}
					}
					/*if (flag) {
						if (jsonParam.get("buildTotalTime") != "" && !"null".equals(jsonParam.get("buildTotalTime"))) {
							buildTotalTime = jsonParam.get("buildTotalTime").toString();
							flag = true;
						}else {
							flag = false;
						}
					}*/
					if (flag) {
						if (jsonParam.get("taskVersion") != "" && !"null".equals(jsonParam.get("taskVersion"))) {
							taskVersion = jsonParam.get("taskVersion").toString();
							flag = true;
						}else {
							flag = false;
						}
					}
					if (flag) {
						if (jsonParam.get("resultUrl") != "" && !"null".equals(jsonParam.get("resultUrl"))) {
							resultUrl = jsonParam.get("resultUrl").toString();
							flag = true;
						}else {
							flag = false;
						}
					}
				}else {
					flag = false;
				}
				params.put("id", id);
				Map<String, Object> autoMap = taskManageMapper.getAutoTestById(params);
				String buildStartTime = "";
				String buildEndTime = "";
				if (autoMap.get("BUILD_START_TIME") != null) {
					buildStartTime = (String) autoMap.get("BUILD_START_TIME");
					buildEndTime = DateTimeUtils.getFormatCurrentTime();
					buildTotalTime= DateTimeUtils.stringToTimeStamp(buildEndTime) - DateTimeUtils.stringToTimeStamp(buildStartTime);
				}
				params.put("buildEndTime", buildEndTime);
				params.put("buildStatus", buildStatus);
				params.put("buildTotalTime", buildTotalTime);
				params.put("taskVersion", taskVersion);
				params.put("applyUrl", resultUrl);
				// 更新自动化测试历史记录
				flag= taskManageMapper.updateAutoTestHistory(params);
			} catch (Exception e) {
				params.put("buildStatus", TaskConstant.JOB_RESULTS_ITEM3);
				// 更新自动化测试历史记录
				flag= taskManageMapper.updateAutoTestHistory(params);
				e.printStackTrace();
			}
			if (flag) {
				info.put("status", "success");
				info.put("message", "修改自动化测试历史记录信息成功！");
			}else {
				info.put("status", "failure");
				info.put("message", "修改自动化测试历史记录信息失败！");
			}
		}else {
			info.put("status", "failure");
			info.put("message", "参数为空！");
		}
		return info;
	}

	@Override
	public List<Map<String, Object>> getPipliningListInfo(Map<String, Object> maps) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			result = taskManageMapper.getPipliningListInfo(maps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void emptyNodeInfo(String taskId) {
		taskManageMapper.emptyNodeInfo(taskId);
	}
	
	@Override
	public Map<String, Object> getPipliningListByProjectId(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		//查询流水线列表
		List<Map<String, String>> pipList = taskManageMapper.queryPipeliningList(pmap);
		if (pipList != null && pipList.size() > 0) {
			for (int i = 0; i < pipList.size(); i++) {
				Map<String,String> pipInfo = pipList.get(i);
				//获取流水线主键
				String pipId = pipInfo.get("ID");
				List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
				//获取流水线最大版本
				String pip_version = "";
				Map<String, String> vmap = taskManageMapper.getMaxPipVersionByPipId(pipId);
				if (vmap != null && vmap.get("MAX_PIP_VERSION") != null) {// 存在最大版本号，表示有构建历史
					pip_version = String.valueOf(vmap.get("MAX_PIP_VERSION"));
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("pipId", pipId);
					paraMap.put("pip_version", pip_version);
					// 查询流水线最大历史版本信息
					taskList = taskManageMapper.queryBuildHistoryByVersion(paraMap);
					//查询该版本下流水线信息
					Map<String,String> pvMap = taskManageMapper.queryPipVersionInfo(paraMap);
					pipInfo.put("BUILD_START_TIME", pvMap.get("BUILD_START_TIME"));//该历史流水线开始时间
					pipInfo.put("BUILD_END_TIME", pvMap.get("BUILD_END_TIME"));//该历史流水线结束时间
					pipInfo.put("BUILD_TOTAL_TIME", pvMap.get("BUILD_TOTAL_TIME"));//该版本流水线耗时
				} else {// 不存在构建历史时，查询流水线原有信息
					taskList = taskManageMapper.queryTaskListByPipId(pipId);
					pip_version = "0";
				}
					
				if(taskList != null && taskList.size() > 0){
					for (int j = 0; j < taskList.size(); j++) {
						Map<String, String> itemMap = taskList.get(j);
						itemMap.put("PIP_VERSION", pip_version);
						String task_id = itemMap.get("TASK_ID");
						String task_type = itemMap.get("TASK_TYPE");
						if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
							//查询构建任务信息
							Map<String, String> buildItem = buildMapper.getBuildById(task_id);
							if(buildItem != null){
								itemMap.put("BUILD_ENVIRONMENT", buildItem.get("BUILD_ENVIRONMENT"));
								itemMap.put("BUILD_TYPE", buildItem.get("BUILD_TYPE"));
								itemMap.put("BUILD_NAME", buildItem.get("BUILD_NAME"));
								itemMap.put("REAL_BUILD_NAME", buildItem.get("REAL_BUILD_NAME"));
//								String warName = JenkinsUtils.getWarName(buildItem.get("REAL_BUILD_NAME"));
								itemMap.put("WARNAME", "ci.war");
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
							//查询 部署任务信息
							Map<String, String> deployItem = deployMapper.getDeployById(task_id);
							if(deployItem != null){
								itemMap.put("DEPLOY_NAME", deployItem.get("DEPLOY_NAME"));
								itemMap.put("MOULD_ID", deployItem.get("MOULD_ID"));
								itemMap.put("REAL_DEPLOY_NAME", deployItem.get("REAL_DEPLOY_NAME"));
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
							//查询自动化测试信息
							Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
							if(autoTestItem != null){
								itemMap.put("TEST_NAME", autoTestItem.get("TEST_NAME"));
								itemMap.put("TEST_TYPE", autoTestItem.get("TEST_TYPE"));
							}
						}
					}
				}
				map.put("pipId_" + pipId, taskList);
				map.put("pip_version_" + pipId, pip_version);
			}
		}
		map.put("status", "success");
		map.put("pipList", pipList);
		return map;
	}

	@Override
	public Map<String, Object> getAutoTestHistory(Map<String, Object> params) {
		 
		return taskManageMapper.getAutoTestHistory(params);
	}

	@Override
	public Map<String, String> getMaxPipVersionByPipId(String pipId) {
		
		return taskManageMapper.getMaxPipVersionByPipId(pipId);
	}

	@Override
	public List<Map<String, String>> queryBuildHistoryByVersion(Map<String, Object> map) {
		
		return taskManageMapper.queryBuildHistoryByVersion(map);
	}

	@Override
	public List<Map<String, String>> queryTaskListByPipId(String pipId) {
		
		return taskManageMapper.queryTaskListByPipId(pipId);
	}

	@Override
	public Map<String, Object> autoTestLogByWs(HttpServletRequest req,String json) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		System.out.println(json);
		JSONArray jsonArray = JSONArray.fromObject(json);
		if(jsonArray != null && jsonArray.size() > 1){
			JSONObject obj1 = jsonArray.getJSONObject(0);
			String usernoRand = obj1.getString("usernoRand");
			JSONObject obj2 = jsonArray.getJSONObject(1);
			String pipId = obj2.getString("taskType");
			String taskId = obj2.getString("taskType");
			String taskType = obj2.getString("taskType");
			String pipeStatus = obj2.getString("taskType");
			String logInfo = obj2.getString("taskType");
			String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"" + pipId + "\",\"taskId\":\"" + taskId + "\",\"wsKey\":\"" + logInfo + "\",\"wsValue\":\"\"}";
			WebSocketServer.sendAllClient(jsonInfo, usernoRand);
		}
//		String usernoRand = req.getParameter("usernoRand");
//		String jsonInfo = req.getParameter("logMessage");
//		String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"a1f6cdd33c3b4422bb6e5c2f604a66da\",\"taskId\":\"69f6e94a2322446fa4b8c96c68f08d7f\",\"taskType\" : \"3\",\"logInfo\":\"自动化测试日志\"}";
//		WebSocketServer.sendAllClient(jsonInfo, usernoRand);
//		try {
//			for (int i = 0; i < 10; i++) {
////				String jsonInfo = "这是一个测试" + i;
//				String jsonInfo = "{\"pipeStatus\" : \"2\",\"pipId\":\"a1f6cdd33c3b4422bb6e5c2f604a66da\",\"taskId\":\"69f6e94a2322446fa4b8c96c68f08d7f\",\"taskType\" : \"3\",\"logInfo\":\"执行日志 " + i + "\"}";
////				WebSocketServer.sendAllClient(jsonInfo, usernoRand);
//				boolean flag = WebSocketServer.sendAllClient(jsonInfo, usernoRand);
////				boolean flag = WebSocketServer.sendAllClient(jsonInfo, usernoRand);
//				if(flag){
//					Thread.sleep(3000);
//				}else{
//					break;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		resultMap.put("result", "true");
		return resultMap;
	}

	@Override
	public Map<String, Object> queryPipList(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
		int pageSize = Integer.parseInt(req.getParameter("pageSize"));
		int scol = 1 + (pageSize * (pageNum - 1));  //开始条数
        int ecol = pageSize * pageNum;  //结束条数
        System.out.println(scol+"----"+ecol);
        paramMap.put("i", scol);
        paramMap.put("j", ecol);
		List<Map<String, String>> pipMap = taskManageMapper.queryPipList(paramMap);
		int pipCount = taskManageMapper.queryPipCount();
		//共几页
		int total = 0;
		if(pipCount % pageSize == 0){
			total = pipCount / pageSize;
		}else{
			 total = pipCount / pageSize + 1;
		}
		resultMap.put("result", pipMap);
		resultMap.put("count", pipCount);
		resultMap.put("total", total);
		return resultMap;
	}

	@Override
	public Map<String, Object> queryPipInfo(HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String id = req.getParameter("id");
		paramMap.put("id", id);
		List<Map<String, String>> pipInfo = taskManageMapper.queryPipInfo(paramMap);
		resultMap.put("pipInfo", pipInfo);
		return resultMap;
	}
	
	/**
	 * 删除机器
	 * 
	 * @param request
	 * @return
	 */
//	private static boolean deleteContainer(String taskId ) {
//		boolean flag = false;
////		String taskId = taskId; // 部署id
//		try {
//			if (taskId != null && !"".equals(taskId)) {
//				String caas_ip = ApplyConstant.CAAS_IP;
//				String caas_port = ApplyConstant.CAAS_PORT;
//				String url = caas_ip + ":" + caas_port + "/szty-web/v2/app/deleteContainer.do";
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("random", taskId);
//				flag = HttpClientUtils.doPost(url, map);
//			} else {
//				logger.info("任务ID为空");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
}
