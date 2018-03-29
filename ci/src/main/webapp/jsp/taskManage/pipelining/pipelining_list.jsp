<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
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
    
    
        <!-- 模态框（Modal） -->
<div class="modal fade" id="logMessageModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
        	<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
			</div>
            <div class="modal-body" id="logMessageBody" style="height: 420px;overflow:auto;"></div>
            <!-- <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary">提交更改</button>
            </div> -->
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/webSocket.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/pipelining/pipelining_list.js"></script>
</html>
