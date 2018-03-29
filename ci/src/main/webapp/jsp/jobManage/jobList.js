//查询任务列表
queryJobList();
function queryJobList(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/jobManage/queryJobList.do",
		data : {
			
		},
		success : function(data){
			var result = data.result;
			if(result == "true"){
				var rows = data.rows;
				if(rows.length > 0){
					$("#jobListTbody").html("");
					var tr = "";
					for (var i = 0; i < rows.length; i++) {
						var jobName = rows[i].jobName;
						var tomcat_ip = rows[i].tomcat_ip;
						var tomcat_port = rows[i].tomcat_port;
						if(jobName != ""){
							tr += "<tr><td>" + jobName + "</td>" +
							"<td name='build'>。。。</td>" +
							"<td name='deploy'>。。。</td>" +
							"<td name='loadUrl'></td>";
							if(tomcat_ip != "" && tomcat_port != ""){
								tr += "<td><a href='#' onclick='getAction(this)'>立即执行</a>&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='updateJob()'>修改</a></td></tr>";
							}else{
								tr += "<td><a onclick='updateJob()'>修改</a></td></tr>";
							}
						}
					}
					$(tr).appendTo($("#jobListTbody"));
				}
			}
		},
		error : function(data){
			alert("查询列表失败！");
		}
	});
}
//新增任务
function creatJob(){
	var formVal = $("#addJobForm").serialize();
	$.ajax({
		type : "post",
		dataType : "json",
		url : "addJob.do",
		data : formVal + "&localhostPath=" + localhostPath(),
		success : function(data){
			var result = data.result;
			if(result == "true"){
				$('#addJobModal').modal('hide');
			}else{
				alert("新增任务失败！");
			}
			queryJobList();
		},
		error : function(data){
			alert("新增任务失败！");
		}
	});
}
//修改任务
function updateJob(){
	$("#addJobModal").modal("show");
	$.ajax({
		type : "post",
		dataType : "json",
		url : "updateJob.do",
		success : function(data){
			var result = data.result;
			var info = data.info;
			if(result == "success"){
				$("#jobName").val(info.jobName); 
				$("#remarks").val(info.remarks); 
				$("#svn_password").val(info.svn_password); 
				$("#svn_url").val(info.svn_url); 
				$("#svn_username").val(info.svn_username); 
				$("#tomcat_ip").val(info.tomcat_ip); 
				$("#tomcat_password").val(info.tomcat_password); 
				$("#tomcat_port").val(info.tomcat_port); 
				$("#tomcat_username").val(info.tomcat_username);
				$("#sign").val("update");
			}else{
				alert("获取信息失败");
			}
		},
		error : function(data){
			alert("新增任务失败！");
		}
	});
}
//立即执行按钮操作
function getAction(obj){
	$(obj).parent().parent().find("[name=build]").html("正在构建&nbsp;&nbsp;&nbsp;<span class='fa fa-spinner fa-pulse'></span>");
	$(obj).parent().parent().find("[name=deploy]").html("");
	$.ajax({
		type : "post",
		dataType : "json",
		url : "getBuildAction.do",
		data : {
			
		},
		success : function(data){
			var result = data.result;
			if(result == "true"){
				$(obj).parent().parent().find("[name=build]").html("构建完成");
				$(obj).parent().parent().find("[name=deploy]").html("正在部署&nbsp;&nbsp;&nbsp;<span class='fa fa-spinner fa-pulse'></span>");
				getDeployAction(obj);
			}else{
				$(obj).parent().parent().find("[name=build]").html("构建失败");
				$(obj).parent().parent().find("[name=deploy]").html("");
			}
		},
		error : function(data){
			$(obj).parent().parent().find("[name=build]").html("构建失败");
			$(obj).parent().parent().find("[name=deploy]").html("");
			alert("执行失败！");
		}
	});
}
//构建任务完成后，执行部署任务
function getDeployAction(obj){
	$.ajax({
		type : "post",
		dataType : "json",
		url : "getDeployAction.do",
		data : {
			
		},
		success : function(data){
			var result = data.result;
			if(result == "true"){
				$(obj).parent().parent().find("[name=deploy]").html("部署完成");
				var loadUrl = data.loadUrl;
				$(obj).parent().parent().find("[name=loadUrl]").html("<a href='" + loadUrl + "' target='_blank'>" + loadUrl + "</a>");
			}else{
				$(obj).parent().parent().find("[name=deploy]").html("部署失败");
			}
		},
		error : function(data){
			$(obj).parent().parent().find("[name=deploy]").html("部署失败");
			alert("执行失败！");
		}
	});
	
}