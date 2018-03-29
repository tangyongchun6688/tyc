package com.ultrapower.ci.control.buildManage.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.control.buildManage.entity.Build;
import com.ultrapower.ci.control.buildManage.service.IBuildService;

import net.sf.json.JSONArray;
@Controller
@RequestMapping("buildManage")
public class BuildController extends BaseController{

	@Resource
	private IBuildService buildService;
	 /**
     * 查询所有信息
     * @return String
	 * @throws UnsupportedEncodingException 
     */
	@ResponseBody
	@RequestMapping("selectPro")
   public void selectPro(HttpServletRequest request, HttpServletResponse res) {
        List<Build> list=buildService.selectPro();
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