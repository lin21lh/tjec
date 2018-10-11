
var baseUrl = contextpath + "ppms/discern/ProjectPrepareDxpjController/";
var urls = {
		queryUrl : baseUrl + "queryProject.do?vfmPjhj=1",
		queryIsExistDxpj : baseUrl + "queryIsExistDxpj.do",
		getZbdfGridColumns : baseUrl + "getZbdfGridColumns.do",
		queryZbList : baseUrl + "queryZbList.do",
		zbdfSave : baseUrl + "zbdfSave.do",
		dxpjAddUrl : baseUrl + "dxpjAdd.do",
		sendDxpj : baseUrl + "sendDxpj.do",
		revokeDxpj : baseUrl + "revokeDxpj.do",
		dxpjDetailUrl : baseUrl + "dxpjDetail.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	$('#backBtn').linkbutton('disable');
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已提交", value : "2"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
			case '1':
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#delBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
				$('#delBtn').linkbutton('disable');
				$('#sendBtn').linkbutton('disable');
				$('#backBtn').linkbutton('enable');
				break;
			default:
				break;
			}
		}
	});
	comboboxFuncByCondFilter(menuid,"proType", "PROTYPE", "code", "name");//项目类型
	comboboxFuncByCondFilter(menuid,"proPerate", "PROOPERATE", "code", "name");//项目运作方式
	comboboxFuncByCondFilter(menuid,"proReturn", "PRORETURN", "code", "name");//项目回报机制
	comboboxFuncByCondFilter(menuid,"proSendtype", "PROSENDTYPE", "code", "name");//项目发起类型
	$("#proTrade").treeDialog({
		title :'选择所属行业',
		dialogWidth : 420,
		dialogHeight : 500,
		hiddenName:'proTrade',
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
	$("#proReturn").combobox("addClearBtn", {iconCls:"icon-clear"});
	loadProjectGrid(urls.queryUrl);
});
var projectDataGrid;
//页面刷新
function showReload() {
	projectDataGrid.datagrid('reload'); 
}
//加载可项目grid列表
function loadProjectGrid(url) {
	projectDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			status : "1",
			menuid : menuid,
			xmhj :xmhj,
			firstNode : true,
			lastNode : false
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "projectid",checkbox : true}  
			          ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
			          ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
			          ,{field : "amount",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
			          ,{field : "czResultName",title : "项目状态",halign : 'center',	width:80,sortable:true}
			          ,{field : "vfmDlpjName",title : "是否VFM定量评价",halign : 'center',	width:100,sortable:true}
			          ,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
			          ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:100	,sortable:true}
			          ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:100,sortable:true	}
			          ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:120,sortable:true}
			          ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
			          ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
			          ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
			          ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
			          ,{field : "sfxmName",title : "示范项目",halign : 'center',	width:100,sortable:true }
			          ,{field : "tjxmName",title : "推介项目",halign : 'center',	width:80,sortable:true }
			          ,{field : "sqbtName",title : "申请补贴",halign : 'center',	width:80,sortable:true }
			          ,{field : "btje",title : "补贴金额（万元）",halign : 'right',align:'right'}
			          ,{field : "proSituation",title : "项目概况",halign : 'center',	width:150,sortable:true }
			          ,{field : "proPhone",title : "联系人电话",halign : 'center',	width:150,sortable:true}
			          ,{field : "proScheme",title : "初步实施方案内容",halign : 'center',	width:150,sortable:true	}
			          ,{field : "proArticle",title : "项目产出物说明",halign : 'center',	width:150,sortable:true	}
			          ,{field : "createusername",title : "创建人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "createtime",title : "创建时间",halign : 'center',	width:130,sortable:true	}
			          ,{field : "updateusername",title : "修改人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "updatetime",title : "修改时间",halign : 'center',	width:130,sortable:true	}
		              ,{field : "status",title : "当前状态",halign : 'center',	width:80,sortable:true,hidden:true}
		              ,{field : "proType",title : "项目类型",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proTrade",title : "所属行业",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proPerate",title : "运作方式",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proReturn",title : "回报机制",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proSendtype",title : "项目发起类型",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proSchemepath",title : "实施方案附件路径",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proReportpath",title : "可行性研究报告路径",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proConditionpath",title : "环评报告路径",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "proArticlepath",title : "产出物附件路径",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "wfid",title : "WFID",halign : 'center',	width:150,sortable:true,hidden:true	}
		              ,{field : "sfxm",title : "示范项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "tjxm",title : "推介项目",halign : 'center',	width:100,sortable:true,hidden:true	 }
			          ,{field : "sqbt",title : "申请补贴",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "vfmDlpj",title : "VFM定量评价",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "pszbid",title : "pszbid",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualVerifytime",title : "定性评估验证完成时间",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualConclusion",title : "定性评估描述",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualDf",title : "定性评价得分",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualResult",title : "定性评估结果",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualPath",title : "定性评估附件",halign : 'center',	width:100,sortable:true ,hidden:true	}
			          ,{field : "qualResultName",title : "定性评估结果",halign : 'center',	width:100,sortable:true ,hidden:true	}
		             ] ]
	});
}
/**
 * 项目查询
 */
function topQuery(){
	var param = {
			status : $("#status").combobox('getValue'),
			proName : $("#proName").val(),
			proPerson : $("#proPerson").val(),
			proTrade :  $("#proTrade").val(),
			proPerate : $("#proPerate").combobox('getValues').join(","),
			proReturn : $("#proReturn").combobox('getValues').join(","),
			proSendtype : $("#proSendtype").combobox('getValues').join(","),
			proType : $("#proType").combobox('getValues').join(","),
			menuid : menuid,
			xmhj : xmhj
		};
	projectDataGrid.datagrid("load", param);
}
/**
 * 新增
 */
function dxpjAdd(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistDxpj,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有定性分析过
		//新增
		parent.$.modalDialog({
			title : "VFM定性评价",
			width : 900,
			height : 610,
			href : urls.dxpjAddUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#pszbid").val(selectRow[0].pszbid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				//是否
				comboboxFuncByCondFilter(menuid,"vfmDlpj", "SYS_TRUE_FALSE", "code", "name",mdDialog);//项目类型
				comboboxFuncByCondFilter(menuid,"qualResult", "JUDGERESULT", "code", "name",mdDialog);//评价结果
				mdDialog.find("#vfmDlpj").combobox("setValue",selectRow[0].vfmDlpj);
				zbdfTableGrid(mdDialog,selectRow[0].pszbid);
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("1");//保存未提交
				}
			}, {
				text : "提交",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("2");//提交
				}
			},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}else{
		dxpjEdit();
	}
}
//定性分析指标
var currentRow_zb = -1;
function zbdfTableGrid(mdDialog,pszbid){
	//动态获取列 
	var result = $.ajax({
		  url: urls.getZbdfGridColumns,
		  async: false,     
		  data: {pszbid:pszbid}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){
		var grid = mdDialog.find("#zbdfGrid").datagrid({
			height: 360,
			title: '定性评价指标得分',
			collapsible: false,
			url : urls.queryZbList,
			queryParams : {pszbid:pszbid},
			singleSelect: true,
			rownumbers : true,
			columns: eval(result.title),
			onClickCell : function(rowIndex, field, value){
				mdDialog.find("#zbdfGrid").datagrid('beginEdit', rowIndex);
			   var editor = mdDialog.find("#zbdfGrid").datagrid('getEditor', {index:rowIndex,field:field});
   			   $(editor.target).next('span').find('input').focus();//聚集焦点
   			   $(editor.target).next('span').find('input').select();//选中
			}
			});
		return grid;
	}else{
		easyui_warn("操作出错！",null);
	}
}
/**
 * 表单提交
 */
function forSubmit(status,flag){
	parent.$.modalDialog.handler.find("#status").val(status);
	parent.$.modalDialog.handler.find("#xmhj").val(xmhj);
	parent.$.modalDialog.handler.find("#updateFlag").val(flag);
	var form = parent.$.modalDialog.handler.find('#prepareAddForm');
	var isValid = form.form('validate');
	if (!isValid) {//验证表单
		return;
	}
	var mdDialog = parent.$.modalDialog.handler;
	//评价指标
	var zbdfGrid = mdDialog.find("#zbdfGrid");
	var zddfData = zbdfGrid.datagrid("getData");
	var zbdfTotal = zddfData.total;
	var isPass = true;
	for(var i=0;i<zbdfTotal;i++){
		//打开行编辑、验证是否通过验证
		zbdfGrid.datagrid('beginEdit', i);
		isPass = zbdfGrid.datagrid('validateRow', i);
		zbdfGrid.datagrid('endEdit', i);//把所有的编辑行锁定
		if(!isPass){
			break;
		}
	}
	//验证评分是否都已经填写完成
	if(!isPass){
		return;
	}
	var zbdfRow = zbdfGrid.datagrid('getRows');
	parent.$.modalDialog.handler.find("#zbdfGridData").val(JSON.stringify(zbdfRow));
	var message =status ==1?"确认要保存？":"确认要提交？";
	parent.$.messager.confirm("操作确认",message, function (r) {
        if (r) {
        	form.form("submit",{
        		url : urls.zbdfSave,
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
        				projectDataGrid.datagrid('reload');
        				parent.$.modalDialog.handler.dialog('close');
        			} else {
        				parent.$.messager.alert('错误', result.title, 'error');
        			}
        			}
        	});
        }
  });
}
/**
 * 修改
 */
function dxpjEdit(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistDxpj,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有定性分析过
		dxpjAdd();
	} else {
		//修改
		parent.$.modalDialog({
			title : "VFM定性评价",
			width : 900,
			height : 610,
			href : urls.dxpjAddUrl,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				mdDialog.find("#projectid").val(selectRow[0].projectid);
				mdDialog.find("#pszbid").val(selectRow[0].pszbid);
				mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
				mdDialog.find("#qualPath").val(selectRow[0].qualPath);
				mdDialog.find("#qualDf").val(selectRow[0].qualDf);
				mdDialog.find("#vfmDlpj").searchbox("setValue", selectRow[0].vfmDlpj);
				mdDialog.find("#qualVerifytime").textbox("setValue", selectRow[0].qualVerifytime);
				mdDialog.find("#qualConclusion").textbox("setValue", selectRow[0].qualConclusion);
				mdDialog.find("#qualResult").searchbox("setValue", selectRow[0].qualResult);
				//是否
				comboboxFuncByCondFilter(menuid,"vfmDlpj", "SYS_TRUE_FALSE", "code", "name",mdDialog);//项目类型
				comboboxFuncByCondFilter(menuid,"qualResult", "JUDGERESULT", "code", "name",mdDialog);//评价结果
				mdDialog.find("#vfmDlpj").combobox("setValue",selectRow[0].vfmDlpj);
				zbdfTableGrid(mdDialog,selectRow[0].pszbid);
			},
			buttons : [ {
				text : "保存",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("1","1");//保存未提交
				}
			}, {
				text : "提交",
				iconCls : "icon-save",
				handler : function() {
					forSubmit("2","1");//提交
				}
			},{
				text : "关闭",
				iconCls : "icon-cancel",
				handler : function() {
					parent.$.modalDialog.handler.dialog('close');
				}
			} ]
		});
	}
}
/**
 * 提交
 */
function sendWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//同步获取该项目是否有新增的项目
	var result = $.ajax({
		  url: urls.queryIsExistDxpj,
		  async: false,     
		  data: {projectid:selectRow[0].projectid,xmhj:xmhj}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){//没有定性分析过
		easyui_warn("该数据未保存录入，请先新增数据！",null);
		return;
	}
	var projectid = selectRow[0].projectid;
	var dxpjid = selectRow[0].dxpjid;
	parent.$.messager.confirm("操作确认", "确认提交选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.sendDxpj,
        		async: false,     
        		data: {projectid:projectid,dxpjid:dxpjid}
        	}).responseText; 
        	result = $.parseJSON(result);
        	easyui_auto_notice(result, function() {
        		topQuery();
			});
        }
	});
}
/**
 * 撤回
 */
function revokeWF(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var projectid = selectRow[0].projectid;
	/*是否可撤回验证*/
	if (!recallYN(xmhj,"dx",projectid)){
		easyui_warn("选中项目定量评价已提交，不允许撤回！",null);
		return;
	}
	var dxpjid = selectRow[0].dxpjid;
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function (r) {
        if (r) {
        	var result = $.ajax({
        		url: urls.revokeDxpj,
        		async: false,     
        		data: {projectid:projectid,dxpjid:dxpjid}
        	}).responseText; 
        	result = $.parseJSON(result);
        	easyui_auto_notice(result, function() {
        		topQuery();
			});
        }
	});
}
/**
 * 详情
 */
function dxpjView(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	//新增
	parent.$.modalDialog({
		title : "VFM定性评价",
		width : 900,
		height : 610,
		href : urls.dxpjDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			mdDialog.find("#projectid").val(selectRow[0].projectid);
			mdDialog.find("#pszbid").val(selectRow[0].pszbid);
			mdDialog.find("#dxpjid").val(selectRow[0].dxpjid);
			mdDialog.find("#qualPath").val(selectRow[0].qualPath);
			mdDialog.find("#qualDf").val(selectRow[0].qualDf);
			mdDialog.find("#vfmDlpjName").textbox("setValue", selectRow[0].vfmDlpjName);
			mdDialog.find("#qualVerifytime").textbox("setValue", selectRow[0].qualVerifytime);
			mdDialog.find("#qualConclusion").textbox("setValue", selectRow[0].qualConclusion);
			mdDialog.find("#qualResultName").textbox("setValue", selectRow[0].qualResultName);
			zbdfTableGridDetail(mdDialog,selectRow[0].pszbid);
		},
		buttons : [{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ]
	});
}

function zbdfTableGridDetail(mdDialog,pszbid){
	//动态获取列 
	var result = $.ajax({
		  url: urls.getZbdfGridColumns,
		  async: false,     
		  data: {pszbid:pszbid}
		}).responseText; 
	result = $.parseJSON(result);
	if(result.success){
		var grid = mdDialog.find("#zbdfGrid").datagrid({
			height: 360,
			title: '定性评价指标得分',
			collapsible: false,
			url : urls.queryZbList,
			queryParams : {pszbid:pszbid},
			singleSelect: true,
			rownumbers : true,
			columns: eval(result.title)
			});
		return grid;
	}else{
		easyui_warn("操作出错！",null);
	}
}