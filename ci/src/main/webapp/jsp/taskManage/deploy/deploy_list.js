/**
 * @time 2017-12-12
 * @author tangongchun
 * @description 实现部署模块的CRUD功能
 *
 */
// 初始化部署列表
getDeployList();

///获取部署列表信息
function getDeployList(){
	$.ajax({
		type : "post",
		dataType : "json",
		url : localhostPath() + "/deploy/deployList.do",
		data : {},
		success : function(data){
			var jsonData = data;
			if (jsonData.status == "success") {
				var html = "";
				var list = jsonData.result;
				if (list != undefined) {
					for (var i = 0; i < list.length; i++) {
						html += "<tr>";
						html += "<td>"+list[i].ID+"</td>";
						html += "<td>"+list[i].DEPLOY_NAME+"</td>";
						html += "<td>"+list[i].CREATE_USER+"</td>";
						html += "<td>"+list[i].CREATE_TIME+"</td>";
						html += "<td class='opera'><a class='greybtn' href='#'>修改</a></td>";
						html += "</tr>";
					}
					$("#deployList").html("");
					$("#deployList").html(html);
				}
			}else {
				alert(jsonData.message);
			}
			setWindowHeight();
		},
		error : function(){
			alert("查询部署列表失败！");
		}
	});
}