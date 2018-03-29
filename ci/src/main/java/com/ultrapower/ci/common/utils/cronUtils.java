package com.ultrapower.ci.common.utils;

public class cronUtils {
	public static String cronExpression(String day,String week,String hour,String minute){
		String cron = "";
		//每月day日hour时minute分执行一次
		if(day != null){
			cron = "0 " + minute + " " + hour + " " + day + " * ?";
		}
		//每周week，hour时minute分执行一次
		if(week != null){
			cron = "0 " + minute + " " + hour + " ? * " + week;
		}
		//每天hour时minute分执行一次
		if(day == null && week == null){
			cron = "0 " + minute + " " + hour + " ? * *";
		}
		return cron;
	}
}
