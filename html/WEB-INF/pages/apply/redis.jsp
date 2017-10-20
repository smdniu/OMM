<%@ page language="java" import="java.util.*,java.lang.*"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>redis应用</title>
<meta name="viewport"
	content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet"
	type="text/css">
<script src="<%=path%>/biz/js/jquery-1.11.1.min.js"></script>

<script type="text/javascript">
var date = new Date();
var infoDetilVal;
var infoVal;
$(function(){
	infoVal = setInterval(getInfo,4000);
	//setInterval(getInfoDetail,2000);
	//异步请求下拉框ip:port
	$.ajax({
		type:"get",
		url:"<%=path%>/redis/getServers",
		dataType: "json",
		async: true,
		success: function(data){
				var ipSer =  document.getElementById("ip_port");
				var ser = data.server;
				var arr = ser.split(", ");
				for(var i=0;i<arr.length;i++){
				// 创建option元素：
				var opEl = document.createElement("option");
				// 创建文本元素：
				alert(arr[i]);
				var textNo = document.createTextNode(arr[i]);
				// 将文本添加到option中．
				opEl.appendChild(textNo);
				opEl.setAttribute("value",arr[i]);
				// 将option添加到第二个下拉列表中
				ipSer.appendChild(opEl);
				}
			}
		});
	
});

function query(){

	clearInterval(infoDetilVal);
	clearInterval(infoVal);
	var ip_port = $("#ip_port").val();
	if(ip_port==""){
		//查询所有
		$("#infoDetil").hide();
		$("#infos").show();
		<%-- $.ajax({
			type:"get",
			url:"<%=path%>/redis/index",
		}); --%>
		infoVal = setInterval(getInfo,4000);
		
	}else{
		var ip = ip_port.split(":")[0];
		var port = ip_port.split(":")[1];
		var key = $("#key").val();
		if(key==""){
			//查询连接详情
			getInfoDetil(ip,port);
			$("#infos").hide();
			$("#infoDetil").show();
			infoDetilVal = setInterval(getInfoDetil,2000,ip,port);
		}else{
			$.ajax({
				type:"post",
				url:"<%=path%>/redis/getValue",
				dataType: "json",
				data:{'ip':ip,'port':port,'key':key},
				async: true,
				success: function(data){
						warnShow(JSON.stringify(data.resultMsg));
					}
				});
		}

	
	}
	
}

function getInfoDetil(ip,port){
$.ajax({
			type:"post",
			url:"<%=path%>/redis/infoDetil",
			dataType: "json",
			data:{'ip':ip,'port':port},
			async: true,
			success: function(data){
					/* $("#infos").hide();
					$("#infoDetil").show(); */
					$("#info_key").html(ip+":"+port);
					$("#resultMsg").html(data.resultMsg);
					
					if("00000" ==data.resultCode){
						
						$("#content").show();
						$("#pid").html(data.process_id);
						$("#version").html(data.redis_version);
						date.setTime(new Date().getTime()
								- data.uptime_in_seconds * 1000);
						$("#start_time").html(date.toLocaleString('chinese', {
									hour12 : false
								}));
						$("#uptime").html(getTimeStr(data.uptime_in_seconds));
						$("#ops").html(data.instantaneous_ops_per_sec);
						$("#clients").html(data.connected_clients);
						$("#cpu_sys").html(data.used_cpu_sys);
						$("#memory").html(data.used_memory_human);
					}else{
						clearInterval(infoDetilVal);
						$("#content").hide();
					}
				}
			});

}

function getInfo(){
	$.ajax({
		type:"get",
		url: "<%=path%>/redis/info",
		dataType : "json",
		async : true,
		success : function(data) {
				for ( var key in data) {
					var map = data[key];
					$(document.getElementById(key + "_resultMsg")).html(
							map.resultMsg);
					if ("00000" == map.resultCode) {
						$(document.getElementById(key + "_content")).show();
						$(document.getElementById(key + "_pid")).html(
								map.process_id);
						$(document.getElementById(key + "_version")).html(
								map.redis_version);
						date.setTime(new Date().getTime()
								- map.uptime_in_seconds * 1000);
						$(document.getElementById(key + "_start_time")).html(
								date.toLocaleString('chinese', {
									hour12 : false
								}));
						$(document.getElementById(key + "_uptime")).html(
								getTimeStr(map.uptime_in_seconds));
	
						$(document.getElementById(key + "_ops")).html(
								map.instantaneous_ops_per_sec);
						$(document.getElementById(key + "_clients")).html(
								map.connected_clients);
						$(document.getElementById(key + "_cpu_sys")).html(
								map.used_cpu_sys);
						$(document.getElementById(key + "_memory")).html(
								map.used_memory_human);
	
					} else {
						$(document.getElementById(key + "_content")).hide();
					}
				}
			}
		});
	}

	function getTimeStr(time) {
		var str = "";
		var time_temp = time;
		var day = parseInt(time_temp / 60 / 60 / 24);
		if (day > 0) {
			str += day + "天";
		}
		time_temp -= day * 24 * 60 * 60;
		var hour = parseInt(time_temp / 60 / 60);
		if (hour > 0) {
			str += hour + "时";
		}
		time_temp -= hour * 60 * 60;
		var minute = parseInt(time_temp / 60);
		if (minute > 0) {
			str += minute + "分";
		}
		time_temp -= minute * 60;
		var second = time_temp % 60;
		str += second + "秒";
		return str
	}
</script>

</head>
<body>
	<!--Redis应用内容区-->
	<!-- 查询条件 -->
	<div style="margin-top: 10px;margin-left:30px;height:50px; ">
		<form>
			<label style="float: left;width:80px;margin-top: 5px">ip:port</label>
			<select id="ip_port" style="float: left;width: 160px;height: 25px;margin-top: 5px;"  name="ip_port">
				<option value="">-请选择-</option>
			</select>
			<label style="float: left;width:80px;margin-left: 20px;margin-top: 5px">key值 </label>
			<input style="float: left;width: 100px;height: 25px;margin-top: 5px;" id="key" name="key" type="text"> 
			<input style="float: left;margin-top: 5px;margin-left: 20px" id="button" type="button" value="查询" onclick="query()">
		</form>
	</div>
	
	<!-- infoDetil -->
	<div id="infoDetil" style="display: none">
		<div id="title" style="background-color: #00a0e9;">
			Redis Server :
			<nobr id="info_key"></nobr>
			&nbsp;&nbsp;
			<nobr id="resultMsg" style="color:red;"></nobr>
			<nobr id="resultCode" style="disply:none;"></nobr>

		</div>
		
		<div id="content" style="width:100%;height:100px;display:none;">
					<div id="base_info" style="float:left;width:50%">
						<table>
							<tr>
								<td>进程ID:<nobr id="pid"></nobr>
								</td>
								<td>版本号:<nobr id="version"></nobr>
								</td>
							</tr>
							<tr>
								<td>启动时间:<nobr id="start_time"></nobr>
								</td>
								<td>运行时间:<nobr id="uptime"></nobr>
								</td>
							</tr>
							<tr>
								<td>服务器每秒执行的命令数量:<nobr id="ops"></nobr>
								</td>
								<td>已连接客户端的数量:<nobr id="clients"></nobr>
								</td>
							</tr>
							<tr>
								<td>Redis服务器耗费的系统CPU:<nobr id="cpu_sys"></nobr>
								</td>
								<td>Redis 分配的内存总量:<nobr id="memory"></nobr>
								</td>
							</tr>
						</table>
					</div>
				</div>
				
	</div>
	
	<div id="infos">
		<c:forEach items="${info}" var="map">
			<div id="${map.key}" style="margin-top: 10px">
				<div id="${map.key}_title" style="background-color: #00a0e9;">
					Redis Server :
					<nobr>${map.key}</nobr>
					&nbsp;&nbsp;
					<nobr id="${map.key}_resultMsg" style="color:red;">${map.value.resultMsg}</nobr>
				</div>
				<div id="${map.key}_content"
					style="width:100%;height:100px;<c:if test="${ '99999' == map.value.resultCode}">display:none;</c:if>">
					<div id="${map.key}_base_info" style="float:left;width:50%">
						<table>
							<tr>
								<td>进程ID:<nobr id="${map.key}_pid"></nobr>
								</td>
								<td>版本号:<nobr id="${map.key}_version"></nobr>
								</td>
							</tr>
							<tr>
								<td>启动时间:<nobr id="${map.key}_start_time"></nobr>
								</td>
								<td>运行时间:<nobr id="${map.key}_uptime"></nobr>
								</td>
							</tr>
							<tr>
								<td>服务器每秒执行的命令数量:<nobr id="${map.key}_ops"></nobr>
								</td>
								<td>已连接客户端的数量:<nobr id="${map.key}_clients"></nobr>
								</td>
							</tr>
							<tr>
								<td>Redis服务器耗费的系统CPU:<nobr id="${map.key}_cpu_sys"></nobr>
								</td>
								<td>Redis 分配的内存总量:<nobr id="${map.key}_memory"></nobr>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>
