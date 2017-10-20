<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>用户管理用户新增修改页</title>
		<meta name="viewport"
			content="target-densitydpi=device-dpi, width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
		<link href="<%=path%>/biz/css/base.css" rel="stylesheet" type="text/css">
		<link href="<%=path%>/biz/css/font-awesome.css" rel="stylesheet" type="text/css">
		<script src="<%=path%>/biz/js/jquery-1.11.1.min.js">
</script>
	</head>
	<body>
		<!--新增弹框-->
        <div id="memberplugin" class="bounce newAdd_bounce">
            <div class="black_bg"></div>
            <div class="white_bg">
                <!--新增用户内容-->
                <div class="user_cont">
                    <!--标题-->
                    <div class="title">新增用户</div>
                    <!--叉号-->
                    <i class="fa fa-times" aria-hidden="true" onclick="cancelMember()"></i>
                    <!--内容行-->
					<form id="addMemberPage" >
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
									账号:
								</label>
								<c:choose>
									<c:when test="${account != null}" >
										<input id="account" type="text" name="ACCOUNT" value="${account}" autofocus required>
									</c:when>
									<c:otherwise>
										<input id="account" type="text" name="ACCOUNT" autofocus required>
									</c:otherwise>
								</c:choose>
								<span class="necessary">*</span>
							</li>
							<li>
								<label for="">
									姓名:
								</label>
								<c:choose>
									<c:when test="${name != null}">
										<input id="name" type="text" name="NAME" value="${name}"  required>
									</c:when>
									<c:otherwise>
										<input id="name" type="text" name="NAME"  required>
									</c:otherwise>
								</c:choose>
								<span class="necessary">*</span>
							</li>
							<li>
								<label for="">
									密码:
								</label>
								<input id="password" type="password" name="PASSWORD" required>
								<span class="necessary">*</span>
							</li>
							<li>
								<label for="">
									确认密码:
								</label>
								<input id="repassword" type="password" name="REPASSWORD" required>
								<span class="necessary">*</span>
							</li>
							<li>
								<label for="">
									部门:
								</label>
								<select name="DEPT">
									<c:forEach items="${depts}" var="data">
										<c:choose>
											<c:when test="${selectedDept.equals(data.NAME)}">
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
							</li>
							<li>
								<label for="">
									电话:
								</label>
								<c:choose>
									<c:when test="${tel != null}">
										<input type="tel" name="TEL" value="${tel}">
									</c:when>
									<c:otherwise>
										<input type="tel" name="TEL">
									</c:otherwise>
								</c:choose>
							</li>
							<li>
								<label for="">
									邮箱:
								</label>
								<c:choose>
									<c:when test="${email != null}">
										<input type="email" name="EMAIL" value="${email}">
									</c:when>
									<c:otherwise>
										<input type="email" name="EMAIL">
									</c:otherwise>
								</c:choose>
							</li>
							<li>
								<label for="">
									IP地址:
								</label>
								<c:choose>
									<c:when test="${ip != null}">
										<input type="text" name="IP" value="${ip}">
									</c:when>
									<c:otherwise>
										<input type="text" name="IP">
									</c:otherwise>
								</c:choose>
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
									加密方式:
								</label>
								<select name="ENCRYPTE" id="">
									<c:forEach items="${encrypts}" var="data">
										<c:choose>
											<c:when test="${selectedEncrypt.equals(data.TYPE)}">
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
									状态:
								</label>
								<select name="STATE" id="">
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
									用户角色：
								</label>
								<ul>
									<c:forEach items="${roles}" var="data">
										<c:choose>
											<c:when test="${selectedRole.contains(data.TYPE)}">
												<li>
													<input type="checkbox" id="" name="ROLE"
														value="${data.TYPE}" checked="checked">
													<span>${data.NAME}</span>
												</li>
											</c:when>
											<c:otherwise>
												<li>
													<input type="checkbox" id="" name="ROLE"
														value="${data.TYPE}">
													<span>${data.NAME}</span>
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</ul>
							</li>
						</ul>
					</form>
					<!--确定取消-->
                    <div class="btn_box">
                        <input type="button" class="sure" value="提交" onclick="subMember()">
                        <input type="button" class="cancel" value="取消" onclick="cancelMember()">
                    </div>
                </div>
            </div>
        </div>
	</body>
<script type="text/javascript">
if(${code != '00000' && msg != null}){
	warnShow(${msg});
}
function subMember(){
	var account = $("#account").val();
	var name = $("#name").val();
	var password = $("#password").val();
	var repassword = $("#repassword").val();
	if(account == null || account == undefined  || "" == account){
		warnShow("账号不能为空!");
		return;
	}
	if(name == null || name == undefined || "" == name){
		warnShow("姓名不能为空!");
		return;
	}
	if(password == null || password == undefined  || "" == password){
		warnShow("密码不能为空!");
		return;
	}
	if(repassword == null || repassword == undefined || "" == repassword){
		warnShow("确认密码不能为空!");
		return;
	}
	if(repassword != password){
		warnShow("两次输入密码不一致!");
		return;
	}
	var memberstr = $("#addMemberPage").serialize();
	$.ajax({
		  type:"POST",
		  url: '<%= path%>/member/addMember',
		  data: memberstr,
		  dataType: "json",
		  cache: false,
		  async: false,
		  success: function(data){
			  if(data.code == "00000"){
				  $("#memberplugin").hide();
				  $("#accountMember").val("");
				  $("#roleCodeMember").val("");
				  $("#appNameMember").val("");
				  selectMember();
			  } else {
				 warnShow(data.msg);
			  }
		  },
		  error:function(){
			  warnShow("系统错误,请稍后再试!");
		  }
		});
}
function cancelMember(){
	$("#memberplugin").hide();
}
</script>
</html>
