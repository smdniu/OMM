<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>角色管理</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js">
</script>
	</head>
	<body>
		<!--用户管理内容区-->
		<div id="rolePage" class="user_manage_cont">
			<div class="nav">
				<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
					<div class="float_left">
					    <label for="">用户名：</label>
                    	<input id="accountMember" type="text" name="account" placeholder="用户名或账号">
                    	<label for="">角&nbsp;&nbsp;色：</label>
                    	<input id="roleCodeMember" type="text" name="roleCode" placeholder="角色名或角色CODE">
                    	<label for="">应用名：</label>
                    	<input id="appNameMember" type="text" name="appName" placeholder="应用名或应用CODE">
					</div>
				</c:if>
				<div class="float_right">
					<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
						<input type="button" value="查询" onclick="selectRole()">
					</c:if>
					<c:if test="${limitRank != null && limitRank.contains('INSERT')}">
						<input type="button" value="新增" onclick="showRoleBounce('add')">
					</c:if>
				</div>
			</div>
            <!--角色表格-->
            <table id="role" class="role" >
                <tr>
                    <th>角色编码</th>
                    <th>角色名</th>
                    <th>关联用户</th>
                    <th>关联应用</th>
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
				<c:forEach items="${roles}" var="data">  
					<tr id="${data.pageId}">
						<td>${data.id}</td>
						<td>${data.name}</td>
						<td>${data.relatMember}</td>
						<td>${data.relatApp}</td>
						<td>${data.createTime}</td>
						<td>${data.expiryTime}</td>
						<td>${data.state}</td>
						<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
							<td>
								<input type="button" value="删除" onclick="deleteRole('${data.id}')">
							</td>
						</c:if>
						<c:if test="${limitRank != null && limitRank.contains('UPDATE')}">
							<td>
								<input type="button" value="修改" onclick="showRoleBounce('update', '${data.id}')">
							</td>
						</c:if>
					</tr>
            	</c:forEach>  
            </table>
			<!--上一页、下一页-->
			<div class="up_down_page">
				<span> <i class="fa fa-fast-backward" aria-hidden="true"></i>
				<input id="pageNumberRole" type="hidden" value="${pageNumber}" />
				<input id="nowPageRole" type="hidden" value="${nowPage}" />
					<i class="fa fa-caret-left" aria-hidden="true"></i> <i
					class="fa fa-caret-right" aria-hidden="true"></i> <i
					class="fa fa-fast-forward" aria-hidden="true"></i> </span>
				<span id="rolePageCount">共${memberCount}条记录，每页${pageNumber}条。${nowPage}/${totalPage}</span>
			</div>
		</div>
	</body>
	<script>
<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
function selectRole(){
	var account = $("#accountRole").val();
	var roleCode = $("#roleCodeRole").val();
	var appName = $("#appNameRole").val();
	$.ajax({
		 type:"POST",
		  url: '<%= path%>/role/selectRole',
		  data:"account=" + account + "&roleCode=" + roleCode + "&appName=" + appName,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  var code = data.code;
			  if(data.code == "00000"){
				 var result = data.result;
				 var limitRank = data.limitRank;
				 $("#role").html("");
				 var url = "<tr><th>id</th><th>角色名</th><th>关联用户</th><th>关联应用</th><th>创建时间</th><th>失效时间</th><th>状态</th>";
				 if(limitRank.indexOf("DELETE") > 0){
					 url += "<th>删除</th>";
				 }
				 if(limitRank.indexOf("UPDATE") > 0){
					 url += "<th>修改</th>";
				 }
				 url += "</tr>";
				 for(var i = 0; i < result.length; i++){
					 url += "<tr id='" + result[i].pageId + "'><td>" + result[i].id + "</td><td>" + result[i].name + "</td><td>" + result[i].relatMember + "</td><td>"+ result[i].relatApp + "</td><td>"+ result[i].createTime + "</td><td>"+ result[i].expiryTime + "</td><td>"+ result[i].state + "</td>";
					 if(limitRank.indexOf("DELETE") > 0){
						 url += "<td><input type='button' value='删除' onclick='deleteRole('" +result[i].id + "')'></td>";
					 }
					 if(limitRank.indexOf("UPDATE") > 0){
						 url += "<td><input type='button' value='修改' onclick='showRoleBounce('update', '" + result[i].id + "')'></td>";
					 }
					 url += "</tr>";
				 }
				 $("#role").html(url);
				 $("#rolePageCount").text("共" + data.memberCount + "条记录，每页" + data.pageNumber + "条。" + data.nowPage + "/" + data.totalPage);
				 $("#pageNumberRole").val(data.nowPage);
				 $("#nowPageRole").val(data.pageNumber);
			  } else {
				 warnShow(data.msg);
			  }
		  },
		  error:function(){
			  warnShow("系统错误,请稍后再试!");
		  }
		});
}
</c:if>
<c:if test="${limitRank != null && (limitRank.contains('INSERT') ||  limitRank.contains('UPDATE'))}">
//用户管理页面点击新增按钮出现弹框
function showRoleBounce(type, index){
	var tab = $("#roleplugin");
	if(tab.length > 0){
		tab.remove();
	}
	$.ajax({
	  type:"POST",
	  url: '<%= path%>/role/rolePage',
	  data:"type=" + type + "&index=" + index,
	  dataType: "html",
	  cache: false,
	  async: false,
	  success: function(html){
		  $("#rolePage").append(html);
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}
</c:if>
<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
function deleteRole(index){
	$.ajax({
		 type:"POST",
		  url: '<%= path%>/role/deleteRole',
		  data:"id=" + index,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  if(data.code == "00000"){
				 $("#" + "role" + index).remove();
			  } else {
				 warnShow(data.msg);
			  }
		  },
		  error:function(){
			  warnShow("系统错误,请稍后再试!");
		  }
		});
}
</c:if>
</script>
</html>
