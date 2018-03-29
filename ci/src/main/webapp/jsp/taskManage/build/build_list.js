
slall();

function slall(){
$.ajax({
    url :localhostPath() + "/buildManage/selectPro.do",
    type : "post",
    dataType : "json",
  /*   data :{"name":input,"remark":remark}, */
    cache : false,
    async : false,
    success : function(data) {
   
    for(var i=0;i<data.length;i++){
    var sd='<tr><td>'+data[i].buildName+'</td><td>'
            +data[i].createUser+'</td><td>'
            +data[i].createTime+'</td><td>'
            +data[i].buildType+'</td><td>'
            +data[i].buildEnvironment+'</td><td>'
            +data[i].codeRepId+'</td></tr>';
         $("#build1").append(sd) ;                
     
    }
    
    setWindowHeight();
  },
   error:function() {
       	alert("ajax失败");
       } 	
})

}

