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
    $(function (){

        /*给'创建线索按钮'添加单击事件*/
        $("#createClueBtn").click(function (){
            //初始化表单
            $("#createClueForm")[0].reset();
            //弹出模态窗口
            $("#createClueModal").modal("show");
        });

        /*给创建线索模态窗口的'保存按钮'添加单击事件*/
        $("#saveCreateClue").click(function (){
            //收集参数
            let appellation = $("#create-appellation").val();
            let owner = $("#create-owner").val();
            let state = $("#create-state").val();
            let source = $("#create-source").val();
            let fullname = $.trim($("#create-fullname").val());
            let company = $.trim($("#create-company").val());
            let job = $.trim($("#create-job").val());
            let email = $.trim($("#create-email").val());
            let phone = $.trim($("#create-phone").val());
            let website = $.trim($("#create-website").val());
            let mphone = $.trim($("#create-mphone").val());
            let description = $.trim($("#create-description").val());
            let contactSummary = $.trim($("#create-contactSummary").val());
            let nextContactTime = $.trim($("#create-nextContactTime").val());
            let address = $.trim($("#create-address").val());

            //表单验证
			if (owner==""||owner==null||company==""||company==null||fullname==""||fullname==null){
				alert("所有者/公司/姓名不能为空！");
				return;
			}
			let regExp=/null|^$|^[\u4E00-\u9FA5A-Za-z]+$/;
			let emailRegExp=/null|^$|^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			let mphoneRegExp=/null|^$|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
			let websiteRegExp=/null|^$|^(http|https|ftp):\/\/([\w-]+\.)+[\w-]+(\/[\w-./?%&=]*)?$/;
			let nextContactTimeRegExp=/null|^$|^\d{4}-\d{1,2}-\d{1,2}/;
			if (!(regExp.test(fullname)&&regExp.test(company)&&regExp.test(job))){
				alert("姓名/公司/职位输入不合法，必须为英文或汉字！");
				return;
			}
			if (!emailRegExp.test(email)){
				alert("邮箱格式输入不合法！");
				return;
			}
			if (!mphoneRegExp.test(mphone)){
				alert("手机号输入不合法！");
				return;
			}
			if (!websiteRegExp.test(website)){
				alert("公司网站输入不合法！");
				return;
			}
			if (!nextContactTimeRegExp.test(nextContactTime)){
				alert("下次联系时间输入不合法，必须为'yyyy-MM-dd'！");
				return;
			}

            //发送异步请求
            $.ajax({
                url:'workbench/clue/saveCreateClue.do',
                data:{
                    fullname:fullname,
                    appellation:appellation,
                    owner:owner,
                    company:company,
                    job:job,
                    email:email,
                    phone:phone,
                    website:website,
                    mphone:mphone,
                    state:state,
                    source:source,
                    description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
                    address:address
                },
                type:'post',
                dataType:'json',
                success:function (data){
                    if (data.code=="1"){
                        //关闭模态窗口，
                        $("#createClueModal").modal("hide");
                        //刷新线索列表，显示第一页数据，保持每页显示条数不变
						queryClueByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                    }else{
                        //修改失败,提示信息,模态窗口不关闭,列表也不刷新
                        alert(data.message);
                        $("#createClueModal").modal("show");
                    }
                }
            });
        });

		//初始时按默认条件进行查询
		queryClueByConditionForPage(1,5);

		//给'查询按钮'添加单击事件，再次查询时按文本框中的条件进行条件查询
		$("#queryClueBtn").click(function (){
			queryClueByConditionForPage(1,$("#demo_pag1").bs_pagination("getOption","rowsPerPage"));
		});

		//给'全选按钮'添加单击事件，选中全选按钮后该页的所有checkbox都被选中,再次点击所有checkbox都取消选中
		$("#checkAll").click(function (){
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		});
		//给所有单选按钮添加单击事件，当所有单选按钮选中则全选按钮也被选中，只要有一个单选按钮没被选中则全选按钮就不会被选中
		$("#tBody").on("click","input[type='checkbox']",function (){
			$("#checkAll").prop("checked",$(".deleteCheckbox").size()==$("#tBody input[type='checkbox']:checked").size());
		});

		//给'删除按钮'添加单击事件
		$("#dropClueBtn").click(function (){
			let checkeds=$("#tBody input[type='checkbox']:checked");

			//进入判断：数组不为空时才会弹出确认框，否则提示用户先选择才能删除，点击确认后才会发送ajax请求执行删除操作
			if (checkeds.size()!=0){
				if (confirm("您确定要删除这些数据吗？")){
					let ids="";
					$.each(checkeds,function (){
						ids += "id="+this.value+"&";
					});
					ids = ids.substr(0,ids.length-1);

					//发送ajax请求
					$.ajax({
						url:'workbench/clue/dropClueByIds.do',
						data:ids,
						type:'post',
						dataType:'json',
						success:function (data){
							if (data.code=="1"){
								//刷新市场活动列，显示第一页数据，《《保持每页显示条数不变（动态获取pageSize，详见$(".mydate").datetimepicker({})的success）》》
								queryClueByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
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

		//给'修改按钮'添加单击事件
		$("#modifyClueBtn").click(function (){
			//获取列表中被选中的checkbox
			let checkeds=$("#tBody input[type='checkbox']:checked");

			//当只选中一条数据时才能进行修改，否则弹出提示信息
			if (checkeds.size()==0){
				alert("请先选择要修改的活动！");
			}else if(checkeds.size()==1){
				//收集参数(id是被选中的活动对应的)
				let id=checkeds[0].value;

				//要将id放到隐藏域中，以标识查询的市场活动，更新时还会根据这个id去更新对应的市场活动
				$("#edit-id").val(id);

				$.ajax({
					url:'workbench/activity/queryClueById.do',
					data:{
						id:id
					},
					type:'post',
					dataType:'json',
					success:function (data){
						//将查询结果回显到表格中
						$("#edit-owner").val(data.owner);
						$("#edit-company").val(data.company);
						$("#edit-appellation").val(data.appellation);
						$("#edit-fullname").val(data.fullname);
						$("#edit-job").val(data.job);
						$("#edit-email").val(data.email);
						$("#edit-phone").val(data.phone);
						$("#edit-website").val(data.website);
						$("#edit-mphone").val(data.mphone);
						$("#edit-state").val(data.state);
						$("#edit-source").val(data.source);
						$("#edit-description").val(data.description);
						$("#edit-contactSummary").val(data.contactSummary);
						$("#edit-nextContactTime").val(data.nextContactTime);
						$("#edit-address").val(data.address);

						//弹出'创建线索的模态窗口'
						$("#editClueModal").modal("show");
					}
				});
			}else{
				alert("每次只能选择一个要修改的活动！");
			}
		});

		/*给'更新'按钮添加单击事件*/
		$("#saveEditClueBtn").click(function (){
			//收集参数
			let id=$("#edit-id").val();
			let owner=$("#edit-owner").val();
			let company=$("#edit-company").val();
			let appellation=$("#edit-appellation").val();
			let fullname=$("#edit-fullname").val();
			let job=$("#edit-job").val();
			let email=$("#edit-email").val();
			let phone=$("#edit-phone").val();
			let website=$("#edit-website").val();
			let mphone=$("#edit-mphone").val();
			let state=$("#edit-state").val();
			let source=$("#edit-source").val();
			let description=$("#edit-description").val();
			let contactSummary=$("#edit-contactSummary").val();
			let nextContactTime=$("#edit-nextContactTime").val();
			let address=$("#edit-address").val();

			//表单验证
			if (owner==""||owner==null||company==""||company==null||fullname==""||fullname==null){
				alert("所有者/公司/姓名不能为空！");
				return;
			}
			let regExp=/null|^$|^[\u4E00-\u9FA5A-Za-z]+$/;
			let emailRegExp=/null|^$|^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			let mphoneRegExp=/null|^$|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
			let websiteRegExp=/null|^$|^(http|https|ftp):\/\/([\w-]+\.)+[\w-]+(\/[\w-./?%&=]*)?$/;
			let nextContactTimeRegExp=/null|^$|^\d{4}-\d{1,2}-\d{1,2}/;
			if (!(regExp.test(fullname)&&regExp.test(company)&&regExp.test(job))){
				alert("姓名/公司/职位输入不合法，必须为英文或汉字！");
				return;
			}
			if (!emailRegExp.test(email)){
				alert("邮箱格式输入不合法！");
				return;
			}
			if (!mphoneRegExp.test(mphone)){
				alert("手机号输入不合法！");
				return;
			}
			if (!websiteRegExp.test(website)){
				alert("公司网站输入不合法！");
				return;
			}
			if (!nextContactTimeRegExp.test(nextContactTime)){
				alert("下次联系时间输入不合法，必须为'yyyy-MM-dd'！");
				return;
			}

			//发送Ajax请求
			$.ajax({
				url:'workbench/activity/modifyClue.do',
				data:{
					id:id,
					owner:owner,
					company:company,
					appellation:appellation,
					fullname:fullname,
					job:job,
					email:email,
					phone:phone,
					website:website,
					mphone:mphone,
					state:state,
					source:source,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//关闭修改模态窗口
						$("#editClueModal").modal("hide");
						//刷新市场活动列，要保证pageNo和pageSize都不变
						queryClueByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息更新失败
						$("#msg").text(data.message);
						//修改模态窗口不关闭 (可以省略不写，但是习惯上可以添加，不会对原弹出的模态窗口有任何影响)
						$("#editClueModal").modal("show");
						//市场活动列表也不刷新
					}
				}
			});
		});
    });

	//调用bs_pagination工具函数显示翻页信息
	function queryClueByConditionForPage(pageNo,pageSize){
		//目标：用户在市场活动主页面填写查询条件,点击"查询"按钮,显示所有符合条件的数据的第一页
		//收集参数
		let fullname=$.trim($("#query-fullname").val());
		let company=$.trim($("#query-company").val());
		let phone=$.trim($("#query-phone").val());
		let mphone=$.trim($("#query-mphone").val());
		let owner=$.trim($("#query-owner").val());
		let source=$("#query-source").val();
		let state=$("#query-state").val();

		//发送请求
		$.ajax({
			url:'workbench/clue/queryClueByCondition.do',
			data:{
				fullname:fullname,
				company:company,
				phone:phone,
				mphone:mphone,
				owner:owner,
				source:source,
				state:state,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data){
				let htmlStr="";
				$.each(data.clueList,function(index,obj){
					htmlStr+="<tr>";
					htmlStr+="<td><input type='checkbox' class='deleteCheckbox' value='"+obj.id+"'/></td>";
					htmlStr+="<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='workbench/clue/toDetailClue.do?id="+obj.id+"';\">"+obj.fullname+"</a></td>";
					htmlStr+="<td>"+obj.company+"</td>";
					htmlStr+="<td>"+obj.phone+"</td>";
					htmlStr+="<td>"+obj.mphone+"</td>";
					htmlStr+="<td>"+obj.source+"</td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.state+"</td>";
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
					totalPages=parseInt(data.totalRows/pageSize)+1;
				}

				//调用bs_pagination工具函数显示翻页信息
				/*将分页操作放入queryClueByConditionForPage()方法发送ajax的success函数中
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

						onChangePage: function(event,pageObj) { //用户每次切换页号currentPage，都会触发这段代码
							queryClueByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
						}
					});
				});
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="createClueForm">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  <c:forEach items="${requestUsers}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
									<option></option>
									<c:forEach items="${requestAppellations}" var="appellation">
										<option value="${appellation.id}">${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-fullname" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-state">
								  <option></option>
									<c:forEach items="${requestClueStates}" var="clueState">
										<option value="${clueState.id}">${clueState.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${requestSources}" var="source">
										<option value="${source.id}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateClue">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<%--添加一个隐藏域用于保存id--%>
		<input type="hidden" id="edit-id"/>
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<c:forEach items="${requestUsers}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <option></option>
									<c:forEach items="${requestAppellations}" var="appellation">
										<option value="${appellation.id}">${appellation.value}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone">
							</div>
							<label for="edit-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
								  <option></option>
									<c:forEach items="${requestClueStates}" var="clueState">
										<option value="${clueState.id}">${clueState.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
									<c:forEach items="${requestSources}" var="source">
										<option value="${source.id}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">这是一条线索的描述信息</textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditClueBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
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
				      <input class="form-control" type="text" id="query-fullname">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="query-company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="query-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${requestSources}" var="source">
							  <option value="${source.id}">${source.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="query-mphone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="query-state">
					  	<option></option>
						  <c:forEach items="${requestClueStates}" var="clueState">
							  <option value="${clueState.id}">${clueState.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryClueBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createClueBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="modifyClueBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="dropClueBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>--%>
					</tbody>
				</table>
				<%--
					美工写的分页查询div不够强大，这里引入bs_pagination框架
					由于div是块元素，为了美观将该div放入这个div中
				--%>
				<div id="demo_pag1"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 60px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
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