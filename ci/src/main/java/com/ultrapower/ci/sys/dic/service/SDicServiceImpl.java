package com.ultrapower.ci.sys.dic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ultrapower.ci.common.utils.IDFactory;
import com.ultrapower.ci.sys.dic.dao.SDicMapper;
import com.ultrapower.ci.sys.dic.entity.SDic;

@Service
@Transactional
public class SDicServiceImpl implements ISDicService {

	@Autowired
	private SDicMapper sdicMapper;

	@Override
	public Map<String, Object> queryDicItemList() {
		// 返回构建环境的集合
		List<SDic> list = sdicMapper.queryDicItemList();
		// 构建环境对应的构建类型的集合
		Map<String, List> map = new HashMap<String, List>();
		for (int i = 0; i < list.size(); i++) {
			// 根据构建环境查询对应的构建类型
			List<SDic> sonlist = sdicMapper.querySonDicItemList(list.get(i).getDicId());
			// 将构建环境与其相对的构建类型的集合放入Map
			map.put(list.get(i).getDicCode(), sonlist);
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("environment", list);
		maps.put("type", map);

		return maps;
	}

	@Override
	public Map<String, Object> queryDicItemListZ(HttpServletRequest req) {
		Map<String, Object> mapZ = new HashMap<String, Object>();
		String id = req.getParameter("id");
		List<SDic> sonlistZ = sdicMapper.querySonDicItemListZ(id);
		mapZ.put("result", "success");
		mapZ.put("buildType", sonlistZ);

		return mapZ;
	}

	@Override
	public List<SDic> queryConsEnvironment() {
		List<SDic> queryDicItemList = sdicMapper.queryDicItemList();
		return queryDicItemList;
	}

	@Override
	public String addConsEnvironment(SDic sdic) {
		if (null == sdic || null == sdic.getDicName()) {
			return null;
		}

		sdic.setDicId(IDFactory.getIDStr());

		sdicMapper.addConsEnvironment(sdic);

		return "success";

	}

	@Override
	public boolean delConsEnvironment(String dicId) {
		try {
			int rt = sdicMapper.delConsEnvironment(dicId);
			if (rt > 0) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;

	}

	@Override
	public List<SDic> queryConsTypeByPid(String dicId) {
		return sdicMapper.querySonDicItemList(dicId);

	}

	@Override
	public void delConsType(String dicId) {
		sdicMapper.delConsType(dicId);

	}

	@Override
	public SDic getConsEnvironment(String dicId) {
		SDic sdic = sdicMapper.getConsEnvironment(dicId);
		return sdic;
	}

	@Override
	public void updateConsEnvironment(SDic sdic) {
		sdicMapper.updateConsEnvironment(sdic);

	}

	@Override
	public void addConsType(SDic sdic) {
		sdic.setDicId(IDFactory.getIDStr());

		sdicMapper.addConsType(sdic);

	}

	@Override
	public void updateConsType(SDic sdic) {
		sdicMapper.updateConsType(sdic);

	}

	@Override
	public SDic getConsType(String dicId) {
		SDic sdic = sdicMapper.getConsEnvironment(dicId);
		return sdic;
	}

	@Override
	public boolean validateDicCode(String dicCode) {
		SDic sdic =sdicMapper.validateDicCode(dicCode);
		System.out.println(sdic+"***"+dicCode);
		System.out.println(sdic);
		if(sdic==null){
			return true;
		}
		return false;
	}

}
