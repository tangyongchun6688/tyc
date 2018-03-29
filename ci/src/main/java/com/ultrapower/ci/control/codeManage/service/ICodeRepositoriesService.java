package com.ultrapower.ci.control.codeManage.service;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.codeManage.entity.CodeRepositories;

public interface ICodeRepositoriesService {
	/**
	 * 查询代码仓库列表
	 */
	Map<String, Object> queryCodeBaseList();

}
