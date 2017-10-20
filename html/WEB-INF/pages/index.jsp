
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>运维管理平台首页</title>
	<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
	<link rel="icon" type="image/png" href="<%= path%>/biz/img/logo_blue.png">
	<link href="<%= path%>/biz/css/base.css" rel="stylesheet" type="text/css">
	<link href="<%= path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
	<script src="<%= path%>/biz/js/jquery-1.11.1.min.js"></script>
</head>
<body>
	<!--顶部标题栏-->
	<div class="m_nav">
		<!--标志-->
		<a>
			<div class="logo">
				<img src="<%= path%>/biz/img/logo.png" alt=""> <span>运维管理平台</span>
			</div>
		</a>
		<!--退出-->
		<a href="<%= path%>/user/logout">
			<div class="exit">
				<img src="<%= path%>/biz/img/exit.png" alt=""> <span>退出登录</span>
			</div>
		</a>
		<!--修改密码-->
		<a href="">
			<div class="modify_password">
				<img src="<%= path%>/biz/img/modify_password.png" alt=""> <span>修改密码</span>
			</div>
		</a>
		<!--搜索-->
		<div class="middle">
			<img src="<%= path%>/biz/img/person.png" alt=""> <span>
				<i>欢迎您！</i> ${username}
			</span>
		</div>
	</div>
	<!--tab标签-->
	<div class="tab_nav" id="tab_nav">
		<ul>
			<!--首页标签-->
			<li class="checked" id="a" onclick="transformTab('a')"><img
				src="<%= path%>/biz/img/index60_blue.png" alt=""> <img
				src="<%= path%>/biz/img/index60_gray.png" alt=""> <span>首页</span>
			</li>
		</ul>
	</div>
	<div class="page" id="page">
		<!--首页内容区-->
		<div id="page_a" class="index_cont">
			<ul>
				<c:forEach items="${menus}" var="data">
					<c:choose>
						<c:when test="${'0'.equals(data.menuType)}">
							<!--Redis应用-->
							<li><a onclick="addTab(0, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/Redis100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'1'.equals(data.menuType)}">
							<!--memchace应用-->
							<li><a onclick="addTab(1, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/memchace100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'2'.equals(data.menuType)}">
							<!--jvmcache应用-->
							<li><a onclick="addTab(2, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/jvmcache100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'3'.equals(data.menuType)}">
							<!--db应用-->
							<li><a onclick="addTab(3, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/db100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'4'.equals(data.menuType)}">
							<!--用户管理-->
							<li><a onclick="addTab(4, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/person100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'5'.equals(data.menuType)}">
							<!--角色管理-->
							<li><a onclick="addTab(5, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/role100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'6'.equals(data.menuType)}">
							<!--应用管理-->
							<li><a onclick="addTab(6, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/application100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'7'.equals(data.menuType)}">
							<!--日志管理-->
							<li><a onclick="addTab(7, '${data.path}', '${data.name}')">
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											<img src="<%= path%>/biz/img/log100.png" alt="">
										</div>
									</div>
							</a></li>
						</c:when>
						<c:when test="${'8'.equals(data.menuType)}">
							<!--单点登录-->
							<li>
									<div class="exchange_box">
										<div>
											<p>${data.name}</p>
											
												<select id='select8'>
													<option></option>
													<c:forEach  items="${data.usernameList}" var="un">
														 <option>${un}</option>
													</c:forEach>													
												</select>
												<img onclick="jump(8, '${data.path}', '${data.name}')" src="<%= path%>/biz/img/single_point100.png" alt="">
										</div>
									</div>
									
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
			</ul>
		</div>
	</div>
	<!--成功弹框-->
	<div id="success" class="bounce success_bounce" style="display: none;">
		<div class="black_bg"></div>
		<div class="white_bg">
			<p>提示</p>
			<div class="tip">
				<!--<i class="fa fa-check-circle-o"></i>-->
				<span>操作成功！</span>
			</div>
			<input class="sure" type="button" value="确定" onclick="successClose()">
		</div>
	</div>
	<!--警告弹框-->
	<div id="warn" class="bounce warn_bounce" style="display: none;">
		<div class="black_bg"></div>
		<div class="white_bg">
			<p>提示</p>
			<div class="tip">
				<!--<i class="fa fa-exclamation-circle"></i>-->
				<span>用户未找到关联角色信息！</span>
			</div>
			<input class="sure" type="button" value="确定" onclick="warnClose()">
		</div>
	</div>
<script>
function warnClose(){
	$("#warn span").text("警告提示框");
	$("#warn").css("display", "none");
}

function warnShow(msg){
	$("#warn span").text(msg);
	$("#warn").css("display", "block");
}

function successShow(msg){
	$("#warn span").text(msg);
	$("#warn").css("display", "block");
}
function successClose(){
	$("#warn span").text("成功提示框");
	$("#success").css("display", "none");
}
function transformTab(index){
	var tab = $("#" + index);
	if(tab.length > 0){
		tab.addClass("checked").siblings().removeClass("checked");
		$("#page > div").hide();
   		$('#page_'+index).show();
	}
}
function jump(index,path,name){
	var code="";
	if(index==8){
	//	var code =document.getElementById('select8').value();
		var code =$('#select8 option:selected').text();//选中的文本
	}
	var tab = $("#" + index);
	
	//	$("#tab_nav ul").append('<li id="' + index + '" onclick="transformTab('+ index +')" ><img src="<%= path%>/biz/img/person60_blue.png" alt=""><img src="<%= path%>/biz/img/person60_gray.png" alt=""><span>' + name + '</span></li>');
		$.ajax({
			type:"POST",
			url: '<%=path%>/' + path + '/index',
			data:"code="+code+"&menuName="+name,
			dataType: "json",
			cache: false,
			async: false,
			success: function(data){
				var result = data.result;
				if(data.result=="success"){
					window.open(data.url2des);
				}else{
					warnShow(name + "页面加载失败!");
				}				
		  	},
		  	error:function(){
				warnShow(name + "页面加载失败!");
		  	}
		});
}

function addTab(index, path, name){
	var tab = $("#" + index);
	if(tab.length > 0){
		tab.addClass("checked").siblings().removeClass("checked");
		$("#page > div").hide();
   		$('#page_'+index).show();
	}else{
		$("#tab_nav ul").append('<li id="' + index + '" onclick="transformTab('+ index +')" ><img src="<%= path%>/biz/img/person60_blue.png" alt=""><img src="<%= path%>/biz/img/person60_gray.png" alt=""><span>' + name + '</span></li>');
		$.ajax({
			type:"GET",
			url: '<%=path%>/' + path + '/index',
			dataType: "html",
			cache: false,
			async: false,
			success: function(html){
				$("#page > div").hide();
		    	$("#page").append("<div id='page_"+index+"'>"+html+"</div>");
		    	var tab = $("#" + index);
		    	tab.addClass("checked").siblings().removeClass("checked");
		  	},
		  	error:function(){
				warnShow(name + "页面加载失败!");
		  	}
		});
 	}
}
</script>
</body>
</html>



