<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>memcache页面</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet"
			type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet"
			type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js"></script>
		<script src="<%=path%>/biz/js/echarts.min.js"></script>
		<style type="text/css">
			table td{
				border: solid 1px #ddd;font-size: 0.08rem;text-align: left;line-height: 1.5;
			}
			.content_bg{
				width: 40%;height: 50%;background: #fff;position: absolute;left:50%;top: 40%;transform: translate(-50%,-50%);border-radius: 0.05rem;
			}
	    </style>

</head>
<script>
var gauge = {};
var category = {};
var date = new Date()
</script>
<body>
	 <!--memchace应用内容区-->
     <div>
        <c:forEach items="${info}" var="map">  
		<div id="${map.key}" style="margin-top: 10px">
			<div id="${map.key}_title" style="background-color:#00a0e9;font-size:15px;padding:8px;" >Memcache Server : 
					<nobr>${map.key}</nobr>&nbsp;&nbsp;
					<nobr id="${map.key}_resultMsg" style="color:red;">${map.value.resultMsg}</nobr>
					<nobr <c:if test="${ '99999' == map.value.resultCode}">style="display:none"</c:if>>
						<input type="button" onclick="showQuery('${map.key}')" style="float:right;margin-right: 20px;background-color:transparent;
    width:50px;
    border:white 1px solid;
    line-height:1.5;
    color:white;" value="查询"/>
					</nobr>
			</div>
			<div id="${map.key}_content" style="margin:20px 0;
    padding:0 0 0 15px;width:100%;height:100px;<c:if test="${ '99999' == map.value.resultCode}">display:none;</c:if>" >
					<div id="${map.key}_base_info" style="float:left;width:30%">
						<table style="font-size:13px;" >
							<tr>
								<td>进程ID:<nobr id="${map.key}_pid"></nobr></td>
								<td>版本号:<nobr id="${map.key}_version"></nobr></td>
							</tr>
							<tr>
								<td>启动时间:<nobr id="${map.key}_start_time"></nobr></td>
								<td>运行时间:<nobr id="${map.key}_uptime"></nobr></td>
							</tr>
							<tr>
								<td>启动内存:<nobr id="${map.key}_limit_maxbytes"></nobr></td>
								<td>保存次数:<nobr id="${map.key}_cmd_set"></nobr></td>
							</tr>
							<tr>
								<td>当前item数:<nobr id="${map.key}_curr_items"></nobr></td>
								<td>删除的item数:<nobr id="${map.key}_evictions"></nobr></td>
							</tr>
						</table>
					</div>
					<div style="float:left;width:300px;height:100px;" id="${map.key}_gauge">
					</div>
					<div style="float:left;width:50%;height:100px;" id="${map.key}_category">
					</div>
			</div>
		</div>
		<script>
			gauge['${map.key}'] = echarts.init(document.getElementById('${map.key}_gauge'));
			category['${map.key}'] = echarts.init(document.getElementById('${map.key}_category'));
		</script>
		</c:forEach>  
	</div>
	<div id="memcache_query" class="bounce" style="display:none;">
		<div class="black_bg"></div>
	    <div class="content_bg" style="background-color: #00a0ea;">
	    	<div style="clear:both">
	    		<p style="float:left;
 font-size:20px;
 padding:0 10px;
 line-height:2;
 color:white;">memcache查询</p>
	    		<a style="float:right;
 font-size:20px;
 padding:0 10px;
 line-height:2;
 color:white;" href="javaScript:void(0)" onclick="closeQuery()">关闭</a>
	    	</div>
	        <div style="clear:both">
	        	<table>
	        		<tr>
	        			<td>key:</td>
	        			<td>
	        				<form id="memcache_query_form" style="float:left;">
	        					<input id="key" name="key" type="text" placeholder="请输入key值" style="font-size:0.08rem;line-height:1.5;color:#fff;background-color:transparent;text-align:center;margin:0.01rem auto;padding:0 0.1rem;border:#efefef 1px solid;"/>
	        					<input id="ip" name="ip" style="display:none;"/>
	        					<input id="port" name="port" style="display:none;"/>
	        				</form>
	        				<input style="float:right;background-color:transparent;color:black;border:#00a0ea 1px solid;margin-right:5px;" class="sure" type="button" value="查询" onclick="query()"/>
	        			</td>
	        		</tr>
	        		<tr>
	        			<td>value:</td>
	        			<td>
	        				<textarea id="memcache_query_result" rows="17" cols="85"></textarea>
	        			</td>
	        		</tr>
	        	</table>
	       </div>
	       
	    </div>
	</div>
	
</body>
<script>
$(function(){
	setInterval(getInfo,2000);
})

gauge_option = {
    tooltip : {
        formatter: "{b} : {c}%"
    },
    series : [
        {
            name: '内存使用情况',
            center: ['70%', '65%'],
            type: 'gauge',
            min: 0,
            max: 100,
            splitNumber: 5,
            radius: '120%',
            axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                    width: 1
                }
            },
            axisTick: {            // 坐标轴小标记
                length: 3,        // 属性length控制线长
                lineStyle: {       // 属性lineStyle控制线条样式
                    color: 'auto'
                }
            },
            splitLine: {           // 分隔线
                length: 5,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                    color: 'auto'
                },
            	fontSize:6
            },
            pointer: {
                width:2
            },
            title : {
            	offsetCenter: [0, '-15%'],
                fontWeight: 'bolder',
                fontSize: 8,

            },
            detail : {
                fontWeight: 'bolder',
                fontSize:8
            },
            data:[{value: 0, name: '内存使用率'}]
        },
        {
            name: 'item数量占比',
            type: 'gauge',
            center: ['25%', '65%'],    // 默认全局居中
            radius: '120%',
            min:20,
            max:120,
            splitNumber:5,
            axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                    width: 1
                }
            },
            axisTick: {            // 坐标轴小标记
                length:3,        // 属性length控制线长
                lineStyle: {       // 属性lineStyle控制线条样式
                    color: 'auto'
                }
            },
            splitLine: {           // 分隔线
                length:5,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                    color: 'auto'
                }
            },
            pointer: {
                width:2
            },
            title: {
                offsetCenter: [0, '-15%'],       // x, y，单位px
                fontSize:8
            },
            detail: {
                fontWeight: 'bolder',
                fontSize:8
            },
            data:[{value: 0,name: 'item数量占比'}]
        }
    ]
};
function setGauge(id,curr_items,total_items,bytes,limit_maxbytes){
	gauge_option.series[0].data[0].value = (bytes*100/limit_maxbytes).toFixed(2) - 0;
	if(curr_items == 0){
		gauge_option.series[1].data[0].value = 0;
	}else{
		gauge_option.series[1].data[0].value = (total_items*100/curr_items).toFixed(2) - 0;
	}
	
	gauge[id].setOption(gauge_option,true);
}

category_option = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#283b56'
            }
        }
    },
    grid: {
        left: '4%',
        right: '4%',
        bottom: '0%',
        top: '5%',
        containLabel: true
    },
    legend: {
        data:[ '连接数','访问量'],
        textStyle:{
            fontSize:6
        }
    },
    xAxis: [
        {
            type: 'category',
            boundaryGap: true,
            axisLabel:{
            	textStyle:{ fontSize:6}
            },
            data: (function (){
                var res = [];
                var len = 10;
                while (len > 0) {
                	res.push("");
                	len--;
                }
                return res;
            })()
        },
        {
            type: 'category',
            boundaryGap: true,
            axisLabel:{
            	textStyle:{ fontSize:6}
            },
            data: (function (){
                var res = [];
                var len = 10;
                while (len > 0) {
                    res.push("");
                    len--;
                }
                return res;
            })()
        }
    ],
    yAxis: [
        {
            type: 'value',
            scale: true,
            name: '连接数',
            max:10,
            min: 0,
            boundaryGap: [1, 1],
            axisLabel:{
            	textStyle:{ fontSize:6}
            }
        },
        {
            type: 'value',
            scale: true,
            name: '访问量',
            max:100,
            min: 0,
            boundaryGap: [1, 1],
            axisLabel:{
            	textStyle:{ fontSize:6}
            }
        }
    ],
    series: [
        {
            name:'连接数',
            type:'line',
            yAxisIndex: 0,
            data:(function (){
                var res = [];
                var len = 0;
                while (len < 10) {
                    res.push(0);
                    len++;
                }
                return res;
            })()
        },
        {
            name:'访问量',
            type:'line',
            yAxisIndex: 1,
            data:(function (){
                var res = [];
                var len = 0;
                while (len < 10) {
                    res.push(0);
                    len++;
                }
                return res;
            })()
        }
    ]
};

var total_connections_before = -1;
var cmd_get_before = -1;


function setCategory(key,total_connections,cmd_get,curr_connections,time){
	if(total_connections_before == -1){
		total_connections_before = total_connections;
	}
	if(cmd_get_before == -1){
		cmd_get_before = cmd_get;
	}

	var total_connections_now = total_connections - total_connections_before;
	var cmd_get_now = cmd_get - cmd_get_before;

	
	total_connections_before = total_connections;
	cmd_get_before = cmd_get;
	
	date.setTime(time*1000);
	var axisData = date.toLocaleTimeString('chinese',{hour12:false}).replace(/^\D*/,'');
	
	var data0 = category_option.series[0].data;
    var data1 = category_option.series[1].data;
    data0.shift();
    data0.push(parseInt(curr_connections));
    data1.shift();
    data1.push(parseInt(cmd_get_now));
    
    category_option.xAxis[0].data.shift();
    category_option.xAxis[0].data.push(axisData);
    category_option.xAxis[1].data.shift();
    category_option.xAxis[1].data.push(total_connections_now);
	
    var max_y0 = getMax(data0);
    var max_y1 = getMax(data1);
  
    max_y0 = Math.ceil(max_y0/5)*5;
    max_y1 = Math.ceil(max_y1/5)*5;
    
    category_option.yAxis[0].max = max_y0;
    category_option.yAxis[0].interval = max_y0/5;
    category_option.yAxis[1].max = max_y1;
    category_option.yAxis[1].interval = max_y1/5;
    
    category[key].setOption(category_option);
}

function getMax(arr){
	var temp = 0;
	for(var i=0;i<arr.length;i++){
		if(arr[i] > temp){
			temp = arr[i];
		}
	}
	return temp;
}

function getInfo(){
	$.ajax({
		type:"get",
		url: "<%= path%>/memcache/info",
		dataType: "json",
		async: true,
		success: function(data){
			for (var key in data) {  
				var map = data[key];
				$(document.getElementById(key+"_resultMsg")).html(map.resultMsg);
				if("00000" == map.resultCode){
					$(document.getElementById(key+"_content")).show();
					$(document.getElementById(key+"_pid")).html(map.pid);
					$(document.getElementById(key+"_version")).html(map.version);
					date.setTime((map.time-map.uptime)*1000);
					$(document.getElementById(key+"_start_time")).html(date.toLocaleString('chinese',{hour12:false}));
					$(document.getElementById(key+"_uptime")).html(getTimeStr(map.uptime));
					$(document.getElementById(key+"_limit_maxbytes")).html(map.limit_maxbytes/1024/1024+"M");
					$(document.getElementById(key+"_curr_items")).html(map.curr_items);
					$(document.getElementById(key+"_evictions")).html(map.evictions);
					$(document.getElementById(key+"_cmd_set")).html(map.cmd_set);

					setGauge(key,map.curr_items,map.total_items,map.bytes,map.limit_maxbytes);
					setCategory(key,map.total_connections,map.cmd_get,map.curr_connections,map.time);
				}else{
					$(document.getElementById(key+"_content")).hide();
				}
	        }
		}
	});
}

function getTimeStr(time){
	var str = "";
	var time_temp = time;
	var day = parseInt(time_temp/60/60/24);
	if(day > 0){
		str += day+"天";
	}
	time_temp -= day*24*60*60;
	var hour = parseInt(time_temp/60/60);
	if(hour > 0){
		str += hour+"时";
	}
	time_temp -= hour*60*60;
	var minute = parseInt(time_temp/60);
	if(minute > 0){
		str += minute+"分";
	}
	time_temp -= minute*60;
	var second = time_temp%60;
	str += second+"秒";
	return str
}

function showQuery(host){
	var arr = host.split(":");
	$("#memcache_query_form #ip").val(arr[0]);
	$("#memcache_query_form #port").val(arr[1]);
	$("#memcache_query").show();
}

function closeQuery(){
	$("#memcache_query").hide();
}

function query(){
	var ip = $("#memcache_query_form #ip").val();
	var port = $("#memcache_query_form #port").val();
	var key = $("#memcache_query_form #key").val();
	
	$.ajax({
		type:"post",
		url: "<%= path%>/memcache/getValue",
		dataType: "json",
		data:{'ip':ip,'port':port,'key':key},
		async: true,
		success: function(data){
			$("#memcache_query_result").text("");
			if("00000" == data.resultCode){
				alert("查询成功");
				$("#memcache_query_result").text(JSON.stringify(data.resultMsg));
			}else{
				alert(data.resultMsg);
			}
		}
	});
}

</script>
</html>
