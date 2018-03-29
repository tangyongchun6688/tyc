package com.ultrapower.ci.control.projectManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.projectManage.entity.ProjectManage;

public interface ProjectManageMapper {
    int deleteByPrimaryKey(String projectId);

    int insert(ProjectManage record);

    int insertSelective(ProjectManage record);

    ProjectManage selectByPrimaryKey(String projectId);

    int updateByPrimaryKeySelective(ProjectManage record);

    int updateByPrimaryKey(ProjectManage record);
    /**
     * 根据插入的数据查询对象
     * @param record
     * @return
     */
    List<ProjectManage> selectByBeanClass(String record);
    
    /**
	 * 查询项目列表
	 * @return
	 */
	public List<Map<String,String>> queryProjectList(Map<String, Object> pmap);
	/**
	 * 通过项目名查询项目是否已存在
	 */
	public List<ProjectManage> selectByname();
	/**
	 * 通过项目数量
	 */
	public String selectNumber();
	/**
	 * 查询所有信息
	 */
	public List<ProjectManage> selectPro();
	/**
	 * 通过项目名查询项目是否已存在
	 */
	public List<ProjectManage> selectToId(String projectId);
	/**
	 * 通过项目id查询流水线id
	 */
    public List<String> selectPidByProjectId(String projectId);
}