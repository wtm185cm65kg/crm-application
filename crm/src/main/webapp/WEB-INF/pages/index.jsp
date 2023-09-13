<%--
	将.html重命名为.jsp不能直接重命名,一定要进行以下操作:
		需要先将html头<!DOCTYPE html>换为jsp头<%@ page contentType="text/html;charset=UTF-8" language="java" %>
		然后再Rename,否则会因为html和jsp编码格式不一致而出现乱码

	原因：IDEA对heml(ISO885-1)和jsp(UTF-8)采用的编码格式是不同的,将html直接重命名为jsp则会有中文乱码问题
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		/*
		不能在最前端加'/',
		因为当前jsp在根目录下,因此不加base标签的默认路径就是 'http://localhost:8080/项目名/'
		如果"settings/qx/user/toLogin.do"则拼接为 http://localhost:8080/项目名/settings/qx/user/toLogin.do
		如果"/settings/qx/user/toLogin.do"则相当于重定向到 http://localhost:8080/settings/qx/user/toLogin.do,会报404
		window.location.href = "/settings/qx/user/toLogin.do";
		*/
		window.location.href = "settings/qx/user/toLogin.do";
	</script>
</body>
</html>