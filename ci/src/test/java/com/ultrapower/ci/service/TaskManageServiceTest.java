package com.ultrapower.ci.service;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ultrapower.ci.baseTest.SpringTestCase;
import com.ultrapower.ci.control.taskManage.dao.TaskManageMapper;

public class TaskManageServiceTest extends SpringTestCase{

	@Autowired
	private TaskManageMapper taskManageMapper;
	
	@Test
	public void getPipVersion(){
		Map<String,String> vmap = taskManageMapper.getMaxPipVersionByPipId("bbefad253e3c4ee584af876d9375993e");
//		if(vmap!= null && vmap.get("MAX_PIP_VERSION") != null){
//			System.out.println(vmap.get("MAX_PIP_VERSION"));
//		}
	}
}
