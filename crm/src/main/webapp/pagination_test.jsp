<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <!--  BOOTSTRAP -->
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <title>演示bootstrap分页插件(bs_pagination)的使用</title>
    <script type="text/javascript">
        $(function() {
            $("#demo_pag1").bs_pagination({
                currentPage:22,   //当前页号（是打开页面时的默认页，不是showRowsPerPage跳转后的默认页），相当于之前的pageNo，默认值为1

                //rowsPerPage、totalRows、totalPages要始终保持一致：rowsPerPage=totalRows/totalPages
                rowsPerPage:20,    //每页显示的条数，相当于之前的pageSize，默认值为10
                totalRows:1000,    //总条数，默认值为1000
                totalPages:50,  //**总页数，必填参数，没有默认值(如果没有指定rowsPerPage，则会自动计算rowsPerPage = 总条数totalRows/总页数totalPages)

                visiblePageLinks:10,    //可见的页链接，表示每次能显示几个页标签（每次能看到当前页在内的前后N页），默认值为5
                showGoToPage: true,     //是否显示'跳转栏'(输入页号进行跳转,用于显示/更改currentPage,只有在总页数>visiblePageLinks时才显示),默认为true
                showRowsPerPage: true,  //是否显示'每页显示的条数栏'(输入每页显示的条数,用于显示/更改rowsPerPage,每次输入后会自动跳到第1页而非第currentPage页),默认为true
                showRowsInfo: true,     //是否显示记录的信息  471-480 of 1000 记录 (p.48/50)，默认为true

                /*这个方法尤为关键，因为可以获取分页的信息.
                  在这之前只是让前端实现了分页效果，如何把分页信息传给后端，让后端根据分页信息进行分页查询
                  这个方法可以将分页信息传出去，以便让后端获取*/
                // returns page_num(currentPage) and rows_per_page(rowsPerPage) after a link has clicked
                onChangePage: function(event,pageObj) { //用户每次切换页号currentPage，都会触发这段代码
                    /*切换currentPage的场景：1.手动点击页号
                    *                       2.在'跳转栏'中输入页号跳转
                    *                       3.在'每页显示的条数栏'输入条数自动跳转到第1页*/
                    alert("pageNo: 第["+pageObj.currentPage+"]页");
                    alert("pageSize: 每页显示["+pageObj.rowsPerPage+"]条记录");
                }
            });
        });
    </script>
</head>
<body>
<div id="demo_pag1"></div>
</body>
</html>
