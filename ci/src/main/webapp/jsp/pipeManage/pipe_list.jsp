<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流水线列表</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
</head>
<body>
	<div class="">
    	<div class="pipebtn" id="addPipBtn"><a href='javascript:void(0)' onclick="gotoAddPipeline()">新建流水线</a></div>
        <div class="pipe_empty" id="NoPipeDiv" style="display:none;">
            <p><span><img src="${pageContext.request.contextPath}/style/images_pipelin/empty-box.png" alt=""></span>您还没有新建流水线</p>
        </div>
        <div id="pipeListDiv" style="margin-bottom: 30px;">
        </div>
    </div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/webSocket.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pipeManage/pipe_list.js"></script>
</html>