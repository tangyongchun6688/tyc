package com.ultrapower.ci.control.pipeManage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IPipeManageService {

	/**
	 * 查询jenkins中认证用户的唯一标识
	 * @param username
	 * @param password
	 * @return
	 */
	public String getJenkinsCredential(String username,String password);
	/**
	 * 增加一条jenkins认证用户信息
	 * @param CredentialId
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean addJenkinsCredential(String CredentialId,String username,String password); 
	/**
	 * 根据项目主键，获取项目的流水线列表
	 * @param req
	 * @return
	 */
	public Map<String, Object> getPipliningListByProjectId(HttpServletRequest req);
	/**
	 * 获取流水线下子任务列表
	 * @param pipId
	 * @return
	 */
	public List<Map<String,String>> getPipeTaskList(String pipId);
	/**
	 * 流水线（子流水线或任务）开始执行，历史表添加记录
	 * @param map
	 * @return
	 */
	public int addBuildHistory(Map<String,String> map);
	/**
     * 修改构建历史表流水线执行状态
     * @param map
     * @return
     */
    public int updateBuildHistory(Map<String,String> map);
    /**
	 * 查询流水线执行历史
	 * @param request
	 * @return
	 */
	public Map<String,Object> queryPipHistoryList(HttpServletRequest req);
	/**
	 * 通过流水线主键获取流水线信息
	 * @param PipId
	 * @return
	 */
	public Map<String,String> getPipeByPipId(String pipId);
	
	public String getTaskInfo(String task_id, String task_type);
	
	public Map<String, Object> getDeployIpAndPortByUrl(String pipId, String pipVersion, String taskType);
	
	/**
	 * 获取构建job名称
	 * @param taskId
	 * @return
	 */
	public String getBuildByJobName(String taskId);
	
	/**
	 * 查询申请机器的ip、port
	 * @param childBuildHistoryId
	 * @return
	 */
	public List<Map<String, Object>> getNodeInfo(String childBuildHistoryId);
	
	/**
	 * 查询自动化测试历史
	 * @param pipTaskId
	 * @param pipVersion
	 * @return
	 */
	public Map<String, Object> getAutoTestHistory(String pipTaskId, String pipVersion);
	/**
	 * 新建流水线组
	 * @param req
	 * @param res
	 * @author mxx
	 */
	public Map<String, Object> addPipeliningGroup(HttpServletRequest request);
	
	/**
	 * 保存构建后的产出物
	 * @param jobInfo
	 * @return
	 */
	public boolean saveBuildHistoryByProducts(Map<String, Object> jobInfo);
	
	/**
	 * 查询构建后的产出物
	 * @param pipId
	 * @param pipVersion
	 * @return
	 */
	public List<Map<String, Object>> getBuildHistoryProducts(String pipId, String pipVersion);
	/**
	 * @author mxx
	 * @description 根据流水线id查询它是否有构建任务或部署任务
	 * @return
	 */
	public Map<String, Object> queryProduct(HttpServletRequest req);
	/**
	 * 启动应用时，状态为正在执行的流水线，修改为执行失败状态
	 */
	public void stopPipelining();
	/**
	 * 查询流水线组信息
	 * @author mxx
	 * @param req
	 * @param res
	 */
	public Map<String, Object> pipGroupInfo(HttpServletRequest request);
	/**
	 * 修改流水线组
	 * @author mxx
	 * @param req
	 * @param res
	 */
	public Map<String, Object> updatePipeliningGroup(HttpServletRequest request);
}
