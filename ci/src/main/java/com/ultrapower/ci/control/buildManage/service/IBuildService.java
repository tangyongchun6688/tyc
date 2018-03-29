package com.ultrapower.ci.control.buildManage.service;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.buildManage.entity.Build;

public interface IBuildService {

	public List<Build> selectPro();
	/**
	 * 通过主键获取构建信息
	 * @param buildId
	 * @return
	 */
	public Map<String,String> getBuildById(String buildId);

}
