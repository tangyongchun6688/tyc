package com.ultrapower.ci.sys.dic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ultrapower.ci.common.base.controller.BaseController;
import com.ultrapower.ci.common.utils.DateTimeUtils;
import com.ultrapower.ci.sys.dic.entity.SDic;
import com.ultrapower.ci.sys.dic.service.ISDicService;

@Controller
@RequestMapping("sdic")
public class SDicController extends BaseController {

	@Resource
	private ISDicService sdicService;

	/**
	 * 
	 * @Title: queryDicItemList @Description: 返回构建环境与构建类型的json @param: @param
	 *         req @param: @param res @return: void @throws
	 */
	@RequestMapping("queryDicItemList")
	public void queryDicItemList(HttpServletRequest req, HttpServletResponse res) {
		try {
			writeUTFJson(res, sdicService.queryDicItemList());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *  返回构建类型的json
	 */
	@RequestMapping("queryDicItemListZ")
	public void queryDicItemListZ(HttpServletRequest req, HttpServletResponse res) {
		try {
			writeUTFJson(res, sdicService.queryDicItemListZ(req));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: queryConsEnvironment @Description: 返回构建环境的列表 @param: @param
	 *         request @param: @param res @param: @return @return:
	 *         String @throws
	 */
	@RequestMapping("queryConsEnvironment")
	public String queryConsEnvironment(HttpServletRequest request, HttpServletResponse res) {

		List<SDic> ConsEnvironmentList = sdicService.queryConsEnvironment();
		request.setAttribute("ConsEnvironmentList", ConsEnvironmentList);
		return "sdic/allDicitem";
	}

	/**
	 * 
	 * @Title: toAddConsEnvironment @Description: 去添加页面 @param: @param
	 *         request @param: @return @return: String @throws
	 */

	@RequestMapping("toAddConsEnvironment")
	public String toAddConsEnvironment(HttpServletRequest request) {

		return "sdic/addDicitem";
	}

	// 根据构建环境的id查询该构建环境
	@RequestMapping("getConsEnvironment")
	public String getConsEnvironment(HttpServletRequest request, String dicId) {
		SDic sdic = sdicService.getConsEnvironment(dicId);
		request.setAttribute("sdic", sdic);
		return "sdic/editDicitem";
	}

	// 根据id修改对应的构建环境
	@RequestMapping("updateConsEnvironment")
	public String updateConsEnvironment(HttpServletRequest request, SDic sdic) {
		 System.err.println("aa");
		sdicService.updateConsEnvironment(sdic);
        System.err.println("aa");
		return "redirect:/sdic/queryConsEnvironment.do";
	}

	// 添加构建环境
	@RequestMapping("addConsEnvironment")
	public String addConsEnvironment(HttpServletRequest request, SDic sdic) {

		sdicService.addConsEnvironment(sdic);

		return "redirect:/sdic/queryConsEnvironment.do";
	}

	// 删除构建环境
	@RequestMapping("delConsEnvironment")
	public void delConsEnvironment(String dicId, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		boolean result = sdicService.delConsEnvironment(dicId);
		jsonMap.put("result", "error");
		System.out.println(result);
		if (result) {
			jsonMap.put("result", "success");
			System.out.println(jsonMap);
		}
		try {
			writeUTFJson(response, jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 根据构建环境的id返回对应的构建类型
	@RequestMapping("queryConsTypeByPid")
	public String queryConsTypeByPid(String dicId, HttpServletRequest request) {
		List<SDic> queryConsTypeByPid = sdicService.queryConsTypeByPid(dicId);
		request.setAttribute("queryConsTypeByPid", queryConsTypeByPid);
		request.setAttribute("consEnvironment", sdicService.getConsEnvironment(dicId));
		
		return "sdic/listType";
	}

	// 删除构建环境对应的构建类型
	@RequestMapping("delConsType")
	public String delConsType(String dicId, HttpServletRequest request, String parentDicId) {
		sdicService.delConsType(dicId);
		
		return "redirect:/sdic/queryConsTypeByPid.do?dicId=" + parentDicId;
	}

	// 去构建类型的页面
	@RequestMapping("toAddConsType")
	public String toAddConsType(HttpServletRequest request, String parentDicId) {
		request.setAttribute("parentDicId", parentDicId);
		return "sdic/addType";
	}

	// 添加构建类型
	@RequestMapping("addConsType")
	public String addConsType(HttpServletRequest request, SDic sdic, String parentDicId) {
		sdicService.addConsType(sdic);
		return "redirect:/sdic/queryConsTypeByPid.do?dicId=" + parentDicId;
	}
     
	/**
	 * 根据id查构建类型
	 */
	@RequestMapping("getConsType")
	public String getConsType(HttpServletRequest request, String dicId) {
		SDic sdic = sdicService.getConsType(dicId);
		request.setAttribute("consType", sdic);
		return "sdic/editType";
	}
	
	/**
	 * 修改构建类型 
	 */
	@RequestMapping("updateConsType")
	public String updateConsType(HttpServletRequest request, SDic sdic) {
		sdicService.updateConsType(sdic);
		return "redirect:/sdic/queryConsEnvironment.do";
	}
	/**
	 * 验证类型
	 */
	@RequestMapping("validateDicCode")
	public void validateDicCode(String dicCode, HttpServletResponse response){
		boolean result = sdicService.validateDicCode(dicCode);
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("result", "error");
		//result=false  存在
		if (result) {
			jsonMap.put("result", "success");
		}
		try {
			writeUTFJson(response, jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前时间
	 * @param req
	 * @param res
	 */
	@RequestMapping("getNowFormatDate")
	public void getNowFormatDate(HttpServletRequest req, HttpServletResponse res) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			String currentTime = DateTimeUtils.getFormatCurrentTime();
			map.put("currentTime", currentTime);
			writeUTFJson(res, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
