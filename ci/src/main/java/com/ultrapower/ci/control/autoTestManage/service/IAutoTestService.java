package com.ultrapower.ci.control.autoTestManage.service;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.autoTestManage.entity.AutoTest;

public interface IAutoTestService {

	List<AutoTest> selectPro();

	/**
     * 通过主键查询自动化测试信息
     * @param autoTestId
     * @return
     */
    public Map<String,String> getAutoTestById(String autoTestId);
}
