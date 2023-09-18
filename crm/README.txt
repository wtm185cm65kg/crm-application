对于前端而言,image和jquery包不需要被保护,否则无法正常使用
            settings和workbench包需要被保护,因为存放了机密文件

Controller的创建和起名规则：
    创建规则->一个资源目录对应一个Controller
    起名规则->最好起资源目录名而不是具体资源名

controller包的放置规则：
    一个crm包下分为三个子包->web、settings、workbench、commons
        web->对应前端的首页(独立于settings和workbench包的资源，即总资源)
        settings->有关系统管理的(一般为登录页)，对应前端的settings包
        workbench->有关工作台的(一般为主页面)，对应前端的workbench包
        commons->公共包，所有包都可访问(比如这里用于存放专门用于封装json的类)
    这样安排主要是便于维护

关于选择同步请求(get/post)还是异步请求(ajax)
    1.响应结果需要刷新整个页面,使用同步请求(异步请求也可以实现，但还是优先选择同步)
    2.响应结果只需刷新局部页面,使用异步请求
    3.响应结果可能刷新整个页面也可能刷新局部页面,使用异步请求

点击submit按钮表示提交同步请求（get/post）
如果想点击后发送异步请求（ajax）则需要将按钮改为button，并给该按钮绑定点击事件，使用jQuery给单击事件绑定发送Ajax请求

选择封装json时,为了让方法更通用，一般选择方法返回值为Object

window.location.href="" 的含义：window代表浏览器，location代表地址栏，href代表要输入url，也就是说通过代码实现了向浏览器地址栏键入url

关于window.open("url","参数/iframe的name")
    当为'iframe的name'时,表示该url在iframe中打开
    当为参数时：1._blank  表示在新页面打开 (可通过parent和children属性实现'类模态页面'与主页面的数据交互。以前的使用方法，这样很麻烦)
               2.表示弹出模态窗口(本质上是一个div，且该div就属于该主页面，很容易进行数据交互。属于革命性技术，意义重大)
                 看到的远近视觉效果由z坐标实现
                 初始值z-index<0，被主页面覆盖，因此看不到
                 设置为z-index>0时，就会显示在主页面之上并给人一种'弹出'的视觉效果
                 用bootstrap来控制z-index的大小(需要引入bootstrap.min.js和bootstrap.min.css)
    如何实现模态窗口的显示与隐藏？
        方式一：通过data-toggle="modal" data-target="#div的id"显示
                   data-dismiss="modal"隐藏(一般在button标签中使用)
        方式二：通过js函数控制 (更灵活，程序员经常用，这两个方法也是bootstrap框架中定义的)
                选择器("#div的id").modal("show")  显示
                选择器("#div的id").modal("hide")  隐藏
        一般美工使用方式一（仅展示效果即可），程序员使用方式二（要实现功能，采用这种方式可以在进行隐藏/显示操作前做一些初始化操作）
        当程序员接手美工的html界面时，删掉按钮上绑定的模态窗口(如<button data-toggle="modal" data-target="#createActivityModal"/>)
                                   并给这个按钮一个id，在js中通过id选择器来操作模态窗口的展示效果
                                        <button id="createActivityBtn"/>
                                        $("#createActivityBtn").click(function (){
                                            $("#createActivityModal").modal("show");
                                        });

使用UUID生成一个独一无二的id：UUID.randomUUID().toString().replaceAll("-","")
    UUID.randomUUID()可以生成一个独一无二的UUID,返回值类型为UUID
    UUID.randomUUID().toString()是为了将UUID转为String类型
    UUID.randomUUID().toString().replaceAll("-","") 是因为自动生成的UUID中间会有'-',想将'-'去除需要使用replaceAll("-",""),且去除后长度才为32位,才符合字段要求

在Controller层
    查询方法直接调用即可（一个controller方法里可调用多个select语句）
    而CUD方法一般要进行try..catch捕捉异常（一个controller方法里只能调用service中的一个CUD语句，想同时调用多个CUD可在service的一个CUD方法中调用另一个CUD方法）

表选择内连接还是外连接：
    看从表外键是否可以为空，可为空要使用外连接，不为空使用内连接即可

关于Controller层封装参数的规则
    如果做查询条件/参数间不属于一个实体类对象/无实体类的主键id，封装成map
    如果做CUD/参数间属于一个实体类对象且有主键id，封装成实体类对象

-------------------------------------------------j--s--p--相--关-------------------------------------------------------
将.html重命名为.jsp不能直接重命名,一定要进行以下操作:
		需要先将html头<!DOCTYPE html>换为jsp头<%@ page contentType="text/html;charset=UTF-8" language="java" %>
		然后再Rename,否则会因为html和jsp编码格式不一致而出现乱码
原因：IDEA对html(ISO885-1)和jsp(UTF-8)采用的编码格式是不同的,将html直接重命名为jsp则会有中文乱码问题


src/main/webapp/WEB-INF/pages/settings/qx/user/login.jsp中的../../../jquery一直不需要变动
因为com.zzk.crm.settings.web.controller.UserController的请求转发本就是从/WEB-INF/pages开始的,因此../../../jquery可顺利遍历到
但实际开发中一般还是这样写 'http://127.0.0.1:8080/crm/jquery'
可以将'http://127.0.0.1:8080/crm/'单独提出来放入<base />标签中
并且可以通过动态的方式获取(以下也可以使用el表达式)
<%
  String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<base href="<%=basePath%>" />
这样就可以直接写为'jquery'了
--------------------------------
以后只要将html改为jsp：1.先改文件头    2.动态获取url头并拼接到base标签，并将原html中的地址微调


jquery的回调函$(function(){});在页面加载完成时才会被调用，相当于window.onload(function(){});


使用jquery获取指定元素的指定属性的值：
  选择器.attr("属性名");    //用来获取那些值不是true/false的属性的值.
  选择器.attr("属性名",属性值);   //给非Boolean属性赋值
  选择器.val();   //用来获取元素的value属性赋值
  选择器.val(属性值);   //给元素的value属性赋值
  选择器.prop("属性名");//用来获取值是true/false的属性的值.例如：checked,selected,readonly,disabled等。
  选择器.prop("属性名",true/false);   //给Boolean属性赋值


jquery事件函数的用法(这里以单击事件click为例)：
   1.给指定的元素添加事件
        选择器.click(function(){js代码});
   2.在指定的元素上模拟发生一次事件
        选择器.click();


在jsp中使用EL表达式获取Cookie：
    ${cookie.cookie的key.属性名}  表明通过key去cookie中找对应的属性值
    不能直接写${cookie的key.属性名}，因为这样会默认从四个作用域里找，而cookie并不存放在作用域中
    'cookie.cookie的key' 是为了获取对应的Cookie
    'cookie.cookie的key.属性名' 是在获取了对应Cookie的基础上去获取这个cookie的属性值(如value、maxAge、path等)
    可以理解为cookie是除四个作用域外的特殊作用域
    类似的作用域还有：param (获取地址栏中url?name=value&name=value&...的name=value)


关于jQuery日期的输入，一般不会选择让用户自行输入（因为后端使用String处理日期，用户随便输入有风险且格式不统一）
    解决方式一：提示+判断，用户何时输入合法何时才算完.      这显然不可取，用户体验很差
    解决方式二：提供一个界面让用户选择日期，统一将选择的日期格式化为想要的格式
        引入 bootstrap-datetimepicker.min.css 和 bootstrap-datetimepicker.js
        使用选择器选择好想要的标签后，调用函数库中的datetimepicker函数，设置好一些参数即可
            $(".mydate").datetimepicker({
                language:'zh-CN',  //采用的语言
                format:'yyyy-mm-dd',  //日期的格式（选择的日期在后台被格式化成何种格式）
                minView:'month', //可以选择的最小视图（这里具体到月即可，在月的窗口可以选择到日.	还可以具体到小时，这样就可以选择到多少分了）
                initialDate:new Date(),  //初始化显示的日期（一般将当前日期当作初始的默认选择日期）
                autoclose:true,  //设置选择完日期或者时间之后，日否自动关闭日历,默认是false
                todayBtn:true,  //设置是否显示"今天"按钮,默认是false
                clearBtn:true  //设置是否显示"清空"按钮，默认是false（其实只有'Clear'，想让其显示中文的'清空'还要去bootstrap-datetimepicker.js中手动修改）
            });
        同时还要将原来的文本框属性值添加readonly，防止用户选择好之后还能对其擅自修改

分页查询的实现
    见webapp/pagination_test.jsp

regExp.test(String属性)：判断该属性的属性值是否符合正则表达式，返回值为Boolean

引入外部js和css时：
    css没有引入顺序，放在哪里引入都可以
    js有引入顺序，先引入被依赖的js
        这里的依赖关系
            bootstrap-datetimepicker.zh-CN.js->bootstrap-datetimepicker.js->bootstrap.min.js->jquery-1.11.1-min.js


在页面中给元素添加事件语法：
  1)原始JS：   onxxxx="function(){}"
  2)使用jquery对象： 选择器.xxxx(function(){});
        缺点：只能给固有元素添加事件
  3)使用jquery的on函数：  父选择器.on("事件类型",子选择器,function(){});
            父元素:必须是固有元素.  可以是直接父元素,也可以是间接父元素（原则上：固有父元素范围越小越好）
            事件类型：跟事件属性和事件函数一一对应。
            子选择器：目标元素,是建立在父选择器基础上的
		优点：不但能给固有元素添加事件，还能够给动态生成的元素添加事件。
		缺点：写法复杂


js截取字符串：
    str.substr(startIndex,length)  //从下标为startIndex的字符开始截取，截取length个字符
    str.substring(startIndex,endIndex)  //从下标为startIndex的字符开始截取，截取到下标是endIndex的字符


jQuery的ajax向后台发送请求时，可以通过data提交参数,data的数据格式有三种格式：
  1)data:{
       k1:v1,
       k2:v2,
       ....
    }
    优势：操作简单
    劣势：1.只能向后台提交一个参数名对应一个参数值的数据
	     2.只能向后台提交字符串数据

  2)data:k1=v1&k2=v2&....
     优势：不但能够向后台提交一个参数名对应一个参数值的数据(a=1&b=2&c=3&...)
          还能向后台提交一个参数名对应多个参数值的数据(a=1&a=2&a=3&...)
     劣势：1.操作麻烦
          2.只能向后台提交字符串数据

  3)data:FormData对象
     优势：不但能提交字符串数据，
          还能提交二进制数据
     劣势：操作更复杂


