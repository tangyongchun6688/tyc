package com.ultrapower.ci.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ultrapower.ci.baseTest.SpringTestCase;
import com.ultrapower.ci.control.deployManage.dao.DeployMapper;

public class DeployServiceTest extends SpringTestCase{

	@Autowired
	private DeployMapper deployMapper;
	
	@Test
	public void selectByPrimaryKey(){
		System.out.println("5555555");
	};
}
