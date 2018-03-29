package com.ultrapower.ci.control.buildManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.buildManage.entity.Build;

public interface BuildMapper {
    int deleteByPrimaryKey(String id);

    int insert(Build record);

    int insertSelective(Build record);

    Build selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Build record);

    int updateByPrimaryKey(Build record);
    /**
     * 查询所有信息
     * @return list
     */
    public List<Build> selectPro();

    /**
	 * 通过主键获取构建信息
	 * @param buildId
	 * @return
	 */
	public Map<String,String> getBuildById(String buildId);
}