<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流水线管理列表</title>
<link href="${pageContext.request.contextPath }/plugin/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/plugin/bootstrap/font/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" >
</head>
<body>

<table style="width: 80%;margin-left: 150px;margin-top: 10px;" class="table table-striped table-bordered table-hover table-condensed table-responsive">
  <caption><a data-toggle="modal" data-target="#addJobModal">新建</a></caption>
  <thead>
    <tr>
      <th>任务名称</th>
      <th>构建</th>
      <th>部署</th>
      <th>URL</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody id="jobListTbody">
    
  </tbody>
</table>

<!-- 模态框（Modal） -->
<div class="modal fade" id="addJobModal" tabindex="-1" role="dialog" aria-labelledby="addJobModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="addJobModalLabel">新建任务</h4>
            </div>
            <div class="modal-body">
				<form class="form-horizontal" role="form" id="addJobForm">
				  <div class="form-group">
				    <label for="jobName" class="col-sm-2 control-label">任务名：</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" name="jobName" id="jobName">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="remarks" class="col-sm-2 control-label">备注：</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" name="remarks" id="remarks">
				    </div>
				  </div>
				  <div class="form-group">
				  	<label class="col-sm-12">svn相关信息</label>
				  </div>
				 <div class="form-group">
				    <label for="svn_url" class="col-sm-2 control-label">地址：</label>
				    <div class="col-sm-10">
				      <input type="text" class="form-control" name="svn_url" id="svn_url">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="svn_username" class="col-sm-2 control-label">账号：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="svn_username" id="svn_username">
				    </div>
				    <label for="svn_password" class="col-sm-2 control-label">密码：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="svn_password" id="svn_password">
				    </div>
				  </div>
				  <div class="form-group">
				  	<label class="col-sm-12">tomcat相关信息</label>
				  </div>
				  <div class="form-group">
				    <label for="tomcat_ip" class="col-sm-2 control-label">IP：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="tomcat_ip" id="tomcat_ip">
				    </div>
				    <label for="tomcat_port" class="col-sm-2 control-label">端口号：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="tomcat_port" id="tomcat_port">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="tomcat_username" class="col-sm-2 control-label">账号：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="tomcat_username" id="tomcat_username">
				    </div>
				    <label for="tomcat_password" class="col-sm-2 control-label">密码：</label>
				    <div class="col-sm-4">
				      <input type="text" class="form-control" name="tomcat_password" id="tomcat_password">
				       <input type="hidden" class="form-control" name="sign" id="sign">
				    </div>
				  </div>
				</form>
            	
            
            
            	<!-- <label>任务名：</label><input type="text" id="jobName"/><br/>
            	<label>备注：</label><input type="text" id="remark"/><br/>
            	<label>svn相关信息：</label><br/>
            	<label>地址：</label><input type="text" id="svn_url"/><br/>
            	<label>账号：</label><input type="text" id="svn_username"/><br/>
            	<label>密码：</label><input type="text" id="svn_password"/><br/>
            	<label>tomcat相关信息：</label><br/>
            	<label>IP：</label><input type="text" id="tomcat_ip"/><br/>
            	<label>端口号：</label><input type="text" id="tomcat_port"/><br/>
            	<label>账号：</label><input type="text" id="tomcat_username"/><br/>
            	<label>密码：</label><input type="text" id="tomcat_password"/><br/> -->
			</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick = "creatJob()">提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
<script  type="text/javascript" src="${pageContext.request.contextPath}/plugin/jquery/jquery.js"></script>
<script  type="text/javascript" src="${pageContext.request.contextPath}/plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/jobManage/jobList.js"></script>
</html>