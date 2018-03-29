<!DOCTYPE HTML>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge, chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="Expires" content="0" />
<meta charset="utf-8">


<title>添加构建类型</title>
<script type="text/javascript" src="<%=basePath%>style/js/jquery-1.9.1.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#dicCode").focusout(function() {
			var dicCode = $("#dicCode").val();
			if (dicCode != null && dicCode != '') {
				checkdicCode(dicCode);
			} 
		});
	});
	function checkdicCode(dicCode) {
		$.ajax({
			url : "${pageContext.request.contextPath}/sdic/validateDicCode.do",
			type : "post",
			dataType : 'JSON',
			data : {
				dicCode : dicCode
			},
			success : function(data) {
				if (data.result == "error") {
					$("#Msg").html("<font color='red'>该编码类型已经存在</font>");
				} else {
					$("#Msg").html("<font color='blue'>该编码类型可以使用</font>");
				}
			}
		});
	}
</script>

</head>

<body>
	<h1>添加构建类型</h1>
	<form action="<%=basePath%>sdic/addConsType.do" method="post">
		<input type="hidden" name="parentDicId" value="${requestScope.parentDicId}"/>
	
		构建类型名称：<br /><input type="text" name="dicName" required="required"
			maxLength="100"  ><br /> 
			构建环境编码：<br /><input type="text" name="dicCode" required="required"
			maxLength="2" id="dicCode"><div id="Msg"></div>
			类型图标：<br /><input type="text"
			name="dicIcon" required="required" maxLength="100"  ><br />
		排序：<br /><input type="text" name="dicSort" required="required" maxLength="4"
			 ><br /> 
		<input type="submit" value="添加">
	</form>
</body>
</html>
