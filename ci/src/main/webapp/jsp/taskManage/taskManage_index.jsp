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
<article>
	<div class="pipe_side" onmouseleave="hideprojectUl()">
    	<div class="pipe_name">
        	<div class="pipephone"></div>
        		<div class="pipename"><b class="nowrap" id="firstProject"></b><span class="arrowdown"></span></div>
            <div class="pipe_namelist">
                <ul id="projectUl">
                    <!-- <li><a href="#">CAS_2.0</a></li>
                    <li><a href="#">CAS_3.0</a></li>
                    <li><a href="#">CAS_4.0</a></li> -->
                </ul>
            </div>
            <p class="nameopera tc"><a href="javascript:void(0)" url="${pageContext.request.contextPath}/jsp/projectManage/pipelining_creatitem.jsp" onclick="showAtRight(this)">新建</a><a href="javascript:void(0)" onclick="updateProject()">修改</a><a class="delete" href="javascript:void(0)" onclick="deleteProject(this)">删除</a></p>
        </div>
        <div class="pipe_sidemenu">
        	<ul>
            	<li class="current" url="" onclick="showAtRight(this)" id="pipeLi"><s class="ico_side01"></s>流水线</li>
            	<li url="${pageContext.request.contextPath}/jsp/taskManage/code/codeBaseList.jsp" onclick="showAtRight(this)"><s class="ico_side02"></s>代码</li>
                <li url="${pageContext.request.contextPath}/jsp/taskManage/build/build_list.jsp" onclick="showAtRight(this)"><s class="ico_side03"></s>构建</li>
                <li url="${pageContext.request.contextPath}/jsp/taskManage/deploy/deploy_list.jsp" onclick="showAtRight(this)"><s class="ico_side04"></s>部署</li>
                <li url="${pageContext.request.contextPath}/jsp/taskManage/autoTest/autoTest_list.jsp" onclick="showAtRight(this)" ><s class="ico_side05"></s>自动化测试</li>
                <li url="http://192.168.95.87:8090/szty-web/v2/app/index.do" onclick="showAtRight(this)" setHeight="true"><s class="ico_side06"></s>应用列表</li>
                <li url="http://192.168.95.87:8090/szty-web/v2/project/toList.do" onclick="showAtRight(this)" setHeight="true"><s class="ico_side07"></s>镜像仓库</li>
                <li url="http://192.168.95.64:38080/cmdb//relationTopo/specialrelindex.jsp?uqt=U_CI_RELATION&classTitle=ResObject&userAccount=root&relationClass=RelRelation&className=ResHost&resId=15705" onclick="showAtRight(this)" setHeight="true"><s class="ico_side08"></s>部署拓扑图</li>
            </ul>
        </div>
    </div>
    
    <div id="workIframeDiv" class="pipe_cnt" style="min-height: 600px">
		<iframe id="workIframe" src="#" scrolling="no" width="100%" style="min-height: 600px" frameborder="0">
		</iframe>
	</div>
	
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
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/commons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jsp/taskManage/taskManage_index.js"></script>
</html>
