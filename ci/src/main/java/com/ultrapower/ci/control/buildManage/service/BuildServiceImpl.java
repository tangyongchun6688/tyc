package com.ultrapower.ci.control.buildManage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.control.buildManage.dao.BuildMapper;
import com.ultrapower.ci.control.buildManage.entity.Build;

@Service
@Transactional
public class BuildServiceImpl implements IBuildService {

	@Autowired
	private BuildMapper buildMapper;

	@Override
	public List<Build> selectPro() {
		List<Build> list=null; 
		list=buildMapper.selectPro();
		return list;
	}

	@Override
	public Map<String, String> getBuildById(String buildId) {
		
		return buildMapper.getBuildById(buildId);
	}
}
