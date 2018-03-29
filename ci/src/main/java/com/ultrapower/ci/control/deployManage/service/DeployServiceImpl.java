package com.ultrapower.ci.control.deployManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.common.utils.RequestUtils;
import com.ultrapower.ci.control.deployManage.dao.DeployMapper;

@Service
@Transactional
public class DeployServiceImpl implements IDeployService {
	private static final Logger logger = Logger.getLogger(DeployServiceImpl.class);
	
	@Autowired
	private DeployMapper deployMapper;

	@Override
	public Map<String, Object> getDeployList(HttpServletRequest request) {
		// 封装结果集信息
		Map<String, Object> info = new HashMap<String, Object>();
		// 获取参数
		Map<String, Object> params = RequestUtils.getParameters(request);
		try {
			List<Map<String,Object>> deployList = deployMapper.getDeployList(params);
			info.put("status", "success");
			if (deployList != null && deployList.size() > 0) {
				info.put("message", "查询部署列表信息成功！");
				info.put("result", deployList);
			}else {
				info.put("message", "未查询到部署列表信息！");
				info.put("result", deployList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("---查询部署列表信息出错---"+e.getMessage());
			info.put("status", "failure");
			info.put("message", "查询部署列表信息失败！");
		}
		return info;
	}

	@Override
	public Map<String, String> getDeployById(String deployId) {
		
		return deployMapper.getDeployById(deployId);
	}
}
