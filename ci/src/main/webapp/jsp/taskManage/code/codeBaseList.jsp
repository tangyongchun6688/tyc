<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>代码仓库列表</title>
<link href="${pageContext.request.contextPath }/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/caas.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/style/js/effects.js"></script>
</head>
<body>
	<div class="pipelining_list">
		<h2 class="h2title">代码仓库列表</h2>
		<div class="tablerow bdec mb50">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<th scope="col" width="40%">url</th>
				<th scope="col" width="20%">版本库类型</th>
				<th scope="col" width="20%">账户</th>
				<th scope="col" width="20%">密码</th>
				 <tbody id="codeList">
				</tbody> 
			</table>
		</div>
	</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/code/codeBaseList.js"></script>
</html>