<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>CAAS应用 - 修改流水线组</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/caas.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/pipeManage/pipelining_update.js"></script>
</head>
<body>
<header>
</header>
    <form id="addPipForm">
    <div class="pipe_cnt pipecreat_cnt" style="margin-left: 0px;">
    	<h2 class="h2title">修改流水线组<!--<br><span class="f12 blue fn">目前流水线有一个任务</span>--></h2>
        <div class="pipeline">
        	<h3 class="h3title">基本信息</h3>
            <ul class="creatpipe">
                <li><b><em class="red f14"> * </em>流水线名称</b><input id="subpip_name" name="subpip_name" type="text" value="" placeholder=""></li>
					<li><b>定时执行</b>
						<div class="timing_tab">
							<label><a href="javascript:void(0);" id="subMoMo1" class="current"	onclick="subchangeMode('1')">手动</a> <a href="javascript:void(0);" id="subMoMo2"	onclick="subchangeMode('2')">自动</a> </label> 
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
						</div></li>
				<li>
                    <b>邮件推送</b>
                    <input id="subemail" name="subemail" type="text" value="" placeholder="请输入邮箱地址,例如：example@example.com.cn">
                </li>
                <li>
					<b>备注</b><textarea id="subremark" name="subremark"></textarea>
                </li>
            </ul>
        	<div class="add_pipeline mt10 mb15"><a onclick="showPipList()" href="javascript:void(0)">添加流水线</a></div>
        	<div class="creatpipe_cnt pipeline_second">
                <div id="emTotal" class="creatpipe_tabc">
                  <div id="subLine"></div>
                </div>
            </div>
            </div>
        <div class="pipebtn tc mb30"><a href="javascript:void(0);" onclick="updatePip()">确认</a><a href="javascript:void(0);" class="whitebtn"  onclick="returnPip();">返回</a></div>
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

