package com.ultrapower.ci.sys.dic.service;

import java.util.List;
import java.util.Map;


import com.ultrapower.ci.sys.dic.entity.SDic;


import javax.servlet.http.HttpServletRequest;


public interface ISDicService {
	
	/**
	 * 
	 * @Title:             queryDicItemList
	 * @Description:     返回构建环境与构建类型的数据
	 * @param:             @return   
	 * @return:         Map<String,Object>   
	 * @throws
	 */
	
	public  Map<String, Object> queryDicItemList();
	/**
	 * 查询构建环境对应的构建列表
	 */
	public  Map<String, Object> queryDicItemListZ(HttpServletRequest req);
    /**
     * 返回所有构建环境
     */
	public List<SDic> queryConsEnvironment();
    /**
     * 增加构建环境
     */
	public String addConsEnvironment(SDic sdic);
    /**
     * 删除构建环境
     */
	public boolean delConsEnvironment(String dicId);
    /**
     * 根据构建环境的id查询对应的构建类型
     */
	public List<SDic> queryConsTypeByPid(String dicId);
    /**
     * 删除构建类型
     */
	public void delConsType(String dicId);
    /**
     * 根据id获取构建环境
     */
	public SDic getConsEnvironment(String dicId);
    /**
     * 更新构建环境
     */
	public void updateConsEnvironment(SDic sdic);
	/**
	 * 
	 *添加构建类型
	 */
	public void addConsType(SDic sdic);
	/**
	 * 修改构建类型
	 */
	public void updateConsType(SDic sdic);
	/**
	 * 查询构建类型
	 */
	public SDic getConsType(String dicId);
	/**
	 * 验证编码类型是否可用
	 */
	public boolean validateDicCode(String dicCode);

}
