<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>运维管理平台</title>
    <meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <link href="<%= path%>/biz/css/base.css" rel="stylesheet" type="text/css">
    <link rel="icon" type="image/png" href="<%= path%>/biz/img/logo_blue.png">
    <link rel="stylesheet" href="<%= path%>/biz/css/font-awesome.css">
    <script src="<%= path%>/biz/js/jquery-1.11.1.min.js"></script>
</head>

<body>
    <!--登录页-->
    <div class="login_page">
        <!--内容区-->
        <div class="content">
            <div class="log_box">
                <!--标题-->
                <div class="title">
                    <img src="<%= path%>/biz/img/logo_blue.png" alt="">
                    <span>运维管理平台</span>
                </div>
                <form action="<%= path%>/user/login" method="post">
                    <!--图片-->
                    <img src="<%= path%>/biz/img/technology_bg1.png" alt="">
                    <!--用户名、密码-->
                    <ul>
                        <li>
                            <label for="">用户名：</label>
                            <input id="username" type="text" placeholder="请输入用户名" name="username" required>
                        </li>
                        <li>
                            <label for="">密码:</label>
                            <input id="password" type="password" placeholder="请输入密码" name="password" required>
                        </li>
                    </ul>
                    <div class="clearfix"></div>
                    <!--按钮-->
                    <input id="sub" type="submit" class="log_btn"  value="提交" >
                </form>

                <!--记住密码、忘记密码-->
                <div class="operate">
                    <div class="left" id="remember">
                        <div class=""></div>
                        <span>记住密码</span>
                    </div>
                    <div class="right">忘记密码？</div>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${code != '00000' && msg != null}">
   		<!--警告弹框-->
	    <div id="warn" class="bounce warn_bounce">
	        <div class="black_bg"></div>
	        <div class="white_bg">
	            <p>提示</p>
	            <div class="tip">
	                <span>${msg}</span>
	            </div>
	            <input  class="sure" type="button" value="确定" onclick="warnClose()">
	        </div>
	    </div>
	</c:if>
    <script>
function warnClose(){
	$("#warn").css("display", "none");
}
    
$("#remember").click(function(){
    if ($('#remember div').hasClass('checked')){
        $('#remember div').removeClass('checked');
    }
    else {
        $('#remember div').addClass('checked');
    }
});
    </script>
</body>
</html>


