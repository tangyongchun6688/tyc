queryCodeList();
function queryCodeList(){
$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath()+"/codeRepositories/queryCodeList.do",
		data : {

		},
		success : function(data) {
			var status = data.status;

			if (status == "success") {

				var codelist = data.result;
				if (codelist.length != 0) {

					$("#codeList").html("");
					var tr = "";
					for (var i = 0; i < codelist.length; i++) {
						tr += "<tr>" + "<td><div class='tdcnt'>"
							+ codelist[i].REP_URL + "</div></td>" + "<td>" + codelist[i].REP_VERSION + "</td><td>"
							+ codelist[i].REP_ACCOUNT_NUMBER + "</td>" + "<td>" + codelist[i].REP_PASSWORD + "</td></tr>"
					}
					$(tr).appendTo($("#codeList"));
				} else {
					alert("列表为空");
				}

			} else {
				alert("查询列表失败 ！");
			}
			 setWindowHeight();
		},
		error : function(data) {
			alert("查询列表失败！");
		}
	});
}