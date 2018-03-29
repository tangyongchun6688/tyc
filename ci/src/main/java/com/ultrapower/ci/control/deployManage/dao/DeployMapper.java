package com.ultrapower.ci.control.deployManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.deployManage.entity.Deploy;

public interface DeployMapper {
    int deleteByPrimaryKey(String id);

    int insert(Deploy record);

    int insertSelective(Deploy record);

    Deploy selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Deploy record);

    int updateByPrimaryKey(Deploy record);

    /**
     * 查询部署列表信息
     * @param params 封装参数
     * @return 返回结果集
     */
	public List<Map<String, Object>> getDeployList(Map<String, Object> params);

	/**
	 * 根据步骤任务 关联ID查询部署所需要的节点
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getNodeInfo(Map<String, Object> params);

	/**
	 * 根据部署ID查询构建job的名称
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getBuildByJobName(Map<String, Object> params);
	/**
	 * 通过主键获取部署信息
	 * @param deployId
	 * @return
	 */
	public Map<String,String> getDeployById(String deployId);

	/**
	 * 获取构建job名称
	 * @param taskId
	 * @return
	 */
	public Map<String, String> getBuildTaskByJobName(String taskId);

	/**
	 * 查询申请机器之后的ip、port
	 * @param childBuildHistoryId
	 * @return
	 */
	public List<Map<String, Object>> getNodeByIpAndPort(String childBuildHistoryId);
}