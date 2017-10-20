<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="warn_db" class="bounce warn_bounce" style="display:none;">
    <div class="black_bg"></div>
    <div class="white_bg">
        <p>警告</p>
        <div class="tip">
            <span>本页面执行的sql将直接提交,请谨慎执行</span>
       </div>
       <input  class="sure" type="button" value="确定" onclick="warnDBClose()">
    </div>
</div>
<div id="warn" class="bounce warn_bounce" style="display:none;">
    <div class="black_bg"></div>
    <div class="white_bg">
        <p id="warn_title"></p>
        <div class="tip">
            <span id="warn_content"></span>
       </div>
       <input  class="sure" type="button" value="确定" onclick="warnClose()">
    </div>
</div>
<script src="<%=path%>/biz/js/ui.js"></script>
<script>
function warnDBClose(){
	$("#warn_db").css("display", "none");
}

function warnDBShow(){
	$("#warn_db").css("display", "inline");
}

function warnClose(){
	$("#warn").css("display", "none");
}

function warnShow(title,content){
	$("#warn_title").html(title);
	$("#warn_content").html(content);
	$("#warn").css("display", "inline");
}

function loadShow(){
	$.blockUI({  
        message: "<img style='margin: 0 auto;' src='<%= path%>/biz/img/wait.gif' />",  
        css: {  
            width: "36",
            height: "36",
            top: "46%",
            left: "47%", 
            border: "0px solid #000",
            opacity:  0.6,//更改遮罩层的透明度
         },
         overlayCSS:  {
        	width: "100%",
            height: "100%",
            backgroundColor: '#000',
            opacity:         0.1,//更改遮罩层的透明度
            cursor:          'wait'
		}
	}); 
}
function loadCloas(){
	$.unblockUI();
}

</script>