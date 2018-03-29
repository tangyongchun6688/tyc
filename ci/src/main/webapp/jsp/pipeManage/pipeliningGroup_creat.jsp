<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
ResourceBundle resource = ResourceBundle.getBundle("sysConfig"); // 不带properties扩展名的文件名

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
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-2.2.2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pipeManage/pipeliningGroup_creat.js"></script>
</head>
<body>
<header>
   <!--  <div class="menu">
    	<h1 class="logo"><a href="#"></a><s class="arrowdown"></s>
        	<div class="system_change">
            	<div class="arrowup"></div>
                <a class="current" href="index.html">CAAS</a>
                <a href="pipelining_index.html">流水线</a>
            </div>
        </h1>
        <ul>
        	<li class="current"><a href="pipelining_index.html"><s class="ico_project"></s>项目管理</a></li>
            <li><a href="#"><s class="ico_caas"></s>CAAS</a></li>
            <li><a href="#"><s class="ico_atp"></s>自动化测试平台</a></li>
        </ul>
        <div class="user"><s class="ico20 ico_user"></s><span>root</span><a href="#">退出</a></div>
     </div>  -->
</header> 
<!-- <article>
	<div class="pipe_side">
    	<div class="pipe_name">
        	<div class="pipephone"></div>
        	<div class="pipename"><b class="nowrap">CAAS2.0</b><span class="arrowdown"></span></div>
            <div class="pipe_namelist">
                <ul>
                    <li><a href="#">CAS_2.0</a></li>
                    <li><a href="#">CAS_3.0</a></li>
                    <li><a href="#">CAS_4.0</a></li>
                </ul>
            </div>
            <p class="nameopera tc"><a href="pipelining_creatitem.html">新建</a><a href="pipelining_creatitem.html">修改</a><a class="delete" href="pipelining_creatitem.html">删除</a></p>
        </div>
        <div class="pipe_sidemenu">
        	<ul>
            	<li class="current"><a href="#"><s class="ico_side01"></s>流水线</a></li>
            	<li><a href="#"><s class="ico_side02"></s>代码</a></li>
                <li><a href="#"><s class="ico_side03"></s>构建</a></li>
                <li><a href="#"><s class="ico_side04"></s>部署</a></li>
                <li><a href="#"><s class="ico_side05"></s>自动化测试</a></li>
                <li><a href="#"><s class="ico_side06"></s>应用列表</a></li>
                <li><a href="#"><s class="ico_side07"></s>镜像仓库</a></li>
                <li><a href="#"><s class="ico_side08"></s>部署拓扑图</a></li>
            </ul>
        </div>
    </div> -->
    <form id="addPipForm"> 
    <div id="view" class="pipecreat_cnt">
    	<!--<h2 class="h2title">新建流水线<br><span class="f12 blue fn">目前流水线有一个任务</span></h2>-->
        <div class="change_tab">
            <ul class="ul_tab">
                <li onclick="show('1')" class="current">新建流水线</li>
                <li onclick="show('2')">新建流水线组</li>
            </ul>
        </div>
        <div class="pipeline">
        <!-- 流水线 -->
        <!-- <form id="addPipForm"> -->
        <div class="pipeline_group" style="display: block;">
        	<h3 class="h3title">基本信息</h3>
            <ul class="creatpipe">
                <li><b><em class="red f14"> * </em>流水线名称</b><input id="pip_name" name="pip_name" type="text" value="" placeholder=""></li>
					<li><b>定时执行</b>
						<div class="timing_tab">
							<label>	<a href="javascript:void(0);" id="MoMo1" class="current" onclick="changeMode('1')">手动</a> <a href="javascript:void(0);" id="MoMo2" onclick="changeMode('2')">自动</a> </label> 
								<input id="executionMode" type="hidden" value="1"></input>
							<!-- <span><select><option>每天</option><option selected>每周</option><option>每月</option></select></span>
                    <span><select><option>星期一</option><option>星期二</option><option>星期三</option><option>星期四</option><option>星期五</option><option>星期六</option><option>星期日</option></select></span> -->
							<span>每天</span> <span><select id="cron" name="time"><option
										value="00:00">00:00</option>
									<option value="01:00">01:00</option>
									<option value="02:00">02:00</option>
									<option value="03:00">03:00</option>
									<option value="04:00">04:00</option>
									<option value="05:00">05:00</option>
									<option value="06:00">06:00</option>
									<option value="07:00">07:00</option>
									<option value="08:00">08:00</option>
									<option value="09:00">09:00</option>
									<option value="10:00">10:00</option>
									<option value="11:00">11:00</option>
							</select></span> <span>点</span>
						</div></li>
				<li>
                    <b>邮件推送</b>
                    <input id="email" name="email" type="text" value="" placeholder="请输入邮箱地址,例如：example@example.com.cn">
                </li>
                <li>
					<b>备注</b><textarea id="remark" name="remark"></textarea>
                </li>
            </ul>
        	<h3 class="h3title">添加任务</h3>
            <div class="creatpipe_cnt">
                <div class=" creatpipe_tabc">
                	<div class="line creatdiv">
                        <h6><em>1</em>构建</h6>
                        <div class="creatdivcnt">
                        <ul class="creatul1">
                            <!-- <li class="creatli">
		                    	<b><em class="red f14"> * </em> 构建环境</b>
		                        <div id="buildEnvironment" name="buildEnvironment" class="environment">
		                        </div>
		                    </li>
                            <li class="creatli">
		                    	<b><em class="red f14"> * </em> 构建类型</b>
		                        <div id="buildType" name="buildType" class="type">
		                       				<a class="current" href="#"><img src="style/images_pipelin/img2_maven.png" alt=""></a>
		                       				<label><input name="buildType" type="radio" value="2" /><img src="style/images_pipelin/img2_maven.png"/></label> 
		                        </div>
		                    </li> -->
		                    <li class="creatli">
                                <b><em class="red f14"> * </em> 构建环境</b>
                                <div id="buildEnvironment" class="environment">
                                	<!-- <a id="1" href="javascript:void(0);" onclick="changeEnvironment(1)"><s class="ico_docker"></s></a>
                                	<a id="2" href="javascript:void(0);" onclick="changeEnvironment(2)"><s class="ico_python"></s></a>
                                	<a id="3" href="javascript:void(0);" onclick="changeEnvironment(3)"><s class="ico_android"></s></a>
                                	<a id="4" href="javascript:void(0);" onclick="changeEnvironment(4)" class="current" ><s class="ico_java"></s></a> -->
                                </div>
                                <input id="environmentHid"  type="hidden" value=""></input>
                            </li>
                            <li class="creatli">
                                <b><em class="red f14"> * </em> 构建类型</b>
                                <div id="buildType" class="type">
                                	<%-- <a class="current" href="javascript:void(0);"><img src="${pageContext.request.contextPath}/style/images_pipelin/img2_maven.png" alt=""></a> --%>
                                </div>
                            	 <input id="typeHid"  type="hidden" value=""></input>
                            </li>
                            
                            <li class="creatli">
		                    	<b><em class="red f14"> * </em>代码仓库</b>
		                    	<input id="svn_url" name="svn_url" class="inptxt" type="text" value="http://60.247.77.236:57665/svn/yfzx-ityy-develop/Ultra-ME/3.0/RongCloud/trunk/src/server/appStore20151116" placeholder="">
		                    </li>
		                    <li class="creatli">
		                    	<b><em class="red f14"> * </em>svn账号</b>
		                    	<input id="svn_account" name="svn_account" class="inptxt" type="text" value="zhangzhongjian" placeholder="">
		                    </li>
		                    <li class="creatli">
		                    	<b><em class="red f14"> * </em>svn密码</b>
		                    	<input id="svn_password" name="svn_password" class="inptxt" type="text" value="zhangzhongjian" placeholder="">
		                    </li>
                        </ul>
                        </div>
                    </div>
                </div>
                
                <div id="deployReview" class="creatpipe_tabc noeditdiv">
                	<div class="line deployediv">
                    	<h6><em>2</em>部署 <div id="deploy" onclick="deployOnoff()" class="checkswitch "><span class="checkswitch_animbg"></span></div></h6>
                        <input id="deployHid"  type="hidden" value="0"></input>
                        <div class="deployedivcnt">
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
                                <b><em class="red f14"> * </em>选择容器模板</b>
		                        <div class="selectmodel"><a  href="javascript:void(0);" id="templet" class="" onclick="templetChange('1')">tomcat+jdk1.8</a><a href="javascript:void(0);">redis</a><a href="javascript:void(0);">mysql</a></div>
		                        <input id="templetMode" type="hidden" value=""></input>
                            </li>
                            <!-- 执行参数配置 -->
                            <li class="creatli">
                                <b><em class="red f14"> * </em>执行参数配置</b>
                                <table id="table" class="config_node">
                                    <tr>
                                        <th width="10%">序号</th>
                                        <th width="40%">名称</th>
                                        <!-- <th>类型</th> -->
                                        <th width="40%">默认值</th>
                                        <th width="10%">操作</th>
                                    </tr>
                                    <tr>
                                        <td name="sort">1</td>
                                        <!-- <td contentEditable="true" name="type"></td> -->
                                        <!-- 类型 -->
                                        <td contentEditable="true"></td>
                                        <!-- 默认值 -->
                                        <td contentEditable="true"></td>
                                        <td class="opera">
                                            <a onclick="addtr()" class="a_add" href="javascript:void(0)"></a>
                                           <!--  <a onclick="deltr(this)" class="a_del" href="javascript:void(0)"></a> -->
                                        </td>
                                    </tr>
                                </table>
                            </li>
                            <!-- 执行参数配置 -->
                        </ul>
                    </div>
                </div>
                
                <div id="autoReview" class="creatpipe_tabc noeditdiv"><!--noeditdiv样式是控制不可编辑的-->
                	<div class="line autotaskdiv">
                    <h6><em>3</em>自动化测试 <div id="auto" class="checkswitch " onclick="autoOnoff()"><span class="checkswitch_animbg"></span></div> </h6><!--开关打开的样式是在chass="checkswitch"后面追加checkswitch_ed即可-->
                    <input id="autoHid"  type="hidden" value="0"></input> <!-- checkswitch_ed -->
                    <div class="autotaskdivcnt">
                    <p class="autotask"><a href="<%=resource.getString("autoTest_ip") %>:<%=resource.getString("autoTest_port") %>/atp/index.jsp?u=niewei1" target="_blank">新建自动化测试任务</a><a class="fresh" onclick="queryAutomatedTask()">刷新</a></p>
                    <div class="tablerow">
                    <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
                          <th scope="col">&nbsp;</th>
                          <th scope="col">文件名称</th>
                          <th scope="col">文件类型</th>
                        </tr>
                        <tr>
                          <td><s class="radio_box radio_boxs"></s></td>
                          <td>知识库文档.txt</td>
                          <td>Web页面</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                      </tbody>
                    </table> -->
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
            </div>
        </div>
       <!--  <div class="pipebtn tc mb30"><a href="javascript:void(0);" id= "new" onclick="newPip();">确认</a><a href="javascript:void(0);" class="whitebtn" onclick="returnPip();">返回</a></div> -->
    
        </div>
		<!-- </form> -->
        <!-- 流水线组 -->
        <div class="pipeline_group pipeline_second" style="display: none;">
            <h3 class="h3title">基本信息</h3>
            <ul class="creatpipe">
                <li><b><em class="f14 red">*</em> 流水线名称</b>
                    <input id="subpip_name" name="subpip_name" type="text" value="" placeholder="">
                </li>
                <li><b>定时执行</b>
                    <div class="timing_tab">
							<label>	<a href="javascript:void(0);" id="subMoMo1" class="current" onclick="subchangeMode('1')">手动</a> <a href="javascript:void(0);" id="subMoMo2" onclick="subchangeMode('2')">自动</a> </label> 
								<input id="subexecutionMode" type="hidden" value="1"></input>
							<!-- <span><select><option>每天</option><option selected>每周</option><option>每月</option></select></span>
                    <span><select><option>星期一</option><option>星期二</option><option>星期三</option><option>星期四</option><option>星期五</option><option>星期六</option><option>星期日</option></select></span> -->
							<span>每天</span> <span><select id="subcron" name="subtime"><option
										value="00:00">00:00</option>
									<option value="01:00">01:00</option>
									<option value="02:00">02:00</option>
									<option value="03:00">03:00</option>
									<option value="04:00">04:00</option>
									<option value="05:00">05:00</option>
									<option value="06:00">06:00</option>
									<option value="07:00">07:00</option>
									<option value="08:00">08:00</option>
									<option value="09:00">09:00</option>
									<option value="10:00">10:00</option>
									<option value="11:00">11:00</option>
							</select></span> <span>点</span>
						</div>
                </li>
                <li>
                    <b>邮件推送</b>
                    <input id="subemail" name="subemail" type="text" value="" placeholder="请输入邮箱地址,例如：example@example.com.cn">
                </li>
                <li><b>备注</b>
                    <textarea id="subremark" name="subremark"></textarea>
                </li>
            </ul>
            <div class="add_pipeline mt10 mb15"><a onclick="showPipList()" href="javascript:void(0)">添加流水线</a></div>
            <div class="creatpipe_cnt">
                <div id="emTotal" class="creatpipe_tabc">
                  <div id="subLine"></div>
                    <!-- <div class="line deployediv">
                        <h6>
                            <em>2</em>KM3.6构建任务
                        </h6>
                        <div class="creatpipe_div">
                            <ul class="userinfo">
                                <li><span>创建人:</span></li>
                                <li><span>创建时间:</span></li>
                                <li><span>最近执行时间:</span></li>
                                <li><span>执行状态:</span></li>
                            </ul>
                            <div class="piple_edit">
                                <a href="###" class="ico20 ico_edit"></a>
                                <a href="###" class="ico20 ico_delete"></a>
                            </div>
                        </div>
                        <div class="deployedivcnt">
                        <ul class="creatul1 deployment">
                            <li class="creatli">
                                <b><em class="red f14"> * </em>执行参数配置</b>
                                <table class="config_node">
                                    <tr>
                                        <th>#</th>
                                        <th>名称</th>
                                        <th>类型</th>
                                        <th>默认值</th>
                                        <th>操作</th>
                                    </tr>
                                    <tr>
                                        <td>井号</td>
                                        <td>名称</td>
                                        类型
                                        <td>
                                            <div class="select">
                                                <p>
                                                    <span>请选择</span>
                                                    <a href="#"></a>
                                                </p>
                                                <ul class="selectcnt">
                                                    <li>依赖型</li>
                                                    <li>自己</li>
                                                </ul>
                                            </div>
                                        </td>
                                        默认值
                                        <td  class="piple_type">
                                            select_show
                                            <div class="select">
                                                <p>
                                                    <span>请选择</span>
                                                    <a href="#"></a>
                                                </p>
                                                <ul class="selectcnt">
                                                    <li>依赖型</li>
                                                    <li>自己</li>
                                                </ul>
                                            </div>
                                            <input type="text" class="select_input">
                                            <div class="create_pipe" style="display: none;">
                                                <h3 class="h3title">KM3.6构建、部署流水线</h3>
                                                <ul class="border_btm">
                                                   <li>
                                                        <span class="order">1</span>
                                                    <span class="task create_task">构建任务——输出:</span>
                                                    <div class="output">
                                                        <a href="#">war/jar</a>
                                                        <a class="current" href="#">war/jar</a>
                                                    </div>
                                                   </li>
                                                   <li>
                                                        <span class="order">2</span>
                                                    <span class="task create_task">部署任务——输出:</span>
                                                    <div class="output">
                                                        <a href="#">war/jar</a>
                                                        <a class="current" href="#">war/jar</a>
                                                    </div>
                                                   </li>
                                                </ul>
                                                <h3 class="h3title">PASM4.0构建、部署流水线</h3>
                                                <ul>
                                                   <li>
                                                        <span class="order">1</span>
                                                    <span class="task create_task">构建任务——输出:</span>
                                                    <div class="output">
                                                        <a href="#">war/jar</a>
                                                        <a class="current" href="#">war/jar</a>
                                                    </div>
                                                   </li>
                                                   <li>
                                                        <span class="order">2</span>
                                                    <span class="task create_task">部署任务——输出:</span>
                                                    <div class="output">
                                                        <a href="#">war/jar</a>
                                                        <a class="current" href="#">war/jar</a>
                                                    </div>
                                                   </li>
                                                </ul>
                                            </div>
                                        </td>
                                        <td class="opera">
                                            <a class="a_add" href=""></a>
                                            <a class="a_del" href=""></a>
                                        </td>
                                    </tr>
                                </table>
                            </li>
                        </ul>
                        
                        </div>
                    </div> -->
                </div>
                
                
            </div>

        </div>
        </div>
        <div id="new" style="display: block;" class="pipebtn tc mb30 mt30"><a href="javascript:void(0);"  onclick="newPip();">确认</a><a href="javascript:void(0);" class="whitebtn" onclick="returnPip();">返回</a></div>
        <div id="subnew" style="display: none;" class="pipebtn tc mb30 mt30"><a href="javascript:void(0);"  onclick="subnewPip();">确认</a><a href="javascript:void(0);" class="whitebtn" onclick="returnPip();">返回</a></div>
    </div>
    </form>
<!-- </article> -->
<!-- <footer>版权所有 © 2016<span class="ml30">神州泰岳 UltraPower</span></footer> -->
<!-- 弹出层 -->
<div class="popups">
    <div>
        <h3 class="h3title">流水线列表</h3>
        <div class="tablerow piple_list mt10">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th width="5%">
                        <s class=""></s>
                    </th>
                    <th>流水线名称</th>
                    <th width="10%">创建人</th>
                    <th width="22%">创建时间</th>
                    <th width="22%">最近执行时间</th>
                    <th width="10%">执行状态</th>
                </tr>
                <!-- <tr>
                    <td><s class="check_box"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-02 10:52:13</td>
                    <td><span class="success">成功</span></td>
                </tr> -->
                <tbody id="pipInfo"></tbody>
                <!-- <tr>
                    <td><s class="check_box check_boxs"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-06 16:13:51</td>
                    <td><span class="error">失败</span></td>
                </tr>
                <tr>
                    <td><s class="check_box"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-02 10:52:13</td>
                    <td><span class="nodo">未执行</span></td>
                </tr>
                <tr>
                    <td><s class="check_box"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-02 10:52:13</td>
                    <td><span class="nodo">未执行</span></td>
                </tr>
                <tr>
                    <td><s class="check_box"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-02 10:52:13</td>
                    <td><span class="success">成功</span></td>
                </tr>
                <tr>
                    <td><s class="check_box"></s></td>
                    <td class="piple_name">KM3.6构建、部署流水线</td>
                    <td>张三</td>
                    <td>2018-02-02 10:52:13</td>
                    <td>2018-02-02 10:52:13</td>
                    <td><span class="success">成功</span></td>
                </tr> -->
            </table>
            <div class="pipelin_page">
                <ul>
                	<div id="page">
                    <li class="pipelin_pageprev">
                        <a onclick="lPage()" href="javascript:void(0)"> << </a>
                    </li>
                    <li class="current">
                        <a onclick="goPage('1','5')" href="javascript:void(0)">1</a>
                    </li>
                    <!-- <li>
                        <a onclick="goPage('2','5')" href="javascript:void(0)">2</a>
                    </li>
                    <li>
                        <a onclick="goPage('3','5')" href="javascript:void(0)">3</a>
                    </li>
                    <li>
                        <a onclick="goPage('4','5')" href="javascript:void(0)">4</a>
                    </li> -->
                    <li class="pipelin_pagenext">
                        <a onclick="nPage()" href="javascript:void(0)">>></a>
                    </li>
                    </div>
                </ul>
                <span id="pipCount" class="ml10"></span><span class="ml10">第 <span id="pageNow">1</span>/<span id="pageTotal"></span> 页</span>
           	</div>

        </div> 
        <div class="pipebtn tr mt10" id="pipebtn">
            <a href="javascript:void(0)"  class="whitebtn">返回</a><a onclick="pipAdd()" href="javascript:void(0)">确定</a>
         </div>  
    </div>
    
</div>
</body>
</html>

