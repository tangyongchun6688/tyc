//获取项目主键
var projectId = getIframeSrcPara("workIframe","projectId");

getPipliningList();
//获取流水线列表
function getPipliningList(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/taskManage/getPipliningListByProjectId.do",
		data : {
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
						var build_start_time = pip.BUILD_START_TIME == undefined ? "--" : pip.BUILD_START_TIME;
						var build_end_time = pip.BUILD_END_TIME == undefined ? "--" : pip.BUILD_END_TIME;;
						var build_total_time = pip.BUILD_TOTAL_TIME;
						var mail_address = pip.MAIL_ADDRESS == undefined ? "" : pip.MAIL_ADDRESS;
						if(build_total_time != undefined){
							build_total_time = millisecondChangeTime(build_total_time);
						}else{
							build_total_time = "--";
						} 
						var $pipInfo = $("<div class='pipetitle' pip_id='" + pip_id + "' pip_name='" + pip_name + "'>" + pip_name +
											"<div class='pipetitle_btn'>" +
											"<a class='bluebtn btn1' href='javascript:void(0)' onclick='batchExecution(this)'><s class='ico20 ico_start'></s>立即执行</a>" +
											"<a class='bluebtn btn2' href='javascript:void(0)' onclick='gotoPipHistory(this)'>执行历史</a>" +
											"<a class='bluebtn btn3' href='javascript:void(0)' onclick='gotoUpdatePipeline(\""+pip_id+"\")'>编辑</a>" +
											"<a class='bluebtn btn4' href='javascript:void(0)' onclick='deletePipeline(this)'>删除</a>" +
											"</div>" +
										"</div>" +
										"<div class='pipeinfo'>" +
											"<span class='mr20'>时间：<span class='time1'>" + build_start_time + "</span> 至  <span class='time2'>" + build_end_time + "</span></span>" +
											"<span class='mr20'>耗时：<span class='consumeTime'>" + build_total_time + "</span></span>" +
											"<span class='mr20'>邮件已推送给：<span class='mailbox'>" + mail_address + "</span>" +
										"</div>");
						$pipInfo.appendTo($pipInfoDiv);
						var taskList = data["pipId_" + pip_id];
						var pip_version = data["pip_version_" + pip_id];
						if(taskList != undefined && taskList.length > 0){
							var $ul = $("<ul class='pipestep' pip_id='" + pip_id + "'></ul>");
							for (var j = 0; j < taskList.length; j++) {
								var task = taskList[j];
								var task_type = task.TASK_TYPE;
								var liHtml = "";
								if(task_type == "1"){//构建任务
									liHtml = getBuildHtml(task,j+1)
								}else if(task_type == "2"){//部署任务
									liHtml = getDeployHtml(task,j+1);
								}else if(task_type == "3"){//自动化测试
									liHtml = getAutoTestHtml(task,j+1)
								}/*else{//流水线历史记录
									
									$pipInfoDiv.find(".pipeinfo .time1").html(build_start_time);
									$pipInfoDiv.find(".pipeinfo .time2").html(build_end_time);
									$pipInfoDiv.find(".pipeinfo .consumeTime").html(build_total_time);
									$pipInfoDiv.find(".pipeinfo .mailbox").html(mail_address);
								}*/
								$(liHtml).appendTo($ul);
							}
							$ul.appendTo($pipInfoDiv);
							$pipInfoDiv.appendTo($("#pipeListDiv"));
						}
						
					}
					$("#NoPipeDiv").hide();
					$("#pipeListDiv").show();
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
//构建任务列表
function getBuildHtml(data,k){
	var build_status = data.BUILD_STATUS;
	var pip_id  = data.PIP_ID;
	var task_type = data.TASK_TYPE;
	var task_type_name = "构建";
	var task_name = data.BUILD_NAME;
	var task_id = data.TASK_ID;
	var pst_id = data.PST_ID;
	var pip_version = data.PIP_VERSION;
	var task_version = data.TASK_VERSION;
//	var build_environment = data.BUILD_ENVIRONMENT;
//	var build_type = data.BUILD_TYPE;
	var build_environment = "java";
	var build_type = "Maven";
	var build_start_time = data.BUILD_START_TIME == undefined ? "--" : data.BUILD_START_TIME;
	var build_end_time = data.BUILD_END_TIME == undefined ? "--" : data.BUILD_END_TIME;
	var build_total_time = data.BUILD_TOTAL_TIME;
	if(build_total_time != undefined){
		build_total_time = millisecondChangeTime(build_total_time);
	}else{
		build_total_time = "--";
	}
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
	}
	var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' pst_id='" + pst_id + "' pip_version='" + pip_version
						+ "'>" + h3Html +
						"<div class='state state1'>" +
							"<p class='p1'><span class='fl'>时间</span><span class='fr'>耗时</span></p>" +
							"<p class='p2'><span class='fl'>" + build_start_time + "</span><span class='fr'>" + build_total_time + "</span></p>" +
//							"<p class='p3'><span class='fl'></span><span class='fr'></span></p>" +
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
	var build_status = data.BUILD_STATUS;
	var pip_id  = data.PIP_ID;
	var task_type = data.TASK_TYPE;
	var task_type_name = "部署";
	var task_name = data.DEPLOY_NAME;
	var task_id = data.TASK_ID;
	var pst_id = data.PST_ID;
	var pip_version = data.PIP_VERSION;
	var task_version = data.TASK_VERSION;
	var build_start_time = data.BUILD_START_TIME == undefined ? "--" : data.BUILD_START_TIME;
	var build_end_time = data.BUILD_END_TIME == undefined ? "--" : data.BUILD_END_TIME;
	var build_total_time = data.BUILD_TOTAL_TIME;
	var apply_url = data.APPLY_URL;
//	var mould_id = data.MOULD_ID;
	var mould_id = "tomcat+jdk1.8";
	var node_ip = data.NODE_IP;
	var node_port = data.NODE_PORT;
	if(build_total_time != undefined){
		build_total_time = millisecondChangeTime(build_total_time);
	}else{
		build_total_time = "--";
	}
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
	}
   var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' pst_id='" + pst_id + "' pip_version='" + pip_version
						+ "'>" + h3Html +
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
	var build_status = data.BUILD_STATUS;
	var pip_id  = data.PIP_ID;
	var task_type = data.TASK_TYPE;
	var task_type_name = "自动化测试";
	var task_name = data.TEST_NAME;
	var testType = data.TEST_TYPE == undefined ? "" : data.TEST_TYPE;
	var task_id = data.TASK_ID;
	var pst_id = data.PST_ID;
	var pip_version = data.PIP_VERSION;
	var task_version = data.TASK_VERSION;
	var build_start_time = data.BUILD_START_TIME == undefined ? "--" : data.BUILD_START_TIME;
	var build_end_time = data.BUILD_END_TIME == undefined ? "--" : data.BUILD_END_TIME;
	var build_total_time = data.BUILD_TOTAL_TIME;
	if(build_total_time != undefined){
		build_total_time = millisecondChangeTime(build_total_time);
	}else{
		build_total_time = "--";
	}
	var apply_url = data.APPLY_URL;
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
//		vitem1 = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
//		+ pip_version + "&task_name=" + task_name + "' target= _blank>查看报告</a>";
	}
   var liHtml = "<li class='" + liClass + "' pip_id='" + pip_id + "' task_type='" + task_type
						+ "' task_name='" + task_name + "' task_id='" + task_id
						+ "' pst_id='" + pst_id + "' pip_version='" + pip_version
						+ "'>" + h3Html +
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
		+ "/jsp/taskManage/pipelining/pipelining_history.jsp?projectId="
		+ projectId + "&pip_id=" + encodeURI(pip_id) + "&pip_name=" + encodeURI(pip_name); 
}

//单个执行任务
//function implementTask(obj,arrJob,pipVersion){
//	var pipId = $(obj).parent().parent().attr("pip_id");
//	var jobName = $(obj).parent().parent().attr("task_name");
//	var taskType = $(obj).parent().parent().attr("task_type");
//	var taskId = $(obj).parent().parent().attr("task_id");
//	var pipStepTaskId = $(obj).parent().parent().attr("pst_id");
//	var buildStartTime = getNowFormatDate();
//	if(pipVersion == undefined){
//		pipVersion = $(obj).parent().parent().attr("pip_version");
//	}
//	$(obj).addClass("noedit");
//	$(obj).parent().parent().find(".pipeing").find("b").html("执行中&nbsp;&nbsp;<i class='fa fa-circle-o-notch fa-spin'></i>");
//	$(obj).parent().parent().find(".fail").removeClass("fail");
//	$(obj).parent().parent().find(".pipeing").find("span").css("width","30%");
//
//	$(obj).parent().parent().find(".kitem").remove();
//	$(obj).parent().parent().find(".fontScroll").remove();
//	
//	$(obj).parent().parent().find(".pipedetail").html("");
//	$.ajax({
//		type : "post",
//		dataType : "json",
//		url : localhostPath() + "/taskManage/executeSingleJob.do",
//		data : {
//			pipId : pipId,
//			jobName : jobName,
//			taskType : taskType,
//			taskId : taskId,
//			pipStepTaskId : pipStepTaskId,
//			buildStartTime : buildStartTime,
//			pipVersion : pipVersion,
//			usernoRand : usernoRand
//		},
//		success : function(data){
//			
//		},
//		error : function(e){
//			alert("执行任务报错！");
//		}
//	});
//}

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
		$.ajax({
			url : localhostPath() + "/taskManage/batchExecution.do",
			type : "post",
			dataType : "json",
			data : {
				pipId : pipId,
				pipName : pipName,
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
//下载war包
function downJobWar(obj){
	var jobName = $(obj).attr("jobName");
	var buildNumber = $(obj).attr("buildNumber");
	window.location.href = localhostPath() + "/taskManage/downJobWar.do?jobName=" 
	+ jobName + "&buildNumber=" + buildNumber;
}

//跳转到新增流水线页面
function gotoAddPipeline(){
	window.location.href = localhostPath() + "/jsp/taskManage/pipelining/pipelining_creat.jsp?projectId=" + projectId;
}
//跳转到流水线修改页面
function gotoUpdatePipeline(pip_id){
	window.location.href = localhostPath() + "/jsp/taskManage/pipelining/pipelining_update2.jsp?pip_id=" + pip_id + "&projectId=" + projectId;
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
		var task_id = jsonData.taskId;
		
		$li = $("#pipeListDiv").find("li[pip_id='" + pip_id + "'][task_id='" + task_id + "']");
		$ul = $("#pipeListDiv").find("ul[pip_id='" + pip_id + "']");
		if(pipeStatus == "0"){//流水线开始执行
			var buildStartTime = jsonData.buildStartTime;
			$ul.parent().find(".pipeinfo .time1").html(buildStartTime);
		}else if(pipeStatus == "1"){//单个任务开始执行
			$li.removeClass();
			$li.addClass("going");
			$li.find(".state1").hide();
			$li.find(".state2").show();
			$li.find(".pipedetail").find("a").remove();
			$("<a href='javascript:void(0)' onclick='showLogInfo(this)'>执行日志</a>").appendTo($li.find(".pipedetail"));
		}else if(pipeStatus == "2"){//执行中
			var logInfo = jsonData.logInfo;
			if(logInfo != undefined){//实时打印日志
				var $logDiv = $("#logMessageBody").find("div[pip_id='" + pip_id + "'][task_id='" + task_id + "']");
				if($logDiv.length > 0){
					var logMessageText = $logDiv.html();
					$logDiv.html(logMessageText + logInfo);
				}else{
					var $div = $("<div class='logInfoDiv' pip_id='" + pip_id + "' task_id='" + task_id + "'>" + logInfo + "</div>");
					$div.appendTo($("#logMessageBody"));
				}
			}else{
				var wsKey = jsonData.wsKey;
				var wsValue = jsonData.wsValue;
				$li.find(".pipedetail").find(".nowrap").html(wsKey + wsValue);
			}
		}else if(pipeStatus == "3"){//单个任务执行结束
			var taskType = jsonData.taskType;
			var status = jsonData.status;
			var message = jsonData.message;
			var buildStatus = jsonData.buildStatus;
			var pipVersion = jsonData.pipVersion;
			var buildTotalTime = jsonData.buildTotalTime;
			var buildStartTime = jsonData.buildStartTime;
			var buildEndTime = jsonData.buildEndTime;
			var applyUrl = jsonData.applyUrl;
			var taskVersion = jsonData.taskVersion;
//			var warName = jsonData.warName;
			var jobName = jsonData.jobName;
//			var testResult = jsonData.testResult;
			if(buildTotalTime != undefined && buildTotalTime != ""){
				buildTotalTime = millisecondChangeTime(buildTotalTime);
			}else{
				buildTotalTime = "--";
			}
			if(status == "success" && buildStatus == "2"){
				$li.addClass("success");
				$li.find(".state1 .p2 .fl").html(buildStartTime);
				$li.find(".state1 .p2 .fr").html(buildTotalTime);
				$li.find(".state1").show();
				$li.find(".state2").hide();
				$li.find("h3").find("a").remove();
				$("<s></s><a class='noedit' href='#'></a>").appendTo($li.find("h3"));
				var applyUrlStr = "";//部署任务，获取访问地址URL
				if(applyUrl != undefined && applyUrl.length > 0){
					var urlArr = applyUrl.split(";");
					if (urlArr.length > 0) {
						if(urlArr.length == 1){
							applyUrlStr += "<a href='" + urlArr[0] + "' target= _blank class='blue'>" + urlArr[0] + "</a>&nbsp;&nbsp;"
						}else{
							for (var k = 0; k < urlArr.length; k++) {
								applyUrlStr += "<a href='" + urlArr[k] + "' target= _blank class='blue'>" + urlArr[k] + "</a>&nbsp;&nbsp;"
							}
						}
					}
				}
				
				var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
							+ taskVersion + "&task_name=" + jobName + "' target= _blank>执行日志</a>";
				if(taskType == "1"){//构建成功
					var testResult = jsonData.testResult;
					var warName = jsonData.warName;
//					if(testResult != ""){
//						$("<p class='kitem'><span>单元测试：</span>" + testResult + "</p>").appendTo($li.find(".pipelist"));
//					}
					$("<p class='kitem'><span>war包：</span><a href='javascript:void(0)' onclick='downJobWar(this)' class='blue' jobName='" 
						+ jobName + "' buildNumber='" + taskVersion + "'>" + warName + "</a></p>").appendTo($li.find(".pipelist"));
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(taskType == "2"){//部署成功
					$("<p class='kitem'><span>部署环境：</span>" + applyUrlStr + "</p>").appendTo($li.find(".pipelist"));
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else {
					logurl = "<a href='" + applyUrl + "' target= _blank>查看报告</a>";
					$("<p class='kitem'><span>测试结果：</span>" + logurl + "</p>").appendTo($li.find(".pipelist"));
					$li.find(".pipedetail").find("a").remove();
//					$(logurl).appendTo($li.find(".pipedetail"));
				}
			}else{
				$li.addClass("fail");
				$li.find(".state1 .p2 .fl").html(buildStartTime);
				$li.find(".state1 .p2 .fr").html(buildTotalTime);
				$li.find(".state1").show();
				$li.find(".state2").hide();
				$li.find("h3").find("a").remove();
				$("<s></s><a class='noedit' href='#'></a>").appendTo($li.find("h3"));
				$("<p class='kitem'><span>失败原因：</span>" + message + "</p>").appendTo($li.find(".pipelist"));
			
				var logurl = "<a href='" + localhostPath() + "/taskManage/queryTaskLoginfo.do?task_version=" 
				+ taskVersion + "&task_name=" + jobName + "' target= _blank class='blue'>执行日志</a>";
				
				if(taskType == "1"){
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(taskType == "2"){//部署
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}else if(taskType == "3"){
					logurl = "<a href='" + applyUrl + "' target= _blank class='blue'>执行日志</a>";
					$li.find(".pipedetail").find("a").remove();
					$(logurl).appendTo($li.find(".pipedetail"));
				}
			}
			$li.find(".pipedetail").find(".nowrap").html("");
		}else if(pipeStatus == "4"){//流水线执行结束
			var buildEndTime = jsonData.buildEndTime; 
			var buildTotalTime = jsonData.buildTotalTime;
			if(buildTotalTime != undefined && buildTotalTime != ""){
				buildTotalTime = millisecondChangeTime(buildTotalTime);
			}else{
				buildTotalTime = "--";
			}
			var mailAddress = jsonData.mailAddress;
			$ul.parent().find(".pipetitle .pipetitle_btn .btn1").removeClass("greybtn").addClass("bluebtn");
			$ul.parent().find(".pipeinfo .time2").html(buildEndTime);
			$ul.parent().find(".pipeinfo .consumeTime").html(buildTotalTime);
			$ul.parent().find(".pipeinfo .mailbox").html(mailAddress);
		}
	}
}
//执行过程中实时查看日志
function showLogInfo(obj){
	$("#logMessageBody").find(".logInfoDiv").hide();
	$li = $(obj).parent().parent();
	var pip_id = $li.attr("pip_id");
	var task_id = $li.attr("task_id");
	$("#logMessageBody").find("div[pip_id='" + pip_id + "'][task_id='" + task_id + "']").show();
	$("#logMessageModal").modal("show");
	
}
//这是一个webSocket测试的例子
//function webSocketTest(){
//	$.ajax({
//		url : localhostPath() + "/taskManage/autoTestLogByWs.do",
//		type : "post",
//		dataType : "json",
//		data : {
//			usernoRand : usernoRand
//		},
//		success : function(data){
//			alert("测试成功！");
//		},
//		error : function(e){
////			alert("立即执行流水线报错了！");
//		}
//	});
//	
//}