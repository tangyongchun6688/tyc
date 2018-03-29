package com.ultrapower.ci.control.deployManage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.control.deployManage.service.IDeployService;

/**
 * @time 2017-12-12
 * @author tangongchun
 * @description 实现部署模块的CRUD功能
 *
 */
@Controller
@RequestMapping("deploy")
public class DeployController extends BaseController{

	@Resource
	private IDeployService deployService;
	
	/**
	 * 查询部署列表信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="deployList",method=RequestMethod.POST)
	public void getDeployList(HttpServletRequest request,HttpServletResponse response){
		try {
			writeUTFJson(response, deployService.getDeployList(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
