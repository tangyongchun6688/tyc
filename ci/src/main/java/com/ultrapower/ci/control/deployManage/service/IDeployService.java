package com.ultrapower.ci.control.deployManage.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IDeployService {

	/**
	 * 查询部署列表信息
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDeployList(HttpServletRequest request);

	/**
	 * 通过主键获取部署信息
	 * @param deployId
	 * @return
	 */
	public Map<String,String> getDeployById(String deployId);
}
