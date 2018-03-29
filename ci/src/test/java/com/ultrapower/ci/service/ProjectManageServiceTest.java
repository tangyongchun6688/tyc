package com.ultrapower.ci.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ultrapower.ci.baseTest.SpringTestCase;
import com.ultrapower.ci.control.projectManage.dao.ProjectManageMapper;

public class ProjectManageServiceTest extends SpringTestCase{

	@Autowired
	private ProjectManageMapper projectManageMapper;
	
	@Test
	public void selectByPrimaryKey(){
		System.out.println("1111");
		System.out.println("===========");
	}
}