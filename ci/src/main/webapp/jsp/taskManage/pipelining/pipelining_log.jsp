<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css" media="screen">
    #editor { 
        position: absolute;
        top: 0px;
        right: 0px;
        bottom: 0px;
        left: 0px;
    }
</style>
</head>
<body>
<div id="editor">
${logInfo }
</div>

</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/jquery/jquery.js"></script>
<script src="${pageContext.request.contextPath }/js/common/src/ace.js" type="text/javascript" charset="utf-8"></script>
 <script>
    var editor = ace.edit("editor");
  /*   editor.setTheme("ace/theme/chaos");//chaos     */  
    editor.getSession().setMode("ace/mode/properties"); //praat  properties 
   /*  $(".ace_layer.ace_gutter-layer.ace_folding-enabled").remove(); */
</script> 
<!-- <script>
document.getElementById("editor").innerHTML = document.getElementById("editor").innerHTML.replace("SUCCESS","<font color=red>$1$2$3</font>");
</script> -->
<!-- document.getElementById("editor").innerHTML = document.getElementById("editor").innerHTML.replace("se","<font color=red>$1$2$3</font>");  -->
<script type="text/javascript">
/* (function($){
	$.fn.textSearch = function(str,options){
		var defaults = {
			divFlag: true,
			divStr: " ",
			markClass: "",
			markColor: "red",
			nullReport: true,
			callback: function(){
				return false;	
			}
		};
		var sets = $.extend({}, defaults, options || {}), clStr;
		if(sets.markClass){
			clStr = "class='"+sets.markClass+"'";	
		}else{
			clStr = "style='color:"+sets.markColor+";'";
		}
		
		//å¯¹å‰ä¸€æ¬¡é«˜äº®å¤„ç†çš„æ–‡å­—è¿˜åŽŸ		
		$("span[rel='mark']").each(function() {
			var text = document.createTextNode($(this).text());	
			$(this).replaceWith($(text));
		});
		
		
		//å­—ç¬¦ä¸²æ­£åˆ™è¡¨è¾¾å¼å…³é”®å­—è½¬åŒ–
		$.regTrim = function(s){
			var imp = /[\^\.\\\|\(\)\*\+\-\$\[\]\?]/g;
			var imp_c = {};
			imp_c["^"] = "\\^";
			imp_c["."] = "\\.";
			imp_c["\\"] = "\\\\";
			imp_c["|"] = "\\|";
			imp_c["("] = "\\(";
			imp_c[")"] = "\\)";
			imp_c["*"] = "\\*";
			imp_c["+"] = "\\+";
			imp_c["-"] = "\\-";
			imp_c["$"] = "\$";
			imp_c["["] = "\\[";
			imp_c["]"] = "\\]";
			imp_c["?"] = "\\?";
			s = s.replace(imp,function(o){
				return imp_c[o];					   
			});	
			return s;
		};
		$(this).each(function(){
			var t = $(this);
			str = $.trim(str);
			if(str === ""){
				alert("å…³é”®å­—ä¸ºç©º");	
				return false;
			}else{
				//å°†å…³é”®å­—pushåˆ°æ•°ç»„ä¹‹ä¸­
				var arr = [];
				if(sets.divFlag){
					arr = str.split(sets.divStr);	
				}else{
					arr.push(str);	
				}
			}
			var v_html = t.html();
			//åˆ é™¤æ³¨é‡Š
			v_html = v_html.replace(/<!--(?:.*)\-->/g,"");
			
			//å°†HTMLä»£ç æ”¯ç¦»ä¸ºHTMLç‰‡æ®µå’Œæ–‡å­—ç‰‡æ®µï¼Œå…¶ä¸­æ–‡å­—ç‰‡æ®µç”¨äºŽæ­£åˆ™æ›¿æ¢å¤„ç†ï¼Œè€ŒHTMLç‰‡æ®µç½®ä¹‹ä¸ç†
			var tags = /[^<>]+|<(\/?)([A-Za-z]+)([^<>]*)>/g;
			var a = v_html.match(tags), test = 0;
			$.each(a, function(i, c){
				if(!/<(?:.|\s)*?>/.test(c)){//éžæ ‡ç­¾
					//å¼€å§‹æ‰§è¡Œæ›¿æ¢
					$.each(arr,function(index, con){
						if(con === ""){return;}
						var reg = new RegExp($.regTrim(con), "g");
						if(reg.test(c)){
							//æ­£åˆ™æ›¿æ¢
							c = c.replace(reg,"â™‚"+con+"â™€");
							test = 1;
						}
					});
					c = c.replace(/â™‚/g,"<mark "+clStr+">").replace(/â™€/g,"</mark>");
					a[i] = c;
				}
			});
			//å°†æ”¯ç¦»æ•°ç»„é‡æ–°ç»„æˆå­—ç¬¦ä¸²
			var new_html = a.join("");
			
			$(this).html(new_html);
			
			if(test === 0 && sets.nullReport){
				alert("æ²¡æœ‰æœç´¢ç»“æžœ");	
				return false;
			}
			
			//æ‰§è¡Œå›žè°ƒå‡½æ•°
			sets.callback();
		});
	};
})(jQuery);

$("body").textSearch("世界杯");
$("body").textSearch("a"); */
</script>
</html>