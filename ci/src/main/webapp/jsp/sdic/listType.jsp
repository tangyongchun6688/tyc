<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge, chrome=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="Expires" content="0" />
<meta charset="utf-8">

<base href="<%=basePath%>">
<script type="text/javascript" src="style/js/jquery-1.9.1.min.js"></script>
<title>构建类型</title>


</head>

<body>
	<h3>
		<a
			href="<%=basePath%>sdic/toAddConsType.do?parentDicId=${requestScope.consEnvironment.dicId}">添加构建类型</a>
	</h3>
	<h3>
		<a href="<%=basePath%>sdic/queryConsEnvironment.do">返回构建环境页面</a>
	</h3>

	<table border="1">
		<tbody>
		 <tr ><td align="center" colspan="5"><font color="#FF0000">${requestScope.consEnvironment.dicName}</font></td></tr> 
		
			<tr>
                
				<th>构建类型的名称</th>
				<th>构建环境的编码类型</th>
				<th>构建类型的序号</th>
				<th>构建图标</th>
				<th>管理</th>
			</tr>

			<c:if test="${!empty queryConsTypeByPid }">
				<c:forEach items="${queryConsTypeByPid}" var="queryConsTypeByPid">

					<tr>
						<td align="center">${queryConsTypeByPid.dicName}</td>
						<td align="center">${queryConsTypeByPid.dicCode}</td>
						<td align="center">${queryConsTypeByPid.dicSort}</td>
						<td align="center">${queryConsTypeByPid.dicIcon}</td>

						<td><a
							href="<%=basePath%>sdic/getConsType.do?dicId=${queryConsTypeByPid.dicId }">编辑</a>
							<a
							href="<%=basePath%>sdic/delConsType.do?dicId=${queryConsTypeByPid.dicId}&parentDicId=${queryConsTypeByPid.parentDicId}">删除</a>
						</td>
					</tr>


				</c:forEach>
			</c:if>
		</tbody>
	</table>
</body>
</html>
