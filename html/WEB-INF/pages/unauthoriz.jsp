<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
    <head>
        <meta charset="utf-8">
        <title>无权访问</title>
        <meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
        <link href="<%= path%>/biz/css/base.css" rel="stylesheet" type="text/css">
        <link href="<%= path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
        <script src="<%= path%>/biz/js/jquery-1.9.1.js"></script>
    </head>
    <body>
        <div id="unauthoriz" class="no_right_page">
            <div class="content">
                <img src="<%= path%>/biz/img/error500.png" alt="">
                <p>你无权访问该应用</p>
            </div>
        </div>
    </body>
</html>


