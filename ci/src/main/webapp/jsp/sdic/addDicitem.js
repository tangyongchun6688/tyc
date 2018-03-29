$(document).ready(function() {
		$("#dicCode").focusout(function() {
			var dicCode = $("#dicCode").val();
			if (dicCode != null && dicCode != '') {
				checkName(dicCode);
			} 
		});
	});
	function checkName(dicCode) {
		$.ajax({
			url : "${pageContext.request.contextPath}/sdic/validateDicCode.do",
			type : "post",
			dataType : 'JSON',
			data : {
				dicCode : dicCode
			},
			success : function(data) {
				if (data.result == "error") {
					$("#Msg").html("<font color='red'>该编码类型已经存在</font>");
				} else {
					$("#Msg").html("<font color='blue'>该编码类型可以使用</font>");
				}
			}
		});
	}