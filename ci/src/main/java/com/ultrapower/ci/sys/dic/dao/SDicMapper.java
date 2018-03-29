package com.ultrapower.ci.sys.dic.dao;

import java.util.List;

import com.ultrapower.ci.sys.dic.entity.SDic;

public interface SDicMapper {
	int deleteByPrimaryKey(String dicId);

	int insert(SDic record);

	int insertSelective(SDic record);

	SDic selectByPrimaryKey(String dicId);

	int updateByPrimaryKeySelective(SDic record);

	int updateByPrimaryKey(SDic record);

	// 查询构建环境的集合
	List<SDic> queryDicItemList();

	// 根据构建环境查询构建类型
	List<SDic> querySonDicItemList(String dicId);

	// 增加构建环境
	void addConsEnvironment(SDic sdic);

	// 删除构建环境
	int delConsEnvironment(String sdic);

	// 根据构建环境id查询构建类型
	List<SDic> querySonDicItemListZ(String id);

	// 删除构建类型
	void delConsType(String dicId);

	// 根据id查询构建环境
	SDic getConsEnvironment(String dicId);

	// 修改构建环境
	void updateConsEnvironment(SDic sdic);

	/**
	 * 
	 * 添加构建类型
	 */
	void addConsType(SDic sdic);

	/**
	 * 修改构建类型
	 */
	void updateConsType(SDic sdic);

	/**
	 * 验证构建类型
	 */
	SDic validateDicCode(String dicCode);

}