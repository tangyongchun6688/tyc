package com.ultrapower.ci.control.codeManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.control.codeManage.dao.CodeRepositoriesMapper;

@Service
@Transactional
public class CodeRepositoriesServiceImpl implements ICodeRepositoriesService {

	@Autowired
	private CodeRepositoriesMapper codeRepositoriesMapper;

	@Override
	public Map<String, Object> queryCodeBaseList() {
		
		
		Map<String, Object> info = new HashMap<String, Object>();
	 
		try {
			List<Map<String, Object>> codeBaseList= codeRepositoriesMapper.queryCodeBaseList();
			info.put("status", "success");
			if (codeBaseList != null && codeBaseList.size() > 0) {
				info.put("message", "查询仓库列表信息成功！");
				info.put("result", codeBaseList);
			}else {
				info.put("message", "未查询到仓库列表信息！");
				info.put("result", codeBaseList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			info.put("status", "failure");
			info.put("message", "查询仓库列表信息失败！");
		}
		return info;
	}
}
