
//加载构建环境与构建类型
environmentList();
function environmentList(){//动态加载构建环境
	
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/sdic/queryDicItemList.do",
		success : function(data){
			//console.log(data);
			var environment = data.environment;
			for(var i = 0;i<environment.length;i++){
				if(i == 0){
				$("#buildEnvironment").append('<label ><input name="environment" type="radio" value="'+environment[i].dicCode+'" /><s class="ico_'+environment[i].dicName+'"></s></label>');
				}else{
					$("#buildEnvironment").append('<label ><input name="environment" disabled="disabled" type="radio" value="'+environment[i].dicCode+'" /><s class="ico_'+environment[i].dicName+'"></s></label>');	
				}
				//onclick = "change(\''+environment[i].dicId+'\')"
				}
			pipInfo();
			setWindowHeight();
		},
		error : function(data){
			alert("调用接口失败！");
		}
	});
}
function change(id){//动态加载构建类型
	//alert(id);
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			id : id
		},
		url : localhostPath() + "/sdic/queryDicItemListZ.do",
		success : function(data){
			//console.log(data);
			var buildType = data.buildType;
			if(data.result = "success"){
				$("#buildType").empty();
				for(var i = 0;i<buildType.length;i++){
					$("#buildType").append('<label><input name="buildType" type="radio" value="'+buildType[i].dicCode+'" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
				}
				 $("input[name='buildType']").get(0).checked = true;//默认选中第一项
			}
			
		},
		error : function(data){
			alert("调用接口失败2！");
		}
	});
}
function change2(id,checkId){//首次进入修改页面
	//alert(id);
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			id : id
		},
		url : localhostPath() + "/sdic/queryDicItemListZ.do",
		success : function(data){
			//console.log(data);
			var buildType = data.buildType;
			if(data.result = "success"){
				$("#buildType").empty();
				for(var i = 0;i<buildType.length;i++){
					$("#buildType").append('<label><input name="buildType" type="radio" value="'+buildType[i].dicCode+'" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
				}
				$("input[name='buildType']").each(function(index) {//给构建环境赋选中值
				    if ($("input[name='buildType']").get(index).value == checkId) {
				        $("input[name='buildType']").get(index).checked = true;
				    }
				});
				setWindowHeight();
			}
			
		},
		error : function(data){
			alert("调用接口失败2！");
		}
	});
}
var kaiguan=1;
function updatePip(){
	if(kaiguan){
	var formVal1 = $("#addPipForm").serialize();
	var formVal2 = $("#addStrJobForm").serialize();
	var formVal3 = $("#addDepJobForm").serialize();
	var pip_name = $("#pip_name").val();
	var executionMode = $("#executionMode").val();
	var templetMode = $("#templetMode").val();
	//获取项目主键
	var projectId = getUrlPara("projectId");
	var id = getUrlPara("pip_id");
	if($("#pip_name").val()=='') {  
        alert("请输入流水线名称!"); 
        return;  
   }
	if($("#svn_url").val()==''){  
	        alert("请输入代码仓库!");  
	        return;  
	 }
	if($("#svn_account").val()==''){  
        alert("请输入svn账号!");  
        return;  
 }
	if($("#svn_password").val()==''){  
        alert("请输入svn密码!");  
        return;  
 }
	if($("#tomcat_account").val()==''){  
        alert("请输入tomcat账号!");  
        return;  
 }
	if($("#tomcat_password").val()==''){  
        alert("请输入tomcat密码!");  
        return;  
 }
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/updatePipeliningInfo.do",
		data : formVal1 +  "&projectId=" + projectId + "&id=" + id + "&executionMode=" + executionMode +"&templetMode=" + templetMode ,
		success : function(data){
			var result = data.result;
			if(result == "true"){
				location.href = "pipelining_list.jsp?projectId=" + projectId 
			}else{
				alert("修改流水线失败！");
			}
		},
		error : function(data){
			alert("修改任务失败！");
		}
	});
}
	 kaiguan = 0;
}
var pip_id = getUrlPara("pip_id");

function pipInfo(){//修改流水线
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			id : pip_id
		},
		url : localhostPath() + "/taskManage/updatePipelining.do",
		success : function(data){
			var info = data.info;
			//console.log(info);
			if(data.result == "success"){
				$("#pip_name").val(info.pipName); 
				$("#email").val(info.mailAddress); 
				$("#svn_url").val(info.svnUrl);
				$("#svn_account").val(info.svnAccount); 
				$("#svn_password").val(info.svnPassword);
				$("#tomcat_account").val(info.tomcatAccount);
				$("#tomcat_password").val(info.tomcatPassword);
				var momo = info.momo;
				var cron = info.cron;
				var templetId = info.templetId;
				$("#cron").val(cron);
				changeMode(momo);
				$("input[name='environment']").each(function(index) {//给构建环境类型赋选中值
				    if ($("input[name='environment']").get(index).value == info.build_environment) {
				        $("input[name='environment']").get(index).checked = true;
				    }
				});
				//$("input[name='environment']").get(info.build_environment).checked  = true;
				change2(info.build_environment_id,info.build_type);
				//给模板赋值
				if(templetId != null){
					$("#templet").addClass("current"); 
					$("#templetMode").val("1");  
				}
				setWindowHeight();			
			}
			
		},
		error : function(data){
			alert("调用修改流水线接口失败！");
		}
	});
}
function returnPip(){
	var projectId = getUrlPara("projectId");
	location.href = "pipelining_list.jsp?projectId=" + projectId
}

window.onload = function () {
	queryAutomatedTaskStart();
};
function queryAutomatedTaskStart(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/queryAutomatedTask.do",
//		data : formVal1 +  "&projectId=" + projectId ,
		success : function(data){
			if(data.code == "200"){
				var tasksList = data.data[0].tasks;
				$("#automatedTask").empty();
				for(var i = 0;i<tasksList.length;i++){
				$("#automatedTask").append('<tr><td><input class="radio_box" type="radio" name="automatedtask" value="'+tasksList[i].taskId+","+tasksList[i].taskName+'" onclick="check(this)"></td><td>'+tasksList[i].taskName+'</td><td>'+tasksList[i].taskType+'</td></tr>');
				}
			}
		},
		error : function(data){
		}
	});
}
function queryAutomatedTask(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/queryAutomatedTask.do",
//		data : formVal1 +  "&projectId=" + projectId ,
		success : function(data){
			if(data.code == "200"){
				var tasksList = data.data[0].tasks;
				$("#automatedTask").empty();
				for(var i = 0;i<tasksList.length;i++){
				$("#automatedTask").append('<tr><td><input class="radio_box" type="radio" name="automatedtask" value="'+tasksList[i].taskId+","+tasksList[i].taskName+'" onclick="check(this)"></td><td>'+tasksList[i].taskName+'</td><td>'+tasksList[i].taskType+'</td></tr>');
				}
			}
		},
		error : function(data){
			alert("查询自动化测试任务失败！");
		}
	});
}
function changeMode(mode){
	if(mode == 1){
		$("#MoMo1").addClass("current"); 
		$("#MoMo2").removeClass("current"); 
		$("#executionMode").val("1");  
	}else{
		$("#MoMo2").addClass("current"); 
		$("#MoMo1").removeClass("current"); 
		$("#executionMode").val("2"); 
	}
}
function templetChange(mode){
	var val = $("#templetMode").val();
	if(val == 1){
		$("#templet").removeClass("current"); 
		$("#templetMode").val("");  
	}else{
		$("#templet").addClass("current"); 
		$("#templetMode").val("1");  
	}
}
var tempradio= null;
function check(checkedRadio)
{
    if(tempradio== checkedRadio){
        tempradio.checked=false;
        tempradio=null;
    }
    else{
        tempradio= checkedRadio;
    }
}