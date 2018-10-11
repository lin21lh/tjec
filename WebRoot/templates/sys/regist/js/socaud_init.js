//设置全局变量
var datagrid_socaud;
/**开发计划
 * socaud_init.js
 * */
var baseUrl = contextpath + "regist/controller/SocialRegistAuditController/";
//路径
var urls = {
	qrySocAud : baseUrl + "qrySocAud.do",
	approveSocAud : baseUrl+ "approveSocAud.do",
	refuseSocAud : baseUrl+ "refuseSocAud.do",
	delSocAud : baseUrl+ "delSocAud.do",
	optSocAudView : baseUrl +"optSocAudView.do"
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
var isOrNot = {
		"0":"否",
		"1":"是"
};
var dealStatus = {
		"0":"未审批",
		"1":"已通过",
		"2":"未通过",
		"3":"已作废"
}
$(function() {
	//加载数据
	loaddatagrid();
	
	//加载查询条件
	loadqryconditon();
});
function loaddatagrid(){
	datagrid_socaud = $("#datagrid_socaud").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qrySocAud,
		queryParams: {
			status : 0
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_socaud",
		showFooter : true,
		columns : [ [ {	field : "socialid",checkbox : true	}
		, {field : "usercode",title : "用户编码",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "username",title : "用户名称",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "userpswd",title : "用户密码",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "linkperson",title : "联系人",halign : 'center',fixed : true,	width : 200,sortable:true}
		, {field : "linkphone",title : "联系电话",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "orgcode",title : "组织机构代码",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "orgname",title : "社会资本名称",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "iscombo",title : "是否是联合体",halign : 'center',fixed : true,	width : 120,sortable:true,formatter: function(value){return isOrNot[value];}}
		, {field : "categoryName",title : "所属行业",halign : 'center',fixed : true,	width : 180,sortable:true}
		, {field : "categoryCode",title : "所属行业",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "preferencesCode",title : "投资偏好",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "preferencesName",title : "投资偏好",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "status",title : "状态",halign : 'center',fixed : true,	width : 120,sortable:true,formatter: function(value){return dealStatus[value];}}
		, {field : "applicationTime",title : "申请时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "auditUser",title : "审批人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "auditUserName",title : "审批人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "auditTime",title : "审批时间",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "remark",title : "备注",halign : 'center',fixed : true,	width : 120,sortable:true}
		]]
	});
}

function loadqryconditon(){
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "0",
		editable : false,
		data : [ {text : "未审批", value : "0"}, {text : "已通过", value : "1"}, {text : "未通过", value : "2"},{text : "已作废", value : "3"}],
		onSelect : function(record) {
			qrySocAud();
			switch (record.value) {
			case '0':
				$('#btn_app').linkbutton('enable');
				$('#btn_ref').linkbutton('enable');
				$('#btn_del').linkbutton('enable');
				break;
			case '1':
				$('#btn_app').linkbutton('disable');
				$('#btn_ref').linkbutton('disable');
				$('#btn_del').linkbutton('disable');
				break; 
			case '2':
				$('#btn_app').linkbutton('disable');
				$('#btn_ref').linkbutton('disable');
				$('#btn_del').linkbutton('disable');
				break; 
			case '3':
				$('#btn_app').linkbutton('disable');
				$('#btn_ref').linkbutton('disable');
				$('#btn_del').linkbutton('disable');
				break; 
			default:
				break;
			}
		}
	});
	
}

function qrySocAud(){
	var param = {
			status : $("#status").combobox('getValue'),
			linkpersoon : $("#linkpersoon").textbox('getValue'),
			linkphone : $("#linkphone").textbox('getValue'),
			usercode : $("#usercode").textbox('getValue'),
			orgname : $("#orgname").textbox('getValue'),
			orgcode : $("#orgcode").textbox('getValue')
		};
	$("#datagrid_socaud").datagrid("load",param);
}

//审批
function editSocAud(){
	var rows = datagrid_socaud.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(360, "socaud_form", types.edit, datagrid_socaud, "社会资本注册审批",urls.optSocAudView+"?optFlag="+types.edit, urls.approveSocAud+"?optFlag="+types.edit);
}

/**
 * 详情
 */
function detSocAud(){
	var rows = datagrid_socaud.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(360, "socaud_form", types.view, datagrid_socaud, "社会资本注册详情",urls.optSocAudView+"?optFlag="+types.view, urls.approveSocAud+"?optFlag="+types.view);
}

/**
 * 窗口
 * @param height
 * @param form
 * @param operType
 * @param dataGrid
 * @param title
 * @param href
 * @param url
 */
function showModalDialog(height, form, operType, dataGrid, title, href, url) {

	var icon = 'icon-' + operType;
	parent.$.modalDialog({
		title : title,
		iconCls : icon,
		width : 850,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			
			f.form("load", row);
			
			mdDialog.find("#iscombo").textbox("setValue",isOrNot[row.iscombo]);
			mdDialog.find("#status").textbox("setValue",dealStatus[row.status]);
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};

function funcOperButtons(operType, url, dataGrid, form) {

	var buttons;
	if(operType=="view"){
		buttons = [ {
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if(operType=="edit"){
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				submitForm(url, form,operType);
			}
		},{
			text : "拒绝",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				submitForm(urls.refuseSocAud, form,operType);
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}
	
	return buttons;
};

/**
 * 提交
 * @param url
 * @param form
 * @param operType
 */
function submitForm(url, form,operType) {
	var form = parent.$.modalDialog.handler.find('#' + form);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	form.form("submit",{
			url : url,
			onSubmit : function() {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = form.form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					//重新加载gride
					parent.$.modalDialog.handler.dialog('close');
					easyui_info(result.title);
					datagrid_socaud.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};



function approveSocAud(){
	var rows = datagrid_socaud.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var socialids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			socialids = rows[i].socialid;
		}else{
			socialids += ","+rows[i].socialid;
		}
	}
	parent.$.messager.confirm("确认同意", "确定同意选择的数据？", function(r) {
		if (r) {
			$.post(urls.approveSocAud, {
				socialid :socialids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_socaud.datagrid("reload");
				});
			}, "json");
		}
	});
}
function refuseSocAud(){
	var rows = datagrid_socaud.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var socialids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			socialids = rows[i].socialid;
		}else{
			socialids += ","+rows[i].socialid;
		}
	}
	parent.$.messager.confirm("确认拒绝", "确定拒绝选择的数据？", function(r) {
		if (r) {
			$.post(urls.refuseSocAud, {
				socialid :socialids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_socaud.datagrid("reload");
				});
			}, "json");
		}
	});
}
function delSocAud(){
	var rows = datagrid_socaud.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var socialids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			socialids = rows[i].socialid;
		}else{
			socialids += ","+rows[i].socialid;
		}
	}
	parent.$.messager.confirm("确认作废", "确定作废选择的数据？", function(r) {
		if (r) {
			$.post(urls.delSocAud, {
				socialids :socialids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_socaud.datagrid("reload");
				});
			}, "json");
		}
	});
}