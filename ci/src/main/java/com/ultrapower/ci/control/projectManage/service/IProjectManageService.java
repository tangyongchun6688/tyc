package com.ultrapower.ci.control.projectManage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ultrapower.ci.control.projectManage.entity.ProjectManage;

public interface IProjectManageService {

	public ProjectManage selectByPrimaryKey(String projectId);
	
	/**
	 * 查询项目列表
	 * @param req
	 * @return
	 */
	public Map<String,Object> queryProjectList(HttpServletRequest req);
	/**
	 * 插入列表
	 * @param ProjectManage
	 * @return
	 */
   public ProjectManage insert(ProjectManage mp); 
    /**
	 * 删除列表
	 * @param String
	 * @return
	 */
   public void delect(String projectId,HttpServletRequest req); 
   /**
	 * 更新列表
	 * @param ProjectManage
	 * @return
	 */
   public void updata(ProjectManage mp); 
   /**
	 * 插入数据后查询返回id
	 * @paramProjectManage
	 * @return
	 */
   public ProjectManage selectByBeanClass(ProjectManage mp);
   /**
  	 * 通过名称查询用户是否存在
  	 * @paramProjectManage
  	 * @return
  	 */
   public String selectbyname(String name,String re);
   /**
 	 * 查询条数信息
 	 * @paramProjectManage
 	 * @return
 	 */  
   
   public String selectAll();
   /**
	 * 查询所有数据库信息
	 * @paramProjectManage
	 * @return
	 */  
   public List<ProjectManage> selectPro();
   /**
 	 * 通过id查询用户是否存在
 	 * @paramProjectManage
 	 * @return
 	 */
  public ProjectManage selectToId(String projectId);
}
