
slall();
setWindowHeight();
function slall(){
	$.ajax({
    url :localhostPath() + "/autoTest/selectPro.do",
    type : "post",
    dataType : "json",
  /*   data :{"name":input,"remark":remark}, */
    cache : false,
    async : false,
    success : function(data) {
    	for(var i=0;i<data.length;i++){
    var sd='<tr><td>'+data[i].testName+'</td><td>'
            +data[i].createUser+'</td><td>'
            +data[i].createTime+'</td><td>'
            +data[i].requestUrl+'</td><td>'
            +data[i].resposeUrl+'</td></tr>';
            $("#build1").append(sd) ;                
            }
    },
   error:function() {
       	alert("ajax失败");
       } 	
})

}
