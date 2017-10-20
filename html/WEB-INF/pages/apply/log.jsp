<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>日志页面</title>
		<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js"></script>
		<script src="<%=path%>/biz/js/my97DatePicker/WdatePicker.js"></script>
	</head>
	<body>
		<%@ include file="../warn.jsp" %>
		<!--日志管理内容区-->
        <div class="log_manage_cont">
        	<div class="nav">
				<div class="float_left" id="log_query_condtion">
				    <label for="">用户名：</label>
                   	<input id="account" type="text" name="account" placeholder="用户名/账号">
                   	<label for="">开始时间：</label>
                   	<input id="startTime" type="text" name="startTime" style="width:180px;" class="Wdate" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})">
                   	<label for="">结束时间：</label>
                   	<input id="endTime" type="text" name="endTime" style="width:180px;" class="Wdate" onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})">
				</div>
				<div class="float_right">
					<input type="button" value="查询" onclick="queryLog()">
				</div>
			</div>
            <!--日志表格-->
            <table id="log" class="log" >
                <tr>
                	<th>序号</th>
                    <th>用户CODE</th>
                    <th>用户名</th>
                    <th>路径</th>
                    <th>描述</th>
                    <th>登录IP</th>
                    <th>操作信息</th>
                    <th>日志类型</th>
                    <th>记录时间</th>
				</tr>
				<tbody id="log_data"></tbody>
            </table>
			<!--上一页、下一页-->
			<div class="up_down_page">
				<span> <i class="fa fa-fast-backward" aria-hidden="true" onclick="fristPage()" style="cursor: pointer;"></i>
					   <i class="fa fa-caret-left" aria-hidden="true" onclick="upPage()" style="cursor: pointer;"></i> 
					   <i class="fa fa-caret-right" aria-hidden="true" onclick="downPage()" style="cursor: pointer;"></i>
					   <i class="fa fa-fast-forward" aria-hidden="true" onclick="lastPage()" style="cursor: pointer;"></i>
			    </span>
				<span id="logPageCount">共0条记录，每页10条。1/1</span>
			</div>
        </div>
	</body>
<script>
//每页个数
var LOG_PAGE_COUNT = 10;
//当前页面起始位置
var startIndex = 1;
var logDataCount = 1;

$(function(){
	queryLog();
})
function upPage(){
	startIndex -= LOG_PAGE_COUNT;
	if(startIndex < 1){
		startIndex = 1;
	}
	queryLog();
}
function downPage(){
	if(logDataCount - startIndex > LOG_PAGE_COUNT){
		startIndex += LOG_PAGE_COUNT;
	}
	queryLog();
}
function fristPage(){
	startIndex = 1;
	queryLog();
}
function lastPage(){
	startIndex = Math.floor(logDataCount/LOG_PAGE_COUNT)*LOG_PAGE_COUNT+1;
	queryLog();
}

function queryLog(){
	var account = $("#log_query_condtion #account").val();
	var startTime = $("#log_query_condtion #startTime").val();
	var endTime = $("#log_query_condtion #endTime").val();
	
	$.ajax({
		type:"post",
		url: "<%= path%>/log/info",
		dataType: "json",
		data:{'account':account,'startTime':startTime,'endTime':endTime,'startIndex':startIndex,'endIndex':startIndex+LOG_PAGE_COUNT},
		async: true,
		success: function(data){
			if("00000" == data.resultCode){
				var div = $("#log_data");
				div.html("");
				
				var logData = data.data;
				for(var i=0;i<logData.length;i++){
					var tr = $("<tr id='"+logData[i].ID+"'>"+
							        "<td>"+(startIndex+i)+"</td>"+
							        "<td>"+logData[i].ACCOUNT+"</td>"+
							        "<td>"+logData[i].NAME+"</td>"+
							        "<td>"+logData[i].PATH+"</td>"+
							        "<td>"+logData[i].PATHNAME+"</td>"+
							        "<td>"+logData[i].REQUEST_IP+"</td>"+
							        "<td>"+logData[i].MSG+"</td>"+
							        "<td>"+logData[i].LOG_TYPE+"</td>"+
							        "<td>"+logData[i].DOTIME+"</td>"+
					           "</tr>");
					div.append(tr);
				}
				
				
				//总条数
				logDataCount = data.count;
				//页数
				var pageNum = Math.ceil(logDataCount/LOG_PAGE_COUNT);
				//当前页数
				var nowPageNum = Math.ceil(startIndex/LOG_PAGE_COUNT);
				$("#logPageCount").html("共"+logDataCount+"条记录，每页"+LOG_PAGE_COUNT+"条。"+nowPageNum+"/"+pageNum);
			}else{
				$("#log_data").html("");
				warnShow('错误','查询日志异常');
			}
		},
		error:function(){
			$("#log_data").html("");
			warnShow('错误','查询异常,请稍后再查询!');
		},
	});
}


</script>
</html>
