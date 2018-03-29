package com.ultrapower.ci.common.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PackJobUtils {

	public static String Packjob(Map<String, String> map) {
		String SvnUrl = map.get("svn_url");
		String mvnCmd = map.get("mvnCmd");
		String SvnCredentialsId = map.get("svncredentialID");
		String Remark = map.get("remark");
		String ConfigFileUrl = map.get("xmlPath1");
		String emailUser = map.get("emailUser");
		String emailSubject = map.get("emailSubject");
		String emailContext = map.get("emailContext");
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(ConfigFileUrl));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获得根节点的属性对象
		Element rootElem = document.getRootElement();
		// 获取描述节点
		Element svnElem1 = rootElem.element("description");
		// 设置描述
		svnElem1.setText(Remark);
		// 获取svn节点
		List<Element> svnElems = rootElem.element("scm").element("locations")
				.elements("hudson.scm.SubversionSCM_-ModuleLocation");
		// 设置svn
		Element svnElem = svnElems.get(0);
		svnElem.element("remote").setText(SvnUrl);
		svnElem.element("credentialsId").setText(SvnCredentialsId);
		if(emailUser!=null&&emailUser!=""){
		Element xAdapterElem = rootElem.element("publishers").element("hudson.plugins.emailext.ExtendedEmailPublisher");
		xAdapterElem.element("recipientList").setText(emailUser);
		xAdapterElem.element("defaultSubject").setText(emailSubject);
		xAdapterElem.element("defaultContent").setText(emailContext);
		}
		Element goals = rootElem.element("goals");
		if(mvnCmd!=null && mvnCmd!=""){
			goals.setText(mvnCmd);
		}
		
		return document.asXML().toString();
	}

	public static String changeXml(Map<String, String> map) {
		// 获取Map集合中的参数
		String project = map.get("job1Name");
		String tomcatcredentialID = map.get("tomcatcredentialID");
		String url = map.get("tomcat_url");
		String xmlPath2 = map.get("xmlPath2");
		String emailUser=map.get("emailUser");
		String emailSubject=map.get("emailSubject");
		String emailContext=map.get("emailContext");
		// 读取XML文件，获得document对象
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(xmlPath2));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// 获得根节点的属性对象
		Element rootElem = document.getRootElement();
		Element projectElem = rootElem.element("builders").element("hudson.plugins.copyartifact.CopyArtifact")
				.element("project");
		projectElem.setText(project);
		Element xAdapterElem = rootElem.element("publishers").element("hudson.plugins.deploy.DeployPublisher")
				.element("adapters").element("hudson.plugins.deploy.tomcat.Tomcat7xAdapter");
		// xAdapterElem.element("userName").setText(userName);
		// xAdapterElem.element("passwordScrambled").setText(passwordScrambled);
		xAdapterElem.element("credentialsId").setText(tomcatcredentialID);
		xAdapterElem.element("url").setText(url);
		
		if(emailUser!=null&&emailUser!=""){
		Element xAdapterElem1 = rootElem.element("publishers").element("hudson.plugins.emailext.ExtendedEmailPublisher");
		xAdapterElem1.element("recipientList").setText(emailUser);
		xAdapterElem1.element("defaultSubject").setText(emailSubject);
		xAdapterElem1.element("defaultContent").setText(emailContext);
	}
		// asXML返回该节点的文本XML表示
		return document.asXML().toString();
	}

}
