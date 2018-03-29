<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>部署列表</title>
<link href="${pageContext.request.contextPath }/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/caas.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/style/js/effects.js"></script>
</head>
<body>
	<div class="pipelining_list">
		<h2 class="h2title">部署列表</h2>
		<div class="tablerow bdec mb50">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<th scope="col" width="20%">主键</th>
                <th scope="col" width="25%">部署名称</th>
                <th scope="col" width="20%">创建人</th>
                <th scope="col" width="20%">创建时间</th>
                <th scope="col" width="15%">操作</th>
				<tbody id="deployList">
					<tr>
						<td>1</td>
						<td>任务1</td>
						<td>Ultrapower</td>
						<td>2017-11-12</td>
						<td class="opera"><a class="greybtn" href="#">修改</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/deploy/deploy_list.js"></script>
</html>
