<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
  <base href="<%=basePath%>">
  <title>演示echarts插件</title>

  <%--第一步：引入插件(需要依赖jQuery)--%>
  <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
  <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>

  <%--第二步：创建容器--%>
  <script type="text/javascript">
    //当容器加载完成之后，对容器调用工具函数
    $(function () {
      //页面加载完后直接发送查询请求
      $.ajax({
        url:'workbench/chart/transaction/queryCountOfTranGroupByStage.do',
        type:'post',
        dataType:'json',
        success:function (data){
          /**调用echarts工具函数，显示漏斗图*/
          //基于准备好的dom，初始化echarts实例
          var myChart = echarts.init(document.getElementById('main'));

          //指定图表的配置项和数据
          var option = {
            title: {
              text: '交易统计图表',
              subtext: '交易表中各个阶段的数量'
            },
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c}"
            },
            toolbox: {
              feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
              }
            },
            legend: { //图例
              data: ['资质审查','丢失的线索','确定决策者']
            },
            series: [{
                name: '数据量',
                type: 'funnel', /**表示采用漏斗图*/
                left: '10%',
                width: '80%',
                label: {
                  formatter: '{b}'
                },
                labelLine: {
                  show: true
                },
                itemStyle: {
                  opacity: 0.7
                },
                emphasis: {
                  label: {
                    position: 'inside',
                    formatter: '{b}: {c}'
                  }
                },
                /*data: [
                  {value: 30, name: '访问'},
                  {value: 10, name: '咨询'},
                  {value: 65, name: '订单'},
                  {value: 15, name: '点击'},
                  {value: 80, name: '展现'}
                ]*/
                data:data /**ajax返回的结果data就是一个封装了name和value参数的数组(List<FunnelIVO>)的json字符串,因此直接传data即可*/
              }
            ]
          };

          //将刚指定的配置项和数据显示图表显示到myChart对象(被初始化后的div对象)中
          myChart.setOption(option);
        }
      });
    });
  </script>
</head>

<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
