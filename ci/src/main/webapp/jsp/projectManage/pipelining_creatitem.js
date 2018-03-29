lic()
function lic() {
	var projectName = getUrlPara("projectName");
	var projectDescribe = getUrlPara("projectDescribe");
	var value=getUrlPara("value");
    if(value==2){
    	$("#input").attr("value",projectName);
    	$("#remark").html(projectDescribe);
    	$("#h").html("修改项目");
    }
}


function chak() {
	var projectId = getUrlPara("projectId");
	var projectDescribe = $("#remark").val();
	var projectName= $("#input").val();
	var value=getUrlPara("value");
	if(value==2){
		$.ajax({
			url : localhostPath() +"/projectManage/updataProjectList.do",
			type : "post",
			dataType:"text",
			data :{"name":projectName,"remark":projectDescribe,"id":projectId},
			success : function(data) {
				location.href = localhostPath() + "/jsp/taskManage/pipelining/pipelining_list.jsp?projectId=" + projectId
				$('#projectUl li:first', window.parent.document).remove();
			
				window.parent.getProjectList();
			},
			error:function() {
				alert("完成更新操作失败");
			} 	

		})
	}else{
		var input=$('#input').val();
		var remark=$('#remark').val();
		if(input.length==0){
			alert("输入不能为空");
		}else{
			$.ajax({
				url : localhostPath() +"/projectManage/doInsertProjectList.do",
				type : "post",
				dataType : "json",
				data :{"name":input,"remark":remark},
				success : function(data) {
					$('#firstProject', window.parent.document).html(input + "<span class='arrowdown'></span>");
					$('#firstProject', window.parent.document).attr("projectId",data.projectId);
					location.href = localhostPath() + "/jsp/taskManage/pipelining/pipelining_list.jsp?projectId=" + projectId
					window.parent.getProjectList();
				},
				error:function() {
					alert("完成添加操作失败");
				} 	

			})
		}
	} 	
}
function re() {
	var projectId = getUrlPara("projectId");
	location.href = localhostPath() + "/jsp/taskManage/pipelining/pipelining_list.jsp?projectId=" + projectId	
}