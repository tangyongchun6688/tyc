package com.ultrapower.ci.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import com.offbytwo.jenkins.model.TestReport;
import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.common.constant.TaskConstant;
import com.ultrapower.ci.control.taskManage.service.TaskManageServiceImpl;

import net.sf.json.JSONObject;

public class JenkinsUtils {
	private static final Logger logger = Logger.getLogger(JenkinsUtils.class);
	public static String jobName="svn-maven-ssh-war";
	public static String jenkins_host=""; // jenkins的ip
	public static String jenkins_port=""; // jenkins的端口
	public static String jenkins_username=""; // jenkins的用户名
	public static String jenkins_password=""; // jenkins的密码
	static{
		jenkins_host = ApplyConstant.JENKINS_HOST;
		jenkins_port = ApplyConstant.JENKINS_PORT;
		jenkins_username = ApplyConstant.JENKINS_USERNAME;
		jenkins_password = ApplyConstant.JENKINS_PASSWORD;
	}
	
	public static JenkinsServer getJenkinsServer(){
		JenkinsServer jenkinsServer = null;
		try {
			jenkinsServer = new JenkinsServer(new URI("http://"+jenkins_host+":"+jenkins_port+"/jenkins"), jenkins_username, jenkins_password);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return jenkinsServer;
		
	}
    /**
     * 生成svn用户名、密码
     * @param credentialID
     * @param svnUsername
     * @param svnPassword
     */
    public static void createCredential(final String credentialID, final String svnUsername, final String svnPassword) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //生成目标主机
        HttpHost targetHost = new HttpHost(jenkins_host, Integer.parseInt(jenkins_port), "http");
        //将认证信息加到请求内容
        HttpClientContext context = getContext();
        //生成表单数据
        List<NameValuePair> formparams = new ArrayList<>();
        String json = "{\"\": \"0\", \"credentials\": {\"scope\": \"GLOBAL\", \"username\": \"" + svnUsername + "\", \"password\": \"" + svnPassword + "\", \"id\": \"" + credentialID + "\", \"description\": \"\", \"stapler-class\": \"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\", \"$class\": \"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"}}";
        formparams.add(new BasicNameValuePair("json", json));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        //生成POST方法
        HttpPost post = new HttpPost("http://" + jenkins_host + ":" + jenkins_port + "/jenkins/credentials/store/system/domain/_/createCredentials");
        post.setEntity(entity);
        //执行方法
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(
                    targetHost, post, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加任务时申请一台tomcat服务器
     * @param jobId
     * @param callbackUrl
     * @return
     */
    public static boolean createTomcatServer(String ultra_caas_host,String ultra_caas_port,String jobId,String callbackUrl){
    	boolean flag = false;
    	CloseableHttpClient httpclient = HttpClients.createDefault();
        //生成目标主机
        HttpHost targetHost = new HttpHost(ultra_caas_host, Integer.parseInt(ultra_caas_port), "http");
        //将认证信息加到请求内容
        HttpClientContext context = getContext();
        //生成表单数据
        List<NameValuePair> formparams = new ArrayList<>();
        String json = "{\"random\" : \"" + jobId + "\",\"callbackUrl\" : \"" + callbackUrl + "\"}";
        formparams.add(new BasicNameValuePair("json", json));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        //生成POST方法
        HttpPost post = new HttpPost("http://" + ultra_caas_host + ":" + ultra_caas_port + "/szty-web/v2/app/createContainer.do");
        post.setEntity(entity);
        //执行方法
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(targetHost, post, context);
            
            HttpEntity entity2 = response.getEntity(); 
            if (entity2 != null) { 
                //将返回的数据直接转成String
                String str = EntityUtils.toString(entity2, "UTF-8") ;
                //注意这里不能写成EntityUtils.toString(entity, "UTF-8"),因为EntityUtils只能调用一次，否则会报错：java.io.IOException: Attempted read from closed stream
                System.out.println("Response content: " + str);  
                JSONObject responseJson = JSONObject.fromObject(str);  
                String result = responseJson.getString("result");
                if(result.equals("success")){
                	flag = true;
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
    	return flag;
    }
    
    /**
     * 获取请求上下文Context
     * 1.从存放配置信息的静态map prop中获取jenkins的ip、端口号、用户名、密码
     * 2.实例化HttpHost、Credentials
     * 3.将Credentials加到Context
     * @return
     */
    private static HttpClientContext getContext(){
        //获取参数
        // 生成目标主机
        HttpHost targetHost = new HttpHost(jenkins_host, Integer.parseInt(jenkins_port), "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        //生成认证凭证
        Credentials creds = new org.apache.http.auth.UsernamePasswordCredentials(jenkins_username, jenkins_password);
        credsProvider.setCredentials(
                new org.apache.http.auth.AuthScope(targetHost.getHostName(), targetHost.getPort()),
                creds);
        //设置认证缓存
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);
        //将凭证加到Context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        return context;
    }    
    /**
     * 创建job
     * @param 任务名称
     * @param 任务对应xml
     * @return
     */
	public static void  createJob(String jobName , String jobXml){
		logger.info("createJobStart***********************33");
		try {
//			StringBuilder build = new StringBuilder();
//			File file = new File(path);
//			InputStream  in = new FileInputStream(file);
//			InputStreamReader read = new InputStreamReader(in);
//	            BufferedReader bufferedReader = new BufferedReader(read);
//	            String lineTxt = null;
//	            while ((lineTxt = bufferedReader.readLine()) != null) {
//	                build.append(lineTxt);
//	            }
			logger.info("createJobStart***********************34");
			JenkinsUtils.getJenkinsServer().createJob(jobName, jobXml);
			logger.info("createJobEnd***********************35");
		} catch (IOException e) {
			logger.info("createJobException***********************36");
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("创建job"+ jobName +"失败");
		}
		logger.info("endendendend***********************37");
	}
	 /**
     * 修改job
     * @param 任务名称
     * @param 任务对应xml
     * @return
     */
	public static void  updateJob(String jobName , String jobXml){
		try {
			JenkinsUtils.getJenkinsServer().updateJob(jobName, jobXml,false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("修改job"+ jobName +"失败");
		}
	}
	
	/**
	 * 执行任务，执行完成后返回true
	 * @param jobName
	 * @return
	 */
	public static Map<String, Object> runJob(String jobName){
		// 存储构建后的job信息
		Map<String, Object> jobInfo = new HashMap<String, Object>();
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				JobWithDetails job = jobs.get(jobName).details();
				int buildNumber = job.getNextBuildNumber();
				System.out.println("queue---getNextBuildNumber------"+buildNumber);
				QueueReference queue = job.build();
				
//				for(int i=0;i<10000;i++){
//					if(jobs.get(jobName).details().getLastCompletedBuild().getNumber()==buildNumber){
//						System.out.println("job执行完成！！！！");
//						
//						BuildWithDetails details = jobs.get(jobName).details().getBuildByNumber(buildNumber).details();
//						String status = details.getResult().toString();
//						if (TaskConstant.JOB_BUILD_STATUS_SUCCESS.equals(status)) {
//							jobInfo.put("flag", TaskConstant.JOB_BUILD_STATUS_SUCCESS);
//						}else {
//							jobInfo.put("flag", TaskConstant.JOB_BUILD_STATUS_FAILURE);
//						}
//						System.out.println("---job构建状态-----"+ status);
//						
//						long duration = details.getDuration();
//						System.out.println("----构建持续时间-----"+	duration);
//						
//						jobInfo.put("buildNumber", buildNumber);
//						jobInfo.put("duration", duration);
//						
//						break;
//					}
//					Thread.sleep(1000);
//				}
				jobInfo.put("buildNumber", buildNumber);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jobInfo;
	}
	
	/**
	 * 获取war名称
	 * @param jobName
	 * @return
	 */
	public static String getWarName(String jobName){
		String warName = "";
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				// 获取job的详细信息
				JobWithDetails jobInfo = jobs.get(jobName).details();
				// 获取构建后job的详细信息
				BuildWithDetails buildWithDetails = jobInfo.getLastBuild().details();
				List<Artifact> list = buildWithDetails.getArtifacts();
				for (int i = 0; i < list.size(); i++) {
					Artifact a = list.get(i);
					String fileName = a.getFileName();
					warName = fileName.substring(0,fileName.lastIndexOf("."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return warName;
	}
	
	/**
	 * 获取构建job的次数
	 * @param jobName job名称
	 * @return
	 */
	public static int getBulidJobByNumber(String jobName){
		int buildNumber = 0;
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				BuildWithDetails details = jobWithDetails.getLastBuild().details();
				buildNumber = details.getNumber();
				System.out.println("当前"+jobName+"的构建次数："+buildNumber);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buildNumber;
	}
	
	/**
	 * 获取构建job的状态
	 * @param jobName job名称
	 * @param buildNumber 
	 * @return
	 */
	public static String getBuildJobByStatus(String jobName, int buildNumber){
		String status = "";
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				BuildWithDetails details = jobWithDetails.getBuildByNumber(buildNumber).details();
				status = details.getResult().toString();
				System.out.println("---job构建状态-----"+ status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * 获取构建job的持续时间
	 * @param jobName job名称
	 * @param buildNumber 构建次数
	 * @return
	 */
	public static long getBuildJobDuration(String jobName){
		long duration = 0;
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				BuildWithDetails details = jobWithDetails.getBuildByNumber(JenkinsUtils.getBulidJobByNumber(jobName)).details();
				duration = details.getDuration();
				System.out.println("----构建持续时间-----"+	duration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration;
	}
	
	/**
	 * httpclient-post请求+参数测试例子
	 */
	public static void postAndParamsTest() {

		//创建HttpClient对象  
        CloseableHttpClient closeHttpClient = HttpClients.createDefault();  
        CloseableHttpResponse httpResponse = null;  
        //发送Post请求  
        HttpPost httpPost = new HttpPost("http://192.168.26.127:8080/ci/taskManage/addTomcatServer.do");  
        //设置Post参数  
        List<NameValuePair> params = Lists.newArrayList();  
        params.add(new BasicNameValuePair("random", "参数测试"));  
        try {  
            //转换参数并设置编码格式  
            httpPost.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));  
            //执行Post请求 得到Response对象  
            httpResponse = closeHttpClient.execute(httpPost);  
            //httpResponse.getStatusLine() 响应头信息  
            System.out.println(httpResponse.getStatusLine());  
            //返回对象 向上造型  
            HttpEntity httpEntity = httpResponse.getEntity();  
            if(httpEntity != null){  
                //响应输入流  
                InputStream is = httpEntity.getContent();  
                //转换为字符输入流  
                BufferedReader br = new BufferedReader(new InputStreamReader(is,Consts.UTF_8));  
                String line = null;  
                while((line=br.readLine())!=null){  
                    System.out.println(line);  
                }  
                //关闭输入流  
                is.close();  
            }  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
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
	
	}
	
	/**
	 * 读取配置文件
	 * @param propertyName
	 * @return
	 */
//	public static String getPropertyValue(String propertyName) {
//		Properties prop = new Properties();
//		InputStream in;
//		try {
//			in = JenkinsUtils.class.getResourceAsStream("/sysConfig.properties");
//			prop.load(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return prop.getProperty(propertyName).trim();
//	}
	
	/**
	 * 获取job构建后的日志
	 * @param jobName
	 * @param buildNumber
	 * @return
	 */
	public static String getJobBuildLogs(String jobName, int buildNumber){
		String result = "";
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				BuildWithDetails details = jobWithDetails.getBuildByNumber(buildNumber).details();
				result = details.getConsoleOutputText();
				System.out.println("---job构建日志---"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 删除jenkins工作空间下的jobs目录下的某个job
	 * @param jobName job名称
	 * @return
	 */
	public static boolean deleteJobInfo(String jobName){
		logger.info("delete***********************40");
		boolean flag = false;
		try {
			logger.info("delete***********************41");
			JenkinsServer jenkinsServer = JenkinsUtils.getJenkinsServer();
			boolean isStart = jenkinsServer.isRunning();
			logger.info("delete***********************42");
			if( isStart){
				Map<String, Job> jobs = jenkinsServer.getJobs();
				if(jobs.get(jobName) != null){
					logger.info("delete***********************43");
					jenkinsServer.deleteJob(jobName);
					logger.info("delete***********************44");
					System.out.println("删除job信息");
				}
				flag = true;
			}else{
				flag = false;
			}
			
		} catch (Exception e) {
			logger.info("delete***********************45");
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 下载构建后job的war包
	 * @param jobName
	 * @return
	 */
	public static InputStream downJobWar(String jobName,int buildNumber){
		InputStream in = null;
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				// 获取创建job的详情
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				// 获取构建后job详情
				BuildWithDetails buildWithDetails = jobWithDetails.getBuildByNumber(buildNumber).details();
				List<Artifact> list = buildWithDetails.getArtifacts();
				for (int i = 0; i < list.size(); i++) {
					// 获取存档对象
					Artifact artifact = list.get(i);
					in = buildWithDetails.downloadArtifact(artifact);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
	
	/**
	 * 获取构建后的job测试报告
	 * @param jobName
	 * @param buildNumber
	 * @return
	 */
	public static Map<String, Object> getTestReport(String jobName,int buildNumber){
		Map<String, Object> reportInfo = new HashMap<String, Object>();
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				// 获取创建job的详情
				JobWithDetails jobWithDetails = jobs.get(jobName).details();
				// 获取构建后job详情
				BuildWithDetails buildWithDetails = jobWithDetails.getBuildByNumber(buildNumber).details();
				// 获取测试报告信息
				TestReport testReport = buildWithDetails.getTestReport();
				
				reportInfo.put("totalCount", testReport.getTotalCount()); // 测试总数量
				reportInfo.put("failCount",testReport.getFailCount()); // 失败测试数量
				reportInfo.put("skipCount", testReport.getSkipCount()); // 测试跳出数量
				System.out.println("--测试结果信息--"+reportInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reportInfo;
	}
	
	/**
	 * 执行任务，执行完成后返回true
	 * @param jobName
	 * @return
	 */
	public static Map<String, Object> jobInfo(String jobName,int buildNumber){
		// 存储构建后的job信息
		Map<String, Object> jobInfo = new HashMap<String, Object>();
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				BuildWithDetails details = jobs.get(jobName).details().getBuildByNumber(buildNumber).details();
				BuildResult status =details.getResult();
				if (TaskConstant.JOB_BUILD_STATUS_SUCCESS.equals(status.toString())) {
					jobInfo.put("flag", TaskConstant.JOB_BUILD_STATUS_SUCCESS);
				}else {
					jobInfo.put("flag", TaskConstant.JOB_BUILD_STATUS_FAILURE);
				}
				long duration = details.getDuration();
				
				jobInfo.put("duration", duration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jobInfo;
	}
	/**
	 * 根据job名查询job是否存在
	 * @param jobName
	 * @return
	 */
	public static boolean exist(String jobName){
		boolean bool = true;
		Map<String, Object> jobInfo = new HashMap<String, Object>();
		try {
			Map<String, Job> jobs = JenkinsUtils.getJenkinsServer().getJobs();
			if(jobs.get(jobName) != null){
				bool = true;
				}else{
					bool = false;
				}
			}catch (Exception e) {
				e.printStackTrace();
			} 
		return bool;
	}
	
	public static void main(String[] args) {
		jobInfo("build_job",1);
		exist("01ce0f25adda45e68679bd9ba4b04904_build");
	}
}
