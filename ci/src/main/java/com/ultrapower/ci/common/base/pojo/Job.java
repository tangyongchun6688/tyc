package com.ultrapower.ci.common.base.pojo;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 作业模型
 * @author niewei
 * @since 1.0
 * @version 1.0.0.2 Jul.15, 2017
 */
public class Job {

    private String id = UUID.randomUUID().toString();                       // 主键
    private String create_date ="";                 // 创建时间
    private String modify_date;                                             // 修改时间
    private String jobName;                                                 // 作业名称：创建人.任务名称
    private String taskName;                                                // 任务名称，后台使用
    private String taskNameZh;                                              // 任务名称，前台使用
    private Map<String, List<String>> layoutResult;                         // 任务编排结果
    private String lang;                                                    // 作业类型：java py or interface
    private String type = "web";                                            // 作业类型：web or ci
    private List<String> browser;                                           // 作业使用的浏览器：目前List只存一个值
    private String browserType = "0";                                       // 浏览器类型，0=系统，1=用户自定义
    private String browserStyle;                                            // 浏览器类型，用于页面显示
    private List<String> script;                                            // 作业执行的测试用例列表
    private String classifyName;                                            // 需求分类信息
    private String svnUrl;                                                  // SVN地址
    private String svnUsername;                                             // SVN用户名
    private String svnPassword;                                             // SVN密码
    private String timeTrigger;                                             // 定时触发
    private String scmTrigger;                                              // 提交触发
    private String emailTrigger;                                            // 邮件发送类型，失败时发送或成功时发送
    private String emailReceiver;                                           // 邮件接收者
    private String userName;                                                // 作业创建者
    //private String executeUserName;                                         // 作业执行人
    private String goals;                                                   // 构建目标
    private String buildNo = "1";                                           // 构建编号/执行次数，从1开始
    private String status = "0";                                            // 作业状态
    //  0 执行完毕，待检查
    // -1 执行完毕，无缺陷
    // -2 执行完毕，有缺陷
    // -3 执行超时
    //  3 构建成功
    //  4 构建失败
    //  5 等待执行
    //  6 停用
    private String hosts;													//shell的hosts
    private String csaHandler;												//远程的csa的url
    private String shellServerUserName;                                     //shell服务器登录名
    private String shellServerPassword;										//shell服务器的密码
    private String shell;													//执行的shell
    private String recallHandler;											//回调函数

    private String lastResultTime;                                          // 最后执行作业报告的创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNameZh() {
        return taskNameZh;
    }

    public void setTaskNameZh(String taskNameZh) {
        this.taskNameZh = taskNameZh;
    }

    public Map<String, List<String>> getLayoutResult() {
        return layoutResult;
    }

    public void setLayoutResult(Map<String, List<String>> layoutResult) {
        this.layoutResult = layoutResult;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getBrowser() {
        return browser;
    }

    public void setBrowser(List<String> browser) {
        this.browser = browser;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getBrowserStyle() {
        return browserStyle;
    }

    public void setBrowserStyle(String browserStyle) {
        this.browserStyle = browserStyle;
    }

    public List<String> getScript() {
        return script;
    }

    public void setScript(List<String> script) {
        this.script = script;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public String getSvnUsername() {
        return svnUsername;
    }

    public void setSvnUsername(String svnUsername) {
        this.svnUsername = svnUsername;
    }

    public String getSvnPassword() {
        return svnPassword;
    }

    public void setSvnPassword(String svnPassword) {
        this.svnPassword = svnPassword;
    }

    public String getTimeTrigger() {
        return timeTrigger;
    }

    public void setTimeTrigger(String timeTrigger) {
        this.timeTrigger = timeTrigger;
    }

    public String getScmTrigger() {
        return scmTrigger;
    }

    public void setScmTrigger(String scmTrigger) {
        this.scmTrigger = scmTrigger;
    }

    public String getEmailTrigger() {
        return emailTrigger;
    }

    public void setEmailTrigger(String emailTrigger) {
        this.emailTrigger = emailTrigger;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(String buildNo) {
        this.buildNo = buildNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getCsaHandler() {
		return csaHandler;
	}

	public void setCsaHandler(String csaHandler) {
		this.csaHandler = csaHandler;
	}

	public String getShellServerUserName() {
		return shellServerUserName;
	}

	public void setShellServerUserName(String shellServerUserName) {
		this.shellServerUserName = shellServerUserName;
	}

	public String getShellServerPassword() {
		return shellServerPassword;
	}

	public void setShellServerPassword(String shellServerPassword) {
		this.shellServerPassword = shellServerPassword;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	public String getRecallHandler() {
		return recallHandler;
	}

	public void setRecallHandler(String recallHandler) {
		this.recallHandler = recallHandler;
	}

    public String getLastResultTime() {
        return lastResultTime;
    }

    public void setLastResultTime(String lastResultTime) {
        this.lastResultTime = lastResultTime;
    }

    public String formatTaskLayoutNo() {
        // 修改作业BuildNo
        String buildNo = getBuildNo();
        String newBuildNo = String.valueOf(Integer.parseInt(buildNo) + 1);
        setBuildNo(newBuildNo);

        // 修改任务LayoutNo
//        String userName = getUserName();
//        String taskName = getTaskName();
//        Task task = taskMap.get(userName).get(taskName);
//        String layoutNo = task.getLayoutNo();
//        String newLayoutNo = String.valueOf(Integer.parseInt(layoutNo) + 1);
//        task.setLayoutNo(newLayoutNo);

        return "";
    }
    
}
