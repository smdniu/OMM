<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>应用配置应用新增修改页面</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js">
</script>
	</head>
	<body>
		<!--新增弹框-->
		<div id="applyplugin" class="bounce newAdd_bounce">
			<div class="black_bg"></div>
			<div class="white_bg">
				<!--新增应用内容-->
				<div class="apply_cont" style="">
					<input type="hidden" id="applyDealType" name="TYPE" value="${type}">
					<input type="hidden" id="applyMenuType" name="MENUTYPE" value="${menuType}">
					<input type="hidden" id="applyMenuId" name="MENUID" value="${menuId}">
					<!--叉号-->
					<i class="fa fa-times" aria-hidden="true" onclick="cancelApply()"></i>
					<c:choose>
						<c:when test="${'ADD'.equals(type)}">
							<!--标题-->
							<div class="title">
								新增应用
							</div>
						</c:when>
						<c:otherwise>
							<!--标题-->
							<div class="title">
								修改应用
							</div>
						</c:otherwise>
					</c:choose>
					<c:if test="${type != null && type.equals('ADD')}">
						<!--内容行-->
						<div class="apply_type">
							<label for="">
								应用类型：
							</label>
							<select name="MENUTYPE" id="apply_type">
								<option value="1">
									memchace
								</option>
								<option value="0">
									Redis
								</option>
								<option value="8">
									单点登录
								</option>
								<option value="3">
									db
								</option>
								<option value="2">
									JVMCache
								</option>
							</select>
							<div class="clearfix"></div>
						</div>
					</c:if>
					<ul style="position: inherit;">
						<c:if test="${memcache != null}">
							<!--memchace类型-->
							<li>
								<form id="memchaceInfo" action="">
									<!--类型内容标题-->
									<div class="type_title">
										<img src="<%=path%>/biz/img/memchace60_gray.png" alt="">
										<p>
											memchace
										</p>
										<div class="clearfix"></div>
									</div>
									<!--操作按钮-->
									<div class="operate_btn">
										<div class="border">
											<i class="fa fa-pencil-square-o blue_font" aria-hidden="true" ></i>
											<span onclick="addApplyInfo('memchaceInfo')">新增</span>
										</div>
										<div class="border">
											<i class="fa fa-trash-o red_font" aria-hidden="true" ></i>
											<span onclick="deleteApplyInfo('memchaceInfo')">删除</span>
										</div>
										<div class="clearfix"></div>
									</div>
									<table id="memchaceInfoTable">
										<thead>
											<tr>
												<th>
													<input type="checkbox">
												</th>
												<th>
													ip
												</th>
												<th>
													端口
												</th>
												<th>
													状态
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${memcache}" var="data">
												<tr>
													<td>
														<input type="checkbox" name="memcacheChecke" checked="checked">
													</td>
													<td>
														<input type="text" name="IP" value="${data.ip}">
													</td>
													<td>
														<input type="text" name="PORT" value="${data.port}">
													</td>
													<td>
														<input type="text" name="STATE" value="${data.state}">
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>
							</li>						
						</c:if>
						<c:if test="${redis != null}">
							<!--Redis类型-->
							<li style="display: none;">
								<form id="redisInfo" action="">
									<!--类型内容标题-->
									<div class="type_title">
										<img src="<%=path%>/biz/img/redis60_gray.png" alt="">
										<p>
											Redis
										</p>
										<div class="clearfix"></div>
									</div>
									<!--操作按钮-->
									<div class="operate_btn">
										<div class="border">
											<i class="fa fa-pencil-square-o blue_font" aria-hidden="true" ></i>
											<span onclick="addApplyInfo('redisInfo')">新增</span>
										</div>
										<div class="border">
											<i class="fa fa-trash-o red_font" aria-hidden="true" ></i>
											<span onclick="deleteApplyInfo('redisInfo')">删除</span>
										</div>
										<div class="clearfix"></div>
									</div>
									<table id="redisTable">
										<thead>
											<tr>
												<th>
													<input type="checkbox">
												</th>
												<th>
													ip
												</th>
												<th>
													端口
												</th>
												<th>
													状态
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${redis}" var="data">
												<tr>
													<td>
														<input name="redisChecked" type="checkbox" checked="checked">
													</td>
													<td>
														<input type="text" name="IP" value="${data.ip}">
													</td>
													<td>
														<input type="text" name="PORT" value="${data.port}">
													</td>
													<td>
														<input type="text" name="state" value="${data.state}">
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>
							</li>
						</c:if>
						<c:if test="${singlepoint != null}">
							<!--单点登录-->
							<li style="display: none;">
								<form id="singleInfo" action="">
									<!--类型内容标题-->
									<div class="type_title">
										<img src="<%=path%>/biz/img/single_point60_gray.png" alt="">
										<p>
											单点登录
										</p>
										<div class="clearfix"></div>
									</div>
									<div class="apply_type">
										<label for="">
											系统名称：
										</label>
										<input id="singleName" type="text" name="NAME" value="${singlepoint.name}">
										<div class="clearfix"></div>
									</div>
									<div class="apply_type">
										<label for="">
											系统地址：
										</label>
										<input id="sinflePath" type="text" name="PATH" value="${singlepoint.path}">
										<div class="clearfix"></div>
									</div>
									<div class="operate_btn">
										<div class="border">
											<i class="fa fa-pencil-square-o blue_font" aria-hidden="true" ></i>
											<span onclick="addApplyInfo('singleInfo')">新增</span>
										</div>
										<div class="border">
											<i class="fa fa-trash-o red_font" aria-hidden="true" ></i>
											<span onclick="deleteApplyInfo('singleInfo')">删除</span>
										</div>
										<div class="clearfix"></div>
									</div>
									<table id="singleTable">
										<thead>
											<tr>
												<th>
													<input type="checkbox">
												</th>
												<th>
													用户名
												</th>
												<th>
													密码
												</th>
												<th>
													状态
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${singlepoint.accPwd}" var="data">
												<tr>
													<td>
														<input name="singlenChecked" type="checkbox">
													</td>
													<td>
														<input type="text" name="ACCOUNT" value="${data.account}">
													</td>
													<td>
														<input type="text" name="PASSWORD" value="${data.password}">
													</td>
													<td>
														<input type="text" name="STATE" value="生效" value="${data.state}">
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>
							</li>
						</c:if>
						<c:if test="${DB != null}">
							<!--db-->
							<li style="display: none;">
								<form id="dbInfo" action="">
									<!--类型内容标题-->
									<div class="type_title">
										<img src="<%=path%>/biz/img/db60_gray.png" alt="">
										<p>
											db
										</p>
										<div class="clearfix"></div>
									</div>
									<!--操作按钮-->
									<div class="operate_btn">
										<div class="border">
											<i class="fa fa-pencil-square-o blue_font" aria-hidden="true" ></i>
											<span onclick="addApplyInfo('dbInfo')">新增</span>
										</div>
										<div class="border">
											<i class="fa fa-trash-o red_font" aria-hidden="true" ></i>
											<span onclick="deleteApplyInfo('dbInfo')">删除</span>
										</div>
										<div class="clearfix"></div>
									</div>
									<table id="dbTable">
										<thead>
											<tr>
												<th>
													<input type="checkbox">
												</th>
												<th>
													数据库
												</th>
												<th>
													最小值
												</th>
												<th>
													最大值
												</th>
												<th>
													状态
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${DB}" var="data">
												<tr>
													<td>
														<input name="dbChecked" type="checkbox">
													</td>
													<td>
														<input type="text" name="DBACCT" value="${data.dbAcct}">
													</td>
													<td>
														<input type="text" name="MIN" value="${data.min}">
													</td>
													<td>
														<input type="text" name="MAX" value="${data.max}">
													</td>
													<td>
														<input type="text" name="STATE" value="${data.state}">
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</form>
							</li>
						</c:if>
						<c:if test="${jvmcache != null}">
							<!--JVMcache-->
							<li style="display: none;">
								<form id="JVMCacheInfo" action="">
									<!--类型内容标题-->
									<div class="type_title">
										<img src="<%=path%>/biz/img/db60_gray.png" alt="">
										<p>
											JVMCache
										</p>
										<div class="clearfix"></div>
									</div>
                                    <div class="grouping-sele">
                                        <p class="grouping-label">分组：</p>
                                        <select name="GROUP" id="groupCode">
                                        	<c:forEach items="${jvmcache}" var="data">
                                        		<option value="${data.code}">${data.name}</option>
											</c:forEach>
                                        </select>
                                        <!--新增分组-->
                                        <span id="new_span">新增分组</span>
                                        <div class="clearfix"></div>
                                    </div>
                                    <!--操作按钮-->
                                    <div class="operate_btn">
                                        <div class="border">
											<i class="fa fa-pencil-square-o blue_font" aria-hidden="true" ></i>
											<span onclick="addApplyInfo('JVMCacheInfo')">新增</span>
										</div>
										<div class="border">
											<i class="fa fa-trash-o red_font" aria-hidden="true" ></i>
											<span onclick="deleteApplyInfo('JVMCacheInfo')">删除</span>
										</div>
										<div class="clearfix"></div>
                                    </div>
									<table id="jvmCacheTable">
										<thead>
											<tr>
												<th>
													<input type="checkbox">
												</th>
												<th>
													ip
												</th>
												<th>
													端口
												</th>
												<th>
													状态
												</th>
											</tr>
										</thead>
                                        <tbody id="groupTbody">
										</tbody>
									</table>
								</form>
							</li>
						</c:if>
					</ul>
					<!--确定取消-->
					<div class="btn_box">
						<input type="button" class="sure" value="提交" onclick="subApply()">
						<input type="button" class="cancel" value="取消" onclick="cancelApply()">
					</div>
				</div>
			</div>
		</div>
		<!--新增分组弹框-->
        <div class="bounce newAdd_bounce " id="add_group" style="display:none;">
            <div class="black_bg"></div>
            <div class="white_bg white_width">
                <!--新增分组内容-->
                <div class="user_cont" style="display: ;">
                    <!--标题-->
                    <div class="title">新增分组</div>
                    <!--叉号-->
                    <i class="fa fa-times" aria-hidden="true" onclick="cancelAddGroup()"></i>
                    <!--内容行-->
                    <ul class="maring-top">
	                    <form id="groupInfo" >
	                        <li class="position_padding">
	                            <label for="" class="padding_none">分组code:</label>
	                            <input type="text" name="CODE">
	                        </li>
	                        <li class="position_padding">
	                            <label for="" class="padding_none">分组名:</label>
	                            <input type="text" name="NAME">
	                        </li>
	                        <li class="position_padding">
	                            <label for="" class="padding_none">分组类:</label>
	                            <input type="text" name="CLASSNAME">
	                        </li>
	                        <li class="position_padding">
	                            <label for="" class="padding_none">ip:port:</label>
	                            <input type="text" name="IPPORT">
	                        </li>
						</form>
                    </ul>
                    <!--确定取消-->
                    <div class="btn_box">
                        <input type="button" class="sure" value="提交" onclick="subAddGroup()">
                        <input type="button" class="cancel" value="取消" onclick="cancelAddGroup()">
                    </div>
                </div>
           </div>
       </div>
	</body>
	<script type="text/javascript">
if(${code != '00000' && msg != null}){
	warnShow(${msg});
}
function subApply(){
	var applyDealType = $("#applyDealType").val();// 新增/更新
	var applyInfo;
	var applyMenuType;
	if(applyDealType == "ADD"){
		applyMenuType = $("#apply_type").val();//新增下拉框
	}else if(applyDealType == "UPDATE"){
		applyMenuType = $("#applyMenuType").val();//更新应用时的应用类型
	}
	var url = "<%= path%>";
	if(applyMenuType == "0"){
		url += "/redis/addRedisConfig";
		applyInfo = "'redisInfo'=" +  getTableContent('redisTable', "redisChecked");
	}else if(applyMenuType == "1"){
		url += "/memcache/addMemcahceConfig";
		applyInfo = "'memchaceInfo'=" +  getTableContent('memchaceInfoTable', "memcacheChecke");
	}else if(applyMenuType == "2"){
		url += "/jvmCache/addJVMCacheConfig";
		applyInfo = "'groupCode'=" + groupCode + "'JVMCacheInfo'=" +  getTableContent('jvmCacheTable', "jvmcacheChecked");
	}else if(applyMenuType == "3"){
		url += "/db/addDbConfig";
		applyInfo = "'dbInfo'=" +  getTableContent('dbTable', "dbChecked");
	}else if(applyMenuType == "8"){
		url += "/singlePoint/addSinglePointConfig";
		var singleName = $("#singleName").val();
		var sinflePath = $("#sinflePath").val();
		applyInfo = "'singleName'=" + singleName + "&'sinflePath'=" + sinflePath + "&'singleInfo'=" +  getTableContent('singleTable', "singlenChecked");
	}
	console.log("applyInfo:" + applyInfo);
	var applyMenuId = $("#applyMenuId").val();//更新应用时的应用id
	$.ajax({
	 type:"POST",
	  url: url,
	  data: applyInfo,
	  dataType: "json",
	  cache: false,
	  async: false,
	  success: function(data){
		  console.log("data:" + data);
		  if(data.code == "00000"){
		  } else {
			  warnShow(data.msg);
		  }
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}

/** 
 * 遍历表格内容返回数组
 * @param  Int   id 表格id
 * @param  str   c 选择的checked的name
 * @return Array
 */
function getTableContent(id, c){
    var G_json = [];
	$("#" + id).find("input[name=" + c + "]").each(function(){
		if($(this).is(":checked")){
			var childrens = $(this).parents().parents().children('td');
			var tmp = "";
			for(var i =1; i < childrens.length; i++){
				tmp += $(childrens[i]).children('input').val() + ":"
			}
			G_json.push(tmp);
		}
	});
	return G_json;
}
function subAddGroup(){
	var groupInfo = $("#groupInfo").serialize();
	$.ajax({
	 type:"POST",
	  url: '<%= path%>/jvmCache/addJVMCacheGroup',
	  data: groupInfo,
	  dataType: "json",
	  cache: false,
	  async: false,
	  success: function(data){
		  console.log("data:" + data);
		  if(data.code == "00000"){
			  var ipPorts = data.result.ipPortList;
			  var html = "";
			  $("#groupCode").append('<option value="' + data.result.code + '" selected = "selected">' + data.result.name + '</option>');
			  for(var i = 0; i < ipPorts.length; i++){
				  html += '<tr><td><input type="checkbox" name="jvmcacheChecked" checked="checked"></td><td><input type="text" name="IP" value="' + ipPorts[i].ip + '"></td><td><input type="text" name="PORT" value="' + ipPorts[i].port + '"></td><td><input type="text" name="STATE" value="' + ipPorts[i].state + '"></td></tr>'
			  }
			  $("#groupTbody").html(html);
			  cancelAddGroup();
		  } else {
			  warnShow(data.msg);
		  }
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}

var groupCode = $("#groupCode").val();
getJVMCacheInfoByGroupId(groupCode);

$("#groupCode").change(function(){
    var groupCode = $("#groupCode").val();
	getJVMCacheInfoByGroupId(groupCode);
});
function getJVMCacheInfoByGroupId(type){
	$.ajax({
	 type:"POST",
	  url: '<%= path%>/jvmCache/getJVMCacheInfoByGroupId',
	  data:"groupCode=" + type,
	  dataType: "json",
	  cache: false,
	  async: false,
	  success: function(data){
		  if(data.code == "00000"){
			  var ipPorts = data.result.ipPortList;
			  var html = ""
			  for(var i = 0; i < ipPorts.length; i++){
				  html += '<tr><td><input type="checkbox" checked="checked"></td><td><input type="text" name="IP" value="' + ipPorts[i].ip + '"></td><td><input type="text" name="PORT" value="' + ipPorts[i].port + '"></td><td><input type="text" name="STATE" value="' + ipPorts[i].state + '"></td></tr>'
			  }
			  $("#groupTbody").html(html);
			  cancelAddGroup();
		  } else {
			  warnShow(data.msg);
		  }
	  },
	  error:function(){
		  warnShow("系统错误,请稍后再试!");
	  }
	});
}
function addApplyInfo(typeId){
	var html = "";
	if (typeId == "memchaceInfo" || typeId == "redisInfo" || typeId == "JVMCacheInfo"){
		html = '<tr><td><input type="checkbox" checked="checked"></td><td><input type="text" name="IP"></td><td><input type="text" name="PORT" ></td><td><input type="text" name="STATE" value="生效" readonly></td></tr>';
	} else if (typeId == "singleInfo"){
		html = '<tr><td><input type="checkbox"></td><td><input type="text" name="ACCOUNT" ></td><td><input type="text" name="PASSWORD" ></td><td><input type="text" name="STATE" value="生效" readonly></td></tr>';
	} else if (typeId == "dbInfo"){
		html = '<tr><td><input type="checkbox"></td><td><input type="text" name="DBACCT" ></td><td><input type="text" name="MIN" ></td><td><input type="text" name="MAX" ></td><td><input type="text" name="STATE" value="生效" readonly></td></tr>';
	} else{
		return;
	}
	$("#" + typeId + " tbody").append(html);
}
function deleteApplyInfo(typeId){
	$("#" + typeId + " input:checked").parent().parent().remove();
}

function cancelApply(){
	$("#applyplugin").hide();
}
//点击表格标题中的多选框，全选表格内容行的多选框
$("th input[type=checkbox]").click(function(){
    if($(this).is(":checked")){
        $("td input[type=checkbox]").attr("checked",true);
    }else{
        $("td input[type=checkbox]").attr("checked",false);
    }
});
 $("#apply_type").change(function(){
     var option=$(this).get(0).selectedIndex+1;
     $(".apply_type").next().children("li:nth-child("+option+")").show().siblings().hide();
 });
 
//点击新增分组按钮弹出新增分组的弹框
 $("#new_span").click(function () {
	 $("#groupInfo input").val("");
     $("#add_group").show()
 });
 //        点击提交按钮隐藏新增分组的弹框
function cancelAddGroup(){
	$("#add_group").hide()
}
</script>
</html>
