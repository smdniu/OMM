<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>用户管理页面</title>
		<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js">
</script>
	</head>
	<body>
		<!--用户管理内容区-->
		<div id="memberPage" class="user_manage_cont">
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
						<input type="button" value="查询" onclick="selectMember()">
					</c:if>
					<c:if test="${limitRank != null && limitRank.contains('INSERT')}">
						<input type="button" value="新增" onclick="showMemberBounce('add')">
					</c:if>
				</div>
			</div>
			<!--用户表格-->
			<table id="memberTable" class="user">
				<tr>
					<th>id</th>
					<th>用户</th>
					<th>部门</th>
					<th>手机号</th>
					<th>邮箱</th>
					<th>关联角色</th>
					<th>创建时间</th>
					<th>失效时间</th>
					<th>加密类型</th>
					<th>状态</th>
					<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
						<th>删除</th>
					</c:if>
					<c:if test="${limitRank != null && limitRank.contains('UPDATE')}">
						<th>修改</th>
					</c:if>
				</tr>
				<c:forEach items="${members}" var="data">  
					<tr id='${data.pageId}'>
						<td>${data.id}</td>
						<td>${data.name}</td>
						<td>${data.dept}</td>
						<td>${data.tel}</td>
						<td>${data.email}</td>
						<td>${data.roles}</td>
						<td>${data.createTime}</td>
						<td>${data.expiryTime}</td>
						<td>${data.encrypt}</td>
						<td>${data.state}</td>
						<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
							<td>
								<input type="button" value="删除" onclick="deleteMember('${data.id}')">
							</td>
						</c:if>
						<c:if test="${limitRank != null && limitRank.contains('UPDATE')}">
							<td>
								<input type="button" value="修改" onclick="showMemberBounce('update', '${data.id}')">
							</td>
						</c:if>
					</tr>
            	</c:forEach>  
            </table>
			<!--上一页、下一页-->
			<div class="up_down_page">
				<span> <i class="fa fa-fast-backward" aria-hidden="true"></i>
				<input id="pageNumberMember" type="hidden" value="${pageNumber}" />
				<input id="nowPageMember" type="hidden" value="${nowPage}" />
					<i class="fa fa-caret-left" aria-hidden="true"></i> <i
					class="fa fa-caret-right" aria-hidden="true"></i> <i
					class="fa fa-fast-forward" aria-hidden="true"></i> </span>
				<span id="memberPageCount">共${memberCount}条记录，每页${pageNumber}条。${nowPage}/${totalPage}</span>
			</div>
		</div>
<script>
<c:if test="${limitRank != null && limitRank.contains('SELECT')}">
function selectMember(){
	var account = $("#accountMember").val();
	var roleCode = $("#roleCodeMember").val();
	var appName = $("#appNameMember").val();
	$.ajax({
		 type:"POST",
		  url: '<%= path%>/member/selectMem',
		  data:"account=" + account + "&roleCode=" + roleCode + "&appName=" + appName,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  var code = data.code;
			  if(data.code == "00000"){
				 var result = data.result;
				 var limitRank = data.limitRank;
				 //$("#memberTable").html("");
				 var url = "<tr><th>id</th><th>用户名</th><th>部门</th><th>手机号</th><th>邮箱</th><th>关联角色</th><th>创建时间</th><th>失效时间</th><th>加密类型</th><th>状态</th>"
				 if(limitRank.indexOf("DELETE") > 0){
					 url += "<th>删除</th>";
				 }
				 if(limitRank.indexOf("UPDATE") > 0){
					 url += "<th>修改</th>";
				 }
				 url += "</tr>";
				 for(var i = 0; i < result.length; i++){
					 url += "<tr id='" + result[i].pageId + "'><td>" + result[i].id + "</td><td>" + result[i].name + "</td><td>" + result[i].dept + "</td><td>" + result[i].tel + "</td><td>" + result[i].email + "</td><td>" + result[i].roles + "</td><td>" + result[i].createTime + "</td><td>" + result[i].expiryTime + "</td><td>" + result[i].encrypt + "</td><td>" + result[i].state + "</td>";
					 if(limitRank.indexOf("DELETE") > 0){
						 url += "<td><input type='button' value='删除' onclick='deleteMember('" +result[i].id + "')'></td>";
					 }
					 if(limitRank.indexOf("UPDATE") > 0){
						 url += "<td><input type='button' value='修改' onclick='showMemberBounce('update', '" +result[i].id + "')'></td>";
					 }
					 url += "</tr>";
				 }
				 $("#memberTable").html(url);
				 $("#memberPageCount").text("共" + data.memberCount + "条记录，每页" + data.pageNumber + "条。" + data.nowPage + "/" + data.totalPage);
				 $("#pageNumberMember").val(data.nowPage);
				 $("#nowPageMember").val(data.pageNumber);
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
function showMemberBounce(type, index){
	var tab = $("#memberplugin");
	if(tab.length > 0){
		tab.remove();
	}
	$.ajax({
	  type:"POST",
	  url: '<%= path%>/member/merberPage',
	  data:"type=" + type + "&index=" + index,
	  dataType: "html",
	  cache: false,
	  async: false,
	  success: function(html){
		  $("#memberPage").append(html);
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}
</c:if>
<c:if test="${limitRank != null && limitRank.contains('DELETE')}">
function deleteMember(index){
	$.ajax({
		  type:"POST",
		  url: '<%= path%>/member/deleteMem',
		  data:"id=" + index,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  if(data.code == "00000"){
				 $("#" + "member" + index).remove();
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
	</body>
</html>
