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

发送同步请求：
    window.location.href="" 的含义：window代表浏览器，location代表地址栏，href代表要输入url，也就是说通过代码实现了向浏览器地址栏键入url
发送异步请求：
    框架工具封装的函数（如这里使用的是jQuery的ajax()方法->$.ajax({url:'',date:{},datatype:'',type:'',success:function(){})
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

所有文件下载的请求只能发送同步请求(GET/POST),虽然效果看上去像异步请求
    1.只能发送同步请求： 因为返回的结果是一个文件，而不是简单的字符串、json等，ajax无法进行解析
    2.看上去像异步请求： 不让返回结果(一个文件)返回到页面上，而是返回到下载窗口上，也就是说页面并没有刷新/跳转，刷新的是下载窗口

文件下载
    jsp页面向ActivityController发送下载请求(get/post)
    -> ActivityController将生成的文件输出到jsp(**OutputStream的路径直接指向jsp文件即可**)
    -> 浏览器接收到响应信息，《《默认情况下》》会直接在显示窗口打开
       如果返回的是网页或json则可以直接打开(网页则跳转、json则暂存)
       如果返回的是Excel这种不可直接打开的文件类型，则自动调用客户端相应软件（WPS、Office等）打开
       只有实在打不开才会激活文件下载窗口，让用户保存到本地再选择用哪些软件打开

       *如果想优先激活文件下载窗口，则
        1.设置响应类型为 二进制流数据application/octet-stream
          格式：response.setContentType("application/octet-stream;charset=UTF-8");
        2.设置响应头信息Content-Disposition的值为attachment，使浏览器解析到该响应头信息后直接激活文件下载窗口
          还可以通过filename指定文件的名称及扩展名
          格式：response.setHeader("Content-Disposition","attachment;filename=my_student_list.xls");

    虽然在使用流的时候会报异常，按常理来说应try..catch捕捉，但是由于最终显示的位置为下载窗口而不是页面，因此即使进行了try..catch，前端也无法查看到
    也就是说对于文件下载而言，其Controller层的异常应直接throws向上抛出，而不是try..catch捕捉

文件上传
    所有的文件上传，上传的格式一定是跟用户提前约定好的，否则无法判断应该将当前拿到的单元格值赋给对象的哪个属性
        对于id而言，要选择自己生成而不能让用户导入，因为可能发生主键冲突（用户瞎jb写），因此会与用户约定不添加id列
        对于外键而言，一般有四种解决方案：
            1.根据name去数据库里查id（不可取，反面案例）
              可能有重名的情况，且会连接磁盘，效率很低
            2.写一个附录，提供user表，用户手动将name换为对应id（效率最高但不现实，小数据量时可用）
              用户体验不好，且数据量很大时要配置很久，只适合小数据量
            3.设置一个公共账户，登陆公共账号的人手动分配外键（最整洁但也最麻烦，至少不需要用户操作）
              不能实时插入外键，要等后期有人手动分配
            4.谁导入的谁负责，就是直接获取session域中当前登录的user（最省事，效率也不低，但是外键会被写死而不是动态的）
              这样会强行将owner设置为当前登录的用户
        对于其他字段而言，具体情况具体分析，静态字段单独拎出来赋值并与用户约定不添加该字段列，动态字段就按序读取并给对应pojo属性赋值
    总结：一般会将id（主键）、外键（这里的3、4情况）、静态字段单独拎出来赋值，
         其余字段要么为动态（就是用户需要在xls中按序提供的）手动按序赋值，要么为空字段（如这里的edit_by和edit_time）无需赋值也无需让用户提供该列

    文件上传的表单必须满足三个条件：
        1.表单组件标签只能使用<input type="file" />
        2.如果采用列表form，则请求方式只能用同步请求post
            get请求只能提交文本数据，参数长度有限制，效率高（数据直接拼接在url后   可以使用缓存机制）
            post请求能提交文本数据，还能提交二进制数据，参数长度没限制，效率较低（数据要转换放到请求体中   无缓存机制，每次访问都要重新发送请求重新获取数据）

          *如果使用异步请求，必须设置type:'post'
        3.表单的编码格式enctype只能为  multipart/form-data
            HTTP协议规定Browser每次向后台提交参数时都会对参数进行统一编码.
                默认采用application/x-www-form-urlencoded，而这种编码格式只能对文本数据进行处理
                如果采用默认的编码格式则会这样处理：浏览器向后台提交的参数都会被统一转为字符串，再将这个字符串采用urlencoded进行编码
                如果采用multipart/form-data（译为：表单数据多样性），则会阻止表单采用默认编码，即上传的是什么数据就是什么数据，不做任何处理
                      <form action="fileUpload.do" method="post" enctype="multipart/form-data">
                          <input type="file" name="myFile" />
                      </form>

           *如果要求异步请求，必须设置processData:false,	//设置ajax向后台提交参数之前，是否把参数统一转换成字符串
                         		   contentType:false,	//设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码
    总结：用jQuery发送ajax请求必不可少的几个参数设置：data:formData,	//传入FormData对象
                                                  processData:false,	//设置ajax向后台提交参数之前，是否把参数统一转换成字符串
                                                  contentType:false,	//设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码
                                                  type:'post',


    SpringMVC提供了一个MultipartFile类，专门用于接收文件类型的数据，像内置对象一样直接在Controller层的方法参数中用即可
    该方法会自动接收来自前端发送的文件，当然参数名要与前端发送的<input type="file" name=""/>的name相同，保证自动注入

    但是使用之前必须在springmvc.xml中配置： SpringMVC的文件上传解析器--CommonsMultipartResolver
    <!-- 配置文件上传解析器   id:必须是multipartResolver，不能省略/改为其他值，否则springmvc无法自动识别并拿到这个bean-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!--设置文件最大上传大小为5M（其实一般不在这里设置，一般在前端进行文件大小的过滤）-->
        <property name="maxUploadSize" value="#{1024*1024*5}"/>
        <!--设置文件的默认编码格式-->
        <property name="defaultEncoding" value="utf-8"/>
    </bean>

对于文件而言，
    直接调用其val()并不能拿到该文件，只能拿到文件的绝对路径名
        let activityFileName = $("#activityFile").val();
    想拿到该文件本身，必须先获取其dom对象，再获取其files属性
        获取dom对象：1.$("#activityFile")[0]	  2.$("#activityFile").get(0)	3.document.getElementsById("activityFile")
        let activityFile = $("#activityFile")[0].files[0];
    想获取该文件的大小：在拿到文件本身的基础上获取其size属性（以byte为最小单位）
        activityFile.size

button按钮的类别：button和submit
    <button type="submit" id="queryClueBtn">查询</button>
    <button type="button" id="queryClueBtn">查询</button>
    如果为该按钮添加事件，且发送的是异步请求，则必须使用type="button"，如果使用type="submit"则在发送异步请求后还会发送同步请求，导致功能失效
    如果为该按钮添加事件，且发送的是同步请求，则最好使用type="submit"，这样发送请求时才能携带表单数据

Controller层方法的返回类型
    1.String    常用于同步请求(get/post)，返回一个url进行重定向/请求转发
    2.Object    常用于异步请求(ajax)，返回一个json让浏览器解析
    3.void      常用于手动向前端发送文件
    TIP:同步请求不要让其返回json(就是同步请求的Controller不要让其返回值为Object)
        浏览器解析不了同步请求的json，只能解析异步请求的json 或 同步请求的url

程序只要访问磁盘就十分吃效率（建立连接->寻找磁道->读取/写入数据），要尽量减少访问这种场景的出现
    java程序访问数据库就相当于程序访问磁盘
    HSSFWorkbook的write()方法是将内存中的数据写入磁盘，也相当于程序访问磁盘

    文件导入导出的优化策略
        文件导出，将 'workbook内存->磁盘->浏览器' 模式变为 'workbook内存->浏览器'
            OutputStream out = response.getOutputStream();
            workbook.write(out);
        文件导入，将 'activityFile内存->磁盘->workbook内存' 模式变为 'activityFile内存->workbook内存'
            InputStream in = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(in);

前端请求携带数据，可以直接被包装到Controller的参数中
    1.前端发送同步请求
        对于get/post，都可以使用form表单的形式发送
        对于get，可以直接在url后添加?再拼接数据
            window.location.href="workbench/activity/exportSelectedActivityExcel.do?"+ids;  //传递的是一个属性有一/多个属性值都可
    2.前端发送异步请求（以jQuery为例）
        let ids="";
        $.each(checkeds,function (){ids += "id="+this.value+"&";});
        ids = ids.substr(0,ids.length-1);
        let formData = new FormData();
    	formData.append("activityFile",activityFile);
        $.ajax({
            url:'workbench/activity/exportSelectedActivityExcel.do',
            data:{...},    //只能传递文本，对于传递的是一个属性只有一个属性值
            data:ids,      //只能传递文本，对于传递的是一个属性有多个属性值
            data:formData, //可传递文件/文本，可传递一个属性有多个属性值
            success:function (data) {...}
        });

使用标签保存数据，以便在需要的时候能够获取到这些数据:
   给标签添加属性原则（表单组件标签：input、textArea、select...）：
       如果是表单组件标签，优先使用value属性
                         当value不方便使用时（有多个值），使用自定义属性;
       如果不是表单组件标签，不推荐使用value（个别浏览器可能会报错），推荐使用自定义属性。
   定位标签：
       优先考虑id属性,其次考虑name属性
       只有id和name属性都不方便使用时，才考虑使用自定义属性
<script />代码块中也能使用EL表达式，实际上只要在jsp页面中，el表达式在任意处都可使用
    如果想将一个el表达式的值当作变量值赋给变量，则：要在el表达式两端加引号，不然会被当做变量处理而不是字符串
    let activityId = ${requestActivity.id};         //let activityId = gysdvbf564153sdfaa;
    let activityId = '${requestActivity.id}';       //let activityId = 'gysdvbf564153sdfaa';

对于数据转存，一般放在Service层，而不放在Controller层，因为放在Controller层会显得很乱
    转存方法的返回值为void而不是int：因为这里面涉及了多个Service层和Mapper层的方法，不方便通过返回值来判断这两个方法是否都执行成功
                                  只能在该service方法中添加事务、在Controller层通过try..catch,只要没报异常就说明转存成功,否则转存失败

提供配置文件：由用户提供,保存在后台服务器上
    关于配置文件的选择：
       a).properties配置文件
              适合配置简单数据(没有属性、子类的概念,仅仅是一些基础的简单信息)，几乎没有冗余数据，解析、传输效率高
              解析相对简单：Properties,*ResourceBundle*
	   b).xml配置文件：标签语言.
	          适合配置复杂数据(通过属性、子标签几乎能表示所有复杂情景)，产生冗余数据，效率低
              解析相对复杂：jdom,*dom4j*
       总结：优先使用.properties，如果.properties不能解决再使用.xml

       这里配置可能性：约定文件名为possibility.properties,放在resources包下
                      阶段的名称（理论上来说使用id更好,不过这里由用户提供,只能用name了）做key、可能性做value

关于pojo类的属性
    数据库中的表在java中一定有实体类相对应，**数据库表中一个字段在实体类中一定有属性相对应**(数据库的字段在pojo类中必须有一个属性与之对应)
    java中的实体类在数据库中不一定有表相对应，**实体类中的属性在数据库表中也不一定有字段相对应**(在保证上面的前提下，pojo类的属性可根据需要自行添加)
    比如与阶段stage相关联的可能性possibility属性，虽然不属于数据库字段，但与pojo类有关联，可以考虑在pojo类中添加该字段
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
  选择器.val();   //用来获取元素的value属性赋值(最好是表单组件标签的value)
  选择器.val(属性值);   //给元素的value属性赋值(最好是表单组件标签的value)
  选择器.prop("属性名");//用来获取值是true/false的属性的值.例如：checked,selected,readonly,disabled等。
  选择器.prop("属性名",true/false);   //给Boolean属性赋值

  TIP:如果获取表单组件标签的value属性值：dom对象.value 或 jquery对象.val()
      如果自定义的属性，不管是什么标签，只能用：jquery对象.attr("属性名");

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
  3)使用jquery的on函数：  父选择器.on("事件类型","子选择器",function(){});
            父元素:必须是固有元素.  可以是直接父元素,也可以是间接父元素（原则上：固有父元素范围越小越好）
            事件类型：跟事件属性和事件函数一一对应。
            子选择器：目标元素,是建立在父选择器基础上的
		优点：不但能给固有元素添加事件，还能够给动态生成的元素添加事件。
		缺点：写法复杂


js截取字符串：
    str.substr(startIndex,length)  //从下标为startIndex的字符开始截取，截取length个字符
    str.substr(startIndex)  //从下标为startIndex的字符开始截取，一直截取到最后
    str.substring(startIndex,endIndex)  //从下标为startIndex的字符开始截取，截取到下标是endIndex的字符

把页面片段显示在动态显示在页面中：
    选择器.text(htmlStr)：覆盖显示在标签的内部,不支持html语言
    选择器.html(htmlStr)：覆盖显示在标签的内部,支持html语言
    选择器.append(htmlStr)：追加显示在指定标签的内部的后边,支持html语言

    选择器.after(htmlStr)：追加显示在指定标签的外部的后边,支持html语言
    选择器.before(htmlStr)：追加显示在指定标签的外部的前边,支持html语言


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


