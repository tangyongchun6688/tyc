<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge, chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
<meta http-equiv="Expires" content="0"/>
<meta charset="utf-8">
    <base href="<%=basePath%>">
    
    <title>编辑构建类型</title>
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
    <h1>编辑构建类型</h1>
	<form action="<%=basePath%>sdic/updateConsType.do" method="post">
	<div >
		<input type="hidden" name="dicId" value="${consType.dicId }"/>
		构建环境名称：<br /><input type="text" name="dicName" 
			 value="${consType.dicName}"><br /> 
			 
			构建类型编码：<br /><input type="text" name="dicCode" required="required"
			maxLength="2" value="${consType.dicCode}" id="dicCode"><div id="Msg"></div><br /> 
			
			
			 类型图标：<br /><input type="text"
			name="dicIcon"  value="${consType.dicIcon}"><br />
			
			
		排序：<br /><input type="text" name="dicSort" 
			value="${consType.dicSort}"><br /> <br />
		<input type="submit" value="修改">
		</div>
	</form>
  </body>
  
</html>
