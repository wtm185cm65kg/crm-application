<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	//表示页面加载完毕后执行该方法
	$(function () {
		//给"登录"按钮添加单击事件
		$("#loginBtn").click(function () {
			//收集参数
			let loginAct=$.trim($("#loginAct").val());
			let loginPwd=$("#loginPwd").val();
			/*.attr("属性名") 用来获取值不是true/false的属性的值.
			  .prop("属性名") 用来获取值是true/false的属性的值.例如：checked,selected,readonly,disabled等。*/
			let isRemPwd=$("#isRemPwd").prop("checked");
			//表单验证，在JavaScript中判断字符串是否为空可使用==""，在Java中则要使用.equals("")
			if(loginAct==""){
				alert("用户名不能为空");
				return;
			}
			if(loginPwd==""){
				alert("密码不能为空");
				return;
			}

			/*为了防止顾客在登陆等待中焦虑，一般会在点击登录按钮后、发送请求前显示一个提示信息*/
			/*$("#msg").text("正在努力验证....");*/

			//发送Ajax请求
			$.ajax({
				url:'settings/qx/user/login.do',
				data:{
					loginAct:loginAct,
					loginPwd:loginPwd,
					isRemPwd:isRemPwd
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//跳转到业务主页面
						window.location.href="workbench/index.do";
					}else{
						//提示信息
						$("#msg").text(data.message);
					}
				},
				beforeSend:function () {//当ajax向后台发送请求之前，会自动执行本函数；
					//该函数的返回值能够决定ajax是否真正向后台发送请求：
					//如果该函数返回true,则ajax会真正向后台发送请求；否则，如果该函数返回false，则ajax放弃向后台发送请求。
					$("#msg").text("正在努力验证....");
					return true;
				},
				beforeSend:function (){ //当Ajax向后台发送请求前会执行本函数(一般)
					//该函数的返回值能够决定Ajax是否真正向后端发请求(执行结果为true会成功发请求，执行结果为false则会选择不发送本次请求)
					//可以将表单验证、在等待验证前显示提示信息的操作放入这里
					/*if(loginAct==""){
						alert("用户名不能为空");
						return;
					}
					if(loginPwd==""){
						alert("密码不能为空");
						return;
					}*/
					$("#msg").text("正在努力验证....");

					//为了让改代码必定执行需要返回true
					return true;
				}
			});
		});

		/*给整个浏览器窗口添加键盘按下Enter事件，提升用户体验*/
		$(window).keydown(function (event){
			//判断按下的键是否为enter键(其对应的键盘编码为13)
			if (event.keyCode==13){
				/*jquery事件函数的用法：
				   1.给指定的元素添加事件
						选择器.click(function(){js代码});
				   2.在指定的元素上模拟发生一次事件
						选择器.click();*/
				$("#loginBtn").click();
			}
		});
	});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<%--使用EL表达式获取Cookie	${cookie.cookie的key.属性名}表明通过key去cookie中找对应的属性值--%>
					<div style="width: 350px;">
						<input class="form-control" id="loginAct" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<%--通过JSTL标签库(专门用于流程处理)，判断是否自动勾选十天免登录按钮--%>
						<label>
							<%--如果cookie中的用户名和密码都不为空则自动勾选--%>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd" checked>
							</c:if>
							<%--如果cookie中的用户名和密码其中有一项为空则不勾选--%>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input type="checkbox" id="isRemPwd">
							</c:if>
							 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg" style="color: red"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>