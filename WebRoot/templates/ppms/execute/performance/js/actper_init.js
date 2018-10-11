//设置全局变量
var datagrid_actper;
/**实际绩效
 * actper_init.js
 * */
var baseUrl = contextpath + "execute/controller/ActualPerformanceController/";
//路径
var urls = {
	qryActPer : baseUrl + "qryActPer.do",
	delActPer : baseUrl+ "delActPer.do",
	optActPerView : baseUrl+ "optActPerView.do",
	saveActPer : baseUrl+ "saveActPer.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	qryProPerformance : baseUrl +"qryProPerformance.do",
	delProPerformance : baseUrl +"delProPerformance.do"
	
};
//类型
var types = {
		view : "view",
		add : "add",
		edit : "edit"
};
//是、否
var isOrNot = {
		"0" : "否",
		"1" : "是"
	};
$(function() {
	//加载数据
	loaddatagrid();
	
	//加载查询条件
	loadqryconditon();
});
function loaddatagrid(){
	datagrid_actper = $("#datagrid_actper").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryActPer,
		queryParams: {
			dealStatus : 3,
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_actper",
		showFooter : true,
		columns : [  [{	field : "projectid",checkbox : true	}
		, {field : "proName",title : "项目名称",halign : 'center',	width : 250,sortable:true}
		, {field : "proTypeName",title : "项目类型",halign : 'center',	width : 80,sortable:true, align:'left'}
		, {field : "createusername",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "amount",title : "总投资",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "proYear",title : "合作年限",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
		, {field : "proTradeName",title : "所属行业",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proPerateName",title : "运作方式",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proReturnName",title : "回报机制",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proSendtypeName",title : "项目发起类型",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "proPerson",title : "项目联系人",halign : 'center',fixed : true,	width : 120,sortable:true}
		, {field : "createuser",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "createtime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "updateusername",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "updateuser",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "updatetime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		
		, {field : "proType",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proPerate",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proTrade",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proReturn",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		, {field : "proSendtype",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true}
		
		
		]]
	});
}

function loadqryconditon(){
	/*$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		disabled:true,
		value : "3",
		editable : false,
		data : [ {text : "录入", value : "3"}, {text : "未录入", value : "4"},{text : "已送审", value : "2"}],
		onSelect : function(record) {
			qryActPer();
			switch (record.value) {
			case '3':
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_send').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('disable');
				break;
			case '4':
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('disable');
				$('#btn_remove').linkbutton('disable');
				$('#btn_send').linkbutton('disable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('disable');
				$('#btn_flow').linkbutton('disable');
				break; 
			case '2':
				$('#btn_add').linkbutton('disable');
				$('#btn_edit').linkbutton('disable');
				$('#btn_remove').linkbutton('disable');
				$('#btn_send').linkbutton('disable');
				$('#btn_revoke').linkbutton('enable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break; 
			default:
				break;
			}
		}
	});*/
	comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name");//项目发起类型
	comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name");//回报机制
	
	/*$("#proSendtype").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proPerate").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proType").combobox("addClearBtn", {iconCls:"icon-clear"});
	$("#proReturn").combobox("addClearBtn", {iconCls:"icon-clear"});*/
	//comboboxFuncByCondFilter(menuid,"proSendtype", "PROTRADE", "code", "name");//所属行业
	
	$("#proTrade").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'proTrade1',
		prompt: "请选择所属行业",
		editable :false,
		multiSelect: true, //单选树
		dblClickRow: true,
		queryParams : {
			menuid : menuid
		},
		url :contextpath + '/base/dic/dicElementValSetController/queryDicTreeElementValsByCFilter.do',
		checkLevs: [1,2,3], //只选择3级节点
		elementcode : "PROTRADE",
		filters:{
			code: "行业代码",
			name: "行业名称"
		}
	});
	
}

/**
 * 查询
 */
function qryActPer(){
	var param = {
			dealStatus : 3,
			proName : $("#proName").textbox('getValue'),
			proTrade : $("#proTrade").treeDialog('getValue'),//所属行业
			proPerate : $("#proPerate").combobox("getValues").join(","),//项目运作方式
			proReturn : $("#proReturn").combobox('getValues').join(","),//回报机制
			proSendtype : $("#proSendtype").combobox('getValues').join(","),//项目发起类型
			proType : $('#proType').combobox('getValues').join(","),//项目类型
			proPerson :  $('#proPerson').textbox('getValue'),//项目联系人
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
			
		};
	$("#datagrid_actper").datagrid("load",param);
}

/**
 * 新增
 */
function addActPer(){
	var rows = datagrid_actper.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	if(rows[0].xmdqhj == '5'){
		easyui_warn("此数据已移交，不可录入！",null);
		return;
	}
	showModalDialog(500, "actper_form", types.add, datagrid_actper, "实际绩效新增",urls.optActPerView+"?optFlag="+types.add, urls.saveActPer+"?optFlag="+types.add);
}

/**
 * 修改
 */
function editActPer(){
	var rows = datagrid_actper.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	if(rows[0].xmdqhj == '5'){
		easyui_warn("此数据已移交，不可录入！",null);
		return;
	}
	showModalDialog(500, "actper_form", types.edit, datagrid_actper, "实际绩效修改",urls.optActPerView+"?optFlag="+types.edit, urls.saveActPer+"?optFlag="+types.edit);
}

/**
 * 详情
 */
function detActPer(){
	var rows = datagrid_actper.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialog(500, "actper_form", types.view, datagrid_actper, "实际绩效详情",urls.optActPerView+"?optFlag="+types.view, urls.saveActPer+"?optFlag="+types.view);
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
		width : 780,
		height : height,
		href : href,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var row = dataGrid.datagrid("getSelections")[0];
			var f = parent.$.modalDialog.handler.find('#' + form);
			f.form("load", row);
			var projectid = mdDialog.find("#projectid").val();
			proPerformance(mdDialog,'',projectid);
			if(operType=="view"){
				mdDialog.find("#fin_add").linkbutton('disable');
				mdDialog.find("#fin_del").linkbutton('disable');
			}else{
				mdDialog.find("#fin_add").linkbutton('enable');
				mdDialog.find("#fin_del").linkbutton('enable');
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};
var lastIndex =0;
function proPerformance(mdDialog,performanceid,projectid){
	var grid = mdDialog.find("#advance_organ").datagrid({
        height: 360,
        url : urls.qryProPerformance,
        queryParams : {performanceid:performanceid,projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'performanceid',
        columns: [[//显示的列
                   { field: 'performanceid', title: '主键id', halign : 'center',width: 100, sortable: true,hidden:true }
                   ,{ field: 'projectid', title: '项目id', halign : 'center',width: 200 ,hidden:true}
                   ,{ field: 'year', title: '年度', halign : 'center',width: 100 ,align:'right',editor: { type: 'numberspinner', options: {min:2000,max:2099,editable:true,required: true,missingMessage:'请输入年度',validType:{length:[0,4]}}}}
                   ,{ field: 'productElement', title: '产出物',halign : 'center', width: 300,sortable: true,editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入产出物',validType:{length:[0,500]}}}}
                   ,{ field: 'unit', title: '计量单位', halign : 'center',width: 100,sortable: true ,editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入计量单位',validType:{length:[0,50]}}}}
                   ,{ field: 'amount', title: '数量', halign : 'center',width: 100,sortable: true  ,align:'right',editor: { type: 'numberbox', options: { required: true,min:0,missingMessage:'请输入数量',validType:{length:[0,9]}}}}
                   ,{ field: 'attachPath', title: '附件路径',halign : 'center', width: 100,hidden:true}
                   ,{ field: 'remark', title: '备注',halign : 'center', width: 200,sortable: true ,editor: { type: 'validatebox',validType:{length:[0,25]}}}
                   ]],
                   toolbar: [{
                	   id:"fin_add",
                       text: '添加', iconCls: 'icon-add', handler: function () {
                       	var data = mdDialog.find("#advance_organ").datagrid("getData");//grid列表
                       	var total = data.total;//grid的总条数
                       	mdDialog.find("#advance_organ").datagrid('appendRow', {row: {}});//追加一行
                       	mdDialog.find("#advance_organ").datagrid("beginEdit", total);//开启编辑
                       }
                   },'-', {
                	   id:"fin_del",
                       text: '删除', iconCls: 'icon-remove', handler: function () {
                           var row = mdDialog.find("#advance_organ").datagrid('getChecked');
                           if(row ==''){
                           	easyui_warn("请选择要删除的数据！");
                           	return;
                           }else{
                           	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
                           		
                                    if (r) {
                                    	if(row[0].performanceid==undefined){//没有录数据的情况下，直接在界面删除
                                       		mdDialog.find("#advance_organ").datagrid('deleteRow',mdDialog.find("#advance_organ").datagrid('getRowIndex',row[0]));
                                       	 }else{//已录入数据的情况下，需要通过数据库删除
	            	                       	$.post(urls.delProPerformance, {
	            	                       		performanceid :row[0].performanceid
	            	            			}, function(result) {
	            	            				easyui_auto_notice(result, function() {
	            	            					grid.datagrid("reload");
	            	            				});
	            	            			}, "json");
                                       	 }
                                    }
                                });
                           }
                        }
                   }],
       onDblClickRow:function (rowIndex, rowData) {
       	mdDialog.find("#advance_organ").datagrid('beginEdit', rowIndex);
       },
       
       onAfterEdit:function(index,row){

           row.editing = false;

           mdDialog.find('#advance_organ').datagrid('refreshRow', index);

       },
     //监听datagrid的点击事件
       onClickRow : function(rowIndex) {
                               //用户点击的跟当前行是一行
                   if (lastIndex != rowIndex) {
                           //否则开始用户点击行编辑,结束上一行编辑
                	   mdDialog.find('#advance_organ').datagrid('endEdit', lastIndex);
                   }
                   lastIndex = rowIndex;
               }
    });
	return grid;
}

/**
 * 按钮
 * @param operType
 * @param url
 * @param dataGrid
 * @param form
 * @returns
 */
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
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请至少录入一条绩效信息！",null);
					return;
				}
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(o.datagrid('getRows'));
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				submitForm(url, form,operType);
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if(operType=="add"){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请至少录入一条绩效信息！",null);
					return;
				}
				var total= o.datagrid("getData").total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定,否则获取不到行数据
				}
				var subStr = JSON.stringify(o.datagrid('getRows'));
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				submitForm(url, form,operType);
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
					datagrid_actper.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};
