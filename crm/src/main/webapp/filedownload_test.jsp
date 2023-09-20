<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
  <base href="<%=basePath%>">
  <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
  <title>文件下载演示</title>

  <script type="text/javascript">
    $(function (){
      /*给下载按钮添加单价事件*/
      $("#fileDownloadBtn").click(function (){
        window.location.href="fileDownload.do";
      });
    });

  </script>
</head>
<body>
<button type="button" id="fileDownloadBtn">下载文件</button>
</body>
</html>
