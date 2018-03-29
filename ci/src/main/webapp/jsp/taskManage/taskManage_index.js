//左侧菜单栏切换工作区页面
function showAtRight(obj){
   var url = $(obj).attr("url");
   
   $(obj).parent().find(".current").removeClass("current"); 
   $(obj).addClass("current");
   $("#workIframe").attr("src",url); 
   
   var setHeight = $(obj).attr("setHeight");
   if(setHeight == "true"){
	   $("#workIframeDiv").css("height","620px");
	   $("#workIframe").css("height","620px");
	   parent.document.getElementById("mainIframeDiv").style.height = "620px";
	   parent.document.getElementById("mainIframe").style.height = "620px";
   }
}

//获取项目列表
getProjectList();
function getProjectList() {
	$("#projectUl").html("");
	$.ajax({
		url : localhostPath() + "/projectManage/selectPro.do",
		type : "post",
		dataType : "json",
		success : function(data) {
			if(data.length > 0){
				var li = "";
				$("#firstProject").html(data[0].projectName);
				$("#firstProject").attr("projectId",data[0].projectId);
				$("#pipeLi").attr("url",localhostPath() + "/jsp/pipeManage/pipe_list.jsp?projectId=" + data[0].projectId);
				$("#workIframe").attr("src",localhostPath() + "/jsp/pipeManage/pipe_list.jsp?projectId=" + data[0].projectId);
				for (var i = 0; i < data.length; i++) {
					li += "<li class='nowrap' projectId='" + data[i].projectId + "' projectName='"
							+ data[i].projectName
							+ "' onclick='switchProject(this)'><a href='#'>"
							+ data[i].projectName + "</a></li>";
				}
				$(li).appendTo($("#projectUl"));
			}
		},
		error : function(e) {
			alert("获取项目列表失败");
		}

	})
}

//项目列表切换项目信息
function switchProject(obj){
	$(".pipe_namelist").hide();
	var projectId = $(obj).attr("projectId");
	var projectName = $(obj).attr("projectName");
	$("#firstProject").attr("projectId",projectId);
	$("#firstProject").html(projectName);
	$("#pipeLi").attr("url",localhostPath() + "/jsp/pipeManage/pipe_list.jsp?projectId=" + projectId);
	$("#pipeLi").click();
}

//鼠标移除隐藏项目列表
function hideprojectUl(){
	$(".pipe_namelist").hide();
}

//修改项目信息
function updateProject(){
	var projectId = $("#firstProject").attr("projectId");
	$.ajax({
	    url : localhostPath() +"/projectManage/selectByProjectId.do",
	    type : "post",
	    dataType : "json",
	    data :{projectId:projectId},
	    success : function(data) {
	    	var url = localhostPath() + "/jsp/projectManage/pipelining_creatitem.jsp?projectId=" 
	    	+ projectId+"&projectDescribe=" + encodeURI(data.projectDescribe)
	    	+"&projectName=" + encodeURI(data.projectName)
	    	+"&value=2";
	    	$("#workIframe").attr("src",url);
	  },
	   error:function() {
	       	alert("进入更新页面失败");
	       } 	

	})
}
//删除项目信息
function deleteProject(obj){
	 if(confirm("确定要删除该项目以及该项目下的流水线吗?")){
		   var projectId = $("#firstProject").attr("projectId");
		    $.ajax({
			    url : localhostPath() +"/projectManage/delectProjectList.do",
			    type : "post",
			    dataType : "text",
			    data :{projectId:projectId},
			    success : function(data) {
			    	 getProjectList();
			    	/* alert("删除成功");*/
			    },
			   error:function() {
				   alert("删除失败");  
			   } 	

			})	 
	 }
		  else{

		  }
	
}
