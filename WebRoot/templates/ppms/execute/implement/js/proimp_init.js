//设置全局变量
var datagrid_proimp;
/**采购结果
 * proimp_init.js
 * */
var baseUrl = contextpath + "execute/controller/ProjectImplementController/";
//路径
var urls = {
	qryProImp : baseUrl + "qryProImp.do",
	delProImp : baseUrl+ "delProImp.do",
	optProImpView : baseUrl+ "optProImpView.do",
	saveProImp : baseUrl+ "saveProImp.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	qryProFinance : baseUrl +"qryProFinance.do",
	subProImp: baseUrl+"subProImp.do",
	revokeProImp: baseUrl+"revokeProImp.do",
	delProFinance: baseUrl+"delProFinance.do"
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
	datagrid_proimp = $("#datagrid_proimp").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryProImp,
		queryParams: {
			dealStatus : 1,
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_proimp",
		showFooter : true,
		columns : [ [ {	field : "projectid",checkbox : true, rowspan:2,	align:'left'}
		, {field : "proName",title : "项目名称",halign : 'center',	width : 250,sortable:true,align:'left', rowspan:2}
		, {field : "proTypeName",title : "项目类型",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		, {field : "statusName",title : "状态",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		,{title:'实施情况信息',colspan:8}
		,{title:'项目信息',colspan:11}
		, {field : "createusername",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "createuser",title : "创建人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "createtime",title : "创建时间",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "updateusername",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		, {field : "updateuser",title : "修改人",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "updatetime",title : "修改时间",halign : 'center',fixed : true,	width : 120,sortable:true,rowspan:2}
		
		, {field : "proType",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proPerate",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proTrade",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proReturn",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "proSendtype",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		, {field : "purchaseid",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		]
		, [
			 {field : "implementid",title : "主键id",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "datatype",title : "数据类型",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "projectCompany",title : "项目公司名称",halign : 'center',	width : 120,sortable:true}
			, {field : "establishTime",title : "项目公司成立时间",halign : 'center',	width : 120,sortable:true}
			, {field : "safeguardMeasure",title : "履约保证措施",halign : 'center',	width : 120,sortable:true}
			, {field : "safeguardMeasurePath",title : "履约保证措施附件",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "financeTime",title : "项目融资交割时间",halign : 'center',	width : 120,sortable:true}
			, {field : "propertyTime",title : "资产交割时间",halign : 'center',	width : 120,sortable:true}
			, {field : "startT_time",title : "项目开工时间",halign : 'center',	width : 120,sortable:true}
			, {field : "endTime",title : "项目完成时间",halign : 'center',	width : 120,sortable:true}
			, {field : "remark",title : "备注",halign : 'center',	width : 120,sortable:true}
			, {field : "status",title : "项目状态",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "amount",title : "总投资",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
			, {field : "proYear",title : "合作年限",halign : 'center',fixed : true,	width : 120,sortable:true,align:'right'}
			, {field : "proTradeName",title : "所属行业",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proPerateName",title : "运作方式",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proReturnName",title : "回报机制",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proSendtypeName",title : "项目发起类型",halign : 'center',fixed : true,	width : 120,sortable:true}
			, {field : "proPerson",title : "项目联系人",halign : 'center',fixed : true,	width : 120,sortable:true}
		 
		]]
	});
}

function loadqryconditon(){
	$("#dealStatus").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [ {text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			qryProImp();
			switch (record.value) {
			case '3':
				$('#btn_add').linkbutton('disable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_send').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('disable');
				break;
			case '1':
				$('#btn_add').linkbutton('enable');
				$('#btn_edit').linkbutton('enable');
				$('#btn_remove').linkbutton('enable');
				$('#btn_send').linkbutton('enable');
				$('#btn_revoke').linkbutton('disable');
				$('#btn_detail').linkbutton('enable');
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
	});
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
	
	$('#btn_revoke').linkbutton('disable');
}

/**
 * 查询
 */
function qryProImp(){
	var param = {
			dealStatus : $("#dealStatus").combobox('getValue'),
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
	$("#datagrid_proimp").datagrid("load",param);
}

/**
 * 新增
 */
function addProImp(){
	var rows = datagrid_proimp.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}else{
		if(rows[0].implementid!=null){
			easyui_warn("已录入的数据，不能再次录入！");
			return;
		}
	}
	showModalDialog(570, "proimp_form", types.add, datagrid_proimp, "实施情况新增",urls.optProImpView+"?optFlag="+types.add, urls.saveProImp+"?optFlag="+types.add);
}

/**
 * 修改
 */
function editProImp(){
	var rows = datagrid_proimp.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}else{
		if(rows[0].implementid==null){
			easyui_warn("该数据还未录入，请先录入数据！");
			return;
		}
	}
	showModalDialog(570, "proimp_form", types.edit, datagrid_proimp, "实施情况修改",urls.optProImpView+"?optFlag="+types.edit, urls.saveProImp+"?optFlag="+types.edit);
}

/**
 * 详情
 */
function detProImp(){
	var rows = datagrid_proimp.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条数据！");
		return;
	}
	showModalDialog(570, "proimp_form", types.view, datagrid_proimp, "实施情况详情",urls.optProImpView+"?optFlag="+types.view, urls.saveProImp+"?optFlag="+types.view);
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
			var projectid = mdDialog.find("#projectid").val();
			proFinanceGrid(mdDialog,'',projectid);
			
			if(operType=="view"){
				//加载附件
				showFileDiv(mdDialog.find("#proimppath"),false, row.safeguardMeasurePath, "30","safeguardMeasurePath");
				mdDialog.find("#fin_add").linkbutton('disable');
				mdDialog.find("#fin_del").linkbutton('disable');
			}else{
				//加载附件
				showFileDiv(mdDialog.find("#proimppath"),true, row.safeguardMeasurePath, "30","safeguardMeasurePath");
				mdDialog.find("#fin_add").linkbutton('enable');
				mdDialog.find("#fin_del").linkbutton('enable');
			}
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};
var isEditFlag = false;
var currentRow = -1;
function proFinanceGrid(mdDialog,implementid,projectid){
	var grid = mdDialog.find("#advance_organ").datagrid({
        height: 200,
        url : urls.qryProFinance,
        queryParams : {implementid:implementid,projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'financeid',
        multiSort:false,
        showFooter : true,
        columns: [[//显示的列
                   { field: 'financeid',checkbox : true, title: '主键id', halign : 'center',width: 100, sortable: true}
                   ,{ field: 'projectid', title: '外键id', halign : 'center',width: 200 ,hidden:true}
                   ,{ field: 'financeCode', title: '融资机构代码', halign : 'center',width: 100 ,editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入融资机构代码',validType:{length:[0,25]}}} }
                   ,{ field: 'financeName', title: '融资机构名称',halign : 'center', width: 300,sortable: true,editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入融资机构名称',validType:{length:[0,50]}}}}
                   ,{ field: 'amount', title: '融资金额（万元）', halign : 'center',width: 100,sortable: true ,editor: { type: 'numberbox', options: { required: true,min:0,precision:2,missingMessage:'请输入融资金额（万元）',validType:{length:[0,17]}}} }
                   ,{ field: 'remark', title: '备注',halign : 'center', width: 300,sortable: true ,editor: { type: 'validatebox',options: {validType:{length:[0,500]}}}}
                   ]],
       toolbar: [{
    	   id:"fin_add",
           text: '添加', iconCls: 'icon-add', handler: function () {
        	   var data = mdDialog.find("#advance_organ").datagrid("getData");//grid列表
        	   var total = data.total;//grid的总条数
        	   if(total!=0){
        		   if(mdDialog.find('#advance_organ').datagrid('validateRow', currentRow)){
        			   	mdDialog.find('#advance_organ').datagrid('endEdit', currentRow);
        				mdDialog.find("#advance_organ").datagrid('appendRow', {row: {}});//追加一行
                       	mdDialog.find("#advance_organ").datagrid("beginEdit", total);//开启编辑
                       	currentRow = mdDialog.find('#advance_organ').datagrid('getRows').length-1;
                       	isEditFlag = true;
        		   }else{
        			   easyui_warn("当前存在正在编辑的行！");
        		   }
        	   }else{
                   	mdDialog.find("#advance_organ").datagrid('appendRow', {row: {}});//追加一行
                   	mdDialog.find("#advance_organ").datagrid("beginEdit", total);//开启编辑
                   	currentRow = mdDialog.find('#advance_organ').datagrid('getRows').length-1;
                   	isEditFlag = true;
        	   }
        	   
           
           }
       },'-', {
    	   id:"fin_del",
           text: '删除', iconCls: 'icon-remove', handler: function () {
               var row = mdDialog.find("#advance_organ").datagrid('getSelections');
               if(row ==''){
               		easyui_warn("请选择要删除的数据！");
               		return;
               }else{
            	   var rowIndex = rowIndex= mdDialog.find("#advance_organ").datagrid('getRowIndex',row[0]);
            	   
               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
	               		
                        if (r) {
                       	 if(row[0].financeid==undefined){//没有录数据的情况下，直接在界面删除
                       		if(rowIndex == currentRow ){
                       			isEditFlag = false;
    	               		}
                       		if(isEditFlag ==false){
                       			mdDialog.find("#advance_organ").datagrid('deleteRow',rowIndex);
                       			calcMoney('',mdDialog);
                       		}else{
                       			if(mdDialog.find('#advance_organ').datagrid('validateRow', currentRow)){
                       				mdDialog.find('#advance_organ').datagrid('endEdit', currentRow);
                       				mdDialog.find("#advance_organ").datagrid('deleteRow',rowIndex);
                           			calcMoney('',mdDialog);
                       			}else{
                       				easyui_warn("当前存在正在编辑的行！");
                       			}
                       		}
                       	 }else{//已录入数据的情况下，需要通过数据库删除
                       		$.post(urls.delProFinance, {
	                       		financeid :row[0].financeid
	            			}, function(result) {
	            				easyui_auto_notice(result, function() {
	            					mdDialog.find("#advance_organ").datagrid('deleteRow',rowIndex);
	            					if(rowIndex = currentRow){
	                           			isEditFlag = false;
	        	               		}
	            					//grid.datagrid("reload");
	            				});
	            			}, "json");
                       	 }
	                       	
                        }
                    });
               }
            }
       }],
       onDblClickRow:function(rowIndex, rowData) {
    	   //结束上一行编辑，开启新行的编辑
    	   if(currentRow != rowIndex && mdDialog.find('#advance_organ').datagrid('validateRow', currentRow)){
    		   //结束上一行编辑
    		   mdDialog.find('#advance_organ').datagrid('endEdit', currentRow);
    		   //开启新行编辑
    			mdDialog.find("#advance_organ").datagrid('beginEdit', rowIndex);
    			currentRow = rowIndex;
    			isEditFlag = true;
    	   }else{
    		   easyui_warn("当前存在正在编辑的行！");
    	   }
    	   
    	    //bindGridEvent(rowIndex,mdDialog);
       },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
    	   
    	   if( mdDialog.find('#advance_organ').datagrid('validateRow', currentRow)){
    		   //结束上一行编辑
    		   mdDialog.find('#advance_organ').datagrid('endEdit', currentRow);
    		   currentRow = -1;
    		   isEditFlag = false;
    	   }
    	   
       },onEndEdit:function(rowIndex, rowData){//结束编辑事件主要用来计算总和
    	   calcMoney(rowIndex,mdDialog);
       }
       
    });
	return grid;
}

//计算
function calcMoney(rowIndex,mdDialog)
{
	var datagrid = mdDialog.find('#advance_organ')
	var rowsnum =  datagrid.datagrid('getRows');
	var sum = 0;
	  for(var i=0;i<rowsnum.length;i++){
		  if(rowsnum[i].amount == undefined || rowsnum[i].amount == null || rowsnum[i].amount ==''){
			  sum +=0;
		  }else{
			  sum +=Number(rowsnum[i].amount);
		  }
	  }
	  var rows = datagrid.datagrid('getFooterRows');
	  rows[0]['amount'] = sum;
	  datagrid.datagrid('reloadFooter');
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
					easyui_warn("请至少录入一条融资信息！",null);
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
					easyui_warn("请至少录入一条融资信息！",null);
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
					datagrid_proimp.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};

/**
 * 删除
 */
function delProImp(){
	var rows = datagrid_proimp.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var implementids = "";
	for(var i=0;i<rows.length;i++){
		
		if(rows[i].implementid==null){
			easyui_warn("数据还未录入，请先录入数据！");
			return;
		}
		
		if(i==0){
			implementids = rows[i].implementid;
		}else{
			implementids += ","+rows[i].implementid;
		}
	}
	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function(r) {
		if (r) {
			$.post(urls.delProImp, {
				implementids :implementids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_proimp.datagrid("reload");
				});
			}, "json");
		}
	});
}

function subProImp(){
	var selectRow = datagrid_proimp.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var implementids = "";
	for(var i=0;i<selectRow.length;i++){
		if(selectRow[i].implementid==null){
			easyui_warn("数据还未录入，请先录入数据！");
			return;
		}
		if(i==0){
			implementids = selectRow[i].implementid;
		}else{
			implementids += ","+selectRow[i].implementid;
		}
	}
	parent.$.messager.confirm("提交确认", "确认提交选中的数据？", function(r) {
		if (r) {
			$.post(urls.subProImp, {
				menuid : menuid,
				implementid : implementids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_proimp.datagrid('reload');
				});
			}, "json");
		}
	});
}

function revokeProImp(){
	var selectRow = datagrid_proimp.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var implementids = "";
	for(var i=0;i<selectRow.length;i++){
		if(selectRow[i].implementid==null){
			easyui_warn("数据还未录入，请先录入数据！");
			return;
		}
		if(i==0){
			implementids = selectRow[i].implementid;
		}else{
			implementids += ","+selectRow[i].implementid;
		}
	}
	parent.$.messager.confirm("撤回确认", "确认要撤回选中的数据？", function(r) {
		if (r) {
			$.post(urls.revokeProImp, {
				menuid : menuid,
				implementid : implementids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_proimp.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 流程信息
 */
function workflowMessage(){
	var selectRow = datagrid_proimp.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}