package com.ultrapower.ci.control.projectManage.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.common.utils.JsonUtils;
import com.ultrapower.ci.common.utils.RequestUtils;
import com.ultrapower.ci.control.projectManage.controller.ProjectManageController;
import com.ultrapower.ci.control.projectManage.dao.ProjectManageMapper;
import com.ultrapower.ci.control.projectManage.entity.ProjectManage;
import com.ultrapower.ci.control.taskManage.service.ITaskManageService;

@Service
@Transactional
public class ProjectManageServiceImpl implements IProjectManageService{
	private static  Logger logger = Logger.getLogger(ProjectManageServiceImpl.class);
	@Autowired
	private ProjectManageMapper projectManageMapper;
	@Autowired
	private ITaskManageService taskManageService;

	@Override
	public ProjectManage selectByPrimaryKey(String projectId) {
		// TODO Auto-generated method stub
		ProjectManage projectManage = projectManageMapper.selectByPrimaryKey(projectId);
		return null;
	}

	@Override
	public Map<String, Object> queryProjectList(HttpServletRequest req) {
		String[] must=new String[]{"limit","offset"};
		String[] nomust=new String[]{"km_id"};
		Map<String, Object> pmap1 =RequestUtils.requestToMap(req, must, nomust);
		
		Map<String, Object> pmap=RequestUtils.getParameters(req);
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,String>> list = projectManageMapper.queryProjectList(pmap);
		map.put("status", "success");
		map.put("rows", list);
		map.put("rowCount", pmap.get("rowCount"));
		map.put("pageCount", pmap.get("pageCount"));
		map.put("tatol", pmap.get("rowCount"));
		return map;
	}
	@Override
	public ProjectManage insert(ProjectManage mp) {
		List<ProjectManage> project=null;
		String uuid = UUID.randomUUID().toString();   
	    uuid = uuid.replace("-", "");               
	    Date date=new Date();
	    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String time=format.format(date);
	    mp.setCreateTime(time);
	    mp.setProjectStatus("0");
	    mp.setCreateUser("root");
		mp.setProjectId(uuid);
		projectManageMapper.insert(mp);
		try{
			project=projectManageMapper.selectByBeanClass(uuid);	
			}catch(Exception e){
			e.printStackTrace();
			 //logger.info("selectByBeanClass方法出现错误");  
			}
		return project.get(0);
	}

	@Override
	public void delect(String projectId,HttpServletRequest req) {
		List<String> list=null;
		try{
			list=projectManageMapper.selectPidByProjectId(projectId);	
			}catch(Exception e){
			e.printStackTrace();
			}
		for(int i=0;i<list.size();i++){
			String pipId=list.get(i);
			taskManageService.deletePipeliningByPipId(pipId);
		}
		    projectManageMapper.deleteByPrimaryKey(projectId);
		
	}

	@Override
	public void updata(ProjectManage mp) {       
	    Date date=new Date();
	    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String time=format.format(date);
	    mp.setCreateTime(time);
	    mp.setProjectStatus("0");
	    mp.setCreateUser("root");	
	    projectManageMapper.updateByPrimaryKey(mp);
	}

	@Override
	public ProjectManage selectByBeanClass(ProjectManage mp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String selectbyname(String name,String re) {
		List<ProjectManage> project=null;
		List<ProjectManage> list=null;
		boolean ss=true;
		try{
			list=projectManageMapper.selectByname();	
			}catch(Exception e){
			e.printStackTrace();
			 //logger.info("selectByname方法出现错误");
			}
		for(ProjectManage s:list){
			if(name.equals(s.getProjectName())){
				ss= false;
			break;
			}
		}
		if(ss){
			ProjectManage mp=new ProjectManage();
			String uuid = UUID.randomUUID().toString();   
		    uuid = uuid.replace("-", "");               
		    Date date=new Date();
		    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time=format.format(date);
		    mp.setCreateTime(time);
		    mp.setProjectStatus("0");
		    mp.setCreateUser("root");
			mp.setProjectId(uuid);
			mp.setProjectName(name);
			mp.setProjectDescribe(re);
			projectManageMapper.insert(mp);
			try{
			project=projectManageMapper.selectByBeanClass(uuid);	
			}catch(Exception e){
			e.printStackTrace();
			// logger.info("selectByBeanClass方法出现错误");
			}
			
			ProjectManage sr=project.get(0);
			return JsonUtils.beanToJson(sr);
		}
			
	
	return "{\"projectId\":\"项目名以存在\"}";
	}

	@Override
	public String selectAll(){
	       String s=null;
		try{
			s=projectManageMapper.selectNumber();		
		}catch(Exception e){
			e.printStackTrace();
			// logger.info("selectAll方法出现错误");
			}
		
		return s;
	}

	@Override
	public List<ProjectManage> selectPro() {
		// TODO Auto-generated method stub
		List<ProjectManage> list=null;
		try{
			list=projectManageMapper.selectPro();		
		}catch(Exception e){
			e.printStackTrace();
			// logger.info("selectAll方法出现错误");
			}

		return list;
	}

	@Override
	public ProjectManage selectToId(String projectId) {
		List<ProjectManage> list=null;
		try{
			list=projectManageMapper.selectToId(projectId);		
		}catch(Exception e){
			e.printStackTrace();
			// logger.info("selectAll方法出现错误");
			}
		return list.get(0);
	}

	
}
