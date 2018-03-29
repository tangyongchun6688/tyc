package com.ultrapower.ci.common.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ultrapower.ci.common.base.pojo.Job;


/**
 * xml文件工具类
 * @since v1.0
 * @version 1.0.0.1 May.6, 2017
 */
public class XMLUtil {

    /**
     * 生成jenkins任务xml文件
     * @param job 获取用户设置的参数
     */
    public static String exportXML(final Job job){
        //获取参数
        String jobName = job.getJobName();
        String timeTrigger = job.getTimeTrigger();
        String userName = job.getUserName();
        String email = job.getEmailReceiver();
        String emailTrigger = job.getEmailTrigger();
        String goals = job.getGoals();
        String scmTrigger = job.getScmTrigger();
        String svnUrl = job.getSvnUrl();
        String svnUsername = job.getSvnUsername();
        String templatePath = "WEB-INF/classes/config/config.xml";
        //读取XML文件，获得document对象
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(templatePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获得根节点的属性对象
        Element rootElem = document.getRootElement();
        //获取svn节点
        List<Element> svnElems = rootElem.element("scm").element("locations").elements("hudson.scm.SubversionSCM_-ModuleLocation");
        //设置svn
        Element svnElem = svnElems.get(0);
        svnElem.element("remote").setText(svnUrl);
        svnElem.element("credentialsId").setText("atp." + svnUrl + "." + svnUsername);
        //获取触发器节点
        Element triggers = rootElem.element("triggers");
        //定时构建触发器
        if(!timeTrigger.equals("false") && !StringUtils.isEmpty(timeTrigger)) {
            Element timeTriggerElem = triggers.addElement("hudson.triggers.TimerTrigger");
            timeTriggerElem.addElement("spec").addText(timeTrigger);
        }
        //提交触发器
        if(scmTrigger.equals("true")) {
            Element scmTriggerElem = triggers.addElement("hudson.triggers.SCMTrigger");
            scmTriggerElem.addElement("spec").addText("* * * * *");
            scmTriggerElem.addElement("ignorePostCommitHooks").addText("false");
        }
        //获取邮箱节点，设置邮箱
        Element emailRootElem = rootElem.element("publishers").element("hudson.plugins.emailext.ExtendedEmailPublisher");
        emailRootElem.element("recipientList").setText(email);
        Element emailTriggerElem = emailRootElem.element("configuredTriggers");
        Element emailElem = null;
        //设置邮箱触发器
        if(emailTrigger.equals("true")){
            Element failureElem = emailTriggerElem.addElement("hudson.plugins.emailext.plugins.trigger.FailureTrigger");
            emailElem = failureElem.addElement("email");
        } else {
            Element alwaysElem = emailTriggerElem.addElement("hudson.plugins.emailext.plugins.trigger.AlwaysTrigger");
            emailElem = alwaysElem.addElement("email");
        }
        emailElem.addElement("subject").setText("$PROJECT_DEFAULT_SUBJECT");
        emailElem.addElement("body").setText("$PROJECT_DEFAULT_CONTENT");
        emailElem.addElement("recipientProviders").addElement("hudson.plugins.emailext.plugins.recipients.ListRecipientProvider");
        emailElem.addElement("attachmentsPattern");
        emailElem.addElement("attachBuildLog").setText("false");
        emailElem.addElement("compressBuildLog").setText("false");
        emailElem.addElement("replyTo").setText("$PROJECT_DEFAULT_REPLYTO");
        emailElem.addElement("contentType").setText("project");

        //设置构建类型
        if(!goals.equals("false") && !StringUtils.isEmpty(goals) && !goals.equals("deploy")) {
            rootElem.element("goals").setText(goals);
        } else if(goals.equals("deploy")){
            rootElem.element("goals").setText("install");
            //动态添加部署的配置信息
            Element containerElem = rootElem.element("publishers").addElement("hudson.plugins.deploy.DeployPublisher");
            containerElem.addAttribute("plugin", "deploy@1.10");
            Element adapters = containerElem.addElement("adapters");
            Element tomcatAdapter = adapters.addElement("hudson.plugins.deploy.tomcat.Tomcat7xAdapter");
            tomcatAdapter.addElement("userName").setText("tomcatRoot");
            tomcatAdapter.addElement("passwordScrambled").setText("MTIz");
            tomcatAdapter.addElement("url").setText("http://localhost:8077");

            containerElem.addElement("contextPath").setText("/atp");
            containerElem.addElement("war").setText("target/atp.war");
            containerElem.addElement("onFailure").setText("true");
        }

        //生成xml文件
        String configFile ="CASE/" + userName + "/xml/" + jobName + "_config.xml";
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("gb2312");
            XMLWriter writer = new XMLWriter(new FileWriter(configFile), format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configFile;
    }

}
