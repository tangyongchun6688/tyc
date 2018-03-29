package com.ultrapower.ci.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class RequestUtils {
	public static final String check_code="checkCode_ci_101";
	/**
	 * 请求对象request转成map 
	 * @param req
	 * @param must   必填列表
	 * @param nomust 非必填列表
	 * @return
	 */
	public static Map<String, Object> requestToMap(HttpServletRequest req,String[] must,String []nomust){
		Map<String, Object> map=new HashMap<String, Object>();
		String p=null;
		if (must!=null) {
			for (int i = 0; i < must.length; i++) {
				p=req.getParameter(must[i]);
				if (p!=null&&p.trim().length()>0) {
					map.put(must[i], p);
				}else{//必填项出现 未填
					return null;
				}
			}
		}
		if (nomust!=null) {
			for (int i = 0; i < nomust.length; i++) {
				p=req.getParameter(nomust[i]);
				if (p!=null&&p.trim().length()>0) {
					map.put(nomust[i], p);
				}else{
					map.put(nomust[i], "");
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取参数值
	 * 增加传值过滤，防止sql注入
	 * @param req
	 * @param paramName
	 * @return
	 */
	public static String getParamValue(HttpServletRequest req,String paramName){
		return req.getParameter(paramName);
	}
	
	/**
	 * 校验string类型必填项
	 * @param str
	 * @return true:参数必填项全部有值
	 */
	public static boolean checkMustParam(String... args) throws Exception{
		boolean flag = true;
		for(String str : args){
			if(str == null || "".equals(str)){
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 数组转list
	 * @param strs
	 * @return
	 * @throws Exception
	 */
	 public static List<String> arrayToList(String[] strs) throws Exception{
	    	List<String> list = new ArrayList<String>();
	    	for(String str : strs){
	    		if(checkMustParam(str)){
	    			list.add(str);
	    		}
	    	}
	    	return list;
	    }
	
	/**
	 * 字符串Decode转UTF-8
	 * @param paramVal
	 * @return
	 */
	public static String strDecodeToUTF(String paramVal){
		try {
			return URLDecoder.decode(paramVal,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 数组参数的操作
	 * @param req
	 * @param must
	 * @param onmust
	 * @param paramCallback 回调接口
	 */
	public static void paramListHandler(HttpServletRequest req,String pre,String[]must,String[] nomust,ParamCallback paramCallback){
		Map<String, String[]> params=new HashMap<String, String[]>();
		int num=0;
		String[] allParams;
		if (nomust==null) {
			allParams=new String[must.length];
		}else{
			allParams=new String[must.length+nomust.length];
		}
		
		for (int i = 0; i < must.length; i++) {
			allParams[i]=must[i];
			String[] ss=req.getParameterValues(pre+must[i]);
			if (ss!=null&&num==0) {
				num=ss.length;
			}else if(ss==null||(num!=0&&num!=ss.length)){
				throw new RuntimeException("缺少必填项!");
			}
			params.put(must[i], ss);
		}
		
		for (int i = 0; i < nomust.length; i++) {
			allParams[must.length+i]=nomust[i];
			String[] ss=req.getParameterValues(pre+nomust[i]);
			if (ss!=null&&ss.length>num) {
				params.put(nomust[i], ss);
			}
		}
		
		Map<String, String> pmap=new HashMap<String, String>();
		for (int j = 0; j < num; j++) {
			pmap.clear();
			String s="";
			for (int i = 0; i < allParams.length; i++) {
				if (params.get(allParams[i])!=null&&params.get(allParams[i]).length-1>=j) {
					String[] ss=(params.get(allParams[i]));
					s=ss[j];
				}
				pmap.put(allParams[i], s);
				s="";
			}
			if (pmap.size()>0) {
				paramCallback.callback(pmap);
			}
		}
	}
	
	/**
	 * 数组参数回调的接口
	 * @author Administrator
	 *
	 */
	public interface ParamCallback{
		public void callback(Map<String, String> map);
	}
	/**
	 * 循环取出http请求中所有参数的值封装在HashMap中
	 * @param req
	 * @return
	 */
	public static Map<String, Object> getParameters(HttpServletRequest req){
		Enumeration params = req.getParameterNames();
		Map<String, Object> pmap = new HashMap<String, Object>();
		while (params.hasMoreElements()) {
			String parametername = new String();
			String parametervalue = new String();
			parametername = (String) params.nextElement();
			parametervalue = (String) req.getParameter(parametername);
			pmap.put(parametername, parametervalue);
		}
		return pmap;
	}
}