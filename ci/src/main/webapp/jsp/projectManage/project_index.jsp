<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>CAAS应用 - 项目 - 首页</title>
<link href="${pageContext.request.contextPath }/plugin/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/style/css/base.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath }/style/css/pipelin.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/style/css/caas.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/style/js/effects.js"></script>

</head>
<body>
<!-- <header>
    <div class="menu">
    	<h1 class="logo"><a href="#"></a><s class="arrowdown"></s>
        	<div class="system_change">
            	<div class="arrowup"></div>
                <a class="current" href="index.html">CAAS</a>
                <a href="pipelining_index.html">流水线</a>
             </div>
        </h1>
        <ul>
        	<li><a href="index.html"><s class="ico_home"></s>概览</a></li>
            <li><a href="OS.html"><s class="ico_os"></s>应用</a></li>
            <li><a href="mirror.html"><s class="ico_mirror"></s>镜像管理</a></li>
            <li><a href="system.html"><s class="ico_system"></s>系统管理</a></li>
            <li><a href="#"><s class="ico_warehouse"></s>仓库管理</a></li>
            <li class="current"><a href="pipelining_index.html"><s class="ico_pipelining"></s>流水线</a></li>
        </ul>
        <div class="user"><s class="ico20 ico_user"></s><span>root</span><a href="#">退出</a></div>
    </div>  
</header> -->

<article class="pipelin_item">
	<div class="pipe_side">
    	<h2>CAAS_2.0</h2>
        <div class="pipe_select">
            <div class="select">
                <p><span>CAAS_2.0</span><a href="#"></a></p>
                <ul class="selectcnt" style="display:block">
                    <li><a href="#">CAS_2.0</a></li>
                </ul>
            </div>
            <a class="btn_creat" href="pipelining_creatitem.html">新建</a>
        </div>
        <div class="pipe_sidemenu">
        	<ul>
            	<li class="current"><a href="#">流水线</a></li>
            	<li><a href="#">代码</a></li>
                <li><a href="#">构建</a></li>
                <li><a href="#">布署</a></li>
            </ul>
        </div>
    </div>
    <div class="pipe_cnt">
    	<!-- <div class="pipebtn"><a href="pipelining_creat.html">新建流水线</a></div> -->
        <div class="pipecnt">
        	<span></span>
            <p>1、项目管理为软件开发团队提供敏捷化项目管理协作服务；编译构建与代码仓库无缝对接，实现获取代码、构建、打包等活动自动化。</p>
            <p>2、部署服务提供可视化、一键式部署服务，实现部署环境标准化和部署过程自动化；</p>
            <p>3、自动化测试提供接口自动化和功能，提高测试质量和效率，实现质量快速反馈；</p>
            <p>4、流水线提供应用程序从构建、部署、测试到发布这整个过程的自动化实现，帮助企业缩短交付周期，提升交付效率。</p>
        </div>
        <div class="pipebtn tc"><a href="pipelining_creatitem.html">新建项目</a></div>
    </div>
</article>
<!-- <footer>版权所有 © 2016<span class="ml30">神州泰岳 UltraPower</span></footer> -->
</body>
</html>
