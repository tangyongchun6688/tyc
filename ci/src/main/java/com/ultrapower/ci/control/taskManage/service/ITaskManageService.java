package com.ultrapower.ci.control.taskManage.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;

public interface ITaskManageService {

	/**
	 * 新建流水线
	 * 
	 * @return
	 */
	public Map<String, Object> addPipelining(HttpServletRequest req);
	/**
	 * 修改流水线
	 * 
	 * @return
	 */
	public Map<String, Object> updatePipeliningInfo(HttpServletRequest req);
	
	/**
	 * 修改流水线信息
	 * 
	 * @return
	 */
	public Map<String, Object> updatePipelining(HttpServletRequest req);

	/**
	 * 查询流水线列表
	 * 
	 * @param req
	 * @return
	 */
	public Map<String, Object> getPipliningList(HttpServletRequest req);

	/**
	 * 执行单个job并将job信息记录到历史表中
	 * @param maps
	 * @return
	 * @throws InterruptedException 
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws DeploymentException 
	 * @throws EncodeException 
	 */
	public Map<String, Object> updateSingleJobHistory(Map<String, Object> maps) throws InterruptedException, DeploymentException, IOException, URISyntaxException, EncodeException;

	/**
	 * 存储流水线记录信息接口
	 * @param maps
	 * @return
	 */
	public Map<String, Object> savePipHistory(Map<String, Object> maps);
	
	/**
	 * 添加Tomcat信息
	 * 
	 * @return
	 */
	public Map<String, Object> addTomcatServer(HttpServletRequest req);
	/**
	 * 查询流水线执行历史
	 * @param request
	 * @return
	 */
	public Map<String,Object> queryPipHistoryList(HttpServletRequest req);
	/**
	 * 获取job日志
	 * @param req
	 * @return
	 */
	public String queryTaskLoginfo(HttpServletRequest req);
	/**
	 * 定时执行流水线
	 * @param req
	 * @return
	 */
	public Map<String,Object> timerExecution(Map<String,Object> map);
	/**
	 * 查询自动化测试任务
	 * @param req
	 * @return
	 */
	public String queryAutomatedTask(HttpServletRequest req);
	/**
	 * 删除流水线
	 * 
	 * @return
	 */
	public Map<String, Object> deletePipeliningById(HttpServletRequest req);
	/**
	 * 删除流水线
	 * @param 流水线id
	 * @return
	 */
	public Map<String, Object> deletePipeliningByPipId(String pipId);
	/**
	 * 新增自动化测试历史记录
	 * @param request
	 * @return
	 */
	public Map<String, Object> saveAutoTestHistory(HttpServletRequest request);
	
	public List<Map<String, Object>> getPipliningListInfo(Map<String, Object> maps);
	
	/**
	 * 清空节点表中的ip及port
	 * @param taskId
	 */
	public void emptyNodeInfo(String taskId);
	/**
	 * 根据项目主键，获取项目的流水线列表
	 * @param req
	 * @return
	 */
	public Map<String, Object> getPipliningListByProjectId(HttpServletRequest req);
	
	/**
	 * 查询自动化历史信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> getAutoTestHistory(Map<String, Object> params);
	/**
     * 获取流水线构建历史最大版本 
     * @param pipId
     * @return
     */
    public Map<String,String> getMaxPipVersionByPipId(String pipId);
    /**
     * 查询一条流水线某一版本下历史
     * @param map
     * @return
     */
    public List<Map<String,String>> queryBuildHistoryByVersion(Map<String,Object> map);
    /**
     * 获取流水线任务信息
     * @param pipId
     * @return
     */
    public List<Map<String,String>> queryTaskListByPipId(String pipId);
    
    /**
     * 通过webSocket获取自动化测试的日志（提供给测试平台）
     * @param req
     * @return
     */
    public Map<String,Object> autoTestLogByWs(HttpServletRequest req,String json);
    /**
	 * 新建流水线组，查询流水线列表
	 * @param req
	 * @param res
	 * @author mxx
	 */
	public Map<String, Object> queryPipList(HttpServletRequest req);
	/**
	 * 新建流水线组，查询流水线列表
	 * @param req
	 * @param res
	 * @author mxx
	 */
	public Map<String, Object> queryPipInfo(HttpServletRequest req);
}
