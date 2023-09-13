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


关于选择同步请求(get/post)还是异步请求(ajax)
    1.响应结果需要刷新整个页面,使用同步请求(异步请求也可以实现，但还是优先选择同步)
    2.响应结果只需刷新局部页面,使用异步请求
    3.响应结果可能刷新整个页面也可能刷新局部页面,使用异步请求


选择封装json时,为了让方法更通用，一般选择方法返回值为Object

window.location.href="" 的含义：window代表浏览器，location代表地址栏，href代表要输入url，也就是说通过代码实现了向浏览器地址栏键入url

使用jquery获取指定元素的指定属性的值：
  选择器.attr("属性名");//用来获取那些值不是true/false的属性的值.
  选择器.prop("属性名");//用来获取值是true/false的属性的值.例如：checked,selected,readonly,disabled等。

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