package com.ultrapower.ci.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sf.json.JSONObject;

public class HttpClientUtils {
	/**
	 * httpclient-post请求+参数测试例子
	 */
	public static boolean doPost(String url,Map<String,String> map) {

		boolean flag = false;
		//创建HttpClient对象  
        CloseableHttpClient closeHttpClient = HttpClients.createDefault();  
        CloseableHttpResponse httpResponse = null;  
        //发送Post请求  
        HttpPost httpPost = new HttpPost(url);  
        //设置Post参数  
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        Iterator iterator = map.entrySet().iterator();  
        while(iterator.hasNext()){  
            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
        }  
        try {  
        	//转换参数并设置编码格式  
            httpPost.setEntity(new UrlEncodedFormEntity(list,Consts.UTF_8));  
            //执行Post请求 得到Response对象  
            httpResponse = closeHttpClient.execute(httpPost);  
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                HttpEntity entity = httpResponse.getEntity();
                flag = true;
                return true;
            }else{
            	flag = false;
            	return false;
            } 
            
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{  
            if(httpResponse != null){  
                try {  
                    httpResponse.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if(closeHttpClient != null){  
                try {  
                    closeHttpClient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return flag;
	}
	
	/**
	 * httpclient-post请求传json格式数据 接收json字符串
	 */
	public static String doPostJson(String url, String json) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		post.addHeader("Authorization", "Basic YWRtaW46");
		String result = "";
		try {
			StringEntity s = new StringEntity(json, "utf-8");
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(s);
			// 发送请求
			HttpResponse httpResponse = client.execute(post);
			// 获取响应输入流
			InputStream inStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
			StringBuilder strber = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
				strber.append(line + "\n");
			inStream.close();
			result = strber.toString();
			result = result.substring(1, result.length() - 2);
			System.out.println(result);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("请求服务器成功，做相应处理");
			} else {
				System.out.println("请求服务端失败");
			}
		} catch (Exception e) {
			System.out.println("请求异常");
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		
		return request;
	}
	
	public static void main(String[] args) {
//		String url = OtherApplyConstant.CASS_CREATE_CONTAINER_URL;
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("random", "3007fc828b8f4adbb62c02a2049a5db4");
//		map.put("callbackUrl", "http://192.168.26.85:8080/ci/taskManage/addTomcatServer.do");
//		HttpClientUtils.doPost(url,map);
		String URL = "http://192.168.26.120:51080/atp/service/rest/v1/tasks/operate";
		JSONObject jsobj1 = new JSONObject();
		String[] taskId = {"task-817c57b3-9468-4c6c-93bf-6d9773bdd7ec"}; 
		jsobj1.put("userName", "niewei1");
        jsobj1.put("taskId", taskId);
        jsobj1.put("applyUrl", "192.168.120.102:51080");
        jsobj1.put("operate", "execute");
        jsobj1.put("callbackUrl", "http://ip:port/caas/line/task");
        JSONObject jsobj2 = new JSONObject();
        jsobj2.put("id", "123456");
        jsobj2.put("pipId", "67890");
        jsobj1.put("callbackParam", jsobj2.toString());
        String json = jsobj1.toString();
        System.out.println(json);
		HttpClientUtils.doPostJson(URL,json);
	}
}


