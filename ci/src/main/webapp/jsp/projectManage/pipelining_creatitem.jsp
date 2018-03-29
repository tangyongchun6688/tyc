<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"  contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>CAAS应用 - 新建项目</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/caas.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script  type="text/javascript" src="${pageContext.request.contextPath}/plugin/jquery/jquery.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
</head>
<body>
<header>
    
</header>
<article>

    <div class="pipe_cnt pipecreat_cnt" style="margin-left:5px">
    	<h4 id="h"  class="h4title">新建项目<br></h4>
        <ul class="creatpipe">
        	<li><b>项目名称</b><input type="text" id="input" placeholder="" value=""/></li>
            <li><b>项目备注</b><textarea id="remark"></textarea></li>
        </ul>
        <div class="pipebtn tc mb30"><a  id="chak" href="javascript:void(0)" value="" onclick="chak()">确认</a>
        <a class="whitebtn" href="javascript:void(0)" onclick="re()">返回</a>
        
       <%--  <a  id="delect" href="${pageContext.request.contextPath}/projectManage/delectProjectList.do?id=f44fbc7fcb5b4d499cc50c3671e77a94"> 删除</a>
        <a  id="updata" href="${pageContext.request.contextPath}/projectManage/updataProjectList.do?id=0cdecad89b1241a088486014195393b4"> 更新</a> --%>
        </div>
   
    </div>
</article>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/projectManage/pipelining_creatitem.js"></script>
</html>

