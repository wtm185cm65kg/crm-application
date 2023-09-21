<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">
	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		/*$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});*/
		/*修复小bug：新添加的备注没有修改、删除两个小图标及其效果
		* 原因：新添加的备注是动态的，而原写法是给静态元素添加事件，并不能给动态元素添加事件，因此要使用on函数给动态元素添加事件*/
		$("#remarkDivList").on("mouseover",".remarkDiv",function (){
			$(this).children("div").children("div").show();
		});
		$("#remarkDivList").on("mouseout",".remarkDiv",function (){
			$(this).children("div").children("div").hide();
		});
		$("#remarkDivList").on("mouseover",".myHref",function (){
			$(this).children("span").css("color","red");
		});
		$("#remarkDivList").on("mouseout",".myHref",function (){
			$(this).children("span").css("color","#E6E6E6");
		});

		/*给'保存按钮'添加单击事件*/
		$("#saveCreateActivityRemark").click(function (){
			//收集参数
			let noteContent = $.trim($("#remark").val());
			let activityId = '${requestActivity.id}';	   //js中也能使用EL表达式,必须加引号，不然会被当做变量处理而不是字符串

			//表单验证：备注内容不能为空
			if(noteContent=="" || noteContent==null){
				alert("备注内容不能为空！");
				return;		//return不能忘！！
			}

			$.ajax({
				url:'workbench/activity/saveCreateActivityRemark.do',
				data:{
					noteContent:noteContent,
					activityId:activityId
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code=="1"){
						//清空输入框
						$("#remark").val("");

						//刷新备注列表，拼接与动态输出备注相同的html语句，拼接到现有备注列表最后
						let htmlStr="";
						htmlStr+="<div id='div_"+data.retData.id+"' class='remarkDiv' style='height: 60px;'>";
						htmlStr+="<img title='${sessionScope.sessionUser.name}' src='image/user-thumbnail.png' style='width: 30px; height:30px;'>";
						htmlStr+="<div style='position: relative; top: -40px; left: 40px;' >";
						htmlStr+="<h5>"+data.retData.noteContent+"</h5>";
						htmlStr+="<font color='gray'>市场活动</font> <font color='gray'>-</font> <b>${requestActivity.name}</b>";
						htmlStr+="<small style='color: gray;'> "+data.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						htmlStr+="<div style='position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;'>";
						htmlStr+="<a class='myHref' name='editA' remarkId='"+data.retData.id+"'  remarkNoteContent='"+data.retData.noteContent+"' href='javascript:void(0);'><span class='glyphicon glyphicon-edit' style='font-size: 20px; color: #E6E6E6;'></span></a>";
						htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp";
						htmlStr+="<a class='myHref' name='deleteA' remarkId='"+data.retData.id+"' href='javascript:void(0);'><span class='glyphicon glyphicon-remove' style='font-size: 20px; color: #E6E6E6;'></span></a>";
						htmlStr+="</div>";
						htmlStr+="</div>";
						htmlStr+="</div>";
						$("#remarkDiv").before(htmlStr);	//拼接到remarkDiv前，也就是原备注列表的末尾
					}else{
						//添加失败,提示信息,输入框不清空,列表也不刷新
						alert(data.message);
					}
				}
			});
		});

		/*批量给动态的'删除图标'添加单击事件*/
		$("#remarkDivList").on("click","a[name='removeA']",function (){	//根据name动态定位元素
			/*收集参数*/
			let id=$(this).attr("remarkId");	//获取之前给标签添加的自定义属性的值（备注的id），$(this)表示获取当前对象(#remarkDivList a[name='deleteA'])

			//直接发送ajax请求，不用进行表单验证（因为是自己写的属性值,保真），不用弹出'是否删除'窗口（因为这种小备注删除还要点确认太麻烦,用户体验不好）
			$.ajax({
				url:'workbench/activity/removeActivityRemarkById.do',
				data: {
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code=="1"){
						//删除成功之后,刷新备注列表
						/*let divId="div_"+id;
						$("#divId").remove();*/
						$("#div_"+id).remove();	 //动态拼接id选择器
					}else{
						//删除失败,提示信息,备注列表不刷新
						alert(data.message);
					}
				}
			});
		});

		/*批量给'修改图标'添加单击事件,弹出模态窗口*/
		$("#remarkDivList").on("click","a[name='editA']",function (){
			let remarkId=$(this).attr("remarkId");
			//let noteContent=$(this).attr("remarkNoteContent");	//又在'#remarkDivList a[name='editA']'标签中添加了一个remarkNoteContent属性值存储原备注的内容
			let noteContent=$("#div_"+remarkId+" h5").text();	//通过备注的大div获取其子标签h5中存储的原备注的内容

			//将id写到该模态窗口的隐藏域中，方便后面点击更新时根据该id像后端发送修改请求
			$("#remarkId").val(remarkId);
			//将原备注的内容写到文本域中
			$("#edit-noteContent").val(noteContent);

			//弹出修改备注模态窗口
			$("#editRemarkModal").modal("show");
		});

		/*给修改模态窗口'更新按钮'添加单击事件*/
		$("#updateRemarkBtn").click(function (){
			//收集参数
			let noteContent=$.trim($("#edit-noteContent").val());
			let id=$("#remarkId").val();

			//备注内容不能为空
			if (noteContent==""||noteContent==null){
				alert("备注内容不能为空！");
				return;
			}

			//发送请求
			$.ajax({
				url:'workbench/activity/saveModifyActivityRemark.do',
				data: {
					noteContent:noteContent,
					id:id
				},
				type:'post',
				dataType: 'json',
				success:function (data){
					if (data.code=="1"){
						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
						//刷新备注列表
						$("#div_"+data.retData.id+" h5").text(noteContent);	//覆盖备注原内容，通过备注的大div获取其子标签h5对象
						$("#div_"+id+" small").text(" "+data.retData.editTime+" 由${sessionScope.sessionUser.name}修改");	//覆盖备注的创建/修改原信息内容，通过备注的大div获取其子标签small对象
					}else{
						//修改失败,提示信息,模态窗口不关闭,列表也不刷新
						alert(data.message);
						$("#editRemarkModal").modal("show");
					}
				}
			});
		});
	});
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 隐藏域，存储备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form" id="form-horizontal">
                        <div class="form-group">
                            <label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${requestActivity.name} <small>${requestActivity.startDate} ~ ${requestActivity.endDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestActivity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestActivity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestActivity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestActivity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestActivity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestActivity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestActivity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestActivity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestActivity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b id="update-describe">
					${requestActivity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>

		<%--遍历activityRemarkList，动态展示已有的备注信息--%>
		<c:forEach items="${requestActivityRemarks}" var="remark">
			<%--给每条备注的大div添加id属性，并使每个id属性值为当前备注的id，方便后期删除、更新时局部刷新页面--%>
			<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
				<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${remark.noteContent}</h5>
					<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestActivity.name}</b> <small style="color: gray;">
						<%--使用三目运算符，根据修改标记是否为1来决定显示的是备注的创建时间还是修改时间--%>
						${remark.editFlag=='1'?remark.editTime:remark.createTime}
						<%--使用三目运算符，根据修改标记是否为1来决定显示的是备注的创建者还是修改者--%>
						由${remark.editFlag=='1'?remark.editBy:remark.createBy}${remark.editFlag=='1'?'修改':'创建'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<%--擅自给'编辑'和'删除'按钮绑定一个remarkId属性，其属性值为当前活动备注的id，用于后面添加点击事件的同时获取对应的活动备注id--%>
						<a class="myHref" name="editA" remarkId="${remark.id}" remarkNoteContent="${remark.noteContent}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name="removeA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>



		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="saveCreateActivityRemark" type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>