//请求路径
var baseUrl = contextpath + "aros/xzfy/controller/CasebaseinfoController/";
var urls = {
	gridUrl:baseUrl + "queryThirdByCaseid.do" //查询案件列表
	,saveUrl:baseUrl + "saveThirdInfo.do" //查询案件列表
	,deleteUrl:baseUrl + "deleteThirdInfo.do" //查询案件列表
};
//默认加载
var caseid = '';
$(function() {
	window.setTimeout("loadThGrid()",1000);
});

var thridDataGrid;
function showReload(){
	thridDataGrid.datagrid("reload");
}

//加载可项目grid列表
function loadThGrid() {
	var caseid = parent.$.modalDialog.handler.find('#caseid').val();
	thridDataGrid = $("#thirdbaseinfoTable").datagrid({
		fit:true,
		stripe:true,
		singleSelect:true,
		rownumbers:true,
		pagination:true,
		remoteSort:false,
		multiSort:true,
		pageSize:30,
		queryParams:{
			caseid: caseid
		},
		url:urls.gridUrl,
		loadMsg:"正在加载，请稍候......",
		toolbar:"#toolbar_center",
		showFooter:true,
        onClickRow:function(rowIndex,rowData){
        	var form = parent.$.modalDialog.handler.find('#thidForm');
        	form.form("load",rowData);
        },
		columns:[[ 
		  {field:"thid", checkbox:true},
		  {field:"thname", title:"第三人名称", halign:"center", width:50, sortable:true},
		  {field:"thtypename", title:"申请人类型", halign:"center", sortable:true},
		  {field:"thidtypename", title:"申请人证件类型", halign:"center", sortable:true},
		  {field:"thidcode", title:"第三人证件号码", halign:"center", sortable:true},
		  {field:"thphone", title:"证件号码", halign:"center", sortable:true},
		  {field:"thaddress", title:"联系电话", halign:"center", sortable:true},
		  {field:"thpostcode", title:"邮政编码", halign:"center", sortable:true},
		  {field:"thidproxyman", title:"第三人委托代理人", halign:"center", sortable:true},
		  {field:"thidproxyphone", title:"第三人委托代理人电话", halign:"center", sortable:true},
		  {field:"thidproxyaddress", title:"第三人委托代理人地址", halign:"center", sortable:true},
		  {field:"caseid", halign:"center", sortable:true, hidden:true},
		  {field:"thtype", halign:"center", sortable:true, hidden:true},
		  {field:"thidtype", halign:"center", sortable:true, hidden:true}
		  ]]
	});
}

function add(){
	var form = parent.$.modalDialog.handler.find('#thidForm');
	form.form('clear');
}
function save(){
		var caseid = parent.$.modalDialog.handler.find('#caseid').val();
		var form = parent.$.modalDialog.handler.find('#thidForm');
		var thname = form.find("#thname").val();
		if(thname == '' || thname == undefined){
			parent.$.messager.alert("错误", "请填写第三人信息", "error");
			return ;
		}
		form.form("submit", {
			url:urls.saveUrl + "?caseid="+caseid,
			onSubmit:function() {
				parent.$.messager.progress({
					title:"提示",
					text:"数据处理中，请稍后...."
				});
				var isValid = form.form("validate");
				if (!isValid) {
					parent.$.messager.progress("close");
				}
				return isValid;
			},
			success:function(result) {
				parent.$.messager.progress("close");
				result = $.parseJSON(result);
				if (result.success) {
					thridDataGrid.datagrid("reload");
					form.form('clear');
				} else {
					parent.$.messager.alert("错误", result.title, "error");
				}
			}
		});
}

/**
 * 删除
 */
function _delete(){
	var selectRow = thridDataGrid.datagrid("getChecked");
	if(selectRow == null || selectRow.length == 0 || selectRow.length > 1){
		easyui_warn("请选择一条数据", null);
		return;
	}
	var row = thridDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("确认删除", "确认要将选中数据删除？", function(r) {
		if (r){
			$.post(urls.deleteUrl, {id:row.thid},
					function(result){
						easyui_auto_notice(result, function() {
							thridDataGrid.datagrid("reload");
					});
			}, "json");
		}
	});
}

