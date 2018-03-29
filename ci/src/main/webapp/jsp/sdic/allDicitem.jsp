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
    <script type="text/javascript" src="style/js/jquery-1.9.1.min.js"></script>
    <title>构建环境</title>
    
	<script type="text/javascript">
	function del(dicId){
		$.get("<%=basePath%>sdic/delConsEnvironment.do?dicId=" + dicId,
		function(data){
			 
			if("false" == data.result){
				alert("删除失败");
				
			}else{
				alert("删除成功");
				window.location.reload();
			}
		});
	}
</script>
  </head>
  
  <body>
    
	<table  align="center"  border="1" >
	<h2  align="center"><a href="<%=basePath%>sdic/toAddConsEnvironment.do">添加构建环境</a></h2>
		<tbody>
			<tr> 
				<th>构建环境的名称</th>
				<th>构建环境的编码类型</th>
				<th>构建环境的序号</th>
				<th>构建图标</th>
				<th>管理</th>
			</tr>
			<c:if test="${!empty ConsEnvironmentList }">
				<c:forEach items="${ConsEnvironmentList}" var="ConsEnvironment">
					<tr>
						<td>${ConsEnvironment.dicName}</td>
						<td>${ConsEnvironment.dicCode}</td>
						<td>${ConsEnvironment.dicSort}</td>
						<td>${ConsEnvironment.dicIcon}</td>
						 
						<td>
							<a href="<%=basePath%>sdic/getConsEnvironment.do?dicId=${ConsEnvironment.dicId }">编辑</a>
							<a href="javascript:del('${ConsEnvironment.dicId }')">删除</a>
							<a href="<%=basePath%>sdic/queryConsTypeByPid.do?dicId=${ConsEnvironment.dicId }">构建类型管理</a>
						</td>
					</tr>				
				</c:forEach>
			</c:if>
		</tbody>
	</table>
  </body>
</html>
