<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>文件上传演示</title>
</head>
<body>
<%--
    文件上传的表单必须满足三个条件：
        1.表单组件标签只能使用<input type="file" />
        2.请求方式只能用同步请求post
            get请求只能提交文本数据，参数长度有限制，效率高（数据直接拼接在url后   可以使用缓存机制）
            post请求能提交文本数据，还能提交二进制数据，参数长度没限制，效率较低（数据要转换放到请求体中   无缓存机制，每次访问都要重新发送请求重新获取数据）
        3.表单的编码格式enctype只能为  multipart/form-data
            HTTP协议规定Browser每次向后台提交参数时都会对参数进行统一编码.
                默认采用application/x-www-form-urlencoded，而这种编码格式只能对文本数据进行处理
                如果采用默认的编码格式则会这样处理：浏览器向后台提交的参数都会被统一转为字符串，再将这个字符串采用urlencoded进行编码

                如果采用multipart/form-data（译为：表单数据多样性），则会阻止表单采用默认编码，即上传的是什么数据就是什么数据，不做任何处理
--%>
<form action="fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile" /><br>
    <input type="text" name="username" /><br>
    <input type="submit" value="提交" /><br>
</form>
</body>
</html>
