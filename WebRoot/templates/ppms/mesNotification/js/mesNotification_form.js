var baseUrl = contextpath + "ppms/mesNotification/MesNotificationController/";
var urls = {
		unselectedUserTree : baseUrl + 'queryUnselectedUser.do',
		selectedUserTree : baseUrl + 'querySelectedUser.do'
	};


function chooseReceive() {
	$('#showPreson').show().dialog({
		title : '选择接收人',
		iconCls : 'icon-view',
		width : 860,
		height : 450,
		href : baseUrl+"showPersonList.do",//跳转打开选择页面
		modal:true,
		resizable: true,
		onLoad : function() {
			//接收类别加载
			$("#messageLb").combobox({
				valueField : "value",
				textField : "text",
				value : "",
				editable : false,
				data : [{text : "全部", value : ""}, 
				        {text : "社会资本", value : "1"}, 
				        {text : "第三方机构", value : "2"},
				        {text : "专家", value : "3"}, 
				        {text : "系统用户", value : "4"}],//与视图view_mes_receive对应
//				onChange: function(nv, ov) {
//							var icon = $("#messageLb").combobox("textbox").prev("span.textbox-addon").find("a:first");
//							if (nv) {
//								icon.css('visibility', 'visible');
//							} else {
//								icon.css('visibility', 'hidden');
//							}
//				},
				onSelect : function(record) {
					doQuery();//点击查询
					
				}
			});
//			$("#messageLb").combobox("addClearBtn", {iconCls:"icon-clear"});
			
			
			// 角色选择用户
			doInit($("#centraltab"), $("#viewId").val());
			
			var cpanel = $('#centraltab').layout('panel', 'center');
			//适用于主页面
			cpanel.panel({
				onResize : function(w, h) {
					if(h){
						var button_height = 0;
						var display_btns = cpanel.find("a:visible");
						display_btns.each(function(i) {
							button_height += 30;
						});
						$('#space_holder').height(
								(h - button_height) / 2);
					}
				}
			});
			//适用于弹出框
			var total_height = cpanel.panel('options').height;
			
			var button_height = 0;
			var display_btns = cpanel.find("a:visible");
			display_btns.each(function(i) {
				button_height += 30;
			});
			$('#space_holder').height(
					(total_height - button_height) / 2);
		},
		buttons : [
		       	{
		    		text : "确定",
		    		iconCls : "icon-redo",
		    		handler : function() {
		    			if (!setValReceive()){
		    				return ;
		    			}
		    			$('#showPreson').dialog('close');
		    			
		    		}
		    	},
		    	{
		    		text : "关闭",
		    		iconCls : "icon-cancel",
		    		handler : function() {
		    			$('#showPreson').dialog('close');
		    		}
		    	}]
	});
}


//接收人页面加载数据
function doInit(jq, usercode) {
	var	param = {
			userCode : usercode
		};
	jq.find("#lgrid").datagrid({
		url : urls.unselectedUserTree,
		queryParams : param,
		title : '可选用户列表',
		remoteSort : false,
		sortOrder : 'asc',
		
		toolbar : $('#tb'),
		columns : [ 
			[ {field : "checkid",checkbox : true}  
			,{field : "lbname",title : "类别",halign : 'center',width:70,sortable:true}
			,{field : "name",title : "名称",halign : 'center',width:120,sortable:true}
			,{field : "phone",title : "手机号码",halign : 'center',width:150,sortable:true}
			
			] 
		],
		fit : true,
		border : false,
		rownumbers : true,
		pagination : false,
		
		onDblClickRow : function(index, row) {
			dblClickChoiseUser(grid_left, grid_right,index, row);
		}
	});

	jq.find("#rgrid").datagrid({
		url : urls.selectedUserTree,
		queryParams : param,
		title : '已选用户列表',
		
		remoteSort : false,
		sortOrder : 'asc',
		
		columns : [ 
			[ {field : "checkid",checkbox : true}  
			,{field : "lbname",title : "类别",halign : 'center',width:70,sortable:true}
			,{field : "name",title : "名称",halign : 'center',width:120,sortable:true}
			,{field : "phone",title : "手机号码",halign : 'center',width:150,sortable:true}
			
			]      
		],
		fit : true,
		border : false,
		
		rownumbers : true,
		pagination : false,
		
		onDblClickRow : function(index, row) {
			dblClickChoiseUser(grid_right, grid_left,index, row);
		}
	});
	
	var grid_left = jq.find("#lgrid");
	var grid_right = jq.find("#rgrid");
	
	// 初始化添加按钮
	jq.find('#addBtn').linkbutton({
		onClick : function(index) {
			choiseUser(grid_left, grid_right);
		}
	});

	jq.find("#delBtn").linkbutton({
		onClick : function() {
			choiseUser(grid_right, grid_left);
		}
	});
	
}

function dblClickChoiseUser(grid_main, grid_sub, index, row) {
	grid_sub.datagrid('appendRow',row);
	grid_main.datagrid('deleteRow', index);
}



//已选和未选用户操作
function choiseUser(grid_main, grid_sub) {

	var rows = grid_main.datagrid("getSelections");
	
	if (rows.length < 1) {
		parent.$.messager.alert('提示', "请选择接收人！", 'warnning');
		return;
	}
	
	//删除数据时，datagrid会自动变更index,多行删除时，需要把删除的row放在一个数组，然后进行删除
	var oldRows = new Array();
	for (var i=0; i<rows.length; i++) {
		grid_sub.datagrid('appendRow',rows[i]);
		oldRows.push(rows[i]);
	}
	
	for (var i=0; i<oldRows.length; i++) {
		grid_main.datagrid('deleteRow', grid_main.datagrid('getRowIndex', oldRows[i]));
	}
	
}

//将选中的接收人赋值给textbox文本框中
function setValReceive()
{
	//获取已选择的用户，把接收人信息赋值到textarea中
	var selRowAry = $("#centraltab").find("#rgrid").datagrid('getData').rows;
	if (selRowAry.length > maxRec){
		easyui_warn("接收方数量最大值为" + maxRec,null);
		return false;
	}
	var viewId="";
	var receiveId="";
	var receiveName="";
	var receiveLb="";
	var receivePhone="";
	
	for(var j=0;j<selRowAry.length;j++)
	{	
		if (selRowAry[j].phone == "" || selRowAry[j].phone == null){
			easyui_warn(selRowAry[j].name + "的手机号码为空！",null);
			return false;
		}
		var selRow = selRowAry[j];
		//拼接接收人姓名,编码
		if(j !=0)
		{
			viewId += ";";
			receiveId += ";";
			receiveName += ";";
			receiveLb += ";";
			receivePhone += ";";
			
			
		}
		viewId += selRow.id;
		receiveId += selRow.receiveid;
		receiveName += selRow.name;
		receiveLb += selRow.lb;
		receivePhone += selRow.phone;
	}
	$("#receiveName").textbox('setValue',receiveName);
	$("#viewId").val(viewId);
	$("#receiveId").val(receiveId);
	$("#receiveLb").val(receiveLb);
	$("#receivePhone").val(receivePhone);
	
	return true;
}


//点击查询
function doQuery()
{
	//获取已选择的用户
	var selRows = $("#centraltab").find("#rgrid").datagrid('getData').rows;
	var usercodeAry="";
	for(var j=0;j<selRows.length;j++)
	{	
		var selRow = selRows[j];

		//拼接接收人姓名,编码
		if(j !=0)
		{
			usercodeAry += ";";
		}
		usercodeAry += selRow.id;
	}

	
	
	//查询参数获取
	var param = {
			messageLb : $("#centraltab").find("#messageLb").combobox("getValue"),
			messageName : $("#centraltab").find("#messageName").val(),
			userCode: usercodeAry
		};	

	$("#centraltab").find("#lgrid").datagrid("load", param);
}


