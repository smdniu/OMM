<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>应用配置页面</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js">
</script>
	</head>
	<body>
		<!--应用管理内容区-->
        <div id="applyPage" class="user_manage_cont">
			<div class="nav">
				<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
					<div class="float_left">
					    <label for="">用户名：</label>
                    	<input id="accountApply" type="text" name="account" placeholder="用户名或账号">
                    	<label for="">角&nbsp;&nbsp;色：</label>
                    	<input id="roleCodeApply" type="text" name="roleCode" placeholder="角色名或角色CODE">
                    	<label for="">应用名：</label>
                    	<input id="appNameApply" type="text" name="appName" placeholder="应用名或应用CODE">
					</div>
				</c:if>
				<div class="float_right">
					<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
						<input type="button" value="查询" onclick="selectApply()">
					</c:if>
					<c:if test="${limitRank != null && limitRank.contains('INSERT')}">
						<input type="button" value="新增" onclick="showApplyBounce('add')">
					</c:if>
				</div>
			</div>
			<!--应用表格-->
            <table id="apply" class="apply" >
				<tr>
					<th>id</th>
                    <th>应用名</th>
                    <th>归属角色</th>
                    <th>应用类型</th>
                    <th>应用路径</th>
                    <th>创建时间</th>
                    <th>失效时间</th>
                    <th>状态</th>
					<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
						<th>删除</th>
					</c:if>
					<c:if test="${limitRank != null && limitRank.contains('UPDATE')}">
						<th>修改</th>
					</c:if>
				</tr>
				<c:forEach items="${applys}" var="data">  
					<tr id='${data.pageId}'>
						<td>${data.id}</td>
						<td>${data.name}</td>
						<td>${data.roles}</td>
						<td>${data.menuType}</td>
						<td>${data.path}</td>
						<td>${data.createTime}</td>
						<td>${data.expiryTime}</td>
						<td>${data.state}</td>
						<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
							<td>
								<input type="button" value="删除" onclick="deleteApply('${data.id}')">
							</td>
						</c:if>
						<c:if test="${limitRank != null && limitRank.contains('UPDATE')}">
							<td>
								<input type="button" value="修改" onclick="showApplyBounce('update', '${data.id}', '${data.menuType}')">
							</td>
						</c:if>
					</tr>
            	</c:forEach>  
            </table>
			<!--上一页、下一页-->
			<div class="up_down_page">
				<span> <i class="fa fa-fast-backward" aria-hidden="true"></i>
				<input id="pageNumberApply" type="hidden" value="${pageNumber}" />
				<input id="nowPageApply" type="hidden" value="${nowPage}" />
					<i class="fa fa-caret-left" aria-hidden="true"></i> <i
					class="fa fa-caret-right" aria-hidden="true"></i> <i
					class="fa fa-fast-forward" aria-hidden="true"></i> </span>
				<span id="applyPage">共${applyCount}条记录，每页${pageNumber}条。${nowPage}/${totalPage}</span>
			</div>
			<c:if test="${limitRank != null && limitRank.contains('INSERT')}">
				<!--弹框-->
				<div class="bounce" style="display: none;">
					<div class="black_bg"></div>
					<div class="white_bg"></div>
				</div>
			</c:if>
		</div>
	</body>
	<script>
<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
function selectApply(){
	var account = $("#accountApply").val();
	var roleCode = $("#roleCodeApply").val();
	var appName = $("#appNameApply").val();
	$.ajax({
		 type:"POST",
		  url: '<%= path%>/apply/selectApply',
		  data:"account=" + account + "&roleCode=" + roleCode + "&appName=" + appName,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  var code = data.code;
			  if(data.code == "00000"){
				 var result = data.result;
				 var limitRank = data.limitRank;
				 $("#apply").html("");
				 var url = "<tr><th>id</th><th>应用名</th><th>归属角色</th><th>应用类型</th><th>应用路径</th><th>创建时间</th><th>失效时间</th><th>状态</th><th>备注</th>";
				 if(limitRank.indexOf("DELETE") > 0){
					 url += "<th>删除</th>";
				 }
				 if(limitRank.indexOf("UPDATE") > 0){
					 url += "<th>修改</th>";
				 }
				 url += "</tr>";
				 for(var i = 0; i < result.length; i++){
					 url += "<tr id='" + result[i].pageId + "'><td>" + result[i].id + "</td><td>" + result[i].name + "</td><td>" + result[i].roles + "</td><td>" + result[i].menuType + "</td><td>" + result[i].path + "</td><td>" + result[i].createTime + "</td><td>" + result[i].expiryTime + "</td><td>" + result[i].state + "</td>";
					 if(limitRank.indexOf("DELETE") > 0){
						 url += "<td><input type='button' value='删除' onclick='deleteApply('" +result[i].id + "')'></td>";
					 }
					 if(limitRank.indexOf("UPDATE") > 0){
						 url += "<td><input type='button' value='修改' onclick='showApplyBounce('update', '" +result[i].id + ", '" + result[i].menuType + "')'></td>";
					 }
					 url += "</tr>";
				 }
				 $("#apply").html(url);
				 $("#applyPage").text("共" + data.applyCount + "条记录，每页" + data.pageNumber + "条。" + data.nowPage + "/" + data.totalPage);
				 $("#pageNumberApply").val(data.nowPage);
				 $("#nowPageApply").val(data.pageNumber);
			  } else {
				 $("#warn span").text(data.msg);
				 $("#warn").css("display", "block");
			  }
		  },
		  error:function(){
			  $("#warn span").text("系统错误,请稍后再试!");
			  $("#warn").css("display", "block");
		  }
		});
}
</c:if>
<c:if test="${limitRank != null && (limitRank.contains('INSERT') ||  limitRank.contains('UPDATE'))}">
//应用管理页面点击新增按钮出现弹框
function showApplyBounce(type, index, menuType){
	var tab = $("#applyplugin");
	if(tab.length > 0){
		tab.remove();
	}
	$.ajax({
	  type:"POST",
	  url: '<%= path%>/apply/applyPage',
	  data:"type=" + type + "&index=" + index + "&menuType=" + menuType,
	  dataType: "html",
	  cache: false,
	  async: false,
	  success: function(html){
		  $("#applyPage").append(html);
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}
</c:if>
<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
function deleteApply(index){
	$.ajax({
		 type:"POST",
		  url: '<%= path%>/apply/deleteApply',
		  data:"id=" + index,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  if(data.code == "00000"){
				 $("#" + "apply" + index).remove();
			  } else {
				 $("#warn span").text(data.msg);
				 $("#warn").css("display", "block");
			  }
		  },
		  error:function(){
			  $("#warn span").text("系统错误,请稍后再试!");
			  $("#warn").css("display", "block");
		  }
		});
}
</c:if>
</script>
</html>
