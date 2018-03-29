package com.ultrapower.ci.common.init;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.control.pipeManage.service.IPipeManageService;
import com.ultrapower.ci.control.taskManage.dao.TaskManageMapper;
import com.ultrapower.ci.control.taskManage.job.PipeJobFactory;
/**
 * 
 * @author yangbin6
 * Description : 当spring 容器初始化完成后执行这个方法
 * 2017年12月20日
 */
public class InitBean implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger logger = Logger.getLogger(InitBean.class);
	@Autowired
	private TaskManageMapper taskManageMapper;
	@Resource
	private IPipeManageService pipeManageService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
			
			//1、获取系统环境变量修改jdbc参数
			this.getSystemEnv();
			//2、初始化系统参数
			this.initSystemParam();
			//3、将正在为执行状态的流水线，修改为执行失败
			this.stopPipelining();
			//4、初始化流水线定时任务
//			this.initPipJob();
			logger.info("这是一个初始化方法=================");
		}
	}
	/**
	 * 获取系统环境变量
	 */
	private void getSystemEnv(){
		Properties prop = new Properties();
		InputStream in;
		try {
			//获取配置文件的值
			in = InitBean.class.getResourceAsStream("/jdbc-ultra.properties");
			prop.load(in);
			//获取系统环境变量
			Map<String, String> envMap = System.getenv();
			for(Iterator<String> itr = envMap.keySet().iterator();itr.hasNext();){
				String key = itr.next();
				if(key.equalsIgnoreCase("jdbcUltra_dbType")){
					 prop.setProperty("jdbcUltra.dbType", envMap.get(key));
				}else if(key.equalsIgnoreCase("jdbcUltra_driverClassName")){
					prop.setProperty("jdbcUltra.driverClassName", envMap.get(key));
				}else if(key.equalsIgnoreCase("jdbcUltra_url")){
					prop.setProperty("jdbcUltra.url", envMap.get(key));
				}else if(key.equalsIgnoreCase("jdbcUltra_username")){
					prop.setProperty("jdbcUltra.username", envMap.get(key));
				}else if(key.equalsIgnoreCase("jdbcUltra_password")){
					prop.setProperty("jdbcUltra.password", envMap.get(key));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 初始化参数（将配置文件sysConfig.properties中的参数初始化全局常量）
	 * 1、优先使用系统环境变量值，环境变量中不存在再使用配置文件中的值
	 */
	private void initSystemParam(){
		Properties prop = new Properties();
		InputStream in;
		try {
			//获取配置文件的值
			in = InitBean.class.getResourceAsStream("/sysConfig.properties");
			prop.load(in);
			Iterator<String> it=prop.stringPropertyNames().iterator();
			//获取系统环境变量
			Map<String, String> envMap = System.getenv();
		while(it.hasNext()){
              String key=it.next();
              if(key.equalsIgnoreCase("jenkins_host")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.JENKINS_HOST = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.JENKINS_HOST = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("jenkins_port")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.JENKINS_PORT = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.JENKINS_PORT = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("jenkins_username")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.JENKINS_USERNAME = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.JENKINS_USERNAME = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("jenkins_password")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.JENKINS_PASSWORD = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.JENKINS_PASSWORD = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("location_tomcat_ip")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.LOCATION_TOMCAT_IP = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.LOCATION_TOMCAT_IP = prop.getProperty(key);
            	  }
            	  
              }else if(key.equalsIgnoreCase("location_tomcat_port")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.LOCATION_TOMCAT_PORT = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.LOCATION_TOMCAT_PORT = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("caas_ip")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.CAAS_IP = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.CAAS_IP = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("caas_port")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.CAAS_PORT = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.CAAS_PORT = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("mail_Server")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.MAIL_SERVER = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.MAIL_SERVER = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("mail_ServerPort")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.MAIL_SERVERPORT = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.MAIL_SERVERPORT = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("mail_from_user")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.MAIL_FROM_USER = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.MAIL_FROM_USER = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("mail_password")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.MAIL_PASSWORD = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.MAIL_PASSWORD = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("mail_fromAddress")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.MAIL_FROMADDRESS = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.MAIL_FROMADDRESS = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("autoTest_ip")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.AUTOTEST_IP = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.AUTOTEST_IP = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("autoTest_port")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.AUTOTEST_PORT = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.AUTOTEST_PORT = prop.getProperty(key);
            	  }
              }else if(key.equalsIgnoreCase("jobLogPath")){
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.JOB_LOG_PATH = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.JOB_LOG_PATH = prop.getProperty(key);
            	  }
              }else if (key.equalsIgnoreCase("temPomXMl")) {
            	  if(envMap.get(key.toUpperCase()) != null){
            		  ApplyConstant.SVN_POM = envMap.get(key.toUpperCase());
            	  }else{
            		  ApplyConstant.SVN_POM = prop.getProperty(key);
            	  }
			}
          }
          in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动应用时，状态为正在执行的流水线，修改为执行失败状态
	 */
	private void stopPipelining(){
		pipeManageService.stopPipelining();
	}
	/**
	 * 初始化流水线定时任务（已经设置为定时执行的流水线，在服务重启时初始化定时任务）
	 */
	public void initPipJob(){
		Map<String, Object> pmap = new HashMap<String, Object>();
		//查询所有定时执行的流水线
		pmap.put("execution_mode", "2");
		List<Map<String, String>> pipList = taskManageMapper.queryPipeliningList(pmap);
		if(pipList != null && pipList.size() > 0){
			for (int i = 0; i < pipList.size(); i++) {
				Map<String,String> item = pipList.get(i);
				String pipId = item.get("ID").toString();
				String timed_cron = item.get("TIMED_CRON");
				if(timed_cron != null){
					//初始化流水线定时任务
					PipeJobFactory.startPipeByQuartz(pipId,timed_cron);
				}
			}
		}
	}
	
	
}
