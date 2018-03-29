/**
 * 获取当前项目地址
 */
var localhostPath2 = function(){
	//获取当前网址，如： http://192.168.26.127:8080/continuedBuild/jobManage/gotoJobList.asp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： /continuedBuild/jobManage/gotoJobList.asp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://192.168.26.127:8080
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/continuedBuild
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
};



var websocket = null;
var usernoRand = Math.ceil(Math.random()*10000000000);

//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
	var localhost = localhostPath2();
	var websocketPath = "ws" + localhost.substr(localhost.indexOf(':')) + "/websocket/" + usernoRand;
	websocket = new WebSocket(websocketPath);
} else {
	alert('当前浏览器 Not support websocket')
}
//连接发生错误的回调方法
websocket.onerror = function() {
//	alert("WebSocket连接发生错误");
//	setMessageInnerHTML("WebSocket连接发生错误");
};
//连接成功建立的回调方法
websocket.onopen = function() {
	//setMessageInnerHTML("WebSocket连接成功");
}
//接收到消息的回调方法
//websocket.onmessage = function(event) {
//	setMessageInnerHTML(event.data);
//}
//连接关闭的回调方法
websocket.onclose = function() {
	//setMessageInnerHTML("WebSocket连接关闭");
}
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	closeWebSocket();
}
//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
	//页面测试显示区域id为message
	//document.getElementById('message').innerHTML += innerHTML + '<br/>';
}
//关闭WebSocket连接
function closeWebSocket() {
	websocket.close();
}
//发送消息
function send() {
//	var message = document.getElementById('text').value;//测试文本框中数据
//	websocket.send(message);
}