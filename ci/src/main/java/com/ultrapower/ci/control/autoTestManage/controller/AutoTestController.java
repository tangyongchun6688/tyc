package com.ultrapower.ci.control.autoTestManage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.control.autoTestManage.entity.AutoTest;
import com.ultrapower.ci.control.autoTestManage.service.IAutoTestService;
import com.ultrapower.ci.control.buildManage.entity.Build;

import net.sf.json.JSONArray;
@Controller
@RequestMapping("autoTest")
public class AutoTestController extends BaseController{

	@Resource
	private IAutoTestService autoTestService;
	 /**
     * 查询所有信息
     * @return String
     */
	@ResponseBody
	@RequestMapping("selectPro")
    public void selectPro(HttpServletResponse res){
        List<AutoTest> list=autoTestService.selectPro();
        PrintWriter writer = null;
		try {
			res.setCharacterEncoding("UTF-8");
			writer = res.getWriter();
			writer.write(JSONArray.fromObject(list).toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
}
