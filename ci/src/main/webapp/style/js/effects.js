// JavaScript Document
$(document).ready(function() {
    $('#addsever').click(function() {
		$('#addsever_app').show();
	});
	$('.cpopdiv > h2 > a').click(function() {
        $('#addsever_app').hide();
    });
	$('#li1').click(function(e) {
        $('.dn').slideToggle();
    });
	
	$(".pipename > b").click(function(){
        $(".pipe_namelist").slideToggle();
    });

	
	//logo list
	$('.logo').click(function(){
		$('.system_change').slideToggle();
		});
	
	//tab	
	    $('.caas_tab > ul > li').click(tab);
	function tab() {
		$(this).addClass('current').siblings().removeClass('current');
		var tab = $(this).attr('title');
		$('#' + tab).show().siblings().hide();
		};
	//pipeling_creat	
	    $('.creatpipe_tab > li').click(tab);
	function tab() {
		$(this).addClass('current').siblings().removeClass('current');
		var tab = $(this).attr('title');
		$('#' + tab).show().siblings().hide();
		};
});

$(document).ready(function(){
			"use strict";
			$(".plusbtn").click(function(){
				var val=parseInt($(this).siblings("input").val())+1;
				$(this).siblings("input").val(val);
			});
			$(".reducebtn").click(function(){
				var val=parseInt($(this).siblings("input").val())-1;
				$(this).siblings("input").val(val);
			});
}); 

$(document).ready(function() {
	 "use strict";
	var barY 
	// = document.getElementById("yellowprocess").innerHTML;
	var barG 
	// = document.getElementById("greenprocess").innerHTML;
	var barB 
	// = document.getElementById("blueprocess").innerHTML;
	
	$("#yellowbar").animate({
		width:barY
	},2000);
	$("#yellowtag").animate({
		left:barY
	},2000);
	
	$("#greenbar").animate({
		width:barG
	},2000);
	$("#greentag").animate({
		left:barG
	},2000);
	
	$("#bluebar").animate({
		width:barB
	},2000);
	$("#bluetag").animate({
		left:barB
	},2000);
	 
});

$(function(){
	"use strict";
	$(".selectbtn").click(function(){
		if($(this).parent("p").siblings("ul").css("display") == "none"){
			$(this).parent("p").siblings("ul").css("display","block");
			$(this).parent("p").siblings("div").css("display","block");
		}else{
			$(this).parent("p").siblings("ul").css("display","none");
			$(this).parent("p").siblings("div").css("display","none");
		}
		
	});
});

$(function(){
	"use strict";
	$(".ico_close").click(function(){
		$(this).siblings("input").val("");
		
	});
});

$(function(){
	"use strict";
	$(".selected li").click(function(){
		var val = $(this).text();
		$(".display").text(val);		
	});
});

$(function(){
	// tab change
	$(".ul_tab").find("li").on("click",function(){
		var index = $(this).index();
		// console.log(index);
		$(this).parent().find("li").removeClass("current").eq(index).addClass("current");
		$(".pipeline_group").eq(index).show().siblings().hide();
	});
	// show
	$(".piple_type").on("click",function(){
		console.log("enter");
		$(this).find(".create_pipe").toggle();
	})

	$(".add_pipeline").find("a").on("click",function(){
		$(".popups").show();
	})
	$("#pipebtn").find("a").on("click",function(){
		$(".popups").hide();
	})
})
