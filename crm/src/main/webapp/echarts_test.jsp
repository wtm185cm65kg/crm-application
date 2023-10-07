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
            //基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            //指定图表的配置项和数据
            var option = {
                title: { //标题
                    text: 'ECharts 入门示例', //大标题
                    subtext:'测试副标题', //副标题
                    textStyle:{ //控制字体风格
                        fontStyle:'italic' //设置为斜体
                    }
                },
                tooltip: { //提示框
                    textStyle:{  //控制提示框风格
                        color:'blue' //设置提示框字体为蓝色
                    }
                },
                legend: { //图例
                    data:['销量','sb','进货量','点击量'] /**只会显示与系列series的名称name相匹配的图例，如这里的'sb'就不会显示*/
                },
                xAxis: { //x轴：数据项轴
                    data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                },
                yAxis: {}, //y轴：数量轴，不写表示采用自适应y轴值

                series: [{ //销量的系列
                    name: '销量', //系列的名称
                    type: 'bar', /**系列的类型：bar--柱状图，line--折线图，还有各种各样的图见官网*/
                    data: [5, 20, 36, 10, 10, 20]//系列的数据
                },{ //进货量的系列
                    name: '进货量', //系列的名称
                    type: 'line', /**系列的类型：line--折线图*/
                    data: [15, 25, 16, 40, 30, 27]//系列的数据
                },{ //点击量的系列
                    name: '点击量', //系列的名称
                    type: 'bar', /**系列的类型：bar--柱状图*/
                    data: [1, 2, 6, 4, 3, 7]//系列的数据
                }]
            };

            //将刚指定的配置项和数据显示图表显示到myChart对象(被初始化后的div对象)中
            myChart.setOption(option);



            //基于准备好的dom，初始化echarts实例
            var myDiv = echarts.init(document.getElementById('div'));
            //指定图表的配置项和数据
            var divOption = {
                title: {
                    text: '漏斗图',
                    subtext: '纯属虚构'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c}%"
                },
                toolbox: {
                    feature: {
                        dataView: {readOnly: false},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                legend: {
                    data: ['展现','点击','访问','咨询','订单']
                },
                series: [
                    {
                        name: '预期',
                        type: 'funnel',
                        left: '10%',
                        width: '80%',
                        label: {
                            formatter: '{b}预期'
                        },
                        labelLine: {
                            show: false
                        },
                        itemStyle: {
                            opacity: 0.7
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}预期: {c}%'
                            }
                        },
                        data: [
                            {value: 60, name: '访问'},
                            {value: 40, name: '咨询'},
                            {value: 20, name: '订单'},
                            {value: 80, name: '点击'},
                            {value: 100, name: '展现'}
                        ]
                    },
                    {
                        name: '实际',
                        type: 'funnel',
                        left: '10%',
                        width: '80%',
                        maxSize: '80%',
                        label: {
                            position: 'inside',
                            formatter: '{c}%',
                            color: '#fff'
                        },
                        itemStyle: {
                            opacity: 0.5,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        emphasis: {
                            label: {
                                position: 'inside',
                                formatter: '{b}: {c}%'
                            }
                        },
                        data: [
                            {value: 30, name: '访问'},
                            {value: 10, name: '咨询'},
                            {value: 65, name: '订单'},
                            {value: 15, name: '点击'},
                            {value: 80, name: '展现'}
                        ]
                    }
                ]
            };
            //将刚指定的配置项和数据显示图表显示到myChart对象(被初始化后的div对象)中
            myDiv.setOption(divOption);
        });
    </script>
</head>

<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 600px;height:400px;"></div>

    <div id="div" style="width: 600px;height:400px;"></div>
</body>
</html>
