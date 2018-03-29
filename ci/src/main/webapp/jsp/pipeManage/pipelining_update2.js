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
			for (var i = 0; i < environment.length; i++) {
				if(i == 0){
					$("#buildEnvironment").append('<a id="' + environment[i].dicCode + '" href="javascript:void(0);" onclick="changeEnvironment(' + environment[i].dicCode + ')" class="current" ><s class="ico_' + environment[i].dicName + '"></s></a>');
					$("#environmentHid").val(environment[i].dicCode);
				}else{
					$("#buildEnvironment").append('<a id="' + environment[i].dicCode + '" href="javascript:void(0);" onclick="changeEnvironment(' + environment[i].dicCode + ')" class="" ><s class="ico_' + environment[i].dicName + '"></s></a>');
				}
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
//				for(var i = 0;i<buildType.length;i++){
//					$("#buildType").append('<label><input name="buildType" type="radio" value="'+buildType[i].dicCode+'" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
//				}
				for (var i = 0; i < buildType.length; i++) {
					$("#buildType").append('<a class="current" href="javascript:void(0);"><img src="../../style/images_pipelin/img2_maven.png" alt=""></a>');
					$("#typeHid").val(buildType[i].dicCode);
				}
//				 $("input[name='buildType']").get(0).checked = true;//默认选中第一项
				setWindowHeight();
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
//				for(var i = 0;i<buildType.length;i++){
//					$("#buildType").append('<label><input name="buildType" type="radio" value="'+buildType[i].dicCode+'" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
//				}
				for (var i = 0; i < buildType.length; i++) {
					$("#buildType").append('<a class="current" href="javascript:void(0);"><img src="../../style/images_pipelin/img2_maven.png" alt=""></a>');
					$("#typeHid").val(buildType[i].dicCode);
				}
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
	var environmentHid = $("#environmentHid").val();//构建环境
	var typeHid = $("#typeHid").val();//构建类型
	var deployHid = $("#deployHid").val()//是否开启部署任务按钮
	var autoHid = $("#autoHid").val()//是否开启自动化测试按钮
	
	var json = [];
      var tabLen = document.getElementById("table");
        for (var i = 1; i < tabLen.rows.length; i++) {
        	 var j = {};
            j.param_name = tabLen.rows[i].cells[1].innerHTML.replace(/(^s*)|(s*$)/g, "");
            j.param_default = tabLen.rows[i].cells[2].innerHTML.replace(/(^s*)|(s*$)/g, "");
            j.param_sort = i;
            if(j.param_name != "" && j.param_default != ""){
            	json.push(j);
            }
        }
        var param = JSON.stringify(json);
        console.log(param);
	//获取项目主键
	var projectId = getUrlPara("projectId");
	var id = getUrlPara("pip_id");
	if($("#pip_name").val().replace(/(^\s*)|(\s*$)/g, "")  =='') {  
        alert("请输入流水线名称!"); 
        return;  
   }
	if($("#svn_url").val().replace(/(^\s*)|(\s*$)/g, "")  ==''){  
	        alert("请输入代码仓库!");  
	        return;  
	 }
	if($("#svn_account").val().replace(/(^\s*)|(\s*$)/g, "")  ==''){  
        alert("请输入svn账号!");  
        return;  
 }
	if($("#svn_password").val().replace(/(^\s*)|(\s*$)/g, "")  ==''){  
        alert("请输入svn密码!");  
        return;  
 }
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/updatePipeliningInfo.do",
		data : formVal1 +  "&projectId=" + projectId + "&id=" + id + "&executionMode=" + executionMode +"&templetMode=" + templetMode + "&environmentHid=" + environmentHid
		+ "&typeHid=" + typeHid+ "&deployHid=" + deployHid+ "&autoHid=" + autoHid + "&param=" + param,
		success : function(data){
			var result = data.result;
			if(result == "true"){
				location.href = "pipe_list.jsp?projectId=" + projectId 
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
//			console.log(info);
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
				var paramInfo = info.paramInfo;
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
				var obj = document.getElementById("deploy");
				var objReview = document.getElementById("deployReview");
//				var obj2 = document.getElementById("auto");
//				var objReview2 = document.getElementById("autoReview");
				if(templetId != null){
					objReview.setAttribute("class", "creatpipe_tabc");
					obj.setAttribute("class", "checkswitch checkswitch_ed");
					$("#deployHid").val("1");
					$("#templet").addClass("current"); 
					$("#templetMode").val("1");  
				}
				if(paramInfo != null && paramInfo.length != 0 ){
					$("#param").empty();
					var paramHtml = '';
					for(var x=0;x<paramInfo.length;x++){
						paramHtml += '<tr>';
						paramHtml += '<td name="sort">'+eval(x+1)+'</td>';
						paramHtml += '<td contentEditable="true">'+paramInfo[x].paramName+'</td>';
						paramHtml += '<td contentEditable="true">'+paramInfo[x].paramDefault+'</td>';
						paramHtml += '<td class="opera">';
						paramHtml += ' <a onclick="addtr()" class="a_add" href="javascript:void(0)"></a>';
						if(x!=0){
							paramHtml += ' <a onclick="deltr(this)" class="a_del" href="javascript:void(0)"></a>';
						}
						paramHtml += '</td>';
						paramHtml += '</tr>';
					}
					$("#param").html(paramHtml);
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
					$("#automatedTask").append('<tr><td><input class="radio_box" type="radio" name="automatedtask" value="'+tasksList[i].taskId+","+tasksList[i].taskName+","+tasksList[i].taskType+'" onclick="check(this)"></td><td>'+tasksList[i].taskName+'</td><td>'+tasksList[i].taskType+'</td></tr>');
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
					$("#automatedTask").append('<tr><td><input class="radio_box" type="radio" name="automatedtask" value="'+tasksList[i].taskId+","+tasksList[i].taskName+","+tasksList[i].taskType+'" onclick="check(this)"></td><td>'+tasksList[i].taskName+'</td><td>'+tasksList[i].taskType+'</td></tr>');
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

function deployOnoff(){
	var obj = document.getElementById("deploy");
	var objReview = document.getElementById("deployReview");
	var obj2 = document.getElementById("auto");
	var objReview2 = document.getElementById("autoReview");
	if($("#deployHid").val()==1){
		objReview.setAttribute("class", "creatpipe_tabc noeditdiv");
		objReview2.setAttribute("class", "creatpipe_tabc noeditdiv");
		obj.setAttribute("class", "checkswitch ");
		obj2.setAttribute("class", "checkswitch ");
		$("#deployHid").val("0");
		$("#autoHid").val("0");
	}else{
		objReview.setAttribute("class", "creatpipe_tabc");
		obj.setAttribute("class", "checkswitch checkswitch_ed");
		$("#deployHid").val("1");
	}
	setWindowHeight();
	  //obj.className = "style2";
	  
}
function autoOnoff(){
	if($("#deployHid").val()==1){
		var obj = document.getElementById("auto");
		var objReview = document.getElementById("autoReview");
		if($("#autoHid").val()==1){
			objReview.setAttribute("class", "creatpipe_tabc noeditdiv");
			obj.setAttribute("class", "checkswitch ");
			$("#autoHid").val("0");
		}else{
			objReview.setAttribute("class", "creatpipe_tabc");
			obj.setAttribute("class", "checkswitch checkswitch_ed");
			$("#autoHid").val("1");
		}
	}else{
		return;
	}
	setWindowHeight();
}

function addtr(){
	var tr=document.createElement("tr");
	var x1=document.createElement("td");
	var x2=document.createElement("td");
//	var x3=document.createElement("td");
	var x4=document.createElement("td");
	x1.innerHTML="";
//	xm.innerHTML="第"+"1"+"学生";
	var del=document.createElement("td");
	x2.setAttribute("contentEditable","true");
//	x3.setAttribute("contentEditable","true");
	x4.setAttribute("contentEditable","true");
	del.setAttribute("class","opera");
	del.innerHTML="<a onclick='addtr()' class='a_add' href='javascript:void(0)'></a> <a onclick='deltr(this)' class='a_del' href='javascript:void(0)'></a>";
	var tab=document.getElementById("table");
	tab.appendChild(tr);
	tr.appendChild(x1);
	tr.appendChild(x2);
//	tr.appendChild(x3);
	tr.appendChild(x4);
	tr.appendChild(del);
	var tr = document.getElementsByTagName("tr");
	
	var len = $('table tr').length;
    for(var i = 1;i<len;i++){
        $('#table tr:eq('+i+') td:first').text(i);
    }
    setWindowHeight();
}
function deltr(obj){
	var tr=obj.parentNode.parentNode;
	tr.parentNode.removeChild(tr);
	var len = $('#table tr').length;
    for(var i = 1;i<len;i++){
        $('#table tr:eq('+i+') td:first').text(i);
    }
    setWindowHeight();
}