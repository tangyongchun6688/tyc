package com.ultrapower.ci.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ultrapower.ci.baseTest.SpringTestCase;
import com.ultrapower.ci.common.utils.IDFactory;
import com.ultrapower.ci.control.pipeManage.dao.PipeManageMapper;

public class PipeManageServiceTest extends SpringTestCase{

	@Autowired
	private PipeManageMapper pipeManageMapper;
	
	@Test
	public void getJenkinsCredential(){
		List<Map<String, String>> list = pipeManageMapper.getJenkinsCredential("admin", "123");
		System.out.println(list.size());
		System.out.println("查询jenkins认证用户是否已存在");
	}
	
	@Test
	public void addJenkinsCredential(){
		Map<String,Object> pmap = new HashMap<String,Object>();
		String jenkinsCredentialId = IDFactory.getIDStr();
		pmap.put("ID", jenkinsCredentialId);
		pmap.put("credentialId", "111111");
		pmap.put("username", "admin");
		pmap.put("password", "123");
		pmap.put("is_use", "1");
		pipeManageMapper.addJenkinsCredential(pmap);
		System.out.println("添加jenkins认证用户成功");
	}
}
