<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>CAAS镜像管理 - 仓库列表</title>
<link href="${pageContext.request.contextPath }/plugin/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/caas.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>


</head>
<body>
<header>

   
    <div class="pipelining_list">
		<h2 class="h2title">自动化测试列表</h2>
		<div class="tablerow bdec mb50">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
        
            <tr>
              <th scope="col">任务名称</th>
              <th scope="col">创建人</th>
              <th scope="col">创建时间</th>
              <th scope="col">执行url</th>
              <th scope="col">回调接口</th>
             <th scope="col" width="15%">操作</th>
            </tr>
            <tbody id="build1">
         
          </tbody>
        </table>
    </div>
</div>
</article>

</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/autoTest/autoTest_list.js"></script>


</html>
