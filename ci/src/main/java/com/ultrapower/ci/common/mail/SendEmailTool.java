package com.ultrapower.ci.common.mail;

import java.io.BufferedReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.ultrapower.ci.common.constant.ApplyConstant;
import com.ultrapower.ci.common.utils.JenkinsUtils;

/**
 * @time 2017-12-19
 * @author tangyongchun
 * @description 邮件发送功能
 *
 */
public class SendEmailTool {

	public static String mailServer = ""; // 邮件服务器配置
	public static String mailServerPort = ""; // 邮件服务器端口
	public static String fromUser = ""; // 邮件发送人
	public static String password = ""; // 邮件发送人密码
	public static String fromAddress = ""; // 邮件发送人地址

	// 读取邮件的配置信息
	static {
		try {
			mailServer = ApplyConstant.MAIL_SERVER;
			mailServerPort = ApplyConstant.MAIL_SERVERPORT;
			fromUser = ApplyConstant.MAIL_FROM_USER;
			password = ApplyConstant.MAIL_PASSWORD;
			fromAddress = ApplyConstant.MAIL_FROMADDRESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件接口
	 * 
	 * @param params 封装邮件推送的内容
	 * @param toAddress 邮件接收地址
	 * @return
	 */
	public static boolean sendEmail(Map<String, String> params) {
		boolean flag = false;
		Transport transport = null;
		String toAddress = "";
		try {
			if (params.get("toAddress") != null && !"".equals(params.get("toAddress"))) {
				toAddress = params.get("toAddress");
			}
			
			// 设置邮件标题
			String title = "ci持续构建";
			// 设置邮件内容
			String template = "异常邮件给您带来的不便请见谅！";
			// 退订邮件地址
			String mailout = "";

			Properties props = new Properties();
			// Setup mail server
			props.put("mail.smtp.host", mailServer);// 存储发送邮件服务器的信息
			// 如果设置了邮件发送端口则用配置的
			if (!"".equalsIgnoreCase(mailServerPort)) {
				props.put("mail.smtp.port", mailServerPort);
			}
			props.put("mail.smtp.auth", "true"); // 验证
			Session mailSession = Session.getDefaultInstance(props);// 根据属性新建一个邮件会话
			mailSession.setDebug(false);

			MimeMessage message = new MimeMessage(mailSession);// 由邮件会话新建一个消息对象
			String address = "";// 邮件发送者
			if (fromAddress.indexOf(",") != -1) {
				String[] arr_fromAddress = fromAddress.split(",");
				String sendName = javax.mail.internet.MimeUtility.encodeText(arr_fromAddress[1]);
				address = sendName + "<" + arr_fromAddress[0] + ">";
			} else {
				address = fromAddress;
			}
			message.setFrom(new InternetAddress(address));

			String recipient = ""; // 收件人
			if (toAddress.indexOf(",") != -1) {
				String[] arr_toAddress = toAddress.split(",");
				// 设置多人接收邮件
				for (int i = 0; i < arr_toAddress.length; i++) {
					recipient = arr_toAddress[i];
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				}
			} else {
				recipient = toAddress;
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}

			message.setSubject(MimeUtility.encodeText(title));

			// 整个邮件的组成方式
			MimeMultipart msgMultipart = new MimeMultipart("mixed");// 邮件的组成方式
			message.setContent(msgMultipart);
			// 正文
			MimeBodyPart content = new MimeBodyPart();
			msgMultipart.addBodyPart(content);
			// 邮件正文 邮件正文之间的组成方式
			MimeMultipart bodyMultipart = new MimeMultipart("related");
			content.setContent(bodyMultipart);
			MimeBodyPart htmlPart = new MimeBodyPart();
			bodyMultipart.addBodyPart(htmlPart);

			String reUrl = "";
			
			Map<String, Object> conMap = new HashMap<String, Object>();
			conMap.put("pipContents", (String) params.get("pipContents"));
			conMap.put("buildContents", (String) params.get("buildContents"));
			conMap.put("deployContents", (String) params.get("deployContents"));
			conMap.put("autoTestContents", "自动化测试内容");
			conMap.put("mailout", mailout);
			conMap.put("maillogo", reUrl);
			conMap.put("path", (String) params.get("path"));

			String modelvm = "messagemail.vm"; // 模板名称
			String templateContent = getTemplateContent(conMap, modelvm);
			htmlPart.setContent(templateContent, "text/html;charset=utf-8");

			message.saveChanges();

			System.out.println("========Email send start=========");
			transport = mailSession.getTransport("smtp");
			transport.connect(mailServer, fromUser, password);
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("=======Email send success=========");
			System.out.println("----邮件----" + toAddress + "已发送完毕！");
			flag = true;
		} catch (Exception e) {
			flag = false;
			System.out.println("邮件发送程序异常=" + params.toString());
			e.printStackTrace();
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 邮件发送的内容
	 * 
	 * @param conMap
	 * @param string
	 * @return
	 */
	private static String getTemplateContent(Map<String, Object> conMap, String modelvm) {
		StringBuffer result = new StringBuffer();
		BufferedReader reader = null;

		try {
			// 获取模板引擎
			VelocityEngine ve = new VelocityEngine();
			// 模板文件所在的路径
			String path = conMap.get("path").toString();
			// 设置参数
			ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
			// 处理中文问题
			ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

			// 初始化模板
			ve.init();
			// Velocity模板的名称
			Template template = ve.getTemplate(modelvm);
			// 获取上下文
			VelocityContext context = new VelocityContext();
			// 获取map的key值的集合，set集合
			Set<String> keySet = conMap.keySet();
			// 遍历key
			for (Object obj : keySet) {
				// 把数据填入上下文
				context.put(obj + "", conMap.get(obj) + "");
				// 输出键与值
				System.out.println("key:" + obj + ",Value:" + conMap.get(obj));
			}
			// 内容替换获取模板内容
			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			result.append(sw.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {

		Map<String, String> params = new HashMap<String, String>();
		System.out.println(sendEmail(params));
	}
}
