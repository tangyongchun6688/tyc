package com.ultrapower.ci.control.autoTestManage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.control.autoTestManage.dao.AutoTestMapper;
import com.ultrapower.ci.control.autoTestManage.entity.AutoTest;

@Service
@Transactional
public class AutoTestServiceImpl implements IAutoTestService {

	@Autowired
	private AutoTestMapper autoTestMapper;

	@Override
	public List<AutoTest> selectPro() {
		List<AutoTest> list=null;
		list=autoTestMapper.selectPro();
		return list;
	}

	@Override
	public Map<String, String> getAutoTestById(String autoTestId) {
		
		return autoTestMapper.getAutoTestById(autoTestId);
	}
}
