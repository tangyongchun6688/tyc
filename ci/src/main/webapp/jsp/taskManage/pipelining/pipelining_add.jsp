<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>CAAS应用 - 新建流水线</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/caas.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/pipelining/pipelining_add.js"></script>
</head>
<body>
<header>
    <!-- <div class="menu">
    	<h1 class="logo"><a href="#"></a></h1>
        <ul>
        	<li><a href="index.jsp"><s class="ico_home"></s>概览</a></li>
            <li class="current"><a href="pipelining.jsp"><s class="ico_pipelining"></s>流水线</a></li>
        </ul>
        <div class="user"><s class="ico20 ico_user"></s><span>root</span><a href="#">退出</a></div>
    </div>   -->
</header>
<article>
	<!-- <div class="pipe_side">
    	<h2>CAAS_2.0</h2>
        <div class="pipe_select">
            <div class="select">
                <p><span>CAAS_2.0</span><a href="#"></a></p>
                <ul class="selectcnt" style="display:block">
                    <li><a href="#">CAS_2.0</a></li>
                </ul>
            </div>
            <a class="btn_creat" href="pipelining_creatitem.jsp">新建</a>
        </div>
        <div class="pipe_sidemenu">
        	<h3 class="current">流水线</h3>
            <ul>
            	<li><a href="#">代码</a></li>
            </ul>
            <h3>构建</h3>
            <ul>
            	<li><a href="#">布署</a></li>
            </ul>
        </div>
    </div> -->
    <form id="addPipForm">
    <!-- <div class="pipe_cnt pipecreat_cnt"> -->
    	<h4 class="h4title">新建流水线<br><span class="f12 blue fn"></span></h4>
    	
        <ul class="creatpipe">
        	<li><b><em class="red f14"> * </em>流水线名称</b><input id="pip_name" name="pip_name" type="text" value="" placeholder=""></li>
            <li><b>定时执行</b>
            	<div class="timing_tab">
                	<label><a id="MoMo1" class="current" onclick="changeMode('1')">手动</a>
                	<a id="MoMo2" onclick="changeMode('2')">自动</a>
                	</label>
                	<input id="executionMode" type="hidden" value="1"></input>
                    <!-- <span><select><option>每天</option><option selected>每周</option><option>每月</option></select></span>
                    <span><select><option>星期一</option><option>星期二</option><option>星期三</option><option>星期四</option><option>星期五</option><option>星期六</option><option>星期日</option></select></span> -->
                   	<span>每天</span>
                    <span><select id="cron" name="time"><option value="00:00">00:00</option><option value="01:00">01:00</option><option value="02:00">02:00</option><option value="03:00">03:00</option>
                    <option value="04:00">04:00</option><option value="05:00">05:00</option><option value="06:00">06:00</option><option value="07:00">07:00</option>
                    <option value="08:00">08:00</option><option value="09:00">09:00</option><option value="10:00">10:00</option><option value="11:00">11:00</option>
                    </select></span>
                    <span>点</span>
                </div>
            </li>
            <li>
            	<b>邮件推送</b>
                <input id="email" name="email" type="text" value="" placeholder="请输入邮箱地址,例如：example@example.com.cn">
            </li>
            <li><b>备注</b><textarea id="remark" name="remark"></textarea></li>
        </ul>
        
        <div class="creatpipe_cnt">
        	<h3>添加任务</h3>
            <ul class="creatpipe_tab">
            	<li class="current" title="tab1">构建</li>
               <!--  <li title="tab2">代码检验</li> -->
                <li title="tab3">部署</li>
                <li title="tab4">自动化测试</li>
            </ul>
            <div>
            <div class="creatpipe_tabc" id="tab1" style="display:block">
            	<ul class="creatul1">
                	<!-- <li class="creatli">
                    	<b><em class="red f14"> * </em> 任务名称</b>
                    	<input class="inptxt" type="text" value="" placeholder="">
                    </li> -->
                    <li class="creatli">
                    	<b><em class="red f14"> * </em> 构建环境</b>
                        <div id="buildEnvironment" name="buildEnvironment" class="environment">
               						<!--  <a href="#"><s class="ico_docker"></s></a>
               						 <a href="#"><s class="ico_python"></s></a>
               						 <a class="current" href="#"><s class="ico_android"></s></a>
               						 <a href="#"><s class="ico_java"></s></a> -->
               						<!--  <label><input name="environment" type="radio" value="1" /><s class="ico_docker"></s></label> 
									<label><input name="environment" type="radio" value="2" /><s class="ico_python"></s> </label> 
									<label><input name="environment" type="radio" value="3" /><s class="ico_android"></s> </label>  
									<label><input name="environment" type="radio" value="4" /><s class="ico_java"></s> </label> -->
                        </div>
                    </li>
                    <li class="creatli">
                    	<b><em class="red f14"> * </em> 构建类型</b>
                        <div id="buildType" name="buildType" class="type">
                       				<!-- <a class="current" href="#"><img src="style/images_pipelin/img2_maven.png" alt=""></a> -->
                       				<!-- <label><input name="buildType" type="radio" value="2" /><img src="style/images_pipelin/img2_maven.png"/></label>  -->
                        </div>
                    </li>
                    <li class="creatli">
                    	<b><em class="red f14"> * </em>代码仓库</b>
                    	<input id="svn_url" name="svn_url" class="inptxt" type="text" value="http://60.247.77.236:57665/svn/yfzx-ityy-develop/Ultra-ME/3.0/RongCloud/trunk/src/server/appStore20151116" placeholder="">
                    	<!-- <div class="select">
                            <p><span>请选择</span><a href="#"></a></p>
                            <ul class="selectcnt">
                                <li><a href="#">代码仓库</a></li>
                            </ul>
                        </div> -->
                    </li>
                    <li class="creatli">
                    	<b><em class="red f14"> * </em>svn账号</b>
                    	<input id="svn_account" name="svn_account" class="inptxt" type="text" value="zhangzhongjian" placeholder="">
                    </li>
                    <li class="creatli">
                    	<b><em class="red f14"> * </em>svn密码</b>
                    	<input id="svn_password" name="svn_password" class="inptxt" type="text" value="zhangzhongjian" placeholder="">
                    </li>
                   <!--  <li class="creatli">
                    	<b>归档</b>
                        <input class="inptxt" type="text" value="" placeholder="">
                    </li> -->
                </ul>
            </div>
            <div class="creatpipe_tabc" id="tab2" style="display:none">代码检验</div>
            <div class="creatpipe_tabc" id="tab3" style="display:none">
            	<ul class="creatul1 deployment">
                	<!-- <li class="creatli">
                    	<b><em class="red f14"> * </em>名称</b>
                        <input class="inptxt" type="text" value="" placeholder="">
                    </li>
                    <li class="creatli">
                    	<b>描述</b>
                        <textarea class="textareatxt"></textarea>
                    </li>
                    <li class="creatli clearfix">
                    	<b><em class="red f14"> * </em>选择</b>
                        <div class="select w3">
                            <p><span>请选择</span><a href="#"></a></p>
                            <ul class="selectcnt">
                                <li><a href="#">代码仓库</a></li>
                            </ul>
                        </div>
                        <div class="select w7">
                            <p><span>请选择</span><a href="#"></a></p>
                            <ul class="selectcnt">
                                <li><a href="#">代码仓库</a></li>
                            </ul>
                        </div>
                    </li> -->
                    <li class="creatli">
                    	<b>选择模板</b>
                        <div class="selectmodel"><a id="templet" class="" onclick="templetChange('1')">tomcat+jdk1.8</a><a>redis</a><a>mysql</a></div>
                        <input id="templetMode" type="hidden" value=""></input>
                    </li>
                     <!-- <li class="creatli">
                    	<b>tomcat账号</b>
                    	<input id="tomcat_account" name="tomcat_account"  disabled="disabled"  class="inptxt" type="text" value="admin" placeholder="">
                    </li>
                     <li class="creatli">
                    	<b>tomcat密码</b>
                    	<input id="tomcat_password" name="tomcat_password"  disabled="disabled"  class="inptxt" type="text" value="admin" placeholder="">
                    </li> -->
                    <!-- <li class="creatli">
                    	<b><em class="red f14"> * </em>配置节点</b>
                        <div class="setnode"><input type="text" value="" placeholder=""><span></span><input type="text" value="" placeholder=""><span></span><input type="text" value="" placeholder=""></div>
                    </li> -->
                </ul>
              <!--   <div class="highbtn"><a href="#">高级配置</a></div>
                <ul class="creatul1 highset">
                	<li class="creatli">
                    	<b>差异化配置文件路径</b>
                        <textarea class="textareatxt"></textarea>
                    </li>
                    <li class="creatli"><b>配置变量<em class="ico20 ico_helpp dib ml5"></em></b>
                    	<div class="variable">
                        	<p><span><em>序号</em><input type="text" value="" placeholder=""></span>
                               <span><em>KEY</em><input type="text" value="" placeholder=""></span>
                               <span><em>VALUE</em><input type="text" value="" placeholder=""></span>
                               <span><em>描述</em><input type="text" value="" placeholder=""></span>
                               <a class="opera" href="#"></a>
                            </p>
                            <p class="pipebtn"><a href="#">添加bond</a></p>
                        </div>
                    </li>
                </ul> -->
            </div>
            <div class="creatpipe_tabc" id="tab4" style="display:none">
				<!-- <p class="autotask">自动化任务创建链接：<a href="#">http://192.168.88.188:58085/</a><a class="fresh" href="#">刷新</a></p> -->
				<p class="autotask"><a href="http://192.168.120.102:8888/atp" target="_blank">新建自动化测试任务</a><a class="fresh" onclick="queryAutomatedTask()">刷新</a></p>
            	<div class="tablerow">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tbody>
                    <tr>
                      <th scope="col">&nbsp;</th>
                      <th scope="col">文件名称</th>
                      <th scope="col">文件类型</th>
                    </tr>
                   </tbody>
                  <tbody id="automatedTask">
                   <!--  <tr>
                      <td><s class="radio_box radio_boxs"></s></td>
                      <td>知识库文档.txt</td>
                      <td>Web页面</td>
                    </tr> -->
                  </tbody>
                </table>
				</div>
			</div>
            	
            </div>
            
            
        </div>
        <div class="pipebtn tc mb30"><a id= "new" onclick="newPip();">确认</a><a class="whitebtn" onclick="returnPip();">返回</a></div>
    <!-- </div> -->
    </form>
</article>
</body>
</html>

