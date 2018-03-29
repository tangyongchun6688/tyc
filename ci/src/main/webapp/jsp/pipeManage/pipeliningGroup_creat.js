
//加载模板
//mouidList();
//function mouidList(){//动态加载模板
//	
//}
//加载构建环境与构建类型
environmentList();
function environmentList() { //动态加载构建环境

	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/sdic/queryDicItemList.do",
		success : function(data) {
//			console.log(data);
			var environment = data.environment;
			for (var i = 0; i < environment.length; i++) {
				if(i == 0){
					$("#buildEnvironment").append('<a id="' + environment[i].dicCode + '" href="javascript:void(0);" onclick="changeEnvironment(' + environment[i].dicCode + ')" class="current" ><s class="ico_' + environment[i].dicName + '"></s></a>');
					$("#environmentHid").val(environment[i].dicCode);
				}else{
					$("#buildEnvironment").append('<a id="' + environment[i].dicCode + '" href="javascript:void(0);" onclick="changeEnvironment(' + environment[i].dicCode + ')" class="" ><s class="ico_' + environment[i].dicName + '"></s></a>');
				}
			}
			change('85c107ae8dab427e9d64e8cedf1c8869');
			//onclick = "change(\''+environment[i].dicId+'\')"
		},
		error : function(data) {
			alert("调用接口失败！");
		}
	});
}
function change(id) { //动态加载构建类型
	//alert(id);
	//	radio.checked=false; 
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			id : id
		},
		url : localhostPath() + "/sdic/queryDicItemListZ.do",
		success : function(data) {
//			console.log(data);
			var buildType = data.buildType;
			if (data.result = "success") {
				$("#buildType").empty();
				for (var i = 0; i < buildType.length; i++) {
//					$("#buildType").append('<label><input class="radio_box" name="buildType" type="radio" value="' + buildType[i].dicCode + '" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
					$("#buildType").append('<a class="current" href="javascript:void(0);"><img src="../../style/images_pipelin/img2_maven.png" alt=""></a>');
					$("#typeHid").val(buildType[i].dicCode);
				}
//				$("input[name='buildType']").get(0).checked = true; //默认选中第一项
				setWindowHeight();
			}

		},
		error : function(data) {
			alert("调用接口失败2！");
		}
	});
}
var kaiguan=1;
function newPip() {
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
//        console.log(param);
	//获取项目主键
	var projectId = getUrlPara("projectId");
	if ($("#pip_name").val().replace(/(^\s*)|(\s*$)/g, "") == '') {
		alert("请输入流水线名称!");
		return;
	}
	if ($("#svn_url").val().replace(/(^\s*)|(\s*$)/g, "")  == '') {
		alert("请输入代码仓库!");
		return;
	}
	if ($("#svn_account").val().replace(/(^\s*)|(\s*$)/g, "")  == '') {
		alert("请输入svn账号!");
		return;
	}
	if ($("#svn_password").val().replace(/(^\s*)|(\s*$)/g, "")  == '') {
		alert("请输入svn密码!");
		return;
	}
	//开启部署开关必选模板判断
	if(deployHid == "1"){
		if(templetMode == ""){
			alert("请选择模板");
			return;
		}
	}
	
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/taskManage/addPipelining.do",
		data : formVal1 + "&projectId=" + projectId + "&executionMode=" + executionMode + "&templetMode=" + templetMode+ "&environmentHid=" + environmentHid
		+ "&typeHid=" + typeHid+ "&deployHid=" + deployHid+ "&autoHid=" + autoHid + "&param=" + param,
		success : function(data) {
			var result = data.result;
			if (result == "true") {
//				alert(projectId);
				location.href = "pipe_list.jsp?projectId=" + projectId
			} else {
				alert("新增流水线失败！");
			}
			setWindowHeight();
		},
		error : function(data) {
			alert("新增任务失败！");
		}
	});
}
	kaiguan = 0;
}
function returnPip() {
	var projectId = getUrlPara("projectId");
	location.href = "pipe_list.jsp?projectId=" + projectId
}
window.onload = function() {
	queryAutomatedTaskStart();

};
function queryAutomatedTaskStart() {
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/taskManage/queryAutomatedTask.do",
		//		data : formVal1 +  "&projectId=" + projectId ,
		success : function(data) {
			if (data.code == "200") {
				var tasksList = data.data[0].tasks;
				$("#automatedTask").empty();
				for (var i = 0; i < tasksList.length; i++) {
					$("#automatedTask").append('<tr><td><input class="radio_box" type="radio" name="automatedtask" value="'+tasksList[i].taskId+","+tasksList[i].taskName+","+tasksList[i].taskType+'" onclick="check(this)"></td><td>'+tasksList[i].taskName+'</td><td>'+tasksList[i].taskType+'</td></tr>');
				}
			}
		},
		error : function(data) {}
	});
}
function queryAutomatedTask() {
	//自己造的data格式与请求接口返回的相同  避免多次请求别人机器

//自己造的data格式与请求接口返回的相同  避免数据变化等问题
	
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/queryAutomatedTask.do",
		success : function(data){
//			console.log(data);
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
function changeMode(mode) {
	if (mode == 1) {
		$("#MoMo1").addClass("current");
		$("#MoMo2").removeClass("current");
		$("#executionMode").val("1");
	} else {
		$("#MoMo2").addClass("current");
		$("#MoMo1").removeClass("current");
		$("#executionMode").val("2");
	}
}
function templetChange(mode) {
	var val = $("#templetMode").val();
	if (val == 1) {
		$("#templet").removeClass("current");
		$("#templetMode").val("");
	} else {
		$("#templet").addClass("current");
		$("#templetMode").val("1");
	}
}
var tempradio = null;
function check(checkedRadio) {
	if (tempradio == checkedRadio) {
		tempradio.checked = false;
		tempradio = null;
	} else {
		tempradio = checkedRadio;
	}
}
function deployOnoff(){
	
//	var documentHeight = $(document.body).outerHeight(true);
//	alert(documentHeight + "=======1");
	
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
	
//	var documentHeight = $(document.body).outerHeight(true);
//	alert(documentHeight + "=======2");
	
	
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

//function changeEnvironment(k){
//	$('#buildEnvironment .current').each(function(i){
//		$(this).removeClass('current');
//		 });
//	var obj = document.getElementById(k);
//	$("#environmentHid").val(k);
//	alert($("#environmentHid").val());
//	obj.setAttribute("class", "current ");
//}
//
//function selectNotAll(){//全选删除class
//	 $('.goods_sale_property').each(function(i){
//	$(this).removeClass('goods_sale_property_checked');
//	 });
//	}
function addtr(){
	var tr=document.createElement("tr");
	var x1=document.createElement("td");
	var x2=document.createElement("td");
	var x4=document.createElement("td");
	x1.innerHTML="";
	var del=document.createElement("td");
	x2.setAttribute("contentEditable","true");
	x4.setAttribute("contentEditable","true");
	del.setAttribute("class","opera");
	del.innerHTML="<a onclick='addtr()' class='a_add' href='javascript:void(0)'></a> <a onclick='deltr(this)' class='a_del' href='javascript:void(0)'></a>";
	var tab=document.getElementById("table");
	tab.appendChild(tr);
	tr.appendChild(x1);
	tr.appendChild(x2);
	tr.appendChild(x4);
	tr.appendChild(del);
	var tr = document.getElementsByTagName("tr");
	var len = $('#table tr').length;//获取id为table的表格共有几行
    for(var i = 1;i<len;i++){
        $('#table tr:eq('+i+') td:first').text(i);//从第二行开始赋序号值
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
function showPipList(){
	var pageNum = 1;
	var pageSize = 5;
	page(pageNum,pageSize);
}
function lPage(){
	var pageNow = document.getElementById("pageNow").innerHTML;  
	pageNow = parseInt(pageNow) - 1;
	var pageSize = 5;
	if(parseInt(pageNow) > 0){
		var pageNow = $("#pageNow").html(pageNow);
		page(pageNow,pageSize);
	}
}	
function nPage(){
	var pageTotal = document.getElementById("pageTotal").innerHTML;  
	var pageNow = document.getElementById("pageNow").innerHTML;  
	pageNow = parseInt(pageNow) + 1;
	var pageSize = 5;
	if(parseInt(pageNow) <= parseInt(pageTotal)){
		var pageNow = $("#pageNow").html(pageNow);
		page(pageNow,pageSize);
	}
}
function goPage(n,s){
	$('#pageNow').html(n);
	page(n,"5");
}
function exchange(){//切换列表下的页码
	
}
function page(pageNum,pageSize){
	var pageNow = parseInt(document.getElementById("pageNow").innerHTML);
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/queryPipList.do",
		data : {
			pageNum : pageNow,
			pageSize : pageSize
		},
		success : function(data){
//			console.log(data);
			var info = data.result;
			var html = '';
			/*onclick="changePie(\''+info[i].ID+'\')"*/
			for(var i=0;i<info.length;i++){
				html += '<tr>';
				html += '<td ><s id="'+info[i].ID+'" onclick="changePie(this)" class="check_box"></s></td>';
				html += '<td class="piple_name">'+info[i].PIP_NAME+'</td>';
				html += '<td>'+info[i].CREATE_USER+'</td>';
				html += '<td>'+info[i].CREATE_TIME+'</td>';
				if(info[i].BUILD_START_TIME!=null){
					html += '<td>'+info[i].BUILD_START_TIME+'</td>';
				}else{
					html += '<td></td>';
				}
				if(info[i].BUILD_STATUS=="2"){
					html += '<td><span class="success">成功</span></td>';
				}else if(info[i].BUILD_STATUS==3){
					html += '<td><span class="error">失败</span></td>';
				}else{
					html += '<td><span class="nodo">未执行</span></td>';
				}
				html += '</tr>';
			}
			var pageTot = parseInt(data.total);//总页数
			$('#pipInfo').html(html);
			$('#pipCount').html("共 "+data.count+" 条");
			$('#pageNow').html(pageNow);
			$('#pageTotal').html(pageTot);
			var min = pageNow - 2;
			var max = pageNow + 2; 
				$('#page').empty();
				var pageHtml = '';
				pageHtml +='<li class="pipelin_pageprev"><a onclick="lPage()" href="javascript:void(0)"><<</a></li>';
				if(pageNow > 2){
					if(max <= pageTot){
						for(var x=eval(pageNow-2);x<eval(pageNow+3);x++){
							if(x == pageNow){
								pageHtml +='<li class="current"><a onclick="goPage(\''+x+'\',\'5\')" href="javascript:void(0)">'+x+'</a></li>';
							}else{
								pageHtml +='<li><a onclick="goPage(\''+x+'\',\'5\')" href="javascript:void(0)">'+x+'</a></li>';
							}
						}
					}else{
						for(var x=eval(pageTot-4);x<=pageTot;x++){
							if(x == pageNow){
								pageHtml +='<li class="current"><a onclick="goPage(\''+x+'\',\'5\')" href="javascript:void(0)">'+x+'</a></li>';
							}else{
								pageHtml +='<li><a onclick="goPage(\''+x+'\',\'5\')" href="javascript:void(0)">'+x+'</a></li>';
							}
						}
					}
				}else{
					if(pageTot >= 5){
						for(var i=1;i<=5;i++){
							if(i == pageNow){
								pageHtml +='<li class="current"><a onclick="goPage(\''+i+'\',\'5\')" href="javascript:void(0)">'+i+'</a></li>';
							}else{
								pageHtml +='<li><a onclick="goPage(\''+i+'\',\'5\')" href="javascript:void(0)">'+i+'</a></li>';
							}
						}
					}else{
						for(var i=1;i<=pageTot;i++){
							if(i == pageNow){
								pageHtml +='<li class="current"><a onclick="goPage(\''+i+'\',\'5\')" href="javascript:void(0)">'+i+'</a></li>';
							}else{
								pageHtml +='<li><a onclick="goPage(\''+i+'\',\'5\')" href="javascript:void(0)">'+i+'</a></li>';
							}
						}
					}
					
				}
				pageHtml +='<li class="pipelin_pagenext"><a onclick="nPage()" href="javascript:void(0)">>></a></li>';
				$('#page').append(pageHtml);
			
		},
		error : function(data){
			alert("查询流水线列表失败！");
		}
	});
}
function changePie(obj){
	$("s").removeClass('check_boxs'); 
	var x = obj;
//	console.log(x);
	obj.setAttribute("class","check_box check_boxs");
}
function pipAdd(){
	var a = document.getElementsByClassName("check_boxs");
//	console.log(a);
//	console.log(a.length);
//	console.log(a.item(0));
	var id = a.item(0).id
//	console.log(a.item(0).id);
	var order = parseInt($("#emTotal h6").length) + 1;
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() +"/taskManage/queryPipInfo.do",
		data : {
			id : id
		},
		success : function(data){
//			console.log(data);
			var info = data.pipInfo;
			var html = '';
			html += '<div id="'+info[0].ID+'" class="line deployediv subLine">';
			html += '<h6>';
//			html += '<em>'+order+'</em>'+info[0].PIP_NAME+'';
			html += '<em>'+order+'</em><p>'+info[0].PIP_NAME+'</p>';
			html += '</h6>';
			html += '<div class="creatpipe_div">';
			html += '<ul class="userinfo">';
			html += '<li><span>创建人：</span>'+info[0].CREATE_USER+'</li>';
			html += '<li><span>创建时间：</span>'+info[0].CREATE_TIME+'</li>';
			if(info[0].BUILD_START_TIME!=null){
				html += '<li><span>最近执行时间：</span>'+info[0].BUILD_START_TIME+'</li>';
			}else{
				html += '<li><span>最近执行时间：</span></li>';
			}
			if(info[0].BUILD_STATUS == "2"){
				html += '<li><span>执行状态：</span>成功</li>';
			}else if(info[0].BUILD_STATUS == "3"){
				html += '<li><span>执行状态：</span>失敗</li>';
			}else{
				html += '<li><span>执行状态：</span>未执行</li>';
			}
			html += '</ul>';
			html += '<div class="piple_edit">';
			html += '<a href="javascript:void(0)" class="ico20 ico_edit"></a>';
			html += '<a onclick="delSubPip(\''+info[0].ID+'\')" href="javascript:void(0)" class="ico20 ico_delete"></a>';
			html += '</div>';
			html += '</div>';
			if(order == 1 ){
				html += '<div style="display:none" class="deployedivcnt">';
			}else{
				html += '<div class="deployedivcnt">';
			}
			html += '<ul class="creatul1 deployment">';
			html += '<li class="creatli">';
			html += '<b><em class="red f14"> * </em>执行参数配置</b>';
			html += '<table id="table'+order+'" class="config_node subtable">';
			html += '<tr>';
			html += '<th width="10%">序号</th>';
			html += '<th width="40%">名称</th>';
			html += '<th width="40%">默认值</th>';
			html += '<th width="10%">操作</th>';
			html += '</tr>';
			html += '<tr>';
			html += '<td>1</td>';
			html += '<td contentEditable="true"></td>';
			html += '<td id=\"'+GenNonDuplicateID()+'\"  class="piple_type">';
			html += '<div onclick="showProduct(this,\''+info[0].ID+'\')" class="select"><p><span>请选择</span><span style="display:none">隐藏得</span><a></a></p></div>';
			html += '</td>';
			html += '<td class="opera">';
			html += '<a onclick="subAddtr(\''+order+'\',\''+info[0].ID+'\')" class="a_add" href="javascript:void(0)"></a>';
			html += '</td>';
			html += '</tr>';
			html += '</table>';
			html += '</li>';
			html += '</ul>';
			html += '</div>';
			html += '</div>';
			
			$('#subLine').before(html);
			setWindowHeight();
		},
		error : function(data){
			alert("查询单条流水线信息失败！");
		}
	});
	setWindowHeight();
}
function subAddtr(order,nPipId){
	var tr=document.createElement("tr");
	var x1=document.createElement("td");
	var x2=document.createElement("td");
//	var x3=document.createElement("td");
	var x4=document.createElement("td");
	x1.innerHTML="";
	var del=document.createElement("td");
	x2.setAttribute("contentEditable","true");
//	x3.setAttribute("contentEditable","true");
//	x4.setAttribute("contentEditable","true");
	x4.innerHTML='<div onclick="showProduct(this,\''+nPipId+'\')" class="select"><p><span>请选择</span><span style="display:none">隐藏得</span><a></a></p></div>';
	x4.setAttribute("id",GenNonDuplicateID());
	x4.setAttribute("class","piple_type");
//	x4.setAttribute("onclick","showProduct(this,'"+nPipId+"')");
	del.setAttribute("class","opera");
	del.innerHTML="<a onclick='subAddtr(\""+order+"\",\""+nPipId+"\")' class='a_add' href='javascript:void(0)'></a> <a onclick='subDeltr(this,\""+order+"\")' class='a_del' href='javascript:void(0)'></a>";
	var tab=document.getElementById("table"+order);
	tab.appendChild(tr);
	tr.appendChild(x1);
	tr.appendChild(x2);
//	tr.appendChild(x3);
	tr.appendChild(x4);
	tr.appendChild(del);
	var tr = document.getElementsByTagName("tr");
	var len = $('#table'+order+' tr').length;
    for(var i = 1;i<len;i++){
        $('#table'+order+' tr:eq('+i+') td:first').text(i);
    }
    setWindowHeight();
}
function subDeltr(obj,order){
	var tr=obj.parentNode.parentNode;
	var tableName = obj.parentNode.parentNode.parentNode.id;
	tr.parentNode.removeChild(tr);
	var len = $('#'+tableName+' tr').length;
    for(var i = 1;i<len;i++){
    	$('#'+tableName+' tr:eq('+i+') td:first').text(i);
    }
    setWindowHeight();
}
//流水线组相关
function subchangeMode(mode) {
	if (mode == 1) {
		$("#subMoMo1").addClass("current");
		$("#subMoMo2").removeClass("current");
		$("#subexecutionMode").val("1");
	} else {
		$("#subMoMo2").addClass("current");
		$("#subMoMo1").removeClass("current");
		$("#subexecutionMode").val("2");
	}
}
function show(flag){
	if(flag==1){
		document.getElementById("new").style.display="";
		document.getElementById("subnew").style.display="none";
	}else{
		document.getElementById("subnew").style.display="";
		document.getElementById("new").style.display="none";
	}
	var documentHeight = $(window).height() - $(document.body).outerWidth(true) > 0 ? $(window).height() : $(document.body).outerWidth(true);
	console.log("hig="+documentHeight);
	parent.document.getElementById("workIframeDiv").style.height = documentHeight + "px";
	parent.document.getElementById("workIframe").style.height = documentHeight + "px";
	parent.parent.document.getElementById("mainIframeDiv").style.height = documentHeight + "px";
	parent.parent.document.getElementById("mainIframe").style.height = documentHeight + "px";
}
var kai = 1;
function subnewPip(){
	if(kai){
	var subPip = $("div.subLine");
	var subtable = $("table.subtable");
	var json = [];
	var info = {};
	for(var j=0;j<subtable.length;j++){
		var json2 = {};
		var tempArr = [];
		//表格中的参数
	    var tabLen = document.getElementById("table"+eval(j+1));
	    var source = $("#table"+eval(j+1)).parents(".subLine");
	    var sourceId = source[0].id;
//	    console.log(sourceId);
	      for (var i = 1; i < tabLen.rows.length; i++) {
	      	 var x = {};
	          x.param_name = tabLen.rows[i].cells[1].innerHTML.replace(/(^s*)|(s*$)/g, "");
	          x.param_source_key = tabLen.rows[i].cells[2].getElementsByTagName("span")[0].innerHTML.replace(/(^s*)|(s*$)/g, "");
	          x.param_source = tabLen.rows[i].cells[2].getElementsByTagName("span")[1].innerHTML.replace(/(^s*)|(s*$)/g, "");
	          x.param_sort = i;
	          x.pip_task_id = sourceId;
	          if(x.param_name != "" && x.param_source_key != "" && x.param_source_key.indexOf("请选择")== -1){
	        	  tempArr.push(x);
	          }
	      }
	      json2.sourceId = sourceId;
	      json2.param = tempArr;
	     
	    //表格中的参数
	      json.push(json2);
	      info.info = json;
	}
	 var param = JSON.stringify(info);
//	 console.log(info);
//	 console.log(isEmptyObject(info));
	if(isEmptyObject(info)){
		alert("请至少添加一个流水线!");
		return;
	}
	var formVal1 = $("#addPipForm").serialize();
	var projectId = getUrlPara("projectId");
	var subexecutionMode = $("#subexecutionMode").val();
	if ($("#subpip_name").val().replace(/(^\s*)|(\s*$)/g, "") == '') {
		alert("请输入流水线名称!");
		return;
	}
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/pipeManage/addPipeliningGroup.do",
		data : formVal1 + "&projectId=" + projectId + "&subexecutionMode=" + subexecutionMode +
		 "&json=" + param,
		success : function(data) {
			var result = data.result;
			if (result == "true") {
//				alert(projectId);
				location.href = "pipe_list.jsp?projectId=" + projectId
			} else {
				alert("新增流水线失败！");
			}
			setWindowHeight();
		},
		error : function(data) {
			alert("新增任务失败！");
		}
	});
	}
	kai = 0;
}
function showProduct(obj,nowId){
//	alert(nowId);
	//当前点击的流水线的id
	var tdId = obj.parentNode.id;
	//获取兄弟节点
	//首先获取到当前流水线之前所有流水线id
	var subPip = $("div.subLine");
	var arr = [];
	var pipIdArr = [];
//	console.log("-----");
//	console.log(subPip[0]);
	var subPip2 = $("div.subLine h6 p");

//	console.log(subPip2.length);
	var str = subPip2[0].innerHTML;
//	console.log(str);
	for(var i=0;i<subPip.length;i++){
		var json = {};
		if(subPip[i].id==nowId){
		break;
		}
		json.id = subPip[i].id;
		json.pipName = subPip2[i].innerHTML;
		arr.push(json);
		pipIdArr.push(subPip[i].id);
	}
//	console.log(pipIdArr);
	//去后台查每个流水线都有什么任务
	var pipTypeArr = queryProduct(pipIdArr);
//	console.log(pipTypeArr);
	//然后根据这些流水线id查询出产出物并展示
	var html = '';
	html += '<div onclick="showProduct(this,\''+nowId+'\')" class="select">';
	html += '<p>';
	html += '<span>请选择</span><span style="display:none">隐藏得</span>';
	html += '<a></a>';
	html += '</p>';
	html += '</div>';
	html += ' <div class="create_pipe" style="display: block;">';
	for(var i=0;i<arr.length;i++){
		
		if(pipTypeArr[i] == "1" || pipTypeArr[i] == "2"){
			html += ' <h3 class="h3title">'+arr[i].pipName+'</h3>';
			html += '<ul class="border_btm">';
			html += '<li>';
			html += '<span class="order">1</span>';
			html += '<span class="task create_task">构建任务——输出:</span>';
			html += '<div class="output">';
			html += '<a onclick="addParam(this,\'projectName\',\''+arr[i].id+'\');" href="javascript:void(0)">应用名称</a>';
			html += '<a onclick="addParam(this,\'warName\',\''+arr[i].id+'\');" class="" href="javascript:void(0)">war/jar</a>';
			html += '</div>';
			html += '</li>';
			html += '<li>';
		}
	   if(pipTypeArr[i] == "2"){
			html += '<span class="order">2</span>';
			html += '<span class="task create_task">部署任务——输出:</span>';
			html += '<div class="output">';
			html += '<a onclick="addParam(this,\'applayUrl\',\''+arr[i].id+'\');" href="javascript:void(0)">应用url</a>';
			html += '<a onclick="addParam(this,\'deploy_ip\',\''+arr[i].id+'\');" href="javascript:void(0)">ip</a>';
			html += '<a onclick="addParam(this,\'deploy_port\',\''+arr[i].id+'\');" class="" href="javascript:void(0)">port</a>';
			html += '</div>';
			html += '</li>';
		}
		html += '</ul>';
	}
	html += '</div>';
	document.getElementById(tdId).innerHTML = html;
	//选中某个产出物时获取它的名字及它所属的流水线id
}
function queryProduct(pipIdArr){//去后台查每个流水线都有什么任务
	var result;
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/pipeManage/queryProduct.do",
		data : "&pipIdArr=" + pipIdArr,
		async:false,
		success : function(data) {
//			console.log(data);
//			console.log(data.type[0]);
			result = data.type;
		},
		error : function(data) {
			alert("查询产出物失败！");
		}
	});
	return result;
}
/**
 * 生成一个用不重复的ID
 */
function GenNonDuplicateID(){
 return Math.random().toString()
}
function addParam(obj,param,paramSource){
	var ntdId = obj.parentNode.parentNode.parentNode.parentNode.parentNode.id;
//	console.log(ntdId);
	var a=document.getElementById(ntdId);//获取页面所有table对象,返回一个含有table对象的数组 
	var b= a.getElementsByTagName("span");//a[0],表示第一个table对象,此句获取此table中所有的span对象
	var c= a.getElementsByTagName("div")
//	console.log(b[0].innerHTML);
//	console.log(b[1].innerHTML);
	b[0].innerHTML = param;
	b[1].innerHTML = paramSource;
	c[1].style.display = "none";
//	document.getElementById(ntdId).innerHTML = param;
}
function delSubPip(subPipId){
//	alert(subPipId);
	$("#"+subPipId).remove();
	var total = $("#emTotal h6").length;
	var emtotal = $("#emTotal h6 em");
//	console.log(emtotal[0]);
//	console.log(emtotal[0].innerHTML);
//	emtotal[0].innerHTML = "666666";
	for(var i=0;i<emtotal.length;i++){
		emtotal[i].innerHTML = parseInt(i)+1;
	}
	var divtotal = $("#emTotal>div>div");
//	console.log(divtotal.length);
//	console.log(divtotal[1]);
	if(divtotal.length != 0){
		divtotal[1].style.display = "none";
	}
	var tot2 = $("#emTotal table");
	for(var i=0;i<tot2.length;i++){
		tot2[i].id = 'table'+eval(i+1);
	}
} 
	  
function isEmptyObject(e) {  
    var t;  
    for (info in e)  
        return !1;  
    return !0  
}  