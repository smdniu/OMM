// JavaScript Document
	
	$(document).ready(function(e) {
		<!--接单成功弹框-->
		$("#order_recei").click(function(){
			$("#succeed_bounce").css("display","block")
		});
		$("#succeed_bounce .sure").click(function(){
			$("#succeed_bounce").css("display","none");
		});
		<!--待指派保存弹框-->
		$("#build_sure").click(function(){
			$("#build_bounce").css("display","block")
		});
		$("#build_bounce .sure").click(function(){
			$("#build_bounce").css("display","none");
		});
		<!--待转派成功弹框-->
		$("#turn_sure").click(function(){
			$("#turn_bounce").css("display","block")
		});
		$("#turn_bounce .sure").click(function(){
			$("#turn_bounce").css("display","none");
		});
		<!--radio按钮切换-->
		$(".m_radio ul").children("li").each(function(index, item) {
            $(item).click(function(){
				$(item).children("div").attr("class","check_box")
				$(item).siblings().children("div").attr("class","uncheck_box");
				return false;
			});
        });
		<!--工单详情页页面切换-->
		$(".switch_title li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
			var index = $(".switch_title li").index(this)
			$("dl dd").eq(index).show().siblings().hide();
		});
		<!--点击收索栏下拉按钮显示搜索条件-->
		var flag = "no";
		$("#arrow_down").click(function(){
			if("no" == flag){
				$(".search_criteria").attr("style","");
				flag = "yes";
				document.getElementById('arrow_down_pic').setAttribute('src','biz/img/up_arrow.png');	
			}else{
				$(".search_criteria").attr("style","display:none;");
				flag = "no";
				document.getElementById('arrow_down_pic').setAttribute('src','biz/img/down_arrow.png');	
			}
		});
		<!--是否接单弹框-->
		$(".save").click(function(){
			$(".save_bounce").attr("style","");
		});
		$(".save_bounce p").click(function(){
			$(".save_bounce").attr("style","display:none;");
		});
		<!--回单详情页增加删除表格-->
		<!--点击增加按钮增加表格-->
		
		var tableContent = $(".receipt .consu .content ul").eq(0).html();
		$(".receipt .consu .list_title .add").click(function(){
			$(".receipt .consu ul").append(tableContent);
			$(".receipt .consu .content ul li").eq(0).siblings().find(".delete").attr("style","");
			deleted();
		});
		function deleted(){
			$(".receipt .consu .content ul li").each(function(index,item){
				$(item).find(".delete").click(function(){
					$(item).remove();
				});
			});
		}
		<!--回单页面中现场图片栏中点击上传图片添加图片-->
		var scencePic = $(".add_scence_pic").html();
		$(".scene_pictures li:nth-child(1) span").click(function(){
			if($(".add_scence_pic .content").length >=3){
				alert("抱歉,最多只能上传3张图片");
				return;
			}
			$(".add_scence_pic").append(scencePic);
			$(".add_scence_pic .content").eq(0).siblings().find(".delete").attr("style","");
			scencePicDeleted();
		});
		function scencePicDeleted(){
			$(".add_scence_pic .content").each(function(index,item){
				$(item).find(".delete").click(function(){
					$(item).remove();
				});
			});
		}
		<!--回单页面中现场图片栏中点击开通结果添加图片-->
		var resuitPic = $(".add_resuit_pic").html();
		$(".scene_pictures li:nth-child(2) span").click(function(){
			if($(".add_resuit_pic .content").length >=3){
				alert("抱歉,最多只能上传3张图片");
				return;
			}
			$(".add_resuit_pic").append(scencePic);
			$(".add_resuit_pic .content").eq(0).siblings().find(".delete").attr("style","");
			
			resuitPicDeleted();
		});
		function resuitPicDeleted(){
			$(".add_resuit_pic .content").each(function(index,item){
				$(item).find(".delete").click(function(){
					$(item).remove();
				});
			});
		}
		
		<!--改址页面，楼栋条件弹框，显示-->
		$(".change_address .address_modify li:nth-child(2)").click(function(){
			$("#building_bounce").css("display","block");
		});
		<!--改址页面，单元条件弹框，显示-->
		$(".change_address .address_modify li:nth-child(3)").click(function(){
			$("#unit_bounce").css("display","block");
		});
		<!--改址页面，楼层条件弹框，显示-->
		$(".change_address .address_modify li:nth-child(4)").click(function(){
			$("#floor_bounce").css("display","block");
		});
		<!--改址页面，户条件弹框，显示-->
		$(".change_address .address_modify li:nth-child(5)").click(function(){
			$("#family_bounce").css("display","block");
		});
		<!--改址页面，标准地址明细弹框，显示-->
		$("#sta_addr").click(function(){
			$("#sta_addr_bounce").css("display","block");
		});
		<!--改址页面，设备弹框，显示-->
		$("#equip").click(function(){
			$("#equip_bounce").css("display","block");
		});
		<!--改址页面，端口弹框，显示-->
		$("#port").click(function(){
			$("#port_bounce").css("display","block");
		});
		
		
		<!--楼栋弹框选择-->
		$("#building_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
			$("#building_bounce").css("display","none");
		});
		<!--单元弹框选择-->
		$("#unit_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
			$("#unit_bounce").css("display","none");
		});
		<!--楼层弹框选择-->
		$("#floor_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
			$("#floor_bounce").css("display","none");
		});
		<!--户弹框选择-->
		$("#family_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
			$("#family_bounce").css("display","none");
		});
		<!--标准地址明细弹框选择-->
		$("#sta_addr_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
		});
		<!--设备弹框选择-->
		$("#equip_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
		});
		<!--设备弹框选择-->
		$("#port_bounce li").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
		});
		
		
		<!--改址页面，楼栋、单元、楼层、户的弹框值，回填页面文本框中，JS开始！-->
		var selected;
		$("#building_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				selected = $(item).children("span").text();
				$("#building_text").text(selected);
			});
		});
		
		$("#unit_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				selected = $(item).children("span").text();
				$("#unit_text").text(selected);
			});
		});
		
		$("#floor_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				selected = $(item).children("span").text();
				$("#floor_text").text(selected);
			});
		});
		
		$("#family_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				selected = $(item).children("span").text();
				$("#family_text").text(selected);
			});
		});
		<!--改址页面，楼栋、单元、楼层、户的弹框值，回填页面文本框中，JS结束！-->
		
		
		<!--点击取消按钮，标准地址明细、设备、端口，弹框关闭-->
		$("#sta_addr_bounce .cancel_btn").click(function(){
			$("#sta_addr_bounce").css("display","none");
		});
		$("#equip_bounce .cancel_btn").click(function(){
			$("#equip_bounce").css("display","none");
		});
		$("#port_bounce .cancel_btn").click(function(){
			$("#port_bounce").css("display","none");
		});
		
		<!--点击确定按钮，标准地址明细、设备、端口，弹框关闭，并将选择的地址回填到对应的地址框中，JS开始！-->
		var textSelection;
		$("#sta_addr_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				textSelection = $(item).children("span").text();
			});
		});
		$("#sta_addr_bounce .sure_btn").click(function(){
			$("#sta_addr_bounce").css("display","none");
			$("#addr_text").text(textSelection);
		});
		
		$("#equip_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				textSelection = $(item).children("span").text();
			});
		});
		$("#equip_bounce .sure_btn").click(function(){
			$("#equip_bounce").css("display","none");
			$("#equip_text").text(textSelection);
		});
		
		$("#port_bounce .wihte_bg ul li").each(function(index,item){
			$(item).click(function(){
				textSelection = $(item).children("span").text();
			});
		});
		$("#port_bounce .sure_btn").click(function(){
			$("#port_bounce").css("display","none");
			$("#port_text").text(textSelection);
		});
		
		<!--待指派-选择施工人员-->
		$(".builders_page tr").click(function(){
			$(this).addClass("checked").siblings().removeClass("checked");
		});
		<!--待指派-选择施工人员,提交成功弹框-->
		$("#build_sure").click(function(){
			$("#build_bounce").attr("display","block");
		});
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	