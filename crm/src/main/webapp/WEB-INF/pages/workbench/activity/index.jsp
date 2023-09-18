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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">
	$(function(){

		/*给'创建'按钮添加单击事件*/
		$("#createActivityBtn").click(function (){
			//初始化操作（使用js操作模态窗口的显示和隐藏的好处就在于可以在隐藏/显示之前进行一些操作）
			/*因为只是隐藏了模态窗口，因此再次打开时还会保留上次填写的信息 (除非刷新了网页)
			  因此每次点击'创建'按钮还要清除上次填写的信息*/
			//重置表单
			$("#createActivityForm").get(0).reset();

			//弹出'创建市场活动的模态窗口'
			$("#createActivityModal").modal("show");
		});

		/*给'保存'按钮添加单击事件*/
		$("#saveBtn").click(function (){
			//收集参数
			let owner=$("#create-marketActivityOwner").val();
			let name=$.trim($("#create-marketActivityName").val());
			let startDate=$.trim($("#create-startTime").val());
			let endDate=$.trim($("#create-endTime").val());
			let cost=$.trim($("#create-cost").val());
			let description=$("#create-describe").val();

			//表单验证
			if (owner==""){
				alert("所有者不能为空！");
				return;
			}
			if (name==""){
				alert("名称不能为空！");
				return;
			}
			//先判断是否填写了开始和结束日期，如果填写了再进行判断
			if (startDate!=""&&endDate!=""){
				//使用字符串大小规则比较两个日期大小(弱类型语言比较大小一律用 > / < / = / ...)
				if (startDate>endDate){
					alert("开始日期不能大于结束日期！");
					return;
				}
			}
			/*使用正则表达式判断具体字符串(这里的cost是一个String)是否符合匹配模式*/
			/*
			  正则表达式：
			     1，语言，语法：定义字符串的匹配模式，可以用来判断指定的具体字符串是否符合匹配模式。
			     2,语法通则：
			      *1)//:在js中定义一个正则表达式.  var regExp=/...../;
			       2)^：匹配字符串的开头位置
			         $: 匹配字符串的结尾
			       3)[]:匹配指定字符集中的一位字符。 var regExp=/^[abc]$/;
			                                    var regExp=/^[a-z0-9]$/;
			       4){}:匹配次数.var regExp=/^[abc]{5}$/;
			            {m}:匹配m此
			            {m,n}：匹配m次到n次
			            {m,}：匹配m次或者更多次
			       5)特殊符号：
			         \d:匹配一位数字，相当于[0-9]
			         \D:匹配一位非数字
			         \w：匹配所有字符，包括字母、数字、下划线。
			         \W:匹配非字符，除了字母、数字、下划线之外的字符。

			         *:匹配0次或者多次，相当于{0,}
			         +:匹配1次或者多次，相当于{1,}
			         ?:匹配0次或者1次，相当于{0,1}
			 */
			let regExp=/^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数！");
				return;
			}

			//发送Ajax请求
			$.ajax({
				url:'workbench/activity/saveCreateActivity.do',
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						//刷新市场活动列，显示第一页数据，《《保持每页显示条数不变（动态获取pageSize，详见$(".mydate").datetimepicker({})的success）》》
						queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息创建失败
						$("#msg").text(data.message);
						//模态窗口不关闭 (可以省略不写，但是习惯上可以添加，不会对原弹出的模态窗口有任何影响)
						$("#createActivityModal").modal("show");
						//市场活动列表也不刷新
					}
				}
			});
		});

		/*当容器加载完成之后，对容器调用工具函数
		  使用类选择器选中开始日期和结束日期这两个标签（其中class="mydate"是自己手动在原有class值上新加的）*/
		//$("input[class='mydate']").datetimepicker({
		$(".mydate").datetimepicker({
			language:'zh-CN',  //采用的语言
			format:'yyyy-mm-dd',  //日期的格式（选择的日期在后台被格式化成何种格式）
			minView:'month', //可以选择的最小视图（这里具体到月即可，在月的窗口可以选择到日.	还可以具体到小时，这样就可以选择到多少分了）
			initialDate:new Date(),  //初始化显示的日期（一般将当前日期当作初始的默认选择日期）
			autoclose:true,  //设置选择完日期或者时间之后，日否自动关闭日历,默认是false
			todayBtn:true,  //设置是否显示"今天"按钮,默认是false
			clearBtn:true  //设置是否显示"清空"按钮，默认是false（其实只有'Clear'，想让其显示中文的'清空'还要去bootstrap-datetimepicker.js中手动修改）
		});

		//初始时按默认条件进行查询
		queryActivityByConditionForPage(1,10);

		//给'查询'按钮绑定单击事件，再次查询时按文本框中的条件进行条件查询
		$("#queryActivityBtn").click(function (){
			//实现点击"查询"按钮,保持每页显示条数不变的需求
			/*bs_pagination的getOption方法：获取属性名
			  $("#element_id").bs_pagination('getOption', 'option_name');
			  不希望每次写死为queryActivityByConditionForPage(1,10)，这样每次输入的'每页显示的条数rowsPerPage'不会被保存
			  因此可以选择动态获取pageSize（就是获取之前键入的pageSize信息）*/
			queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});

		//给'全选按钮'添加单击事件，选中全选按钮后该页的所有checkbox都被选中,再次点击所有checkbox都取消选中
		$("#checkAll").click(function (){
			/*if (this.checked) {
				//写法一：类选择器
				//$(".deleteCheckbox").checked=true;
				//写法二：选中id="tBody"下的所有一级<input />，该写法不可取（input并不是其一级子标签，而是二级）
				//$("#tBody>input").checked=true;
				//写法三：选中id="tBody"下的所有<input type='checkbox'/>，区别写法二只是将'>'换为了空格
				$("#tBody input[type='checkbox']").prop("checked",true);
			}else{
				$(".deleteCheckbox").prop("checked",false);
			}*/
			//精简写法
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		});

		//给所有单选按钮添加单击事件，当所有单选按钮选中则全选按钮也被选中，只要有一个单选按钮没被选中则全选按钮就不会被选中
		/*这种方式只能给固有元素添加事件（固有元素：调用函数前元素已经在页面存在，这个元素就是固有元素）
		  而单选按钮是动态生成的（详见queryActivityByConditionForPage()的success），因此无法向其绑定事件
		  	虽然这里看到的结果是已经生成了单选按钮，但实际上该行代码在queryActivityByConditionForPage()执行完之前就已经运行完了
		  	因为queryActivityByConditionForPage(1,10)采用的是异步请求，因此程序并不会等待该函数执行完毕才执行当前代码
		  	而是将queryActivityByConditionForPage(1,10)交给另一个线程，主线程继续向下运行
		  	也就是说执行到该行代码时，刚刚的queryActivityByConditionForPage(1,10)还没执行完成，类选择器批量绑定事件自然也无法完成
		  解决方法：采用同步请求get/post，而不是异步请求ajax，但这显然不现实*/
		/*$(".deleteCheckbox").click(function (){
			//如果列表中所有checkbox标签的长度与所有被选中的checkbox标签的长度相等，则说明全选按钮应被选中，反之则不会被选中
			/!*if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true);
			}else{
				$("#checkAll").prop("checked",false);
			}*!/
			//精简写法
			$("#checkAll").prop("checked",$(".deleteCheckbox").size()==$("#tBody input[type='checkbox']:checked").size());
		});*/
		//这种方式不但可以给静态元素添加事件，还可以给动态元素添加事件，是jQuery后来推出的添加事件方式
		/*使用jquery的on函数：父选择器.on("事件类型",子选择器,function(){});
                   父元素:必须是固有元素.  可以是直接父元素,也可以是间接父元素（原则上：固有父元素范围越小越好）
				   事件类型：跟事件属性和事件函数一一对应。
				   子选择器：目标元素,是建立在父选择器基础上的*/
		$("#tBody").on("click","input[type='checkbox']",function (){
			$("#checkAll").prop("checked",$(".deleteCheckbox").size()==$("#tBody input[type='checkbox']:checked").size());
		});

		//给'删除按钮'添加单击事件，确认删除后所有被选中的checkbox都被删除
		$("#removeActivityBtn").click(function (){
			//收集参数
			let checkeds=$("#tBody input[type='checkbox']:checked");

			//进入判断：数组不为空时才会弹出确认框，否则提示用户先选择才能删除，点击确认后才会发送ajax请求执行删除操作
			if (checkeds.size()!=0){
				if (confirm("您确定要删除这些数据吗？")){

					let ids="";
					$.each(checkeds,function (){
						ids += "id="+this.value+"&";
					});
					/*js截取字符串：
                        str.substr(startIndex,length)  //从下标为startIndex的字符开始截取，截取length个字符
                        str.substring(startIndex,endIndex)  //从下标为startIndex的字符开始截取，截取到下标是endIndex的字符*/
					//ids = ids.substring(0,checkeds.size()-1);
					ids = ids.substr(0,ids.length-1);
					alert(ids);

					//发送ajax请求
					$.ajax({
						url:'workbench/activity/removeActivityByIds.do',
						/*向后台提交字符串数据的另一种写法	 ->  data:k1=v1&k1=v2&....
							  不但能够向后台提交一个参数名对应一个参数值的数据(a=1&b=2&c=3&...)
				   			  还能向后台提交一个参数名对应多个参数值的数据(a=1&a=2&a=3&...)
			 			  应用场景：当需要传递'一个参数名对应多个参数值'的字符串时*/
						data:ids,
						type:'post',
						dataType:'json',
						success:function (data){
							if (data.code=="1"){
								//刷新市场活动列，显示第一页数据，《《保持每页显示条数不变（动态获取pageSize，详见$(".mydate").datetimepicker({})的success）》》
								queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert(data.message);
							}
						}
					});
				}
			}else{
				alert("请先选择需要删除的市场活动！")
			}
		});

		/*给'修改'按钮添加单击事件*/
		$("#modifyActivityBtn").click(function (){
			//获取列表中被选中的checkbox
			let checkeds=$("#tBody input[type='checkbox']:checked");

			//当只选中一条数据时才能进行修改，否则弹出提示信息
			if (checkeds.size()==0){
				alert("请先选择要修改的活动！");
			}else if(checkeds.size()==1){
				//收集参数(id是被选中的活动对应的)
				//let id=checkeds.val();
				//let id=checkeds.get(0).value;
				let id=checkeds[0].value;

				//要将id放到隐藏域中，以标识查询的市场活动，更新时还会根据这个id去更新对应的市场活动
				$("#edit-id").val(id);

				$.ajax({
					url:'workbench/activity/queryActivityById.do',
					data:{
						id:id
					},
					type:'post',
					dataType:'json',
					success:function (data){
						//将查询结果回显到表格中
						//只需要设置好owner即可，浏览器会自动将符合owner的option自动设为默认option
						$("#edit-marketActivityOwner").val(data.owner);
						$("#edit-marketActivityName").val(data.name);
						$("#edit-startTime").val(data.startDate);
						$("#edit-endTime").val(data.endDate);
						$("#edit-cost").val(data.cost);
						$("#edit-describe").val(data.description);

						//弹出'创建市场活动的模态窗口'
						$("#editActivityModal").modal("show");
					}
				});
			}else{
				alert("每次只能选择一个要修改的活动！");
			}
		});

		/*给'更新'按钮添加单击事件*/
		$("#saveEditActivityBtn").click(function (){
			//收集参数
			//需要获取id（放在隐藏域中），因为要根据id进行update
			let id=$("#edit-id").val();
			let owner=$("#edit-marketActivityOwner").val();
			let name=$.trim($("#edit-marketActivityName").val());
			let startDate=$("#edit-startTime").val();
			let endDate=$("#edit-endTime").val();
			let cost=$.trim($("#edit-cost").val());
			let description=$.trim($("#edit-describe").val());

			//表单验证
			if (owner==""){
				alert("所有者不能为空！");
				return;
			}
			if (name==""){
				alert("名称不能为空！");
				return;
			}
			//先判断是否填写了开始和结束日期，如果填写了再进行判断
			if (startDate!=""&&endDate!=""){
				if (startDate>endDate){
					alert("开始日期不能大于结束日期！");
					return;
				}
			}
			//判断cost是否符合正则表达式--为非负整数
			let regExp=/^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数！");
				return;
			}

			//发送Ajax请求
			$.ajax({
				url:'workbench/activity/modifyActivity.do',
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description,
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//关闭修改模态窗口
						$("#editActivityModal").modal("hide");
						//刷新市场活动列，要保证pageNo和pageSize都不变
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息更新失败
						$("#msg").text(data.message);
						//修改模态窗口不关闭 (可以省略不写，但是习惯上可以添加，不会对原弹出的模态窗口有任何影响)
						$("#editActivityModal").modal("show");
						//市场活动列表也不刷新
					}
				}
			});
		});
	});

	/*在入口函数外封装 按查询条件检索数据 功能
	  这种函数要定义在jquery的回调函$(function(){});外，才能在回调函数中调用*/
	function queryActivityByConditionForPage(pageNo,pageSize){
		//目标：用户在市场活动主页面填写查询条件,点击"查询"按钮,显示所有符合条件的数据的第一页
		//收集参数
		let name=$("#query-name").val();
		let owner=$("#query-owner").val();
		let startDate=$("#query-startDate").val();
		let endDate=$("#query-endDate").val();
		//let pageNo=1;
		//let pageSize=10;

		//发送请求
		$.ajax({
			url:'workbench/activity/queryActivityByConditionForPage.do',
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data){
				//替换总记录条数中的值（这里使用分页框架bs_pagination，因此不再需要该行语句）
				//$("#totalRowsB").text(data.totalRows);

				/*显示市场活动的列表,需要使用遍历
				  这里不使用JSTL遍历是因为现在是在js代码块中，不能使用，且这里只是使用Ajax接收数据，四个域中并没有数据，也无法使用EL获取数据*/
				let htmlStr="";
				$.each(data.activityList,function (index,obj){
					htmlStr+="<tr class='active'>";
					htmlStr+="<td><input type='checkbox' class='deleteCheckbox' value='"+obj.id+"'/></td>";
					htmlStr+="<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='detail.jsp';\">"+obj.name+"</a></td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.startDate+"</td>";
					htmlStr+="<td>"+obj.endDate+"</td>";
					htmlStr+="</tr>";
				});
				$("#tBody").html(htmlStr);

				//每次换页完毕都取消全选按钮
				$("#checkAll").prop("checked",false);

				//根据pageSize和pageNo计算总页数totalPages，以便传入分页信息中
				let totalPages=1;
				if (data.totalRows%pageSize==0){
					totalPages=data.totalRows/pageSize;
				}else{
					/*parseInt(): JavaScript的系统函数，截取浮点型的整数部分（相当于向下取整）
                    * 还有一个很NB的系统函数: 模拟执行字符串代码eval()，如何使用自行查阅*/
					totalPages=parseInt(data.totalRows/pageSize)+1;
				}

				//调用bs_pagination工具函数显示翻页信息
				/*将分页操作放入queryActivityByConditionForPage()方法发送ajax的success函数中
                  因为只有放在这里才能确保动态的获取到totalRows*/
				$(function() {
					$("#demo_pag1").bs_pagination({
						currentPage:pageNo,   //当前页号（是打开页面时的默认页，不是showRowsPerPage跳转后的默认页），相当于pageNo，默认值为1

						//rowsPerPage、totalRows、totalPages要始终保持一致：rowsPerPage*totalPages=totalRows
						rowsPerPage:pageSize,    //每页显示的条数，相当于pageSize，默认值为10
						totalRows:data.totalRows,    //总条数，默认值为1000
						totalPages:totalPages,  //**总页数，必填参数，没有默认值(如果没有指定rowsPerPage，则会自动计算rowsPerPage = 总条数totalRows/总页数totalPages)

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
							/*在onChangePage中手动刷新页面的方式：调用其爹--queryActivityByConditionForPage(pageNo,pageSize)
							只有每次切换页号时才会执行onChangePage的函数
							因此不会无限调用queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage)而陷入死循环*/
							queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
						}
					});
				});
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="createActivityForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <%--动态显示request中的数据--%>
								  <c:forEach items="${requestUsers}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<%--给开始日期添加一个新的class属性值mydate--%>
								<input type="text" class="form-control mydate" id="create-startTime" readonly />
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<%--给结束日期添加一个新的class属性值mydate--%>
								<input type="text" class="form-control mydate" id="create-endTime" readonly />
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<%--添加一个隐藏域用于保存id--%>
		<input type="hidden" id="edit-id"/>
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<%--动态显示request中的数据--%>
									<c:forEach items="${requestUsers}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-startTime" readonly />
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-endTime" readonly />
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name"/>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner"/>
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text"  id="query-endDate">
				    </div>
				  </div>

				  <%--将type="submit"改为type="button"，因为点击查询按钮应发送异步请求ajax，如果是submit则表示发送的是同步请求--%>
				  <%--<button type="submit" class="btn btn-default">查询</button>--%>
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" id="modifyActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="removeActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
				<%--
					美工写的分页查询div不够强大，这里引入bs_pagination框架
					由于div是块元素，为了美观将该div放入这个div中
				--%>
				<div id="demo_pag1"></div>
			</div>

			<%--美工写的分页查询div不够强大，这里引入bs_pagination框架--%>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
		</div>
		
	</div>
</body>
</html>