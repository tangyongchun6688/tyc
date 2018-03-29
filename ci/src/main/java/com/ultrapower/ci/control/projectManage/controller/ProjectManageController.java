package com.ultrapower.ci.control.projectManage.controller;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.common.utils.JsonUtils;
import com.ultrapower.ci.control.projectManage.entity.ProjectManage;
import com.ultrapower.ci.control.projectManage.service.IProjectManageService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("projectManage")
public class ProjectManageController extends BaseController{
	

	@Resource
	private IProjectManageService projectManageService;

	/**
	 * 查询项目列表
	 * @param req
	 * @param res
	 */
	@RequestMapping("queryProjectList")
	public void queryKmList(HttpServletRequest req,HttpServletResponse res){
		try {
			writeUTFJson(res,projectManageService.queryProjectList(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@ResponseBody
	@RequestMapping("insertProjectList")
	public String insertKmList(String name,String remark){
		ProjectManage mp=new ProjectManage();
		mp.setProjectName(name);
		mp.setProjectDescribe(remark);
		ProjectManage project=projectManageService.insert(mp);
		JsonUtils.beanToJson(project);
		return JsonUtils.beanToJson(project);
	}
	@ResponseBody
	@RequestMapping("doInsertProjectList")
	public String doinsertKmList(String name,String remark){
		String ProjectManage=projectManageService.selectbyname(name,remark);
		return ProjectManage;
	}

	@ResponseBody
	@RequestMapping("delectProjectList")
	public void delectKmList(String projectId,HttpServletRequest req){
		projectManageService.delect(projectId,req);
	 
	}
	@ResponseBody
	@RequestMapping("updataProjectList")
	public void updataKmList(String name,String remark,String id){
		ProjectManage mp=new ProjectManage();
		mp.setProjectId(id);
		mp.setProjectName(name);
		mp.setProjectDescribe(remark);
		projectManageService.updata(mp);
	}
	@ResponseBody
	@RequestMapping("selectAll")
   public String selectAll(){
        String project=projectManageService.selectAll();
		String s="{\"number\":\""+project+"\"}";
        return s;
	}
	@ResponseBody
	@RequestMapping("selectPro")
   public void selectPro(HttpServletResponse res){
        List<ProjectManage> list=projectManageService.selectPro();
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
	
	@ResponseBody
	@RequestMapping("selectByProjectId")
   public void selectToName(HttpServletResponse res,String projectId){
		try {
			writeUTFJson(res,projectManageService.selectToId(projectId));
		} catch (Exception e) {
			e.printStackTrace();
		}
       

	}
}
