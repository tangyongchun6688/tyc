var pip_id = getUrlPara("pip_id");

pipGroupInfo();
function pipGroupInfo(){//查询流水线组信息
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			id : pip_id
		},
		url : localhostPath() + "/pipeManage/pipGroupInfo.do",
		success : function(data){
//			console.log(data);
			var info = data.info;
			if(data.result == "success"){
				$("#subpip_name").val(info.pipName); 
				$("#subemail").val(info.mailAddress); 
				var momo = info.momo;
				var cron = info.cron;
				$("#subcron").val(cron);
				subchangeMode(momo);
			
			var subPipInfo = data.subPipInfo;
			console.log(subPipInfo);
//			console.log(subPipInfo.length);
			for(var i=0;i<subPipInfo.length;i++){
				var html = '';
				html += '<div id="'+subPipInfo[i].pipInfo[0].ID+'" class="line deployediv subLine">';
				html += '<h6>';
//				html += '<em>'+order+'</em>'+info[0].PIP_NAME+'';
				html += '<em>'+eval(i+1)+'</em><p>'+subPipInfo[i].pipInfo[0].PIP_NAME+'</p>';
				html += '</h6>';
				html += '<div class="creatpipe_div">';
				html += '<ul class="userinfo">';
				html += '<li><span>创建人:</span>'+subPipInfo[i].pipInfo[0].CREATE_USER+'</li>';
				html += '<li><span>创建时间:</span>'+subPipInfo[i].pipInfo[0].CREATE_TIME+'</li>';
				if(subPipInfo[i].pipInfo[0].BUILD_START_TIME!=null){
					html += '<li><span>最近执行时间:</span>'+subPipInfo[i].pipInfo[0].BUILD_START_TIME+'</li>';
				}else{
					html += '<li><span>最近执行时间:</span></li>';
				}
				if(subPipInfo[i].pipInfo[0].BUILD_STATUS == "2"){
					html += '<li><span>执行状态:</span>成功</li>';
				}else if(subPipInfo[i].pipInfo[0].BUILD_STATUS == "3"){
					html += '<li><span>执行状态:</span>失敗</li>';
				}else{
					html += '<li><span>执行状态:</span>未执行</li>';
				}
				html += '</ul>';
				html += '<div class="piple_edit">';
				html += '<a href="javascript:void(0)" class="ico20 ico_edit"></a>';
				html += '<a onclick="delSubPip(\''+subPipInfo[i].pipInfo[0].ID+'\')" href="javascript:void(0)" class="ico20 ico_delete"></a>';
				html += '</div>';
				html += '</div>';
				if(eval(i+1) == 1 ){
					html += '<div style="display:none" class="deployedivcnt">';
				}else{
					html += '<div class="deployedivcnt">';
				}
				html += '<ul class="creatul1 deployment">';
				html += '<li class="creatli">';
				html += '<b><em class="red f14"> * </em>执行参数配置</b>';
				html += '<table id="table'+eval(i+1)+'" class="config_node subtable">';
				html += '<tr>';
				html += '<th width="10%">序号</th>';
				html += '<th width="40%">名称</th>';
				html += '<th width="40%">默认值</th>';
				html += '<th width="10%">操作</th>';
				html += '</tr>';
				if(subPipInfo[i].paramInfo.length == 0){
					html += '<tr>';
					html += '<td>1</td>';
					html += '<td contentEditable="true"></td>';
					html += '<td id=\"'+GenNonDuplicateID()+'\"  class="piple_type">';
					html += '<div onclick="showProduct(this,\''+subPipInfo[i].pipInfo[0].ID+'\')" class="select"><p><span>请选择</span><span style="display:none">隐藏得</span><a></a></p></div>';
					html += '</td>';
					html += '<td class="opera">';
					html += '<a onclick="subAddtr(\''+eval(i+1)+'\',\''+subPipInfo[i].pipInfo[0].ID+'\')" class="a_add" href="javascript:void(0)"></a>';
					html += '</td>';
					html += '</tr>';
				}else{
						for(var j=0;j<subPipInfo[i].paramInfo.length;j++){
							html += '<tr>';
							html += '<td>'+eval(j+1)+'</td>';
							html += '<td contentEditable="true">'+subPipInfo[i].paramInfo[j].PARAM_NAME+'</td>';
							html += '<td id=\"'+GenNonDuplicateID()+'\"  class="piple_type">';
							html += '<div onclick="showProduct(this,\''+subPipInfo[i].pipInfo[0].ID+'\')" class="select"><p><span>'+subPipInfo[i].paramInfo[j].PARAM_SOURCE_KEY+'</span><span style="display:none">'+subPipInfo[i].paramInfo[j].PARAM_SOURCE+'</span><a></a></p></div>';
							html += '</td>';
							html += '<td class="opera">';
							if(j == 0){
								html += '<a onclick="subAddtr(\''+eval(i+1)+'\',\''+subPipInfo[i].pipInfo[0].ID+'\')" class="a_add" href="javascript:void(0)"></a>';
							}else{
								html += '<a onclick="subAddtr(\''+eval(i+1)+'\',\''+subPipInfo[i].pipInfo[0].ID+'\')" class="a_add" href="javascript:void(0)"></a>';
								html += '<a onclick="subDeltr(this,\''+eval(i+1)+'\')" class="a_del" href="javascript:void(0)"></a>';
							}
							html += '</td>';
							html += '</tr>';
						}
					}
				html += '</table>';
				html += '</li>';
				html += '</ul>';
				html += '</div>';
				html += '</div>';
				$('#subLine').before(html);
			}
				//回显已有的子流水线
//				var html = '';
//				html += '<div id="'+info[0].ID+'" class="line deployediv subLine">';
//				html += '<h6>';
////				html += '<em>'+order+'</em>'+info[0].PIP_NAME+'';
//				html += '<em>'+order+'</em><p>'+info[0].PIP_NAME+'</p>';
//				html += '</h6>';
//				html += '<div class="creatpipe_div">';
//				html += '<ul class="userinfo">';
//				html += '<li><span>创建人:</span>'+info[0].CREATE_USER+'</li>';
//				html += '<li><span>创建时间:</span>'+info[0].CREATE_TIME+'</li>';
//				if(info[0].BUILD_START_TIME!=null){
//					html += '<li><span>最近执行时间:</span>'+info[0].BUILD_START_TIME+'</li>';
//				}else{
//					html += '<li><span>最近执行时间:</span></li>';
//				}
//				if(info[0].BUILD_STATUS == "2"){
//					html += '<li><span>执行状态:</span>成功</li>';
//				}else if(info[0].BUILD_STATUS == "3"){
//					html += '<li><span>执行状态:</span>失敗</li>';
//				}else{
//					html += '<li><span>执行状态:</span>未执行</li>';
//				}
//				html += '</ul>';
//				html += '<div class="piple_edit">';
//				html += '<a href="javascript:void(0)" class="ico20 ico_edit"></a>';
//				html += '<a onclick="delSubPip(\''+info[0].ID+'\')" href="javascript:void(0)" class="ico20 ico_delete"></a>';
//				html += '</div>';
//				html += '</div>';
//				if(order == 1 ){
//					html += '<div style="display:none" class="deployedivcnt">';
//				}else{
//					html += '<div class="deployedivcnt">';
//				}
//				html += '<ul class="creatul1 deployment">';
//				html += '<li class="creatli">';
//				html += '<b><em class="red f14"> * </em>执行参数配置</b>';
//				html += '<table id="table'+order+'" class="config_node subtable">';
//				html += '<tr>';
//				html += '<th width="10%">序号</th>';
//				html += '<th width="40%">名称</th>';
//				html += '<th width="40%">默认值</th>';
//				html += '<th width="10%">操作</th>';
//				html += '</tr>';
//				html += '<tr>';
//				html += '<td>1</td>';
//				html += '<td contentEditable="true"></td>';
//				html += '<td id=\"'+GenNonDuplicateID()+'\"  class="piple_type">';
//				html += '<div onclick="showProduct(this,\''+info[0].ID+'\')" class="select"><p><span>请选择</span><span style="display:none">隐藏得</span><a></a></p></div>';
//				html += '</td>';
//				html += '<td class="opera">';
//				html += '<a onclick="subAddtr(\''+order+'\',\''+info[0].ID+'\')" class="a_add" href="javascript:void(0)"></a>';
//				html += '</td>';
//				html += '</tr>';
//				html += '</table>';
//				html += '</li>';
//				html += '</ul>';
//				html += '</div>';
//				html += '</div>';
//				$('#subLine').before(html);
				
				
				setWindowHeight();			
			}
		},
		error : function(data){
			alert("调用查询流水线信息接口失败！");
		}
	});
}
function subchangeMode(mode){
	if(mode == 1){
		$("#subMoMo1").addClass("current"); 
		$("#subMoMo2").removeClass("current"); 
		$("#subexecutionMode").val("1");  
	}else{
		$("#subMoMo2").addClass("current"); 
		$("#subMoMo1").removeClass("current"); 
		$("#subexecutionMode").val("2"); 
	}
}
var pip_id = getUrlPara("pip_id");
var kai=1;
function updatePip(){
	if(kai){
	var subPip = $("div.subLine");
//	var ddd2 = $("#table1").parents(".subLine");
//	console.log(ddd2[0].id);
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
		url : localhostPath() + "/pipeManage/updatePipeliningGroup.do",
		data : formVal1 + "&projectId=" + projectId + "&subexecutionMode=" + subexecutionMode +
		  "&json=" + param + "&pip_id=" + pip_id,
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
var pip_id = getUrlPara("pip_id");

function returnPip(){
	var projectId = getUrlPara("projectId");
	location.href = "pipe_list.jsp?projectId=" + projectId
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
	var id = a.item(0).id;
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
	var tableName = 'table'+order;
	tr.parentNode.removeChild(tr);
	var len = $('#'+tableName+' tr').length;
    for(var i = 1;i<len;i++){
    	$('#'+tableName+' tr:eq('+i+') td:first').text(i);
    }
    setWindowHeight();
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
	for(var i=0;i<emtotal.length;i++){
		emtotal[i].innerHTML = parseInt(i)+1;
	}
	var divtotal = $("#emTotal>div>div");
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