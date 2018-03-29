package com.ultrapower.ci.control.autoTestManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.autoTestManage.entity.AutoTest;
import com.ultrapower.ci.control.buildManage.entity.Build;

public interface AutoTestMapper {
    int deleteByPrimaryKey(String id);

    int insert(AutoTest record);

    int insertSelective(AutoTest record);

    AutoTest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AutoTest record);

    int updateByPrimaryKey(AutoTest record);
    /**
     * 查询所有信息
     * @return list
     */
    public List<AutoTest> selectPro();
    /**
     * 通过主键查询自动化测试信息
     * @param autoTestId
     * @return
     */
    public Map<String,String> getAutoTestById(String autoTestId);
}