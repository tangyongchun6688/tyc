//获取项目主键
var projectId = getIframeSrcPara("workIframe","projectId");
//获取流水线主键
var pip_id = getIframeSrcPara("workIframe","pip_id");
//获取流水线名称
var pip_name = getIframeSrcPara("workIframe","pip_name");

//获取流水线执行历史列表
queryPipHistoryList();
function queryPipHistoryList(){
	
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/pipeManage/queryPipHistoryList.do",
		data : {
			pip_id : pip_id
		},
		success : function(data){
			var status = data.status;
			if(status == "success"){
				var vList = data.vList;
				$("#pipeHisListDiv").html("");
				if(vList != undefined && vList.length > 0){
					for (var i = 0; i < vList.length; i++) {
						var build_start_time = vList[i].BUILD_START_TIME == undefined ? "--" : vList[i].BUILD_START_TIME;
						var build_end_time = vList[i].BUILD_END_TIME == undefined ? "--" : vList[i].BUILD_END_TIME;
						var build_total_time = vList[i].BUILD_TOTAL_TIME;
						var mail_address = vList[i].MAIL_ADDRESS == undefined ? "" : vList[i].MAIL_ADDRESS;
						if(build_total_time != undefined){
							build_total_time = millisecondChangeTime(build_total_time);
						}else{
							build_total_time = "--";
						}
						var $pipInfoDiv = $("<div class='pipecnt'></div>");
						var $pipInfo = $("<div class='pipetitle'>" + pip_name + "</div>" +
									"<div class='pipeinfo'>" +
										"<span class='mr20'>时间：" + build_start_time + "至" + build_end_time + "</span>" +
										"<span class='mr20'>耗时：" + build_total_time + "</span>" +
										"<span class='mr20'>邮件已推送给：" + mail_address + "</span>" +
									"</div>");
						$pipInfo.appendTo($pipInfoDiv);
						var pip_version = vList[i].PIP_VERSION;
						var teskJson = data["pip_version" + pip_version];
						if(teskJson.length > 0){
							teskJson = JSON.parse(teskJson);
							var $ul = $("<ul class='pipestep' pip_id='" + pip_id + "' pip_version='" + pip_version + "'></ul>");
							for (var j = 0; j < teskJson.length; j++) {
								var task = teskJson[j];
								var task_type = task.task_type;
								var taskInfo = task.taskInfo;
								var liHtml = "";
								if(task_type == "0"){//子流水线
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
						$pipInfoDiv.appendTo($("#pipeHisListDiv"));
					}
				}else{
					$("#NoPipeDiv").show();
					$("#pipeHisListDiv").hide();
				}
			}else{
				alert("获取流水线执行历史失败！");
			}
			setWindowHeight();
		},
		error : function(e){
			alert("获取流水线执行历史失败！");
		}
	});
}

//返回流水线列表页面
function gotoPipList(){
	window.location.href = localhostPath()
			+ "/jsp/pipeManage/pipe_list.jsp?projectId="
			+ projectId;
}