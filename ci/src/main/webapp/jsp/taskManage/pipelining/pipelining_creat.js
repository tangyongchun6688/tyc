
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
			console.log(data);
			var buildType = data.buildType;
			if (data.result = "success") {
				$("#buildType").empty();
				for (var i = 0; i < buildType.length; i++) {
//					$("#buildType").append('<label><input class="radio_box" name="buildType" type="radio" value="' + buildType[i].dicCode + '" /><img src="../../../style/images_pipelin/img2_maven.png"/></label>');
					$("#buildType").append('<a class="current" href="javascript:void(0);"><img src="../../../style/images_pipelin/img2_maven.png" alt=""></a>');
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
		+ "&typeHid=" + typeHid+ "&deployHid=" + deployHid+ "&autoHid=" + autoHid,
		success : function(data) {
			var result = data.result;
			if (result == "true") {
				location.href = "pipelining_list.jsp?projectId=" + projectId
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
	location.href = "pipelining_list.jsp?projectId=" + projectId
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
			console.log(data);
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