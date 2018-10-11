//设置全局变量
var datagrid_prores;
/**开发计划
 * prores_audit.js
 * */
var baseUrl = contextpath + "procurement/controller/ProcurementResultController/";
//路径
var urls = {
	qryProRes : baseUrl + "qryProRes.do",
	delProRes : baseUrl+ "delProRes.do",
	optProResView : baseUrl+ "optProResView.do",
	saveProRes : baseUrl+ "saveProRes.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	backWorkFlow : baseUrl + "backWorkFlow.do",
	resultExpertGrid : baseUrl + "resultExpertGrid.do",
	resultGroupGrid : baseUrl + "resultGroupGrid.do",
	saveAuditData : baseUrl + "saveAuditData.do",
	advanceOrganUrl : baseUrl +"advanceOrganUrl.do"
};
//类型
var types = {
		audit_view : "audit_view",
		add : "add",
		edit : "edit",
		audit_single : "audit_single", 
		audit_multiple :"audit_multiple",
		back_multiple :"back_multiple"
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
	datagrid_prores = $("#datagrid_prores").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		url : urls.qryProRes,
		queryParams: {
			dealStatus : 1,
			isAudit : 1,
			activityId :activityId,
			firstNode : firstNode,
			lastNode : lastNode,
			menuid : menuid
		},
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_prores",
		showFooter : true,
		columns : [ [ {	field : "projectid",checkbox : true, rowspan:2,	align:'left'}
		, {field : "proName",title : "项目名称",halign : 'center',	width : 250,sortable:true,align:'left', rowspan:2}
		, {field : "proTypeName",title : "项目类型",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		, {field : "statusName",title : "状态",halign : 'center',	width : 80,sortable:true, align:'left',rowspan:2}
		,{title:'采购结果信息',colspan:9}
		,{title:'项目信息',colspan:13}
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
		, {field : "advanceid",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		
		]
		, [
			 {field : "purchaseid",title : "主键id",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "datatype",title : "数据类型",halign : 'center',	width : 120,align:'left',sortable:true,rowspan:1,hidden:true}
			, {field : "purchaseNoticeTime",title : "采购公告时间",halign : 'center',	width : 120,sortable:true}
			, {field : "purchaseNoticeMedia",title : "公告发布媒体",halign : 'center',	width : 120,sortable:true}
			, {field : "fileCommitTime",title : "响应文件提交时间",halign : 'center',	width : 120,sortable:true}
			, {field : "fileJudgeTime",title : "响应文件评审时间",halign : 'center',	width : 120,sortable:true}
			, {field : "negotiateTime",title : "结果确认谈判时间",halign : 'center',	width : 120,sortable:true}
			, {field : "govVerifyTime",title : "政府审核时间",halign : 'center',	width : 120,sortable:true}
			, {field : "contractTime",title : "合同签署时间",halign : 'center',	width : 120,sortable:true}
			, {field : "contractPublishTime",title : "合同公布时间",halign : 'center',	width : 120,sortable:true}
			, {field : "contractPublishMedia",title : "合同公布媒体",halign : 'center',	width : 120,sortable:true}
			, {field : "contractPath",title : "PPP项目合同附件路径",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "remark",title : "备注",halign : 'center',	width : 120,sortable:true,hidden:true}
			, {field : "wfid",title : "工作流ID",halign : 'center',	width : 120,sortable:true,hidden:true}
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
		data : [ {text : "未处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			qryProRes();
			switch (record.value) {
			case '1':
				$('#btn_send').linkbutton('enable');
				$('#btn_back').linkbutton('enable');
				$('#btn_detail').linkbutton('enable');
				$('#btn_flow').linkbutton('enable');
				break;
			case '2':
				$('#btn_send').linkbutton('disable');
				$('#btn_back').linkbutton('disable');
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
	
	$('#btn_add').linkbutton('disable');
}

/**
 * 查询
 */
function qryProRes(){
	var param = {
			isAudit : 1,
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
	$("#datagrid_prores").datagrid("load",param);
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
function showModalDialogAudit(height, form, operType, dataGrid, title, href) {
	var selectRow = datagrid_prores.datagrid('getChecked');
	
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==0){
			wfids = selectRow[i].wfid;
		}else{
			wfids += ","+selectRow[i].wfid;
		}
	}
	var icon = 'icon-edit';
	if("audit_view"==operType){
		icon = "icon-view";
	}
	
	if("audit_single"==operType || "audit_view"==operType){
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
				//if("view"==operType){
					//加载附件
					showFileDiv(mdDialog.find("#prorespath"),false, row.contractPath, "24","contractPath");
				/*}else{
					//加载附件
					showFileDiv(mdDialog.find("#prorespath"),true, row.contractPath, "30","contractPath");
				}*/
				
				var advanceid = mdDialog.find("#advanceid").val();
				advanceOrganGrid(mdDialog,advanceid,'');
				var queryParams = {optFlag:'edit',purchaseid:row.purchaseid};
				resultExpert(mdDialog,queryParams);
				if("audit_view"==operType){
					resultGroupGridDetail(mdDialog,row.purchaseid);
				}else{
					resultGroupGrid(mdDialog,row.purchaseid);
				}
				
			},
			buttons : funcOperButtons(operType, dataGrid, form,"")
		});
	}else{
		parent.$.modalDialog({
			title : title,
			iconCls : icon,
			width : 450,
			height : 210,
			href : href,
			onLoad : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var row = dataGrid.datagrid("getSelections")[0];
				var f = parent.$.modalDialog.handler.find('#' + form);
				f.form("load", row);
				//加载附件
				showFileDiv(mdDialog.find("#prorespath"),true, row.contractPath, "30","contractPath");
			},
			buttons : funcOperButtons(operType,dataGrid, form,wfids)
		});
	}
	
};

function advanceOrganGrid(mdDialog,advanceid,projectid){
	var grid = mdDialog.find("#advance_organ").datagrid({
		title : '资格预审机构',
        height: 200,
        url : urls.advanceOrganUrl,
        queryParams : {advanceid:advanceid,projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        idField: 'organid',
        columns: [[//显示的列
                   { field: 'advanceid', title: '主键id', halign : 'center',width: 100, sortable: true,hidden:true }
                   ,{ field: 'projectid', title: '项目id', halign : 'center',width: 200 ,hidden:true}
                   ,{ field: 'organ_code', title: '机构代码', halign : 'center',width: 100 ,hidden:true}
                   ,{ field: 'organ_name', title: '机构名称',halign : 'center', width: 200,sortable: true}
                   ,{ field: 'organ_content', title: '机构说明', halign : 'center',width: 170,sortable: true }
                   ,{ field: 'iscombo', title: '是否联合体', halign : 'center',width: 100,sortable: true,formatter: function(value){return isOrNot[value];} }
                   ,{ field: 'ispass', title: '是否通过预审', halign : 'center',width: 100,sortable: true,formatter: function(value){return isOrNot[value];}  }
                   ,{ field: 'score', title: '评分', width: 70,halign : 'center' }
                   ,{ field: 'isbid', title: '是否中选',halign : 'center', width: 100,formatter: function(value){return isOrNot[value];},editor: { type: 'checkbox', options: { on: '1',off: '0'}}}
                   ,{ field: 'remark', title: '备注',halign : 'center', width: 200,sortable: true }
                   ]]
    });
	return grid;
}
var currentRow_group=-1;
function resultGroupGrid(mdDialog,purchaseid){
	var grid = mdDialog.find("#resultGroup").datagrid({
		title : '采购结果确认谈判工作组',
        height: 190,
        url : urls.resultGroupGrid,
        queryParams : {purchaseid:purchaseid},
        rownumbers : true,
        singleSelect: true,
        idField: 'negotiationid',
        columns: [[//显示的列
                   { field: 'negotiationid', title: '主键id', halign : 'center',width: 100, sortable: true,hidden:true }
                   ,{ field: 'purchaseid', title: '项目id', halign : 'center',width: 200 ,hidden:true}
                   ,{ field: 'name', title: '姓名', halign : 'center',width: 100 
                	,editor: { type: 'validatebox', options: { required:true,missingMessage:'请输入姓名',validType:{length:[0,50]}} }   
                   }
                   ,{ field: 'phone', title: '联系方式',halign : 'center', width: 200,sortable: true
                	   ,editor: { type: 'validatebox', options: { required:true,missingMessage:'请输入联系方式',validType:{length:[0,50]}} }     
                   }
                   ,{ field: 'duty', title: '职位或职责',halign : 'center', width: 200,sortable: true
                	   ,editor: { type: 'validatebox', options: { required:true,missingMessage:'请输入职位或职责',validType:{length:[0,50]}} }     
                   }
                   ,{ field: 'remark', title: '说明',halign : 'center', width: 300,sortable: true 
                	   ,editor: { type: 'validatebox', options: { validType:{length:[0,500]}} }     
                   }
                   ]],
		           toolbar: [{ id:"expert_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#resultGroup").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#resultGroup').datagrid('validateRow', currentRow_group)){
		            			   	mdDialog.find('#resultGroup').datagrid('endEdit', currentRow_group);
		            				mdDialog.find("#resultGroup").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#resultGroup").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_group = mdDialog.find('#resultGroup').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#resultGroup").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#resultGroup").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_group = mdDialog.find('#resultGroup').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#resultGroup").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#resultGroup").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_group==rowIndex){
		    	                        		currentRow_group = -1;
		    	                        	}else if(rowIndex<currentRow_group){
		    	                        		currentRow_group = currentRow_group-1;
		    	                        	}
		    	                       		mdDialog.find("#resultGroup").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_group != rowIndex && mdDialog.find('#resultGroup').datagrid('validateRow', currentRow_group)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#resultGroup').datagrid('endEdit', currentRow_group);
		        		   //开启新行编辑
		        			mdDialog.find("#resultGroup").datagrid('beginEdit', rowIndex);
		        			currentRow_group = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#resultGroup').datagrid('validateRow', currentRow_group)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#resultGroup').datagrid('endEdit', currentRow_group);
		        		   currentRow_group = -1;
		        	   }
		           }
    });
	return grid;
}

function resultGroupGridDetail(mdDialog,purchaseid){
	var grid = mdDialog.find("#resultGroup").datagrid({
		title : '采购结果确认谈判工作组',
        height: 240,
        url : urls.resultGroupGrid,
        queryParams : {purchaseid:purchaseid},
        rownumbers : true,
        singleSelect: true,
        idField: 'negotiationid',
        columns: [[//显示的列
                   { field: 'negotiationid', title: '主键id', halign : 'center',width: 100, sortable: true,hidden:true }
                   ,{ field: 'purchaseid', title: '项目id', halign : 'center',width: 200 ,hidden:true}
                   ,{ field: 'name', title: '姓名', halign : 'center',width: 100 
                	,editor: { type: 'validatebox', options: { validType:{length:[0,50]}} }   
                   }
                   ,{ field: 'phone', title: '联系方式',halign : 'center', width: 200,sortable: true
                	   ,editor: { type: 'validatebox', options: { validType:{length:[0,50]}} }     
                   }
                   ,{ field: 'duty', title: '职位或职责',halign : 'center', width: 200,sortable: true
                	   ,editor: { type: 'validatebox', options: { validType:{length:[0,50]}} }     
                   }
                   ,{ field: 'remark', title: '说明',halign : 'center', width: 300,sortable: true 
                	   ,editor: { type: 'validatebox', options: { validType:{length:[0,500]}} }     
                   }
                   ]]
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
function funcOperButtons(operType, dataGrid, form,wfids) {

	var buttons;
	if("audit_single"==operType){
		buttons = [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				//保存采购结果确认谈判工作组
				var o=parent.$.modalDialog.handler.find("#resultGroup");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请录入工作组信息！",null);
					return;
				}
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultGroupData").val(subStr);
				
						parent.$.modalDialog.handler.find("#"+form).form("submit",{
							url : urls.saveAuditData,
							queryParams:{
								menuid : menuid,
								solutionid : parent.$.modalDialog.handler.find('#solutionid').val()
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									//重新加载gride
									parent.$.modalDialog.handler.dialog('close');
									easyui_info(result.title);
									datagrid_prores.datagrid("reload");
								} else {
									parent.$.messager.alert('错误', result.title, 'error');
								}
							}
						});
						
					}
		},{
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				//保存采购结果确认谈判工作组
				var o=parent.$.modalDialog.handler.find("#resultGroup");
				if(o.datagrid('getRows').length<1){
					easyui_warn("请录入工作组信息！",null);
					return;
				}
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultGroupData").val(subStr);
				
				parent.$.messager.confirm("审核确认", "确认要审核同意？", function(r) {
					if (r) {
						
						parent.$.modalDialog.handler.find("#"+form).form("submit",{
							url : urls.sendWFUrl,
							queryParams:{
								menuid : menuid,
								activityId :activityId,
								solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
								firstNode : firstNode,
								lastNode : lastNode,
								wfid :parent.$.modalDialog.handler.find('#wfid').val()
							},
							success : function(result) {
								parent.$.messager.progress('close');
								result = $.parseJSON(result);
								if (result.success) {
									//重新加载gride
									parent.$.modalDialog.handler.dialog('close');
									easyui_info(result.title);
									datagrid_prores.datagrid("reload");
								} else {
									parent.$.messager.alert('错误', result.title, 'error');
								}
							}
						});
						
					}
				});
			}
		},{
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				parent.$.messager.confirm("退回确认", "确认要退回？", function(r) {
					if (r) {
						$.post(urls.backWorkFlow, {
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
							wfid :parent.$.modalDialog.handler.find('#wfid').val(),
							opinion: parent.$.modalDialog.handler.find('#opinion').val()
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								datagrid_prores.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if("audit_multiple"==operType){
		buttons = [ {
			text : "同意",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				var opinion = parent.$.modalDialog.handler.find('#opinion').val()
				parent.$.messager.confirm("审核确认", "确认要审核同意？", function(r) {
					if (r) {
						$.post(urls.sendWFUrl, {
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
							opinion : opinion,
							wfid :wfids
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								datagrid_prores.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else if("back_multiple"==operType){
		buttons = [ {
			text : "退回",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var isValid = parent.$.modalDialog.handler.find("#"+form).form('validate');
				if (!isValid) {
					return;
				}
				
				var opinion = parent.$.modalDialog.handler.find('#opinion').val()
				parent.$.messager.confirm("退回确认", "确认要退回？", function(r) {
					if (r) {
						$.post(urls.backWorkFlow, {
							menuid : menuid,
							activityId :activityId,
							solutionid : parent.$.modalDialog.handler.find('#solutionid').val(),
							firstNode : firstNode,
							lastNode : lastNode,
							opinion : opinion,
							wfid :wfids
						}, function(result) {
							easyui_auto_notice(result, function() {
								parent.$.modalDialog.handler.dialog('close');
								datagrid_prores.datagrid('reload');
							});
						}, "json");
					}
				});
			}
		},{
			text : "关闭",
			iconCls : "icon-cancel",
			handler : function() {
				parent.$.modalDialog.handler.dialog('close');
			}
		} ];
	}else{
		buttons = [{
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
					datagrid_prores.datagrid("reload");
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
			}
		});
};

/**
 * 删除
 */
function delProRes(){
	var rows = datagrid_prores.datagrid("getSelections");
	if (rows.length < 1) {
		easyui_warn("请至少选择一条数据！");
		return;
	}
	var solutionids = "";
	for(var i=0;i<rows.length;i++){
		if(i==0){
			solutionids = rows[i].solutionid;
		}else{
			solutionids += ","+rows[i].solutionid;
		}
	}
	parent.$.messager.confirm("确认撤回", "确定删除选择的数据？", function(r) {
		if (r) {
			$.post(urls.delProRes, {
				solutionids :solutionids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_prores.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 项目审核
 */
function sendWF(){
	var selectRow = datagrid_prores.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条记录！",null);
		return;
	}else if(selectRow.length==1){
		
		showModalDialogAudit(600, "prores_form", types.audit_single, datagrid_prores, "采购结果审核",urls.optProResView+"?optFlag="+types.audit_single);
		
	}else{
		showModalDialogAudit(580, "audit_multiple", types.audit_multiple, datagrid_prores, "采购结果审核",urls.optProResView+"?optFlag="+types.audit_multiple);
	}
	
}
function backWorkFlow(){
	var selectRow = datagrid_prores.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条记录！",null);
		return;
	}
	showModalDialogAudit(580, "audit_multiple", types.back_multiple, datagrid_prores, "采购结果退回",urls.optProResView+"?optFlag="+types.back_multiple);
	/*var row = datagrid_prores.datagrid("getSelections")[0];
	parent.$.messager.confirm("退回确认", "确认要退回所选数据？", function(r) {
		if (r) {
			$.post(urls.backWorkFlow, {
				menuid : menuid,
				activityId :activityId,
				firstNode : firstNode,
				lastNode : lastNode,
				solutionid : row.solutionid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_prores.datagrid('reload');
				});
			}, "json");
		}
	});*/
}

/**
 * 详情
 */
function detProRes(){
	var rows = datagrid_prores.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}
	showModalDialogAudit(600, "prores_form", types.audit_view, datagrid_prores, "采购结果详情",urls.optProResView+"?optFlag="+types.audit_view);
}

/**
 * 流程信息
 */
function workflowMessage(){
	var selectRow = datagrid_prores.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
function resultExpert(mdDialog,queryParams){
	var grid = mdDialog.find("#resultExpert").datagrid({
		height: 200,
		title: '采购专家列表',
		collapsible: false,
		url : urls.resultExpertGrid,
		queryParams : queryParams,
		singleSelect: true,
		rownumbers : true,
		idField: 'purexpertid',
		columns: [[//显示的列
		           {field: 'purexpertid', title: '序号', width: 100,  checkbox: true },
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center',
		        	   editor: { 
		        		   type: 'combogrid',
		        		   options: { 
		        			   	panelWidth:600,    
		        			    idField:'name',
		        			    pagination : true,
		        			    rownumbers : true,
		        			    textField:'name',  
		        			    mode:'remote',
		        			    url:urls.qryExpertByQ,    
		        			    columns:[[    
		        			        {field:'expertid',title:'专家编码',width:60},    
		        			        {field:'name',title:'专家名称',width:100},    
		        			        {field:'sexName',title:'性别',width:120},
		        			        {field:'politicsStatusName',title:'政治面貌',width:120},
		        			        {field:'phoneNumber',title:'联系方式',width:120},
		        			        {field:'isEmergencyName',title:'应急专家',width:80},
		        			        {field:'expertTypeName',title:'专家类型',width:120},
		        			        {field:'highestDegreeName',title:'最高学历',width:80},
		        			        {field:'highestOfferingName',title:'最高学位',width:80},
		        			        {field:'majorTypeName',title:'从事专业类别',width:120},
		        			        {field:'industryName',title:'所属行业',width:120},
		        			        {field:'bidMajorName',title:'评标专业方向',width:250},
		        			        {field:'qualificationName',title:'执业资格证书',width:350},
		        			        {field:'research',title:'个人研究以及专业成就',width:350},
		        			        
		        			    ]],
		        			    onClickRow: function (rowIndex, rowData) {
		        			       var  typeObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertType'});
		        			       var  phoneObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertPhone'});
		        			       var  majorObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'bidmajor'});
		        			       var  expertIdObj = grid.datagrid('getEditor', {index:currentRow_expert,field:'expertid'});
		        			       mdDialog.find(typeObj.target).val(rowData.expertTypeName);
		        			       mdDialog.find(phoneObj.target).val(rowData.phoneNumber);
		        			       mdDialog.find(majorObj.target).val(rowData.bidMajorName);
		        			       mdDialog.find(expertIdObj.target).val(rowData.expertid);
		        			    }
		           	
		        		   } 
		        	   }
		           },{ field: 'expertType', title: '专家类型', width: 100,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,50]}} }
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,30]}} }
		           },{ field: 'bidmajor', title: '评标专业', width: 300,halign : 'center',
		        	   editor: { type: 'validatebox', options: { editable:false,validType:{length:[0,200]}} }
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center',
		        	   editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入负责区域',validType:{length:[0,50]}} }
		           },{ field: 'expertid', title: 'expertId', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'advanceid', title: 'advanceid', width: 100,halign : 'center',hidden:true,
		        	   editor: { type: 'validatebox', options: { editable:false} }
		           },{ field: 'remark', title: '说明', width: 300,halign : 'center',
		        	   editor: { type: 'validatebox', options: { validType:{length:[0,100]}} }
		           }]]
	});
	return grid;
}
