package com.ultrapower.ci.common.constant;
/**
 * 
 * @author yangbin6
 * Description : 流水线、任务常量类
 * 2017年12月7日
 */
public class TaskConstant {

	//任务执行结果
	public final static String JOB_RESULTS_ITEM0 = "0";//正在进行
	public final static String JOB_RESULTS_ITEM1 = "1";//未开始
	public final static String JOB_RESULTS_ITEM2 = "2";//成功
	public final static String JOB_RESULTS_ITEM3 = "3";//失败
	
	//任务类型
	public final static String TASK_TYPE_ITEM0 = "0"; //子流水线
	public final static String TASK_TYPE_ITEM1 = "1"; //构建任务
	public final static String TASK_TYPE_ITEM2 = "2"; //部署任务
	public final static String TASK_TYPE_ITEM3 = "3"; //自动化测试任务
	public final static String TASK_TYPE_ITEM4 = "4";
	
	// job构建状态
	public final static String JOB_BUILD_STATUS_SUCCESS = "SUCCESS";//成功
	public final static String JOB_BUILD_STATUS_FAILURE = "FAILURE";//失败
	
}
