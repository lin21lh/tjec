var personalDataGrid;

/**
 * 选择用户
 * @param type
 */

function selectUser(dialogId,type){
	
	var href = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/selectUser.do";
	
	dialogId.dialog({
		title:"选择用户",
		width:900,
		height:600,
		href:href,
		onLoad:function(){
		    //所有机构列表
			openUserDataGrid(dialogId);
		},
		buttons:[{
			id:"saveBtn",
			text:"选择",
			iconCls:"icon-save",
			handler:function() {
				var selectRow = personalDataGrid.datagrid("getChecked");
				if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
					easyui_warn("请选择一位用户信息 !", null);
					return;
				}
				
				var row = personalDataGrid.datagrid("getSelections")[0];
				if(type == 'undertaker'){
					$("#undertaker").textbox('setValue', row.username);
					$("#undertakeid").val(row.userid);
				} else {
					$("#assessor").textbox('setValue', row.username);
				}
				dialogId.dialog('close');  
				
			}
		},{
			text:"关闭",
			iconCls:"icon-cancel",
			handler:function() {
				dialogId.dialog('close');  
			}
		}]
	});
}

/**
 * 打开用户Grid页面
 * @param mdDialog
 */
function openUserDataGrid(dialog) {
	
	var href = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/queryUserList.do";
	userDataGrid = dialog.find("#mechanismTable").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		height:400,
		title: '机构列表',
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		queryParams:{
			menuid:menuid,
			firstNode:true,
			lastNode:false
		},
		pageSize:30,
		url:href,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_user",
		showFooter : true,
		columns : [ [ {
			field : "userid",
			checkbox : true
		}, 
		{
			field : "orgcodeMc",
			title : "机构名称",
			halign : 'center',
			width : 220,
			fixed : true,
			sortable:true
		}
		] ],
		onSelect : function(rowIndex, rowData) {
			loadRoleDataGrid(rowData,dialog);
		},
		onLoadSuccess : function(data) {
			userDataGrid.datagrid('selectRow', 0);
		},
        onLoadError : function() {
			
		}
	});
}


/**
 * 加载表字段定义数据表格
 */
function loadRoleDataGrid(rowData,dialog) {
	
	var href = contextpath + "aros/bxzfy/controller/RcasebaseinfoController/queryUserList.do";
	var userid = rowData.userid;
	alert(userid);
	personalDataGrid = dialog.find("#personalTable").datagrid({
		fit : true,
		border : false,
		singleSelect : true,
		title: '人员列表',
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		queryParams:{
			menuid:menuid,
			userid:userid
		},
		url : href,
		loadMsg : "正在加载，请稍候......",
		columns : [ [ {
			field : "userid",
			checkbox : true
		}, {
			field : "usercode",
			title : "用户编码",
			halign : 'center',
			width : 100
		},{  
			field : "username",
			title : "用户名称",
			halign : 'center',
			fixed : true,
			width : 120,
			sortable:true
		},{  
			field : "createtime",
			title : "创建日期",
			halign : 'center',
			width :150
		}
		] ],
		onLoadError : function() {
		}
	});
}
