package com.ultrapower.ci.control.pipeManage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.common.utils.IDFactory;
import com.ultrapower.ci.common.utils.RequestUtils;
import com.ultrapower.ci.common.utils.cronUtils;
import com.ultrapower.ci.control.autoTestManage.dao.AutoTestMapper;
import com.ultrapower.ci.control.buildManage.dao.BuildMapper;
import com.ultrapower.ci.control.deployManage.dao.DeployMapper;
import com.ultrapower.ci.control.pipeManage.dao.PipeManageMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class PipeManageServiceImple implements IPipeManageService{
	
	@Autowired
	private PipeManageMapper pipeManageMapper;
	@Autowired
	private BuildMapper buildMapper;
	@Autowired
	private DeployMapper deployMapper;
	@Autowired
	private AutoTestMapper autoTestMapper;

	@Override
	public String getJenkinsCredential(String username, String password) {
		String CredentialId = "";
		List<Map<String, String>> list = pipeManageMapper.getJenkinsCredential(username, password);
		if(list != null && list.size() > 0){
			CredentialId = list.get(0).get("CREDENTIALID");
		}
		return CredentialId;
	}

	@Override
	public boolean addJenkinsCredential(String credentialId, String username, String password) {
		Map<String,Object> pmap = new HashMap<String,Object>();
		String jenkinsCredentialId = IDFactory.getIDStr();
		pmap.put("ID", jenkinsCredentialId);
		pmap.put("credentialId", credentialId);
		pmap.put("username", username);
		pmap.put("password", password);
		pmap.put("is_use", "1");
		pipeManageMapper.addJenkinsCredential(pmap);
		return true;
	}

	@Override
	public Map<String, Object> getPipliningListByProjectId(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(req);
		//查询流水线列表
		List<Map<String, String>> pipList = pipeManageMapper.queryPipeliningList(pmap);
		if(pipList != null && pipList.size() > 0){
			for (int i = 0; i < pipList.size(); i++) {
				Map<String,String> pipInfo = pipList.get(i);
				String pipId = pipInfo.get("ID");
				List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
				//获取流水线最大版本
				String pip_version = "";
				Map<String, String> vmap = pipeManageMapper.getMaxPipVersionByPipId(pipId);
				if (vmap != null && vmap.get("MAX_PIP_VERSION") != null) {// 存在最大版本号，表示有构建历史
					pip_version = String.valueOf(vmap.get("MAX_PIP_VERSION"));
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("pipId", pipId);
					paraMap.put("pip_version", pip_version);
					// 查询流水线最大历史版本信息
					taskList = pipeManageMapper.queryBuildHistoryByVersion(paraMap);
					
					//查询该版本下流水线信息
					Map<String,String> pvMap = pipeManageMapper.queryPipVersionInfo(paraMap);
					if(pvMap != null){
						pipInfo.put("build_start_time", pvMap.get("BUILD_START_TIME"));//该历史流水线开始时间
						pipInfo.put("build_end_time", pvMap.get("BUILD_END_TIME"));//该历史流水线结束时间
						if (pvMap.get("BUILD_TOTAL_TIME") != null) {
							String build_total_time = DateTimeUtils
									.timeStampToSecond(Long.valueOf(pvMap.get("BUILD_TOTAL_TIME")));
							pipInfo.put("build_total_time", build_total_time);// 该版本流水线耗时
						}
						pipInfo.put("build_status", pvMap.get("BUILD_STATUS"));//该流水线状态
					}
					
					System.out.println(pipId + "存在最大版本号，表示有构建历史");
				}else {// 不存在构建历史时，查询流水线原有信息
					taskList = pipeManageMapper.queryTaskListByPipId(pipId);
					pip_version = "0";
					System.out.println(pipId + "不存在构建历史时，查询流水线原有信息");
				}
				
				StringBuffer teskJson = new StringBuffer("[");
				if(taskList != null && taskList.size() > 0){
					for (int j = 0; j < taskList.size(); j++) {
						Map<String, String> itemMap = taskList.get(j);
//						itemMap.put("PIP_VERSION", pip_version);
						String piptask_id = itemMap.get("PIPTASK_ID");
						String task_id = itemMap.get("TASK_ID");
						String task_type = itemMap.get("TASK_TYPE");
						List<Map<String,String>> proList = new ArrayList<Map<String,String>>();
						if(!pip_version.equals("0")){//存在构建历史时，查询产出物
							String build_history_id = itemMap.get("BUILD_HISTORY_ID");
							proList = pipeManageMapper.getProductByBuildHistoryId(build_history_id);
						}
						JSONObject taskInfo = new JSONObject();
						if(task_type == null){
							taskInfo = null;
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM0)){//子流水线
							String childLineInfo = this.getChildLineInfo(task_id,pipId,pip_version);
							taskInfo.put("childLineInfo", childLineInfo);
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
							//查询构建任务信息
							Map<String, String> buildItem = buildMapper.getBuildById(task_id);
							if(buildItem != null){
								taskInfo = JSONObject.fromObject(buildItem);
//								String warName = JenkinsUtils.getWarName(buildItem.get("REAL_BUILD_NAME"));
//								taskInfo.put("WARNAME", warName);
								String warName = "";
								for (int k = 0; k < proList.size(); k++) {
									Map<String,String> proMap = proList.get(k);
									if(proMap != null && proMap.get("PRODUCT_KEY").equals("warName")){
										warName = proMap.get("PRODUCT_VALUE");
									}
								}
								taskInfo.put("WARNAME", warName);
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
							//查询 部署任务信息
							Map<String, String> deployItem = deployMapper.getDeployById(task_id);
							if(deployItem != null){
								taskInfo = JSONObject.fromObject(deployItem);
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
							//查询自动化测试信息
							Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
							if(autoTestItem != null){
								taskInfo = JSONObject.fromObject(autoTestItem);
							}
						}
						if(taskInfo != null){
							taskInfo.put("pipId", pipId);
							taskInfo.put("piptask_id", piptask_id);
							taskInfo.put("task_id", task_id);
							taskInfo.put("task_type", task_type);
							taskInfo.put("pip_version", pip_version);
							taskInfo.put("build_status", itemMap.get("BUILD_STATUS"));
							taskInfo.put("task_version", itemMap.get("TASK_VERSION"));
							taskInfo.put("apply_url", itemMap.get("APPLY_URL"));
							taskInfo.put("node_ip", itemMap.get("NODE_IP"));
							taskInfo.put("node_port", itemMap.get("NODE_PORT"));
							
							taskInfo.put("build_start_time", itemMap.get("BUILD_START_TIME"));
							taskInfo.put("build_end_time", itemMap.get("BUILD_END_TIME"));
							String build_total_time = itemMap.get("BUILD_TOTAL_TIME");
							if (build_total_time != null) {
								taskInfo.put("build_total_time",
										DateTimeUtils.timeStampToSecond(Long.valueOf(build_total_time)));
							}
							
							teskJson.append("{\"task_type\" : \"" + task_type + "\",\"taskInfo\" : " + taskInfo.toString() + "},");
						}
					}
					teskJson.deleteCharAt(teskJson.length() - 1);
				}
				teskJson.append("]");
				map.put("pipId_" + pipId, teskJson.toString());
				map.put("pip_version_" + pipId, pip_version);
			}
		}
		map.put("status", "success");
		map.put("pipList", pipList);
		return map;
	}

	/**
	 * 获取子流水线下任务信息
	 * @param pipId
	 * @return
	 */
	private String getChildLineInfo(String pipId,String parentPipId,String pip_version) {
		// 获取流水线下任务列表
		List<Map<String, String>> taskList = new ArrayList<Map<String, String>>();
		if(pip_version.equals("0")){//没有执行历史
			taskList = pipeManageMapper.queryTaskListByPipId(pipId);
		}else{
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pipId", pipId);
			paraMap.put("pip_version", pip_version);
			paraMap.put("parentPipId", parentPipId);
			// 查询流水线最大历史版本信息
			taskList = pipeManageMapper.queryBuildHistoryByVersion(paraMap);
		}
		JSONArray chidLineArr = new JSONArray();
		if(taskList != null && taskList.size() > 0){
			for (int j = 0; j < taskList.size(); j++) {
				Map<String, String> itemMap = taskList.get(j);
				String pip_name = itemMap.get("PIP_NAME");
				String piptask_id = itemMap.get("PIPTASK_ID");
				String task_id = itemMap.get("TASK_ID");
				String task_type = itemMap.get("TASK_TYPE");
				List<Map<String,String>> proList = new ArrayList<Map<String,String>>();
				if(!pip_version.equals("0")){//存在构建历史时，查询产出物
					String build_history_id = itemMap.get("BUILD_HISTORY_ID");
					proList = pipeManageMapper.getProductByBuildHistoryId(build_history_id);
				}
				
				JSONObject childTaskInfo = new JSONObject();
				if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
					//查询构建任务信息
					Map<String, String> buildItem = buildMapper.getBuildById(task_id);
					if(buildItem != null){
						childTaskInfo = JSONObject.fromObject(buildItem);
						childTaskInfo.put("childTaskName", "构建任务");
//						String warName = JenkinsUtils.getWarName(buildItem.get("REAL_BUILD_NAME"));
//						taskInfo.put("WARNAME", warName);
						String warName = "";
						for (int k = 0; k < proList.size(); k++) {
							Map<String,String> proMap = proList.get(k);
							if(proMap != null && proMap.get("PRODUCT_KEY").equals("warName")){
								warName = proMap.get("PRODUCT_VALUE");
							}
						}
						childTaskInfo.put("WARNAME", warName);
					}
				}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
					//查询 部署任务信息
					Map<String, String> deployItem = deployMapper.getDeployById(task_id);
					if(deployItem != null){
						childTaskInfo = JSONObject.fromObject(deployItem);
						childTaskInfo.put("childTaskName", "部署任务");
					}
				}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
					//查询自动化测试信息
					Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
					if(autoTestItem != null){
						childTaskInfo = JSONObject.fromObject(autoTestItem);
						childTaskInfo.put("childTaskName", "自动化测试任务");
					}
				}
				childTaskInfo.put("childPipName", pip_name);
				childTaskInfo.put("childPiptask_id", piptask_id);
				childTaskInfo.put("childTaskId", task_id);
				childTaskInfo.put("childTaskType", task_type);
				childTaskInfo.put("childBuildStatus", itemMap.get("BUILD_STATUS"));
				childTaskInfo.put("childBuildStartTime", itemMap.get("BUILD_START_TIME"));
				childTaskInfo.put("childTaskVersion", itemMap.get("TASK_VERSION"));
				childTaskInfo.put("apply_url", itemMap.get("APPLY_URL"));
				chidLineArr.add(childTaskInfo);
			}
		}
		return chidLineArr.toString();
	}

	@Override
	public List<Map<String, String>> getPipeTaskList(String pipId) {
		List<Map<String, String>> taskList = pipeManageMapper.queryTaskListByPipId(pipId);
		return taskList;
	}

	@Override
	public int addBuildHistory(Map<String, String> map) {
		int k = pipeManageMapper.addBuildHistory(map);
		return k;
	}

	@Override
	public int updateBuildHistory(Map<String, String> map) {
		int k = pipeManageMapper.updateBuildHistory(map);
		return k;
	} 

	@Override
	public Map<String, Object> queryPipHistoryList(HttpServletRequest req) {
		String pipId = req.getParameter("pip_id");
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询流水线所有的执行历史版本
		List<Map<String, String>> vList = pipeManageMapper.queryPipVersionList(pipId);
		if (vList != null && vList.size() > 0) {
			for (int i = 0; i < vList.size(); i++) {
				Map<String, String> vmap = vList.get(i);
				String pip_version = vmap.get("PIP_VERSION");
				Map<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put("pipId", pipId);
				paraMap.put("pip_version", pip_version);
				// 查询流水线历史版本信息
				List<Map<String, String>> taskList = pipeManageMapper.queryRealHistoryByVersion(paraMap);
				StringBuffer teskJson = new StringBuffer("[");
				if(taskList != null && taskList.size() > 0){
					for (int j = 0; j < taskList.size(); j++) {
						Map<String, String> itemMap = taskList.get(j);
						String task_id = itemMap.get("TASK_ID");
						String task_type = itemMap.get("TASK_TYPE");
						JSONObject taskInfo = new JSONObject();
						if(task_type == null){
							taskInfo = null;
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM0)){//子流水线
							String childLineInfo = this.getChildLineInfo(task_id,pipId,pip_version);
							taskInfo.put("childLineInfo", childLineInfo);
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM1)){//构建任务
							//查询构建任务信息
							Map<String, String> buildItem = buildMapper.getBuildById(task_id);
							if(buildItem != null){
								taskInfo = JSONObject.fromObject(buildItem);
//								String warName = JenkinsUtils.getWarName(buildItem.get("REAL_BUILD_NAME"));
//								taskInfo.put("WARNAME", warName);
								taskInfo.put("WARNAME", "ci.war");
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM2)){//部署任务
							//查询 部署任务信息
							Map<String, String> deployItem = deployMapper.getDeployById(task_id);
							if(deployItem != null){
								taskInfo = JSONObject.fromObject(deployItem);
							}
						}else if(task_type.equals(TaskConstant.TASK_TYPE_ITEM3)){//自动化测试任务
							//查询自动化测试信息
							Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
							if(autoTestItem != null){
								taskInfo = JSONObject.fromObject(autoTestItem);
							}
						}
						if(taskInfo != null){
							taskInfo.put("pipId", pipId);
							taskInfo.put("task_id", task_id);
							taskInfo.put("task_type", task_type);
							taskInfo.put("pip_version", pip_version);
							taskInfo.put("build_status", itemMap.get("BUILD_STATUS"));
							taskInfo.put("apply_url", itemMap.get("APPLY_URL"));
							taskInfo.put("node_ip", itemMap.get("NODE_IP"));
							taskInfo.put("node_port", itemMap.get("NODE_PORT"));
							
							taskInfo.put("build_start_time", itemMap.get("BUILD_START_TIME"));
							taskInfo.put("build_end_time", itemMap.get("BUILD_END_TIME"));
							String build_total_time = itemMap.get("BUILD_TOTAL_TIME");
							if (build_total_time != null) {
								taskInfo.put("build_total_time",
										DateTimeUtils.timeStampToSecond(Long.valueOf(build_total_time)));
							}
							taskInfo.put("task_version", itemMap.get("TASK_VERSION"));
							teskJson.append("{\"task_type\" : \"" + task_type + "\",\"taskInfo\" : " + taskInfo.toString() + "},");
						}
						
					}
					teskJson.deleteCharAt(teskJson.length() - 1);
				}
				teskJson.append("]");
				map.put("pip_version" + pip_version, teskJson.toString());
			}
		}
		map.put("status", "success");
		map.put("vList", vList);
		return map;
	}

	@Override
	public Map<String, String> getPipeByPipId(String pipId) {
		
		return pipeManageMapper.getPipeByPipId(pipId);
	}
	
	@Override
	public String getTaskInfo(String task_id, String task_type) {
		String jobName = null;
		Map<String, String> taskInfo = new HashMap<String, String>();
		if (task_type.equals(TaskConstant.TASK_TYPE_ITEM1)) {
			taskInfo = buildMapper.getBuildById(task_id);// 获取构建任务名称
			if (taskInfo != null) {
				if(taskInfo.get("BUILD_NAME") != null && !"".equals(taskInfo.get("BUILD_NAME"))){
					jobName = taskInfo.get("BUILD_NAME");
				}
			}
		}else if (task_type.equals(TaskConstant.TASK_TYPE_ITEM2)) {
			taskInfo = deployMapper.getDeployById(task_id);// 获取部署任务名称
			if (taskInfo != null) {
				if(taskInfo.get("DEPLOY_NAME") != null && !"".equals(taskInfo.get("DEPLOY_NAME"))){
					jobName = taskInfo.get("DEPLOY_NAME");
				}
			}
		}else if (task_type.equals(TaskConstant.TASK_TYPE_ITEM3)) {
			Map<String, String> autoTestItem = autoTestMapper.getAutoTestById(task_id);
			if (taskInfo != null) {
				if (autoTestItem.get("AUTOTEST_ID") != null && !"".equals(autoTestItem.get("AUTOTEST_ID"))) {
					jobName = autoTestItem.get("AUTOTEST_ID");
				}
			}
		}
		return jobName;
	}

	@Override
	public Map<String, Object> getDeployIpAndPortByUrl(String pipId, String pipVersion, String taskType) {
		Map<String, Object> deployMap = pipeManageMapper.getDeployIpAndPortByUrl(pipId,pipVersion,taskType);
		return deployMap;
	}

	@Override
	public String getBuildByJobName(String taskId) {
		String buildName = ""; // 构建任务名称
		Map<String, String> buildMap= deployMapper.getBuildTaskByJobName(taskId);
		if (buildMap.get("BUILD_NAME") != null && !"".equals(buildMap.get("BUILD_NAME"))) {
			buildName = buildMap.get("BUILD_NAME");
		}
		return buildName;
	}

	@Override
	public List<Map<String, Object>> getNodeInfo(String childBuildHistoryId) {
		
		return deployMapper.getNodeByIpAndPort(childBuildHistoryId);
	}

	@Override
	public Map<String, Object> getAutoTestHistory(String pipTaskId, String pipVersion) {
		
		return pipeManageMapper.getAutoTestHistory(pipTaskId,pipVersion);
	}

	@Override
	public Map<String, Object> addPipeliningGroup(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(request);
		String subid = IDFactory.getIDStr();// 流水线Id
		pmap.put("subid", subid);
		String subtime = (String) pmap.get("subtime");// 定时执行时间
		String day = null;
		String week = null;
		String hour = subtime.substring(0,2);
		String minute = subtime.substring(3,5);
		String subcron = cronUtils.cronExpression(day, week, hour, minute);
		String subexecutionMode = request.getParameter("subexecutionMode");//执行方式
		String project_id = request.getParameter("projectId");//执行方式
		pmap.put("subcron", subcron);
		pmap.put("subexecutionMode", subexecutionMode);
		pmap.put("project_id", project_id);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date()); // 创建时间
		pmap.put("subpip_create_time", datetime);
		pmap.put("subpip_create_user", "test");
		this.addPipeliningInfo(pmap);//流水线表
		pmap.put("pip_steps_type", "0");
		//流水线任务参数配置
		String arrString2 = (String)request.getParameter("json");
		JSONObject jsonObject = JSONObject.fromObject(arrString2);
        Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
        if(mapJson.size() != 0){
	        List<Map<String, Object>> jsonInfo = (List<Map<String, Object>>) mapJson.get("info");
	        for(int i=0;i<jsonInfo.size();i++){
	        	String pipStepTaskId = IDFactory.getIDStr();
	        	pmap.put("pipStepTaskId", pipStepTaskId);
				pmap.put("taskID", jsonInfo.get(i).get("sourceId"));
				pmap.put("pip_steps_sort", i + 1);
				this.addPipeliningTask(pmap);//流水线任务关联表
				List<Map<String, Object>> paramInfo = (List<Map<String, Object>>) jsonInfo.get(i).get("param");
				System.out.println(paramInfo.size());
				if(paramInfo.size() != 0){
					List<Map> listParam = new ArrayList<Map>();//批量插入的list
					for(int j=0;j<paramInfo.size();j++){
						Map<String, Object> tempmapMap = new HashMap<String, Object>();
						String pipeliningTaskParameter = IDFactory.getIDStr();//流水线任务参数配置表id
						tempmapMap.put("id", pipeliningTaskParameter);
						tempmapMap.put("pip_task_id", pipStepTaskId);//流水线任务关联id
						tempmapMap.put("param_name", paramInfo.get(j).get("param_name"));
						tempmapMap.put("param_source", paramInfo.get(j).get("param_source"));//参数来源1
						tempmapMap.put("param_source_key", paramInfo.get(j).get("param_source_key"));//参数来源2
						tempmapMap.put("param_sort", paramInfo.get(j).get("param_sort"));
						listParam.add(tempmapMap);
					}
					if(listParam.size() != 0){
						this.addPipeliningTaskParameter(listParam);//参数表
					}
				}
	        }
        }
		
//		JSONArray jArray= JSONArray.fromObject(arrString2);
//		Collection collection = JSONArray.toCollection(jArray, Map.class);
//		List<Map> list = new ArrayList<Map>();               
//		Iterator it = collection.iterator();
//		int x = 0;
//		while (it.hasNext()) {
//		             Map<String, Object> map4 = (Map<String, Object>) it.next();
////		             map4.put("pip_task_id",temp.get(x));
//		             list.add(map4);
//		             x++;
//		}
//		List<Map> listParam = new ArrayList<Map>();//批量插入的list
//		for(int i=0;i<list.size();i++){
//			Map<String, Object> tempmapMap = new HashMap<String, Object>();
//			String pipeliningTaskParameter = IDFactory.getIDStr();//流水线任务参数配置表id
//			tempmapMap.put("id", pipeliningTaskParameter);
//			tempmapMap.put("pip_task_id", list.get(i).get("pip_task_id"));//流水线任务关联id
//			tempmapMap.put("param_name", list.get(i).get("param_name"));
//			tempmapMap.put("param_source", list.get(i).get("param_source"));//参数来源1
//			tempmapMap.put("param_source_key", list.get(i).get("param_source_key"));//参数来源2
//			tempmapMap.put("param_sort", list.get(i).get("param_sort"));
//			listParam.add(tempmapMap);
//		}
//		if(listParam.size() != 0){
//			this.addPipeliningTaskParameter(listParam);
//		}
		map.put("result", "true");
		return map;
	}
	/**
	 * 流水线表插入
	 * @param req
	 * @param res
	 * @author mxx
	 */
	private String addPipeliningInfo(Map<String, Object> mapParam){
		pipeManageMapper.addPipeliningInfo(mapParam);
		return null;
		
	}
	/**
	 * 流水任务关联线表插入
	 * @param req
	 * @param res
	 * @author mxx
	 */
	private String addPipeliningTask(Map<String, Object> mapParam){
		pipeManageMapper.addPipeliningTask(mapParam);
		return null;
	}
	/**
	 * 流水线任务参数配置插入
	 * @param req
	 * @param res
	 * @author mxx
	 */
	private String addPipeliningTaskParameter(List<Map> list){
		pipeManageMapper.addPipeliningTaskParameter(list);
		return null;
	}

	@Override
	public boolean saveBuildHistoryByProducts(Map<String, Object> jobInfo) {
		
		return pipeManageMapper.saveBuildHistoryByProducts(jobInfo);
	}

	@Override
	public List<Map<String, Object>> getBuildHistoryProducts(String pipId, String pipVersion) {
		// 封装产出物
		List<Map<String, Object>> listProdouct = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list =pipeManageMapper.getBuildHistoryProducts(pipId,pipVersion);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> maps = list.get(i);
				if (maps.get("PRODUCT_KEY") != null && maps.get("PRODUCT_VALUE") != null) {
					String prodouctKey = maps.get("PRODUCT_KEY").toString();
					String prodouctValue = maps.get("PRODUCT_VALUE").toString();
					Map<String, Object> products = new HashMap<String, Object>();
					products.put("envName", prodouctKey);
					products.put("envVal", prodouctValue);
					listProdouct.add(products);
				}
			}
		}
		return listProdouct;
	}

	@Override
	public Map<String, Object> queryProduct(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String arrString = req.getParameter("pipIdArr");
		String[] pipIdArr = arrString.split(",");
		List result = new ArrayList();
		for(int i=0;i<pipIdArr.length;i++){
			String type = queryPType(pipIdArr[i]);
			result.add(type);
		}
		map.put("type", result);
		System.out.println(result);
		return map;
	}
	
	private String queryPType(String pipId){
		String type = "0";
		Map<String, Object> map = new HashMap<String, Object>();
		//根据流水线id查询它有什么任务
		map.put("pipId", pipId);
		List<Map<String, Object>> list = pipeManageMapper.queryPType(map);
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("TASK_TYPE").equals("1")){
				type = "1";
			}else if(list.get(i).get("TASK_TYPE").equals("2")){
				type = "2";
			}
		}
		return type;
		
	}

	@Override
	public void stopPipelining() {
		pipeManageMapper.stopPipelining();
	}

	@Override
	public Map<String, Object> pipGroupInfo(HttpServletRequest request) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		Map<String, Object> mapInfo = new HashMap<String, Object>();
		List<Map<String,Object>> subPipInfo = new ArrayList<Map<String,Object>>();
		String id = request.getParameter("id");
		Map<String, Object> info = pipeManageMapper.queryPipGroup(id);
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
		//查询流水线组下的子流水线id
		List<Map<String,Object>> pipList = pipeManageMapper.queryPipList(id);
		for(int i=0;i<pipList.size();i++){
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			String pip_id = (String) pipList.get(i).get("ID");
			//查询每条子流水线基本信息
			List<Map<String,Object>> pipInfo = pipeManageMapper.queryPipInfo(pip_id);
			//查询每条子流水线参数信息
			List<Map<String,Object>> paramInfo = pipeManageMapper.queryPipParam(pip_id);
			mapTemp.put("pipInfo", pipInfo);
			mapTemp.put("paramInfo", paramInfo);
			subPipInfo.add(mapTemp);
		}
		
		mapResult.put("result", "success");
		mapResult.put("info", mapInfo);
		mapResult.put("subPipInfo", subPipInfo);
		return mapResult;
	}

	@Override
	public Map<String, Object> updatePipeliningGroup(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> pmap = RequestUtils.getParameters(request);
		//修改基本信息
		String subid = request.getParameter("pip_id");//流水线组id
		pmap.put("subid", subid);
		String subtime = (String) pmap.get("subtime");// 定时执行时间
		String day = null;
		String week = null;
		String hour = subtime.substring(0,2);
		String minute = subtime.substring(3,5);
		String subcron = cronUtils.cronExpression(day, week, hour, minute);
		String subexecutionMode = request.getParameter("subexecutionMode");//执行方式
		String project_id = request.getParameter("projectId");//执行方式
		pmap.put("subcron", subcron);
		pmap.put("subexecutionMode", subexecutionMode);
		pmap.put("project_id", project_id);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = tempDate.format(new java.util.Date()); // 创建时间
		pmap.put("subpip_create_time", datetime);
		pmap.put("subpip_create_user", "test");
		pipeManageMapper.updatePip(pmap);// 修改流水线表
		pmap.put("pip_steps_type", "0");
		//重新插入参数信息
//		String arrString = request.getParameter("arrString");
//		String[] arr = arrString.split(",");
//		System.out.println(arr.length);
//		pmap.put("pip_steps_type", "0");
//		this.delPipeliningTask(subid);//删除流水线任务关联表
//		for(int i=0;i<arr.length;i++){
//			String pipStepTaskId = IDFactory.getIDStr();
//			pmap.put("pipStepTaskId", pipStepTaskId);
//			pmap.put("taskID", arr[i]);
//			pmap.put("pip_steps_sort", i + 1);
//			this.addPipeliningTask(pmap);//流水线任务关联表
//		}
		//流水线任务参数配置
//		String arrString2 = request.getParameter("json");
//		JSONArray jArray= JSONArray.fromObject(arrString2);
//		Collection collection = JSONArray.toCollection(jArray, Map.class);
//		List<Map> list = new ArrayList<Map>();               
//		Iterator it = collection.iterator();
//		while (it.hasNext()) {
//		             Map<String, Object> map4 = (Map<String, Object>) it.next();
//		             list.add(map4);
//		}
//		List<Map> listParam = new ArrayList<Map>();//批量插入的list
//		for(int i=0;i<list.size();i++){
//			Map<String, Object> tempmapMap = new HashMap<String, Object>();
//			String pipeliningTaskParameter = IDFactory.getIDStr();//流水线任务参数配置表id
//			tempmapMap.put("id", pipeliningTaskParameter);
//			tempmapMap.put("pip_task_id", list.get(i).get("pip_task_id"));//流水线任务关联id
////			tempmapMap.put("task_type", "");
//			tempmapMap.put("param_name", list.get(i).get("param_name"));
////			tempmapMap.put("param_type", "参数类型");
//			tempmapMap.put("param_source", list.get(i).get("param_source"));//参数来源1
//			tempmapMap.put("param_source_key", list.get(i).get("param_source_key"));//参数来源2
////			tempmapMap.put("param_describe", "描述");
//			tempmapMap.put("param_sort", list.get(i).get("param_sort"));
//			listParam.add(tempmapMap);
//		}
//		this.delPipeliningTaskParameter(subid);//删除参数
//		if(listParam.size() != 0){
//			this.addPipeliningTaskParameter(listParam);
//		}
		this.delPipeliningTask(subid);//删除流水线任务关联表
		this.delPipeliningTaskParameter(subid);//删除参数
		String arrString2 = (String)request.getParameter("json");
		JSONObject jsonObject = JSONObject.fromObject(arrString2);
        Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
        List<Map<String, Object>> jsonInfo = (List<Map<String, Object>>) mapJson.get("info");
        for(int i=0;i<jsonInfo.size();i++){
        	String pipStepTaskId = IDFactory.getIDStr();
        	pmap.put("pipStepTaskId", pipStepTaskId);
			pmap.put("taskID", jsonInfo.get(i).get("sourceId"));
			pmap.put("pip_steps_sort", i + 1);
			this.addPipeliningTask(pmap);//流水线任务关联表
			List<Map<String, Object>> paramInfo = (List<Map<String, Object>>) jsonInfo.get(i).get("param");
			System.out.println(paramInfo.size());
			if(paramInfo.size() != 0){
				List<Map> listParam = new ArrayList<Map>();//批量插入的list
				for(int j=0;j<paramInfo.size();j++){
					Map<String, Object> tempmapMap = new HashMap<String, Object>();
					String pipeliningTaskParameter = IDFactory.getIDStr();//流水线任务参数配置表id
					tempmapMap.put("id", pipeliningTaskParameter);
					tempmapMap.put("pip_task_id", pipStepTaskId);//流水线任务关联id
					tempmapMap.put("param_name", paramInfo.get(j).get("param_name"));
					tempmapMap.put("param_source", paramInfo.get(j).get("param_source"));//参数来源1
					tempmapMap.put("param_source_key", paramInfo.get(j).get("param_source_key"));//参数来源2
					tempmapMap.put("param_sort", paramInfo.get(j).get("param_sort"));
					listParam.add(tempmapMap);
				}
				if(listParam.size() != 0){
					this.addPipeliningTaskParameter(listParam);//参数表
				}
			}
        }
		map.put("result", "true");
		return map;
	}

	private void delPipeliningTask(String subid) {
		pipeManageMapper.delPipeliningTask(subid);
		
	}

	private void delPipeliningTaskParameter(String subid) {
		pipeManageMapper.delPipeliningTaskParameter(subid);
		
	}
}
