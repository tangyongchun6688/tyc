<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>流水线执行历史</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
</head>
<body>
    <div class="pipehistory">
    	<div class="pipebtn ">执行历史<a href="javascript:void(0)" onclick="gotoPipList()">返回</a></div>
        <div class="pipe_empty" id="NoPipeDiv" style="display:none;">
            <p><span><img src="${pageContext.request.contextPath}/style/images_pipelin/empty-box.png" alt=""></span>您还没有执行过流水线</p>
        </div>
        <div id="pipeHisListDiv" style="margin-bottom: 30px;">
        	
        </div>
    </div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/webSocket.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pipeManage/pipe_list.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pipeManage/pipe_history.js"></script>
</html>
