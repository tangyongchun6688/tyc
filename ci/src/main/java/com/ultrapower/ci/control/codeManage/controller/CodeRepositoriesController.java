package com.ultrapower.ci.control.codeManage.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.control.codeManage.service.ICodeRepositoriesService;


@Controller
@RequestMapping("codeRepositories")
public class CodeRepositoriesController extends BaseController {

	@Resource
	private ICodeRepositoriesService codeRepositoriesService;

	/**
	 * 查询代码仓库列表
	 */
	@RequestMapping(value="queryCodeList",method=RequestMethod.POST)
	public void queryCodeList(HttpServletRequest req, HttpServletResponse res) {
		
		writeUTFJson(res, codeRepositoriesService.queryCodeBaseList());
		//return JsonUtils.beanToJson(codeRepositoriesService.queryCodeBaseList());
	}

	@RequestMapping("toqueryCodeList")
	public String toListJsp() {
		return "taskManage/code/codeBaseList";
	}
}
