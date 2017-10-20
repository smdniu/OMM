<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>db应用</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet"
			type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet"
			type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js"></script>
		<script src="<%=path%>/biz/js/jquery.cookie.js"></script>
	</head>
<body>
	<%@ include file="../warn.jsp" %>
    <div class="data_page">
        <!--侧边栏-->
        <div class="side_bar">
            <ul id="db_tree">
            	<c:forEach var="map" items="${ds}">
		            <li class="ds" ds="${map.key}" init="false">
                    	<div class="relative">
                            <i class="fa fa-plus-square-o" aria-hidden="true"></i>
                            <i class="fa fa-minus-square-o" aria-hidden="true" style="display: none;"></i>
                        </div>
                        <span title="${map.value.username}">${map.key}(${map.value.username})</span>
                        <div class="clearfix"></div>
                        <!--第二级-->
                        <ul style="display: none;">
                            <li>
                                <div class="relative">
                                    <i class="fa fa-plus-square-o" aria-hidden="true"></i>
                                    <i class="fa fa-minus-square-o" aria-hidden="true" style="display: none;"></i>
                                </div>
                                <span>子菜单1</span>
                                <div class="clearfix"></div>
                                <ul style="display: none;">
                                    <li>
                                        <div class="relative">
                                            <i class="fa fa-plus-square-o" aria-hidden="true"></i>
                                            <i class="fa fa-minus-square-o" aria-hidden="true" style="display: none;"></i>
                                        </div>
                                        <span>子菜单2</span>
                                        <div class="clearfix"></div>
                                    </li>
                                </ul>
                            </li>
                        </ul>
               		</li>
		        </c:forEach>
            </ul>
        </div>
        <!--内容区-->
        <div class="page_content">
            <div class="top">
                <div class="content">
                   <div contenteditable="true" style="min-height:100%" id="sql_edit" ></div>
                </div>
            </div>
            <div class="middle">
            	<select id="ds" >
            		<c:forEach var="map" items="${ds}">
               			<option value ="${map.key}">${map.key}(${map.value.username})</option>
		        	</c:forEach>
            	</select>
            	<input type="button" onclick="executeSelect()" value="执行选择"/>
            	<input type="button" onclick="executeAll()" value="执行所有"/>
            	<div style="float:right">
            	    <p>查询数量:</p>
            		<input id="maxNum" value="100" />
            	</div>
            </div>
            <div class="bottom">
                <div class="content">
                    <div class="tab_nav">
						<ul id="result_ul">
						</ul>
					</div>
					<div id="result_page">
					</div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

<script>
$(function(){
	warnDBShow();
})

$("#db_tree").on("click","li",function(event){
   	event.stopPropagation();
   	if($(this).hasClass("table")){
   		return;
   	}
   	if($(this).hasClass("ds")){
		$(this).css("background-color","#f7f7f7").siblings().css("background-color","#fff");
		$(this).children(".clearfix").css("border-bottom","solid 1px #ddd").siblings().children(".clearfix").css("border-bottom","none");
 
		var $ul = $(this).children("ul");
		var $plus = $(this).children().children(".fa-plus-square-o");
		var $minus = $(this).children().children(".fa-minus-square-o");
           
   		if($(this).attr("init")=="false"){
   			$ul.html("");
   			$(this).attr("init",getUserAllTable($(this).attr("ds"),$ul));
       	}
           
       	if($ul.css("display")=='none'){
       		$ul.css('display','block');
       	}else{
       		$ul.css('display','none');
       	}
       
       	if($plus.css("display")=='none'){
       		$plus.css('display','block');
       	}else{
       		$plus.css('display','none');
       	}
       	if($minus.css("display")=='none'){
       		$minus.css('display','block');
       	}else{
       		$minus.css('display','none');
       	}
   	}
});

function executeSelect(){
	var sql = getSelectText();
	if(sql && sql.toString().length > 0){
		executeSql(sql,$("#ds").find("option:selected").val(),0,$("#maxNum").val());
	}else{
		warnShow('警告','请选择sql');
	}
}

function executeAll(){
	var sql = $("#sql_edit").text();
	if(sql && sql.toString().length > 0){
		executeSql(sql,$("#ds").find("option:selected").val(),0,$("#maxNum").val());
	}else{
		warnShow('警告','请输入sql');
	}
}

function getSelectText(){
	var txt = '';
    //适用于IE 
	if (document.selection && document.selection.createRange){ 
        txt = document.selection.createRange().text; 
    //适用于其他浏览器
	} else if (window.getSelection){ 
        txt = window.getSelection(); 
    }
    return txt.toString();
}

function executeSql(sql,ds,startIndex,endIndex){
	sql = sql.replace(/\s+/g, " ");
	$.ajax({
		type:"post",
		url: "<%= path%>/db/execute",
		dataType: "json",
		data:{'sql':sql,'ds':ds,'startIndex':startIndex,'endIndex':endIndex},
		async: true,
		success: function(data){
			clearResult();
			var index = 0;
			for (var key in data) {  
				addResult(index++,key,data[key]);
	        }
			$("#result_ul > li").eq(0).click();
		},
		error:function(){
			clearResult();
			warnShow('错误','执行异常,请重试');
		},
		beforeSend: function(){
			loadShow();
		},
		complete: function(){  
			loadCloas(); 
		}
	});
}

function clearResult(){
	$("#result_page").html("");
	$("#result_ul").html("");
}

function addResult(index,sql,data){
	var div = $('<div style="display:none;" id="result_page_'+index+'"></div>');
	if(data.resultMsg){
		div.append('<lable >'+sql+'</lable><nobr style="color:red;">'+data.resultMsg+'</nobr>');
	}
	if("99999" == data.resultCode){
		warnShow('错误',data.resultMsg);
	}else if("00000" == data.resultCode){
		var rs = data.data;
		var colums = data.colums;
		var table = $('<table></table>');
		var tr_h = $("<tr></tr>");
		for(var col=0; col < colums.length;col++){
			var th = $("<th>"+colums[col]+"</th>");
			tr_h.append(th);
		}
		table.append(tr_h);
		for(var row=0 ; row < rs.length; row++){
			var tr_d = $("<tr></tr>");
			for(var col=0; col < colums.length;col++){
				var td = "";
				if(rs[row][colums[col]]){
					td = rs[row][colums[col]];
				}
				var td = $("<td>"+td+"</td>");
				tr_d.append(td);
			}
			table.append(tr_d);
		}
		div.append(table);
	}
	$("#result_page").append(div);
	
	var li = $('<li id="result_ul_'+index+'" onclick="selectResult(\''+index+'\')"><span>result_'+index+'</span></li>');
	$("#result_ul").append(li);
}

function selectResult(index){
	var li = $("#result_ul_" + index);
	if(li.length > 0){
		li.addClass("checked").siblings().removeClass("checked");
		$("#result_page > div").hide();
   		$('#result_page_'+index).show();
	}
}

function getUserAllTable(ds,ul){
	var init_flag = "true";
	$.ajax({
		type:"post",
		url: "<%= path%>/db/getUserAllTable",
		dataType: "json",
		data:{'ds':ds},
		async: true,
		success: function(data){
			if("00000" == data.resultCode){
				var tables = data.data;
				for(var i=0;i<tables.length;i++){
					var li = $("<li class='table' title='"+tables[i].TABLE_NAME+"'><span>&nbsp;&nbsp;&nbsp;&nbsp;"+tables[i].TABLE_NAME+"</span></li>");
					ul.append(li);
				}
			}else{
				warnShow('错误','获取表名异常,请重试');
				init_flag = "false";
			}
		},
		error:function(){
			warnShow('错误','获取表名异常,请重试');
			init_flag = "false";
		},
	});
	return init_flag;
}

</script>