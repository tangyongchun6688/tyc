//获取项目主键
var projectId = getIframeSrcPara("workIframe","projectId");
getPipliningList();
//获取流水线列表
function getPipliningList(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/pipeManage/getPipliningListByProjectId.do",
		data : {
//			projectId : "b982f58edc544b809ae1e13b0c260756"
			projectId : projectId
		},
		success : function(data){
			var status = data.status;
			if(status == "success"){
				var pipList = data.pipList;
				$("#pipeListDiv").html("");
				if(pipList != undefined && pipList.length > 0){
					for (var i = 0; i < pipList.length; i++) {
						var $pipInfoDiv = $("<div class='pipecnt'></div>");
						var pip = pipList[i];
						var pip_id = pip.ID;
						var pip_name = pip.PIP_NAME;
						var mail_address = pip.MAIL_ADDRESS == undefined ? "--" : pip.MAIL_ADDRESS;
						var pip_version = data["pip_version_" + pip_id];
						
						var build_start_time = pip.build_start_time == undefined ? "--" : pip.build_start_time;
						var build_end_time = pip.build_end_time == undefined ? "--" : pip.build_end_time;
						var build_total_time = pip.build_total_time == undefined ? "--" : pip.build_total_time;
						var build_status = pip.build_status;
						var isPipGroup = "0";//是否流水线组，0否，1是
						var $pipInfo = $("<div class='pipetitle' pip_id='" + pip_id + "' isPipGroup='" + isPipGroup + "' pip_name='" + pip_name + "' pip_version='" + pip_version + "' mail_address = '" + mail_address + "'>" + pip_name +
								"<div class='pipetitle_btn'>" +
								"<a class='bluebtn btn1' href='javascript:void(0)' onclick='batchExecution(this)'><s class='ico20 ico_start'></s>立即执行</a>" +
								"<a class='bluebtn btn2' href='javascript:void(0)' onclick='gotoPipHistory(this)'>执行历史</a>" +
								"<a class='bluebtn btn3' href='javascript:void(0)' onclick='gotoUpdatePipeline(this)'>编辑</a>" +
								"<a class='bluebtn btn4' href='javascript:void(0)' onclick='deletePipeline(this)'>删除</a>" +
								"</div>" +
							"</div>" +
							"<div class='pipeinfo'>" +
								"<span class='mr20'>时间：<span class='time1'>" + build_start_time + "</span> 至  <span class='time2'>" + build_end_time + "</span></span>" +
								"<span class='mr20'>耗时：<span class='consumeTime'>" + build_total_time + "</span></span>" +
								"<span class='mr20'>邮件已推送给：<span class='mailbox'>" + mail_address + "</span>" +
							"</div>");
							$pipInfo.appendTo($pipInfoDiv);
							var teskJson = data["pipId_" + pip_id];
							
//							var pip_version = data["pip_version_" + pip_id];
							if(teskJson.length > 0){
								teskJson = JSON.parse(teskJson);
								var $ul = $("<ul class='pipestep' pip_id='" + pip_id + "' pip_version='" + pip_version + "'></ul>");
								for (var j = 0; j < teskJson.length; j++) {
									var task = teskJson[j];
									var task_type = task.task_type;
									var taskInfo = task.taskInfo;
									var liHtml = "";
									if(task_type == "0"){//子流水线
										$pipInfo.attr("isPipGroup","1");
										liHtml = getChildHtml(taskInfo,j+1)
									}else if(task_type == "1"){//构建任务
										liHtml = getBuildHtml(taskInfo,j+1);
									}else if(task_type == "2"){//部署任务
										liHtml = getDeployHtml(taskInfo,j+1);
									}else if(task_type == "3"){//自动化测试任务
										liHtml = getAutoTestHtml(taskInfo,j+1)
									}
									$(liHtml).appendTo($ul);
								}
							}
							$ul.appendTo($pipInfoDiv);
							$pipInfoDiv.appendTo($("#pipeListDiv"));
					}
				}else{
					$("#NoPipeDiv").show();
					$("#pipeListDiv").hide();
				}
			}else{
				alert("查询流水线列表失败!");
			}
			setWindowHeight();
		},
		error : function(){
			alert("查询流水线列表失败!");
		}
	});
}

//获取子流水线列表
function getChildHtml(data,k){
	var pip_id  = data.pipId;
	var piptask_id = data.piptask_id;
	var task_id = data.task_id;
	var task_type = data.task_type;
	var pip_version = data.pip_version;
	var childLineInfo = data.childLineInfo;
	
	var task_name = "";
	var build_status = data.build_status;
	var build_start_time = data.build_start_time == undefined ? "--" : data.build_start_time;
	var build_end_time = data.build_end_time == undefined ? "--" : data.build_end_time;
	var build_total_time = data.build_total_time == undefined ? "--" : data.build_total_time;
	var liClass = "";
	var h3Html = "";
	var pipelist = "";
	var childPipName = "";
	if(childLineInfo.length > 0){
		for (var i = 0; i < childLineInfo.length; i++) {
			
			var lineInfo = childLineInfo[i];
			childPipName = lineInfo.childPipName;
			var childTaskName = lineInfo.childTaskName;
			var childPiptask_id = lineInfo.childPiptask_id;
			var childTaskId = lineInfo.childTaskId;
			var childTaskType = lineInfo.childTaskType;
			var childBuildStatus = lineInfo.childBuildStatus;
			var childBuildStartTime = lineInfo.childBuildStartTime;
			var childTaskVersion = lineInfo.childTaskVersion;
			var childJobName = "";
			var buildProduct = "";
			if(childTaskType == "1"){//构建任务
				childJobName = lineInfo.BUILD_NAME;
				var warname = lineInfo.WARNAME;
				if(childBuildStatus == "2"){
					buildProduct = "<a href='javascript:void(0)' onclick='downJobWar(this)' class='blue kitem' jobName='" 
						+ childJobName + "' buildNumber='" + childTaskVersion + "'>" + warname + "</a>";
				}
			}else if(childTaskType == "2"){//部署任务
				childJobName = lineInfo.DEPLOY_NAME;
				if(childBuildStatus == "2"){
					var apply_url = lineInfo.apply_url;
					if(apply_url != undefined){
						var urlArr = apply_url.split(";");
						if (urlArr.length > 0) {
							if(urlArr.length == 1){
								buildProduct += "<a href='" + urlArr[0] + "' target= _blank class='blue kitem'>" + urlArr[0] +"</a>&nbsp;&nbsp;"
							}else{
								for (var k = 0; k < urlArr.length; k++) {
									buildProduct += "<a href='" + urlArr[k] + "' target= _blank class='blue kitem'>" + urlArr[k] + "</a>&nbsp;&nbsp;"
								}
							}
						}
					}
				}
			}else if(childTaskType == "3"){//自动化测试任务
				childJobName = lineInfo.TEST_NAME;
				if(childBuildStatus == "2"){
					var apply_url = lineInfo.apply_url;
					if(apply_url != undefined){
						var urlArr = apply_url.split(";");
						if (urlArr.length > 0) {
							if(urlArr.length == 1){
								buildProduct += "<a href='" + urlArr[0] + "' target= _blank class='blue kitem'>查看报告</a>&nbsp;&nbsp;"
							}else{
								for (var k = 0; k < urlArr.length; k++) {
									buildProduct += "<a href='" + urlArr[k] + "' target= _blank class='blue kitem'>查看报告" + (k+1) + "</a>&nbsp;&nbsp;"
								}
							}
						}
					}
				}
			}
			var childTaskClass = "";
			var childLogUrl = "";
			if(childBuildStatus == undefined || build_status == "1"){//未开始执行
				
			}else if(childBuildStatus == "2"){//执行成功
				childLogUrl = "<a class='daily kitem' href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
				+ childTaskVersion + "&task_name=" + childJobName + "' target= _blank>日志</a>";
			}else if(childBuildStatus == "3"){//执行失败
				childTaskClass = "red";
				childLogUrl = "<a class='daily kitem' href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
				+ childTaskVersion + "&task_name=" + childJobName + "' target= _blank>日志</a>";
			}else{//正在执行
				childTaskClass = "green";
			}
			
			pipelist +=  "<p class='" + childTaskClass 	+ "' childPiptask_id='" + childPiptask_id + "'>" +
								"<span>" + childTaskName + "</span>" +
								buildProduct + 
								childLogUrl +
						 "</p>";
		}
	}
	if(build_status == undefined || build_status == "1"){
		h3Html = "<h3><span>" + k + "</span>" + childPipName + "<a class='noedit' href='#'></a></h3>";
	}else if(build_status == "2"){
		liClass = "success";
		h3Html = "<h3><span>" + k + "</span>" + childPipName + " <s></s><a class='noedit' href='#'></a></h3>";
	}else if(build_status == "3"){
		liClass = "fail";
		h3Html = "<h3><span>" + k + "</span>" + childPipName + " <s></s><a class='noedit' href='#'></a></h3>";
	}else{
		liClass = "going";
		h3Html = "<h3><span>" + k + "</span>" + childPipName + " <s></s><a class='noedit' href='#'></a></h3>";
	}
	
	var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' piptask_id='" + piptask_id + "'>" + h3Html +
						"<div class='state state1'>" +
						"<p class='p1'><span class='fl'>时间</span><span class='fr'>耗时</span></p>" +
						"<p class='p2'><span class='fl'>" + build_start_time + "</span><span class='fr'>" + build_total_time + "</span></p>" +
					"</div>" +
					"<div class='state state2' style='display:none'></div>" +
					"<div class='pipelist' >" +
						pipelist +
//						"<p class='kitem'><span>" + kitem1 + "</span>" + vitem1 + "</p>" +
//						"<p class='kitem'><span>" + kitem2 + "</span>" + vitem2 + "</p>" +
					"</div>" +
					"<div class='pipedetail'><p class='nowrap'></p></div>" +
					"</li>";
	
		return liHtml;
}
//构建任务列表
function getBuildHtml(data,k){
	var pip_id  = data.pipId;
	var piptask_id = data.piptask_id;
	var task_id = data.task_id;
	var task_type = data.task_type;
	var pip_version = data.pip_version;
//	var task_name = "构建";
	var task_name = data.BUILD_NAME;
	
	var build_status = data.build_status;
//	var task_type_name = "构建";
//	var pst_id = data.PST_ID;
	var task_version = data.task_version;
//	var build_environment = data.BUILD_ENVIRONMENT;
//	var build_type = data.BUILD_TYPE;
	var build_environment = "java";
	var build_type = "Maven";
	var build_start_time = data.build_start_time == undefined ? "--" : data.build_start_time;
	var build_end_time = data.build_end_time == undefined ? "--" : data.build_end_time;
	var build_total_time = data.build_total_time == undefined ? "--" : data.build_total_time;
	var warname = data.WARNAME;
	var liClass = "";
	var h3Html = "";
	var kitem1 = "";
	var vitem1 = "";
	var logInfo = "";
	if(build_status == undefined || build_status == "1"){
		h3Html = "<h3><span>" + k + "</span>构建 <a class='noedit' href='#'></a></h3>";
	}else if(build_status == "2"){
		liClass = "success";
		h3Html = "<h3><span>" + k + "</span>构建 <s></s><a class='noedit' href='#'></a></h3>";
		kitem1 = "war包：";
		vitem1 = "<a href='javascript:void(0)' onclick='downJobWar(this)' class='blue' jobName='" 
			+ task_name + "' buildNumber='" + task_version + "'>" + warname + "</a>";
		logInfo = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
		+ task_version + "&task_name=" + task_name + "' target= _blank>执行日志</a>";
	}else if(build_status == "3"){
		liClass = "fail";
		h3Html = "<h3><span>" + k + "</span>构建 <s></s><a class='noedit' href='#'></a></h3>";
		logInfo = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
		+ task_version + "&task_name=" + task_name + "' target= _blank>执行日志</a>";
	}else{
		liClass = "going";
		h3Html = "<h3><span>" + k + "</span>构建<s></s><a class='noedit' href='#'></a></h3>";
	}
	var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
					+ "' task_name='" + task_name + "' task_id='" + task_id
					+ "' piptask_id='" + piptask_id + "'>" + h3Html +
					"<div class='state state1'>" +
						"<p class='p1'><span class='fl'>时间</span><span class='fr'>耗时</span></p>" +
						"<p class='p2'><span class='fl'>" + build_start_time + "</span><span class='fr'>" + build_total_time + "</span></p>" +
				//		"<p class='p3'><span class='fl'></span><span class='fr'></span></p>" +
					"</div>" +
					"<div class='state state2' style='display:none'></div>" +
					"<div class='pipelist' >" +
						"<p><span>构建环境：</span>" + build_environment + "</p>" +
						"<p><span>构建类型：</span>" + build_type + "</p>" +
						"<p class='kitem'><span>" + kitem1 + "</span>" + vitem1 + "</p>" +
					"</div>" +
					"<div class='pipedetail'><p class='nowrap'></p>" + logInfo + "</div>" +
				"</li>";
	return liHtml;
}

//部署任务列表
function getDeployHtml(data,k){
	var pip_id  = data.pipId;
	var piptask_id = data.piptask_id;
	var task_id = data.task_id;
	var task_type = data.task_type;
	var pip_version = data.pip_version;
//	var task_name = "部署";
	var task_name = data.DEPLOY_NAME;
	
	
	var build_status = data.build_status;
	
//	var task_type_name = "部署";
//	var pst_id = data.PST_ID;
	
	var task_version = data.task_version;
	var build_start_time = data.build_start_time == undefined ? "--" : data.build_start_time;
	var build_end_time = data.build_end_time == undefined ? "--" : data.build_end_time;
	var build_total_time = data.build_total_time == undefined ? "--" : data.build_total_time;
	var apply_url = data.apply_url;
//	var mould_id = data.MOULD_ID;
	var mould_id = "tomcat+jdk1.8";
	var node_ip = data.node_ip;
	var node_port = data.node_port;
	var applyUrlStr = "";//部署任务，获取访问地址URL
	if(apply_url != undefined){
		var urlArr = apply_url.split(";");
		if (urlArr.length > 0) {
			if(urlArr.length == 1){
				applyUrlStr += "<a href='" + urlArr[0] + "' target= _blank class='blue'>" + urlArr[0] +"</a>&nbsp;&nbsp;"
			}else{
				for (var k = 0; k < urlArr.length; k++) {
					applyUrlStr += "<a href='" + urlArr[k] + "' target= _blank class='blue'>" + urlArr[k] + "</a>&nbsp;&nbsp;"
				}
			}
		}
	}
	var liClass = "";
	var h3Html = "";
	var kitem1 = "";
	var vitem1 = "";
	var kitem2 = "";
	var vitem2 = "";
	var logInfo = "";
	if(build_status == undefined || build_status == "1"){
		h3Html = "<h3><span>" + k + "</span>部署 <a class='noedit' href='#'></a></h3>";
	}else if(build_status == "2"){
		liClass = "success";
		h3Html = "<h3><span>" + k + "</span>部署<s></s><a class='noedit' href='#'></a></h3>";
		kitem1 = "IP端口：";
		if(node_ip != null && node_port != null){
			vitem1 = node_ip + ":" + node_port;
		}
		kitem2 = "部署环境：";
		vitem2 = applyUrlStr;
		logInfo = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
		+ task_version + "&task_name=" + task_name + "' target= _blank >执行日志</a>";
	}else if(build_status == "3"){
		liClass = "fail";
		h3Html = "<h3><span>" + k + "</span>部署 <s></s><a class='noedit' href='#'></a></h3>";
		kitem1 = "IP端口：";
		if(node_ip != null && node_port != null){
			vitem1 = node_ip + ":" + node_port;
		}
		logInfo = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
		+ task_version + "&task_name=" + task_name + "' target= _blank >执行日志</a>";
	}else{
		liClass = "going";
		h3Html = "<h3><span>" + k + "</span>部署<s></s><a class='noedit' href='#'></a></h3>";
	}
   var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' piptask_id='" + piptask_id + "'>" + h3Html +
					"<div class='state state1'>" +
						"<p class='p1'><span class='fl'>时间</span><span class='fr'>耗时</span></p>" +
						"<p class='p2'><span class='fl'>" + build_start_time + "</span><span class='fr'>" + build_total_time + "</span></p>" +
						"<p class='p3'><span class='fl'></span><span class='fr'></span></p>" +
					"</div>" +
					"<div class='state state2' style='display:none'></div>" +
					"<div class='pipelist' >" +
						"<p><span>容器镜像：</span>" + mould_id + "</p>" +
						"<p class='kitem'><span>" + kitem1 + "</span>" + vitem1 + "</p>" +
						"<p class='kitem'><span>" + kitem2 + "</span>" + vitem2 + "</p>" +
					"</div>" +
					"<div class='pipedetail'><p class='nowrap'></p>" + logInfo + "</div>" +
				"</li>";
	return liHtml;
}

//获取自动化测试任务列表
function getAutoTestHtml(data,k){
	var pip_id  = data.pipId;
	var piptask_id = data.piptask_id;
	var task_id = data.task_id;
	var task_type = data.task_type;
	var pip_version = data.pip_version;
//	var task_type_name = "自动化测试";
	var task_name = data.TEST_NAME;
	var build_status = data.build_status;
	
	var testType = data.TEST_TYPE == undefined ? "" : data.TEST_TYPE;
	
//	var pst_id = data.PST_ID;
	
	var task_version = data.task_version;
	var build_start_time = data.build_start_time == undefined ? "--" : data.build_start_time;
	var build_end_time = data.build_end_time == undefined ? "--" : data.build_end_time;
	var build_total_time = data.build_total_time == undefined ? "--" : data.build_total_time;
	var apply_url = data.apply_url;
	var applyUrlStr = "";//部署任务，获取访问地址URL
	if(apply_url != undefined){
		var urlArr = apply_url.split(";");
		if (urlArr.length > 0) {
			if(urlArr.length == 1){
				applyUrlStr += "<a href='" + urlArr[0] + "' target= _blank class='blue'>查看报告</a>&nbsp;&nbsp;"
			}else{
				for (var k = 0; k < urlArr.length; k++) {
					applyUrlStr += "<a href='" + urlArr[k] + "' target= _blank class='blue'>查看报告" + (k+1) + "</a>&nbsp;&nbsp;"
				}
			}
		}
	}
	var liClass = "";
	var h3Html = "";
	var kitem1 = "";
	var vitem1 = "";
	var kitem2 = "";
	var vitem2 = "";
	if(build_status == undefined || build_status == "1"){
		h3Html = "<h3><span>" + k + "</span>自动化测试 <a class='noedit' href='#'></a></h3>";
	}else if(build_status == "2"){
		liClass = "success";
		h3Html = "<h3><span>" + k + "</span>自动化测试<s></s><a class='noedit' href='#'></a></h3>";
		kitem1 = "测试结果：";
		vitem1 = applyUrlStr;
//		vitem1 = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
//		+ pip_version + "&task_name=" + task_name + "' target= _blank>查看报告</a>";
	}else if(build_status == "3"){
		liClass = "fail";
		h3Html = "<h3><span>" + k + "</span>自动化测试 <s></s><a class='noedit' href='#'></a></h3>";
		kitem1 = "测试结果：";
		vitem1 = applyUrlStr;
	}else{
		liClass = "going";
		h3Html = "<h3><span>" + k + "</span>自动化测试<s></s><a class='noedit' href='#'></a></h3>";
	}
   var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' piptask_id='" + piptask_id + "'>" + h3Html +
					"<div class='state state1'>" +
						"<p class='p1'><span class='fl'>时间</span><span class='fr'>耗时</span></p>" +
						"<p class='p2'><span class='fl'>" + build_start_time + "</span><span class='fr'>" + build_total_time + "</span></p>" +
						"<p class='p3'><span class='fl'></span><span class='fr'></span></p>" +
					"</div>" +
					"<div class='state state2' style='display:none'></div>" +
					"<div class='pipelist' >" +
						"<p><span>测试任务：</span>" + task_name + "</p>" +
						"<p><span>测试类型：</span>" + testType + "</p>" +
						"<p class='kitem'><span>" + kitem1 + "</span>" + vitem1 + "</p>" +
						"<p class='kitem'><span>" + kitem2 + "</span>" + vitem2 + "</p>" +
					"</div>" +
					"<div class='pipedetail'><p class='nowrap'></p></div>" +
				"</li>";
	return liHtml;
}
//查询流水线执行历史
function gotoPipHistory(obj){
	var pip_id = $(obj).parent().parent().attr("pip_id");
	var pip_name = $(obj).parent().parent().attr("pip_name");
	window.location.href = localhostPath()
		+ "/jsp/pipeManage/pipe_history.jsp?projectId="
		+ projectId + "&pip_id=" + encodeURI(pip_id) + "&pip_name=" + encodeURI(pip_name); 
}

//立即执行按钮事件
function batchExecution(obj){
	if($(obj).hasClass("bluebtn")){
		//立即执行按钮不可编辑
		$(obj).removeClass("bluebtn").addClass("greybtn");
		var $li = $(obj).parent().parent().parent().find("ul li");
		for (var i = 0; i < $li.length; i++) {
			$($li[i]).removeClass();//移除li的所有class属性
			$($li[i]).find("h3").find("s").remove();
			$($li[i]).find(".state .p2 .fl").html("--");
			$($li[i]).find(".state .p2 .fr").html("--");
			$($li[i]).parent().parent().find(".pipeinfo .time1").html("--");
			$($li[i]).parent().parent().find(".pipeinfo .time2").html("--");
			$($li[i]).parent().parent().find(".pipeinfo .consumeTime").html("--");
			$($li[i]).find(".pipelist .kitem").remove();
			$($li[i]).find(".pipedetail").find("a").remove();
		}
		
		var pipId = $(obj).parent().parent().attr("pip_id");
		var pipName = $(obj).parent().parent().attr("pip_name");
		var pipVersion = $(obj).parent().parent().attr("pip_version");
		var mail_address = $(obj).parent().parent().attr("mail_address");
		$.ajax({
			url : localhostPath() + "/pipeManage/batchExecution.do",
			type : "post",
			dataType : "json",
			data : {
				pipId : pipId,
				pipName : pipName,
				pipVersion : pipVersion,
				mail_address : mail_address,
				usernoRand : usernoRand
			},
			success : function(data){
				var result = data.result;
				$(obj).removeClass("greybtn").addClass("bluebtn");
			},
			error : function(e){
				//alert("立即执行流水线报错了！");
			}
		});
	}
}

//使用websocket时获取数据
if(websocket != null){
	websocket.onmessage = function(event) {
//		$("#message").html(event.data);
		acceptWebSocketData(event.data);
	}
}
//解析数据，并显示在页面上
function acceptWebSocketData(data){
	if(isJSON(data)){
		var jsonData = JSON.parse(data);
		var pipeStatus = jsonData.pipeStatus;
		var pip_id = jsonData.pipId;
		var piptask_id = jsonData.piptask_id;
		var task_id = jsonData.taskId;
		var task_type = jsonData.task_type;
		$ul = $("#pipeListDiv").find("ul[pip_id='" + pip_id + "']");
		$li = $("#pipeListDiv").find("li[pip_id='" + pip_id + "'][piptask_id='" + piptask_id + "']");
		if(pipeStatus == "0"){//流水线开始执行
			var buildStartTime = jsonData.buildStartTime;
			$ul.parent().find(".pipeinfo .time1").html(buildStartTime);
		}else if(pipeStatus == "1"){//单个任务开始执行
			
			$li.removeClass();
			$li.addClass("going");
			$li.find(".state1").hide();
			$li.find(".state2").show();
			$li.find(".pipedetail").find("a").remove();
			/*$("<a href='javascript:void(0)' onclick='showLogInfo(this)'>执行日志</a>").appendTo($li.find(".pipedetail"));*/
			if(task_type == "0"){//子流水线类型任务
				var childPiptask_id = jsonData.childPiptask_id;
				if(childPiptask_id != undefined){
					var $childPiptask = $li.find(".pipelist").find("p[childPiptask_id='" + childPiptask_id + "']");
					$childPiptask.addClass("green");
				}
			}
		}else if(pipeStatus == "2"){//执行中
			var wsKey = jsonData.wsKey;
			var wsValue = jsonData.wsValue;
			$li.find(".pipedetail").find(".nowrap").html(wsKey + wsValue);
		}else if(pipeStatus == "3"){//单个任务执行结束
			var buildStartTime = jsonData.buildStartTime;
			var buildEndTime = jsonData.buildEndTime;
			var buildTotalTime = jsonData.buildTotalTime;
			
			var taskVersion = jsonData.taskVersion;
			var jobName = jsonData.jobName;
			var warName = jsonData.warName;
			
			var deploy_ip = jsonData.deploy_ip;
			var deploy_port = jsonData.deploy_port;
			var applyUrl = jsonData.applyUrl;
			var applyUrlStr = "";//部署任务，获取访问地址URL
			if(applyUrl != undefined && applyUrl.length > 0){
				var urlArr = applyUrl.split(";");
				if (urlArr.length > 0) {
					if(urlArr.length == 1){
						applyUrlStr += "<a href='" + urlArr[0] + "' target= _blank class='blue kitem'>" + urlArr[0] + "</a>&nbsp;&nbsp;"
					}else{
						for (var k = 0; k < urlArr.length; k++) {
							applyUrlStr += "<a href='" + urlArr[k] + "' target= _blank class='blue kitem'>" + urlArr[k] + "</a>&nbsp;&nbsp;"
						}
					}
				}
			}
//			$li = $("#pipeListDiv").find("li[pip_id='" + pip_id + "'][piptask_id='" + piptask_id + "']");
			$li.removeClass();
			$li.find(".state1 .p2 .fl").html(buildStartTime);
			$li.find(".state1 .p2 .fr").html(buildTotalTime);
			$li.find(".state1").show();
			$li.find(".state2").hide();
			$li.find("h3").find("a").remove();
			var buildStatus = jsonData.buildStatus;
			var childPiptask_id = jsonData.childPiptask_id;
			if(buildStatus == "2"){//执行成功
				if(childPiptask_id == undefined){
					$li.addClass("success");
				}
				if(task_type == "0"){//子流水线类型任务
					if(childPiptask_id != undefined){
						var $childPiptask = $li.find(".pipelist").find("p[childPiptask_id='" + childPiptask_id + "']");
						$childPiptask.removeClass();
						var childTaskType = jsonData.childTaskType;
						var buildProduct = "";
						if(childTaskType == "1"){//子流水线中的构建任务
							buildProduct = "<a href='javascript:void(0)' onclick='downJobWar(this)' class='blue kitem' jobName='" 
								+ jobName + "' buildNumber='" + taskVersion + "'>" + warName + "</a>";
						}else if(childTaskType == "2"){//子流水线中的部署任务
							buildProduct = applyUrlStr;
						}else if(childTaskType == "3"){//子流水线中的自动化测试任务
							if(apply_url != undefined){
								var urlArr2 = apply_url.split(";");
								if (urlArr2.length > 0) {
									if(urlArr2.length == 1){
										buildProduct += "<a href='" + urlArr2[0] + "' target= _blank class='blue kitem'>查看报告</a>&nbsp;&nbsp;"
									}else{
										for (var k = 0; k < urlArr.length; k++) {
											buildProduct += "<a href='" + urlArr2[k] + "' target= _blank class='blue kitem'>查看报告" + (k+1) + "</a>&nbsp;&nbsp;"
										}
									}
								}
							}
						}
						//子流水线任务产出物
						$(buildProduct).appendTo($childPiptask);
						//子流水线的任务执行日志
						var childLogUrl = "<a class='daily kitem' href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
						+ taskVersion + "&task_name=" + jobName + "' target= _blank>日志</a>";
						$(childLogUrl).appendTo($childPiptask);
						
//						$childPiptask.html($childPiptask.html() + "执行成功");
					}
				}else if(task_type == "1"){//构建任务
					$("<p class='kitem'><span>war包：</span><a href='javascript:void(0)' onclick='downJobWar(this)' class='blue' jobName='" 
							+ jobName + "' buildNumber='" + taskVersion + "'>" + warName + "</a></p>").appendTo($li.find(".pipelist"));
					var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
					+ taskVersion + "&task_name=" + jobName + "' target= _blank>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(task_type == "2"){//部署任务
					$("<p class='kitem'><span>部署环境：</span>" + applyUrlStr + "</p>").appendTo($li.find(".pipelist"));
					var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
					+ taskVersion + "&task_name=" + jobName + "' target= _blank>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(task_type == "3"){//自动化测试任务
					logurl = "<a href='" + applyUrl + "' target= _blank>查看报告</a>";
					$("<p class='kitem'><span>测试结果：</span>" + logurl + "</p>").appendTo($li.find(".pipelist"));
					$li.find(".pipedetail").find("a").remove();
				}
				$li.find(".pipedetail").find(".nowrap").html("");
			}else{//执行失败
				if(childPiptask_id == undefined){
					$li.addClass("fail");
				}
				if(task_type == "0"){//子流水线类型任务
					var childPiptask_id = jsonData.childPiptask_id;
					if(childPiptask_id != undefined){
						var $childPiptask = $li.find(".pipelist").find("p[childPiptask_id='" + childPiptask_id + "']");
						$childPiptask.removeClass();
						$childPiptask.addClass("red");
//						$childPiptask.html($childPiptask.html() + "执行失败！");
					}
				}else if(task_type == "1"){//构建任务
					var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
					+ taskVersion + "&task_name=" + jobName + "' target= _blank class='blue'>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(task_type == "2"){//部署任务
					var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
					+ taskVersion + "&task_name=" + jobName + "' target= _blank class='blue'>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(task_type == "3"){//自动化测试任务
					logurl = "<a href='" + applyUrl + "' target= _blank class='blue'>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}
			}
			
		}else if(pipeStatus == "4"){//主流水线执行结束
			var buildEndTime = jsonData.buildEndTime;
			var buildTotalTime = jsonData.buildTotalTime;
			var pipVersion = jsonData.pipVersion;
			var mail_address = jsonData.mail_address;
			var $pipetitleDiv = $("#pipeListDiv").find(".pipecnt").find("[pip_id='" + pip_id + "']");
			$ul.parent().find(".pipeinfo .time2").html(buildEndTime);
			$ul.parent().find(".pipeinfo .consumeTime").html(buildTotalTime);
			$ul.parent().find(".pipeinfo .mailbox").html(mail_address);
			$ul.attr("pip_version",pipVersion);
			$pipetitleDiv.attr("pip_version",pipVersion);
		}
	}
}

//下载war包
function downJobWar(obj){
	var jobName = $(obj).attr("jobName");
	var buildNumber = $(obj).attr("buildNumber");
	window.location.href = localhostPath() + "/taskManage/downJobWar.do?jobName=" 
	+ jobName + "&buildNumber=" + buildNumber;
}
//跳转到新增流水线页面
function gotoAddPipeline(){
	window.location.href = localhostPath() + "/jsp/pipeManage/pipeliningGroup_creat.jsp?projectId=" + projectId;
}
//跳转到流水线修改页面
function gotoUpdatePipeline(obj){
	var pip_id = $(obj).parent().parent().attr("pip_id");
	var isPipGroup = $(obj).parent().parent().attr("isPipGroup");
	if (isPipGroup == '1'){
		window.location.href = localhostPath() + "/jsp/pipeManage/pipelining_update.jsp?pip_id=" + pip_id + "&projectId=" + projectId;
	}else{
		window.location.href = localhostPath() + "/jsp/pipeManage/pipelining_update2.jsp?pip_id=" + pip_id + "&projectId=" + projectId;
	}
//	
	
}
//删除流水线
function deletePipeline(obj){
	var pip_id = $(obj).parent().parent().attr("pip_id");
	 if(confirm("确定要删除该流水线吗？")){
		 $.ajax({
				type : "post",
				dataType : "json",
				url : localhostPath() + "/taskManage/deletePipeliningById.do",
				data : {
					pipId : pip_id,
				},
				success : function(data){
					var result = data.result;
					if(result == "success"){
						$(obj).parent().parent().parent().remove();
//						alert("删除流水线成功！");
					}else{
						alert("删除流水线失败！");
					}
					
					setWindowHeight();
				},
				error : function(e){
					alert("删除流水线失败！");
				}
			});
	 }
}