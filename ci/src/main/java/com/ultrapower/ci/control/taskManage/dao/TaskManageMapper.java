package com.ultrapower.ci.control.taskManage.dao;

import java.util.List;
import java.util.Map;

import com.ultrapower.ci.control.taskManage.entity.TaskManage;

public interface TaskManageMapper {
    int deleteByPrimaryKey(String taskId);

    int insert(TaskManage record);

    int insertSelective(TaskManage record);

    TaskManage selectByPrimaryKey(String taskId);

    int updateByPrimaryKeySelective(TaskManage record);

    int updateByPrimaryKey(TaskManage record);
    /**
	 * 流水线表输入数据
	 * @author mxx
	 * @return
	 */
	int insertPip(Map<String, Object> pmap);
	/**
	 * 流水线步骤表插入数据
	 * @return
	 */
	int insertPipSteps(Map<String, Object> pmap);
	/**
	 * 流水线任务关联表插入数据
	 * @return
	 */
	int insertPipStepsTask(Map<String, Object> pmap);
	/**
	 * 代码仓库表插入数据
	 * @return
	 */
	int insertCodeRep(Map<String, Object> pmap);
	/**
	 * 构建任务表插入数据
	 * @return
	 */
	int insertTaskBuild(Map<String, Object> pmap);
	/**
	 * 部署任务表插入数据
	 * @return
	 */
	int insertTaskDeploy(Map<String, Object> pmap);
	
	
    
    /**
     * 查询流水线列表信息
     * @param map
     * @return
     */
    public List<Map<String,String>> queryPipeliningList(Map<String,Object> map);
    
    /**
     * 修改流水线表信息
     * @param map
     * @return
     */
    int updatePip(Map<String, Object> pmap);
    /**
     * 修改代码仓库表信息
     * @param map
     * @return
     */
    int updateCodeRep(Map<String, Object> pmap);
    /**
     * 修改构建任务表信息
     * @param map
     * @return
     */
    int updateTaskBuild(Map<String, Object> pmap);
    /**
     * 修改部署任务表信息
     * @param map
     * @return
     */
    int updateTaskDeploy(Map<String, Object> pmap);
    
    /**
     * 获取流水线构建历史最大版本 
     * @param pipId
     * @return
     */
    public Map<String,String> getMaxPipVersionByPipId(String pipId);
    /**
     * 获取流水线任务信息
     * @param pipId
     * @return
     */
    public List<Map<String,String>> queryTaskListByPipId(String pipId);
    /**
     * 查询一条流水线某一版本下真实历史
     * @param map
     * @return
     */
    public List<Map<String,String>> queryRealHistoryByVersion(Map<String,Object> map);
    /**
     * 查询一条流水线某一版本下历史（以最后一次流水线包含任务为准，历史中没有执行也会补全）
     * @param map
     * @return
     */
    public List<Map<String,String>> queryBuildHistoryByVersion(Map<String,Object> map);
    /**
     * 查询指定版本下流水线信息
     * @param map
     * @return
     */
    public Map<String,String> queryPipVersionInfo(Map<String,Object> map);
    /**
     * 查询流水线所有的执行历史版本
     * @param pipId
     * @return
     */
    public List<Map<String,String>> queryPipVersionList(String pipId);
    
    /**
     * 新增流水线历史记录
     * @param params
     * @return
     */
	boolean insertPipHistory(Map<String, Object> params);

	/**
	 * 更新流水线历史记录
	 * @param params
	 * @return
	 */
	boolean updatePipHistory(Map<String, Object> params);

	/**
	 * 新增构建job历史记录
	 * @param params
	 * @return
	 */
	boolean insertJobHistory(Map<String, Object> params);

	/**
	 * 修改job执行的历史记录
	 * @param params
	 * @return
	 */
	boolean updateSingleJobHistory(Map<String, Object> params);

	/**
	 * 查询单个job的历史记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getSingleJobHistory(Map<String, Object> params);

	/**
	 * 查询构建job时处于失败状态的
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getBuildJobFailure(Map<String, Object> params);
	/**
	 * 节点表插入数据
	 * @param params
	 * @return
	 */
	boolean insertNodeInfo(Map<String, Object> params);
	/**
	 * 自动化测试表插入数据
	 * @param params
	 * @return
	 */
	boolean insertTaskAuto(Map<String, Object> params);
	/**
	 * 节点表修改数据
	 * @param params
	 * @return
	 */
	boolean updateNodeInfo(Map<String, Object> params);
	/**
	 * 节点表清空ip 端口
	 * @param params
	 * @return
	 */
	boolean emptyNodeInfo(String taskId);
	/**
	 * 查询部署任务的名称
	 * @param 部署任务id
	 * @return
	 */
	String queryDeployName(String jobId);
	/**
	 * 查询部署任务的名称
	 * @param 部署任务id
	 * @return
	 */
	String queryRealDeployName(String jobId);
	/**
	 * 查询构建任务的名称
	 * @param 部署任务id
	 * @return
	 */
	String queryBuildName(String jobId);
	/**
	 * 查询节点的ID 即tomcat唯一标识
	 * @param params
	 * @return
	 */
	String queryNodeId(String jobId);
	/**
	 * 查询tomcat账号
	 * @param params
	 * @return
	 */
	String queryTomcatAccount(String jobId);
	/**
	 * 查询tomcat密码
	 * @param params
	 * @return
	 */
	String queryTomcatPassword(String jobId);
	/**
	 * 查询tomcat账号密码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> queryTomcatInfo2(String jobId);
	/**
	 * 节点表修改数据2（名称 密码）
	 * @param params
	 * @return
	 */
	boolean updateNodeInfo2(Map<String, Object> params);
	/**
	 * 自动化测试表修改数据
	 * @param params
	 * @return
	 */
	boolean updateTaskAutoInfo(Map<String, Object> params);
	/**
	 * 查询流水线名称
	 * @param params
	 * @return
	 */
	Map<String, Object> queryPipName(String pipId);
	/**
	 * 查询部署任务信息
	 * @param params
	 * @return
	 */
	Map<String, Object> queryDeployTaskInfo(String pipId);
	/**
	 * 查询svn账号密码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> querySvnInfo(String pipId);
	/**
	 * 查询tomcat账号密码
	 * @param params
	 * @return
	 */
	List<Map<String, String>> queryTomcatInfo(String pipId);
	/**
	 * 查询构建环境及类型
	 * @param params
	 * @return
	 */
	List<Map<String, String>> querytaskInfo(String pipId);
	/**
	 * 查询收件人邮箱
	 * @param params
	 * @return
	 */
	String queryEmail(String jobId);
	/**
	 * 删除流水线表相关信息
	 * @param pipId
	 * @return
	 */
	int deletePipelining_info(String taskId);
	/**
	 * 删除流水线步骤表相关信息
	 * @param pipId
	 * @return
	 */
	int deletePipelining_steps(String taskId);
	/**
	 * 删除步骤任务关联表相关信息
	 * @param pipId
	 * @return
	 */
	int deletePipelining_steps_task(String taskId);
	/**
	 * 删除流水线历史表相关信息
	 * @param pipId
	 * @return
	 */
	int deleteBuildHistory(String taskId);
	/**
	 * 删除构建任务表相关信息
	 * @param pipId
	 * @return
	 */
	int deleteTask_build(String taskId);
	/**
	 * 删除部署任务表相关信息
	 * @param pipId
	 * @return
	 */
	int deleteTask_deploy(String taskId);
	/**
	 * 删除流水线任务关联表相关信息
	 * @param pipId
	 * @return
	 */
	int deletePipelining_task(String taskId);
	/**
	 * 删除步骤表中的某个步骤
	 * @param pipId
	 * @return
	 */
	int deletePipeliningSteps(Map<String, Object> params);
	/**
	 * 删除步骤任务关联表表
	 * @param pipId
	 * @return
	 */
	int deletePipeliningStepsTask(Map<String, Object> params);
	/**
	 * 删除节点表相关信息
	 * @param pipId
	 * @return
	 */
	int deleteNode_info(String taskId);
	/**
	 * 删除自动化测试表
	 * @param pipId
	 * @return
	 */
	int deleteTaskAutoTest(String taskId);
	/**
	 * 删除代码仓库表相关信息
	 * @param pipId
	 * @return
	 */
	int deletecode_repositories(String taskId);
	/**
	 * 获取jenkins构建任务名
	 * @param pipId
	 * @return
	 */
	String queryJenkinsBuildName(String id);
	/**
	 * 获取jenkins部署任务名
	 * @param pipId
	 * @return
	 */
	String queryJenkinsDeployName(String id);
	/**
	 * 获取自动化测试任务id
	 * @param pipId
	 * @return
	 */
	String queryTaskAutoId(String id);
	/**
	 * 通过主键查询一条流水线信息
	 * @param pipId
	 * @return
	 */
	public Map<String,String> selectOnePipByPipId(String pipId);

	/**
	 * 更新自动化测试历史记录
	 * @param params
	 * @return
	 */
	public boolean updateAutoTestHistory(Map<String, Object> params);

	/**
	 * 查询自动化测试记录
	 * @param params
	 * @return
	 */
	public Map<String, Object> getAutoTestHistory(Map<String, Object> params);

	/**
	 * 清空自动化测试历史记录中已有的状态
	 * @param params
	 * @return
	 */
	public boolean cleanAutoTestByStatus(Map<String, Object> params);

	/**
	 * 根据任务id查询部署后的应用url
	 * @param taskId 
	 * @return
	 */
	public Map<String, Object> getDeployHistroyByUrl(String taskId);

	/**
	 * 根据流水线id及版本号查询流水线相关历史记录
	 * @param maps
	 * @return
	 */
	List<Map<String, Object>> getPipliningListInfo(Map<String, Object> maps);
	/**
	 * 通过主键修改流水线的执行方式（1表示手动执行，2表示定时执行）
	 * @param map
	 */
	public int updateExecutionModeById(Map<String,Object> map);

	/**
	 * 根据自动化测试流水线历史id查询开始时间
	 * @param params
	 * @return
	 */
	public Map<String, Object> getAutoTestById(Map<String, Object> params);
	/**
	 * 根据流水线id查询所有部署任务的id
	 * @param params
	 * @return
	 */
	List<Map<String, String>> selectTaskId(String pipId);
	/**
	 * 查询指定流水线历史版本中部署任务关联的节点IP和端口
	 * @param map
	 * @return
	 */
	public Map<String, Object> getDeployIpAndPortByUrl(Map<String,Object> map);
	/**
	 * 根据流水线id查询所有部署任务的id2
	 * @param params
	 * @return
	 */
	List<Map<String, String>> selectTaskId2(String pipId);

	/**
	 * 根据流水线id查询流水线的结束时间、耗时、邮件地址
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getPiplineById(Map<String, Object> params);
	/**
	 * 插入任务参数配置表数据
	 * @param params
	 * @return
	 */

	boolean insertTaskParameter(List<Map> list);
	/**
	 * 新建流水线组，查询流水线列表
	 * @param params
	 * @return
	 * @author mxx
	 * @param j 
	 * @param i 
	 */

	List<Map<String, String>> queryPipList(Map<String, Object> paramMap);
	/**
	 * 新建流水线组，查询流水线列表总数  
	 * @param params
	 * @return
	 * @author mxx
	 * @param j 
	 * @param i 
	 */
	int queryPipCount();
	/**
	 * 新建流水线组，选中某条流水线的信息查询
	 * @param params
	 * @return
	 * @author mxx
	 * @param j 
	 * @param i 
	 */
	List<Map<String, String>> queryPipInfo(Map<String, Object> paramMap);
	/**
	 * 修改单流水线查询参数列表
	 * @author mxx
	*/
	List<Map<String, String>> queryparamInfo(String pipId);
	/**
	 * 删除流水线任务关联表中的一条
	 * @param pipId
	 * @return
	 */
	int deletePipelining_task2(Map<String, Object> delMap);
	/**根据task_id删除现有的参数
	 * 
	 * @param taskDeployId
	 */
	int  deleteTaskParameter(String taskDeployId);
}