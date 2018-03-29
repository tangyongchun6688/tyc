
document.getElementById("mainIframe").onload = function() {
	
	//设置mainIframeDiv高度
	var windowHeight = $(window).height();
	var headerHeight = $("header").height();
//	var footerHeight = $("footer").height();
//	$("#mainIframeDiv").css("height",(windowHeight - headerHeight - footerHeight) + "px");
//	$("#mainIframeDiv").css("height",(windowHeight - headerHeight) + "px");
};

function gotoHome(){
	window.location.href = localhostPath();
}