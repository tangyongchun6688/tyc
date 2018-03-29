package com.ultrapower.ci.control.pipeManage.dao;

import java.util.List;
import java.util.Map;

public interface PipeManageMapper {

	/**
	 * 查询jenkins中认证用户的唯一标识
	 * @param username
	 * @param password
	 * @return
	 */
	public List<Map<String,String>> getJenkinsCredential(String username,String password);
	/**
	 * 增加一条jenkins认证用户信息
	 * @param pmap
	 */
	public void addJenkinsCredential(Map<String,Object> pmap);
	/**
     * 查询流水线列表信息
     * @param map
     * @return
     */
    public List<Map<String,String>> queryPipeliningList(Map<String,Object> map);
    /**
     * 获取流水线构建历史最大版本 
     * @param pipId
     * @return
     */
    public Map<String,String> getMaxPipVersionByPipId(String pipId);
    /**
     * 获取流水线任务信息
     * @param pipId
     * @return
     */
    public List<Map<String,String>> queryTaskListByPipId(String pipId);
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
     * 查询一条流水线某一版本下历史
     * @param map
     * @return
     */
    public List<Map<String,String>> queryBuildHistoryByVersion(Map<String,Object> map);
    /**
     * 查询流水线所有的执行历史版本
     * @param pipId
     * @return
     */
    public List<Map<String,String>> queryPipVersionList(String pipId);
    /**
     * 查询一条流水线某一版本下真实历史
     * @param map
     * @return
     */
    public List<Map<String,String>> queryRealHistoryByVersion(Map<String,Object> map);
    /**
     * 通过流水线主键获取流水线信息
     * @param pipId
     * @return
     */
    public Map<String,String> getPipeByPipId(String pipId);
    
    /**
     * 查询部署时申请的ip、port
     * @param pipId
     * @param pipVersion
     * @param taskType
     * @return
     */
	public Map<String, Object> getDeployIpAndPortByUrl(String pipId, String pipVersion, String taskType);
	
	/**
	 * 查询自动化测试历史
	 * @param pipTaskId
	 * @param pipVersion
	 * @return
	 */
	public Map<String, Object> getAutoTestHistory(String pipTaskId, String pipVersion);
	/**
     * 查询指定版本下流水线信息
     * @param map
     * @return
     */
    public Map<String,String> queryPipVersionInfo(Map<String,Object> map);
    /**
	 * 流水线表插入
	 * @param pipTaskId
	 * @param pipVersion
	 * @return
	 */
	public void addPipeliningInfo(Map<String, Object> mapParam);
	/**
	 * 流水线任务关联表插入
	 * @param pipTaskId
	 * @param pipVersion
	 * @return
	 */
	public void addPipeliningTask(Map<String, Object> mapParam);
	
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
	 * 流水线任务参数配置插入
	 * @param pipId
	 * @param pipVersion
	 * @return
	 */
	public boolean addPipeliningTaskParameter(List<Map> list);
	
	/**
	 * 根据流水线id查询它有什么任务
	 * @param map 
	 * @return
	 */
	public List<Map<String, Object>> queryPType(Map<String, Object> map);
	/**
	 * 启动应用时，状态为正在执行的流水线，修改为执行失败状态
	 */
	public void stopPipelining();
	/**
	 * 通过构建历史主键查询产出物
	 * @param build_history_id
	 * @return
	 */
	public List<Map<String,String>> getProductByBuildHistoryId(String build_history_id);
	/**
	 * 查询流水线组基本信息
	 * @param map 
	 * @return
	 */
	public Map<String, Object> queryPipGroup(String id);
	/**
	 * 查询流水线组下的子流水线id
	 * @param 流水线组id 
	 * @return
	 */
	public List<Map<String,Object>> queryPipList(String id);
	/**
	 * 查询每条子流水线基本信息
	 * @param 子流水线id 
	 * @return
	 */
	public List<Map<String,Object>> queryPipInfo(String id);
	/**
	 * 查询每条子流水线参数信息
	 * @param 子流水线id 
	 * @return
	 */
	public List<Map<String, Object>> queryPipParam(String id);
	/**
	 * 修改流水线表
	 * @param 流水线id
	 * @return
	 */
	int updatePip(Map<String, Object> pmap);
	/**
	 * 根据流水线组id删除子流水线参数
	 * @param 流水线id
	 * @return
	 */
	public void delPipeliningTaskParameter(String subid);
	/**
	 * 根据流水线组id删除流水线任务关联表
	 * @param 流水线id
	 * @return
	 */
	public void delPipeliningTask(String subid);
}
