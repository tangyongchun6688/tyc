<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>CAAS应用 - 流水线</title>
<link href="${pageContext.request.contextPath}/style/css/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>
</head>
<body>
<header>
    <div class="menu">
    	<h1 class="logo"><a href="#" onclick="gotoHome()"></a>
        </h1>
        <ul>
            <li class="current"><a class="ico_pipelining2" href="#"><s class="ico_pipelining1 ico_project"></s>项目管理</a></li>
        	<li><a href="http://192.168.95.87:8090/szty-web" target="_blank"><s class="ico_caas"></s>容器管理</a></li>
            <li><a href="http://192.168.120.102:8888/atp/index.jsp?u=niewei1" target="_blank"><s class="ico_atp"></s>自动化测试管理</a></li>
        </ul>
        <div class="user"><s class="ico20 ico_user"></s><span>root</span><a href="#">退出</a></div>
    </div>  
</header>
	<div id="mainIframeDiv" style="width: 100%; min-height: 600px;">
		<iframe name="my" scrolling="no" id="mainIframe"
			src="${pageContext.request.contextPath }/jsp/taskManage/taskManage_index.jsp"
			style="width: 100%; min-height: 600px;" frameborder="0"
			onload="setMyIframeHeight(this)"></iframe>
	</div>


	<!-- <article>
	<div class="pipe_side">
    	<div class="pipe_name">
        	<div class="pipephone"></div>
        	<b class="pipename">CAAS2.0 <span class="arrowdown"></span></b>
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
            	<li class="current"><a href="#">流水线</a></li>
            	<li><a href="#">代码</a></li>
                <li><a href="#">构建</a></li>
                <li><a href="#">布署</a></li>
            </ul>
        </div>
    </div> -->
    	
    <%-- <div id="mainIframeDiv" style="width: 100%;">
		<iframe name="my" id="mainIframe" src="${pageContext.request.contextPath }/jsp/taskManage/taskManage_index.jsp" width="100%" height="100%" frameborder="0"></iframe>
	</div> --%>
	
	
    <!-- <div class="pipe_cnt">
    	<div class="pipebtn"><a href="pipelining_creat.html">新建流水线</a></div>
        <div class="pipe_empty" style="display:none;">
            <p><span><img src="style/images_pipelin/empty-box.png" alt=""></span>您还没有新建流水线</p>
        </div>
        <div class="pipecnt">
        	<div class="pipetitle">
            	testByliushuixian 002
                <div class="pipetitle_btn"><a class="bluebtn" href="#"><s class="ico20 ico_start"></s>立即执行</a><a class="greybtn" href="#">定时执行</a><a class="greybtn" href="#">编辑</a><a class="greybtn" href="#">删除</a></div>    
            </div>
            <div class="pipeinfo"><span class="mr20">时间：12-22  12:11:23  至  12-22  12:14:45</span><span class="mr20">耗时：00:00:06</span><span class="mr20">邮件已推送给：xx@xx.com，xx@xx.</span></div>
            <ul class="pipestep">
            	<li class="success">
                	<h3><span>1</span>构建 <s></s><a href="#"></a></h3>
                    <div class="state">
                    	<p class="p1"><span class="fl">时间</span><span class="fr">耗时</span></p>
                        <p class="p2"><span class="fl">--</span><span class="fr">--</span></p>
                        <p class="p3"><span class="fl"></span><span class="fr"></span></p>
                    </div>
                    <div class="pipelist">
                    	<p><span>构建环境：</span>java</p>
                        <p><span>打包：</span><a href="#">ci.war</a></p>
                        <p><span>部署环境：</span></p>
                    </div>
                    <div class="pipedetail"><p class="nowrap">执行数据执行数据执行数据执行数据</p><a href="#">执行历史 </a></div>
                </li>
                <li class="fail">
                	<h3><span>2</span>布署 <s></s><a class="noedit" href="#"></a></h3>
                    <div class="state">
                    	<p class="p1"><span class="fl">时间</span><span class="fr">耗时</span></p>
                        <p class="p2"><span class="fl">01-12 13:23:45</span><span class="fr">---</span></p>
                        <p class="p3"><span class="fl"></span><span class="fr"></span></p>
                    </div>
                    <div class="pipelist">
                    	<p><span>构建环境：</span>java</p>
                        <p><span>打包：</span>ci.war</p>
                        <p><span>部署环境：</span><a href="#">访问环境</a></p>
                    </div>
                    <div class="pipedetail"><p class="nowrap">执行数据执行数据执行数据执行数据</p><a href="#">执行历史 </a></div>
                </li>
                <li>
                	<h3><span>3</span>自动化测试 <a href="#"></a></h3>
                    <div class="state"><img src="style/images_pipelin/press.gif" alt="" /></div>
                    <div class="state" style="display:none">
                    	<p class="p1"><span class="fl">时间</span><span class="fr">耗时</span></p>
                        <p class="p2"><span class="fl">01-12 13:23:45</span><span class="fr">23<em class="f12">s</em></span></p>
                        <p class="p3"><span class="fl"></span><span class="fr"></span></p>
                    </div>
                    <div class="pipelist">
                    	<p><span>容器镜像：</span></p>
                        <p><span>服务器IP端口：</span></p>
                        <p><span>部署环境：</span></p>
                    </div>
                    <div class="pipedetail"><p class="nowrap">执行数据执行数据执行数据执行数据</p><a href="#">执行历史 </a></div>
                </li>
                <li>
                	<h3><span>4</span>自动化测试 <a href="#"></a></h3>
                    <div class="state">
                    	<p class="p1"><span class="fl">时间</span><span class="fr">耗时</span></p>
                        <p class="p2"><span class="fl">---</span><span class="fr">---</span></p>
                        <p class="p3"><span class="fl"></span><span class="fr"></span></p>
                    </div>
                    <div class="pipelist">
                    	<p><span>容器镜像：</span></p>
                        <p><span>服务器IP端口：</span></p>
                        <p><span>部署环境：</span></p>
                    </div>
                    <div class="pipedetail"><p class="nowrap"></p><a href="#">执行历史 </a></div>
                </li>
            </ul>
        </div>
        
    </div> -->
</article>
<footer>版权所有 © 2016<span class="ml30">神州泰岳 UltraPower</span></footer>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/mian.js"></script>
</html>
