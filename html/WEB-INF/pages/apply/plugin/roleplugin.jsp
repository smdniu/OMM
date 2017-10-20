<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>角色管理角色新增修改页</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/jquery.treeview.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/screen.css" rel="stylesheet" type="text/css">
		<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.9.1.js"></script>
		<script src="<%=path%>/biz/js/jquery.js" type="text/javascript"></script>
		<script src="<%=path%>/biz/js/jquery.cookie.js" type="text/javascript"></script>
		<script src="<%=path%>/biz/js/jquery.treeview.js" type="text/javascript"></script>
		<link rel="stylesheet" href="<%=path%>/biz/css/jquery.treeview.css">
		<link rel="stylesheet" href="<%=path%>/biz/css/screen.css">
		<script src="<%=path%>/biz/js/demo.js"></script>
</script>
	</head>
	<body>
		<!--新增弹框-->
		<div id="roleplugin" class="bounce newAdd_bounce">
		    <div class="black_bg"></div>
            <div class="white_bg">
			<!--新增角色内容-->
			<div class="role_cont">
				<!--标题-->
				<div class="title">新增角色</div>
				<!--叉号-->
				<i class="fa fa-times" aria-hidden="true" onclick="cancelRole()"></i>
				<!--内容行-->
				<form id="addRolePage">
					<ul>
						<input type="hidden" name="TYPE" value="${type}">
						<c:choose>
							<c:when test="${id != null}">
								<input type="hidden" name="ID" value="${id}">
							</c:when>
							<c:otherwise>
								<input type="hidden" name="ID" value="">
							</c:otherwise>
						</c:choose>
						<li>
							<label for="">
								角色编码:
							</label>
							<c:choose>
								<c:when test="${roleCode != null}" >
									<input id="roleCode" type="text" name="ROLECODE" value="${roleCode}" autofocus required>
								</c:when>
								<c:otherwise>
									<input id="roleCode" type="text" name="ROLECODE" autofocus required>
								</c:otherwise>
							</c:choose>
							<span class="necessary">*</span>
						</li>
						<li>
							<label for="">
								名称:
							</label>
							<c:choose>
								<c:when test="${name != null}">
									<input id="roleName" type="text" name="NAME" value="${name}"  required>
								</c:when>
								<c:otherwise>
									<input id="roleName" type="text" name="NAME"  required>
								</c:otherwise>
							</c:choose>
							<span class="necessary">*</span>
						</li>
						<li>
							<label for="">
								状态:
							</label>
							<select name="STATE">
								<c:forEach items="${states}" var="data">
									<c:choose>
										<c:when test="${selectedState.equals(data.TYPE)}">
											<option value="${data.TYPE}" selected="selected">
												${data.NAME}
											</option>
										</c:when>
										<c:otherwise>
											<option value="${data.TYPE}">
												${data.NAME}
											</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</li>
						<li>
							<label for="">
								创建时间:
							</label>
							<c:choose>
								<c:when test="${createTime != null}">
									<input type="date" name="CREATETIME" value="${createTime}">
								</c:when>
								<c:otherwise>
									<input type="date" name="CREATETIME">
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								失效时间:
							</label>
							<c:choose>
								<c:when test="${expiryTime != null}">
									<input type="date" name="EXPIRYTIME" value="${expiryTime}">
								</c:when>
								<c:otherwise>
									<input type="date" name="EXPIRYTIME">
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								扩展字段1:
							</label>
							<c:choose>
								<c:when test="${ext1 != null}">
									<input type="text" name="EXT1" value="${ext1}">
								</c:when>
								<c:otherwise>
									<input type="text" name="EXT1">
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								扩展字段2:
							</label>
							<c:choose>
								<c:when test="${ext2 != null}">
									<input type="text" name="EXT2" value="${ext2}">
								</c:when>
								<c:otherwise>
									<input type="text" name="EXT2">
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								扩展字段3:
							</label>
							<c:choose>
								<c:when test="${ext3 != null}">
									<input type="text" name="EXT3" value="${ext3}">
								</c:when>
								<c:otherwise>
									<input type="text" name="EXT3">
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								备注:
							</label>
							<c:choose>
								<c:when test="${remark != null}">
									<textarea name="REMARK" value="${remark}"/>
								</c:when>
								<c:otherwise>
									<textarea name="REMARK"/>
								</c:otherwise>
							</c:choose>
						</li>
						<li>
							<label for="">
								角色授权：
							</label>
							<!--文件夹层级显示隐藏-->
							<ul id="browser" class="filetree treeview"
								style="padding-left: 20px; margin-left: 20%; float: inherit;">
								<li class="collapsable">
									<div class="hitarea collapsable-hitarea"></div>
									<input class="radio_box" type="checkbox" title="全选"
										style="margin-left: 5px">
									<span class="folder">Folder 2</span>
									<ul style="display: block; width: 100%;">
										<li class="collapsable">
											<div class="hitarea collapsable-hitarea">
											</div>
											<input class="radio_box" type="checkbox" title="全选"
												style="margin-left: 5px">
											<span class="folder">Subfolder 2.1</span>
											<ul id="folder21" style="display: block; width: 100%">
												<li>
													<input class="radio_box" type="checkbox" title="全选"
														style="margin-left: 5px">

													<span class="file">File 2.1.1</span>

												</li>
												<li class="last">
													<input class="radio_box" type="checkbox" title="全选"
														style="margin-left: 5px">

													<span class="file">File 2.1.2</span>

												</li>
											</ul>
										</li>
										<li class="last">
											<span class="file">File 2.2</span>
										</li>
									</ul>
								</li>
								<li class="collapsable">
									<div class="hitarea collapsable-hitarea"></div>
									<input class="radio_box" type="checkbox" title="全选"
										style="margin-left: 5px">
									<span class="folder">Folder 2</span>
									<ul style="display: block; width: 100%;">
										<li class="collapsable">
											<div class="hitarea collapsable-hitarea">
											</div>
											<input class="radio_box" type="checkbox" title="全选"
												style="margin-left: 5px">
											<span class="folder">Subfolder 2.1</span>
											<ul id="folder21" style="display: block; width: 100%">
												<li>
													<input class="radio_box" type="checkbox" title="全选"
														style="margin-left: 5px">

													<span class="file">File 2.1.1</span>

												</li>
												<li class="last">
													<input class="radio_box" type="checkbox" title="全选"
														style="margin-left: 5px">

													<span class="file">File 2.1.2</span>

												</li>
											</ul>
										</li>
										<li class="last">
											<span class="file">File 2.2</span>
										</li>
									</ul>
								</li>
								<li class="collapsable">
									<div class="hitarea collapsable-hitarea"></div>
									<input class="radio_box" type="checkbox" title="全选"
										style="margin-left: 5px">
									<span class="folder">Folder 2</span>
									<ul style="display: block; width: 100%;">
										<li class="collapsable">
											<div class="hitarea collapsable-hitarea">
											</div>
											<input class="radio_box" type="checkbox" title="全选"
												style="margin-left: 5px">
											<span class="folder">Subfolder 2.1</span>
										</li>
										<li class="last">
											<span class="file">File 2.2</span>
										</li>
									</ul>
								</li>


								<li class="last">
									<span class="file">File 4</span>
								</li>
							</ul>
						</li>
					</ul>
				</form>
				<!--确定取消-->
				<div class="btn_box">
					<input type="submit" class="sure" value="确定" onclick="subRole()">
					<input type="button" class="cancel" value="取消" onclick="cancelRole()">
				</div>
			</div>
		</div>
	</body>
<script type="text/javascript">
if(${code != '00000' && msg != null}){
	warnShow(${msg});
}
function subRole(){
}

function cancelRole(){
	$("#roleplugin").hide();
}
//点击选中子级选框
$(".collapsable > input").each(function(index,item){
    $(item).click(function(){
        if ("no" == flag) {
            $(this).parent().find("ul input").each(function(index,item){
                $(item).attr("checked","true");
            });
            flag = "yes";
        } else {
            $(this).parent().find("ul input").each(function(index,item){
                $(item).attr("checked","");
            });
            flag = "no";
        }
    });
});
</script>
</html>
