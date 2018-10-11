//设置全局变量
var datagrid_prores;
/**采购结果
 * prores_init.js
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
	resultExpertGrid : baseUrl + "resultExpertGrid.do",
	qryExpertByQ : baseUrl + "qryExpertByQ.do",
	advanceOrganUrl : baseUrl +"advanceOrganUrl.do"
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
		, {field : "orgcode",title : "隐藏",halign : 'center',fixed : true,	width : 120,sortable:true,hidden:true,rowspan:2}
		
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
		data : [ {text : "待处理", value : "1"},{text : "已处理", value : "2"}],
		onSelect : function(record) {
			qryProRes();
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
				$('#btn_flow').linkbutton('enable');
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
function qryProRes(){
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
	$("#datagrid_prores").datagrid("load",param);
}

/**
 * 新增
 */
function addProRes(){
	var rows = datagrid_prores.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}else{
		if(rows[0].purchaseid!=null){
			easyui_warn("已录入的数据，不能再次录入！");
			return;
		}
	}
	showModalDialog(540, "prores_form", types.add, datagrid_prores, "采购结果新增",urls.optProResView+"?optFlag="+types.add, urls.saveProRes+"?optFlag="+types.add);
}

/**
 * 修改
 */
function editProRes(){
	var rows = datagrid_prores.datagrid("getSelections");
	if (rows.length != 1) {
		easyui_warn("请选择一条项目数据！");
		return;
	}else{
		if(rows[0].purchaseid==null){
			easyui_warn("该数据还未录入，请先录入数据！");
			return;
		}
	}
	showModalDialog(540, "prores_form", types.edit, datagrid_prores, "采购结果修改",urls.optProResView+"?optFlag="+types.edit, urls.saveProRes+"?optFlag="+types.edit);
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
	showModalDialog(540, "prores_form", types.view, datagrid_prores, "采购结果详情",urls.optProResView+"?optFlag="+types.view, urls.saveProRes+"?optFlag="+types.view);
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
			if("view"==operType){
				//加载附件
				showFileDiv(mdDialog.find("#prorespath"),false, row.contractPath, "24","contractPath");
			}else{
				//加载附件
				showFileDiv(mdDialog.find("#prorespath"),true, row.contractPath, "24","contractPath");
			}
			var advanceid = mdDialog.find("#advanceid").val();
			advanceOrganGrid(mdDialog,advanceid,'');
			if(operType=="add"){
				var queryParams = {advanceid:row.advanceid,optFlag:'add'}
				resultExpert(mdDialog,queryParams);
			}else{
				var queryParams = {purchaseid:row.purchaseid,optFlag:'edit'}
				resultExpert(mdDialog,queryParams);
			}
			resultExpert(mdDialog,advanceid,queryParams)
			
		},
		buttons : funcOperButtons(operType, url, dataGrid, form)
	});
};
var lastIndex =0;
function advanceOrganGrid(mdDialog,advanceid,projectid){
	var grid = mdDialog.find("#advance_organ").datagrid({
        height: 160,
        title : '资格预审机构'+'<font style="color:#EC5757;font-size:smaller">(请标注中选机构)</font>',
        url : urls.advanceOrganUrl,
        queryParams : {advanceid:advanceid,projectid:projectid},
        rownumbers : true,
        singleSelect: true,
        collapsible: false,
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
                   ]],
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
var currentRow_expert = -1;
function resultExpert(mdDialog,queryParams){
	var grid = mdDialog.find("#resultExpert").datagrid({
		height: 400,
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
		           }]],
		           toolbar: [{ id:"expert_add",
		        	   text: '添加', iconCls: 'icon-add', handler: function () {
		        		   var data = mdDialog.find("#resultExpert").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#resultExpert').datagrid('validateRow', currentRow_expert)){
		            			   	mdDialog.find('#resultExpert').datagrid('endEdit', currentRow_expert);
		            				mdDialog.find("#resultExpert").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#resultExpert").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_expert = mdDialog.find('#resultExpert').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#resultExpert").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#resultExpert").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_expert = mdDialog.find('#resultExpert').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#resultExpert").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#resultExpert").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_expert==rowIndex){
		    	                        		currentRow_expert = -1;
		    	                        	}else if(rowIndex<currentRow_expert){
		    	                        		currentRow_expert = currentRow_expert-1;
		    	                        	}
		    	                       		mdDialog.find("#resultExpert").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_expert != rowIndex && mdDialog.find('#resultExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#resultExpert').datagrid('endEdit', currentRow_expert);
		        		   //开启新行编辑
		        			mdDialog.find("#resultExpert").datagrid('beginEdit', rowIndex);
		        			currentRow_expert = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#resultExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#resultExpert').datagrid('endEdit', currentRow_expert);
		        		   currentRow_expert = -1;
		        		   isEditFlag = false;
		        	   }
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
				//var o=parent.$.modalDialog.handler.find("#advance_organ").datagrid('getData');
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				
				//保存专家信息
				var o=parent.$.modalDialog.handler.find("#resultExpert");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultExpertData").val(subStr);
				
				submitForm(url, form,operType);
			}
		},{
			text : "保存并送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//var o=parent.$.modalDialog.handler.find("#advance_organ").datagrid('getData');
				
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				
				//保存专家信息
				var o=parent.$.modalDialog.handler.find("#resultExpert");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultExpertData").val(subStr);
				
				submitForm(url+"&sendFlag=1&menuid="+menuid+"&activityId="+activityId, form,operType);
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
				//var o=parent.$.modalDialog.handler.find("#advance_organ").datagrid('getData');
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				
				//保存专家信息
				var o=parent.$.modalDialog.handler.find("#resultExpert");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultExpertData").val(subStr);
				
				submitForm(url, form,operType);
			}
		},{
			text : "保存并送审",
			iconCls : "icon-save",
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				//var o=parent.$.modalDialog.handler.find("#advance_organ").datagrid('getData');
				var o=parent.$.modalDialog.handler.find("#advance_organ");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#subStr").val(subStr);
				
				//保存专家信息
				var o=parent.$.modalDialog.handler.find("#resultExpert");
				var data = o.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					o.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var subStr = JSON.stringify(data.rows);
				parent.$.modalDialog.handler.find("#resultExpertData").val(subStr);
				submitForm(url+"&sendFlag=1&menuid="+menuid+"&activityId="+activityId, form,operType);
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
	var purchaseids = "";
	for(var i=0;i<rows.length;i++){
		if(rows[i].purchaseid==null){
			easyui_warn("数据还未录入，请先录入数据！");
			return;
		}
		if(i==0){
			purchaseids = rows[i].purchaseid;
		}else{
			purchaseids += ","+rows[i].purchaseid;
		}
	}
	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function(r) {
		if (r) {
			$.post(urls.delProRes, {
				purchaseids :purchaseids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_prores.datagrid("reload");
				});
			}, "json");
		}
	});
}

/**
 * 项目送审
 */
function sendWF(){
	var selectRow = datagrid_prores.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var purchaseids = "";
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(selectRow[i].purchaseid==null){
			easyui_warn("该数据还未录入，请先录入数据！");
			return;
		}
		if(i==0){
			purchaseids = selectRow[i].purchaseid;
			wfids = selectRow[i].wfid;
		}else{
			purchaseids += ","+selectRow[i].purchaseid;
			wfids += ","+selectRow[i].wfid;
		}
	}
	parent.$.messager.confirm("送审确认", "确认送审选中的数据？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				purchaseid : purchaseids,
				firstNode : firstNode,
				lastNode : lastNode,
				wfid :wfids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_prores.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 项目已送审撤回
 */
function revokeWF(){
	var selectRow = datagrid_prores.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0){
		easyui_warn("请至少选择一条数据！",null);
		return;
	}
	var purchaseids = "";
	var wfids = "";
	for(var i=0;i<selectRow.length;i++){
		if(i==0){
			purchaseids = selectRow[i].purchaseid;
			wfids = selectRow[i].wfid;
		}else{
			purchaseids += ","+selectRow[i].purchaseid;
			wfids += ","+selectRow[i].wfid;
		}
	}
	parent.$.messager.confirm("撤回确认", "确认撤回选中的数据？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				purchaseid : purchaseids,
				wfid :wfids
			}, function(result) {
				easyui_auto_notice(result, function() {
					datagrid_prores.datagrid('reload');
				});
			}, "json");
		}
	});
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