
var baseUrl = contextpath + "ppms/procurement/ProjectAdvanceController/";
var sfArray = [{ "value": "1", "text": "是" }, { "value": "0", "text": "否" }];
var urls = {
	queryUrl : baseUrl + "queryProject.do",
	organQueryUrl : baseUrl + "queryOrgan.do",
	advanceaddUrl : baseUrl + "advanceAdd.do",
	addCommitUrl : baseUrl + "advanceAddCommit.do",
	editCommitUrl : baseUrl + "advanceAddCommit.do",
	projectDelete : baseUrl + "projectDelete.do",
	sendWFUrl : baseUrl + "sendWorkFlow.do",
	revokeWFUrl : baseUrl + "revokeWorkFlow.do",
	advanceExpertGrid : baseUrl + "advanceExpertGrid.do",
	qryExpertByQ : baseUrl + "qryExpertByQ.do",
	advanceDetailUrl : baseUrl + "advanceDetail.do"
};
var panel_ctl_handles = [ {
	panelname : '#qpanel1', // 要折叠的面板id
	gridname : "#changeAccountDataGrid", // 刷新操作函数
	buttonid : '#openclose' // 折叠按钮id
} ];
//默认加载
$(function() {
	$('#backBtn').linkbutton('disable');
	//工作流状态初始化
	$("#status").combobox({
		valueField : "value",
		textField : "text",
		value : "1",
		editable : false,
		data : [{text : "待处理", value : "1"}, {text : "已处理", value : "2"}],
		onSelect : function(record) {
			topQuery();
			switch (record.value) {
			case '1':
				$('#addBtn').linkbutton('enable');
				$('#editBtn').linkbutton('enable');
				$('#sendBtn').linkbutton('enable');
				$('#backBtn').linkbutton('disable');
				break;
			case '2':
				$('#addBtn').linkbutton('disable');
				$('#editBtn').linkbutton('disable');
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
	loadProjectGrid(urls.queryUrl);
});
var advanceDataGrid;
//页面刷新
function showReload() {
	advanceDataGrid.datagrid('reload'); 
}
//加载可项目grid列表
function loadProjectGrid(url) {
	advanceDataGrid = $("#projectDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : true,
		rownumbers : true,
		pagination : true,
		remoteSort:false,
		multiSort:true,
		pageSize : 30,
		queryParams: {
			menuid : menuid,
			status : 1,
			activityId : activityId,
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
		              ,{field : "statusName",title : "项目状态",halign : 'center',	width:80,sortable:true}
		              ,{field : "amount",title : "项目总投资（万元）",halign : 'right',align:'right',width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
		              ,{field : "noticeTime",title : "公告发布时间",halign : 'center',	width:100,sortable:true	}
		              ,{field : "publishMedia",title : "公告发布媒体",halign : 'center',	width:120,sortable:true	}
		              ,{field : "inquiryResultName",title : "预审结果",halign : 'center',	width:80,sortable:true	}
		              ,{field : "inquiryTime",title : "资格预审时间",halign : 'center',	width:80,sortable:true	}
		              ,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:100,sortable:true	}
		              ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:150	,sortable:true}
		              ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:150,sortable:true	}
		              ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:150,sortable:true}
		              ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:150,sortable:true}
		              ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:150,sortable:true	}
		              ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:150,sortable:true	}
		              ,{field : "proSituation",title : "项目概况",halign : 'center',	width:100,sortable:true }
		              ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
		              ,{field : "proPhone",title : "联系人电话",halign : 'center',	width:150,sortable:true}
		              ,{field : "proScheme",title : "初步实施方案内容",halign : 'center',	width:150,sortable:true	}
		              ,{field : "proArticle",title : "项目产出物说明",halign : 'center',	width:100,sortable:true	}
		              ,{field : "createusername",title : "创建人",halign : 'center',	width:150,sortable:true	}
		              ,{field : "createtime",title : "创建时间",halign : 'center',	width:150,sortable:true	}
		              ,{field : "updateusername",title : "修改人",halign : 'center',	width:150,sortable:true	}
		              ,{field : "updatetime",title : "修改时间",halign : 'center',	width:150,sortable:true	}
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
		              ,{field : "advanceid",title : "advanceid",halign : 'center',	width:150,sortable:true,hidden:true	}
		             ] ]
	});
}
/**
 * 新增按钮
 */
function advanceAdd(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	var advanceid = row.advanceid;
	if(advanceid != null){//如果advanceid不是空，则表示修改
		advanceEdit();
		return;
	}
	parent.$.modalDialog({
		title : "预审结果录入",
		iconCls : 'icon-add',
		width : 900,
		height : 650,
		href : urls.advanceaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			comboboxFuncByCondFilter(menuid,"inquiryResult", "JUDGERESULT", "code", "name", mdDialog);//
			advanceGrid(mdDialog, '','');
			qualExpertGrid(mdDialog,'');
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#advanceAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceorgan").val(rowstr);
				
				//保存专家
				var grid = mdDialog.find("#advanceExpert");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceExpertData").val(rowstr);
				
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(row.projectid);
				submitForm(urls.addCommitUrl,"advanceAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#advanceAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');//获取所有的grid数据
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceorgan").val(rowstr);
				
				//保存专家
				var grid = mdDialog.find("#advanceExpert");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceExpertData").val(rowstr);
				
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(row.projectid);
				submitForm(urls.addCommitUrl,"advanceAddForm","1");
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
//新增提交表单
function submitForm(url,form,workflowflag){
	var form = parent.$.modalDialog.handler.find('#' + form);
	parent.$.modalDialog.handler.find("#workflowflag").val(workflowflag);
	parent.$.modalDialog.handler.find("#activityId").val(activityId);
	var isValid = form.form('validate');
	if (!isValid) {
		return;
	}
	if(workflowflag=="1"){//提交
		parent.$.messager.confirm("送审确认", "确认要送审？", function(r) {
			if (r) {
				form.form("submit",
						{
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
							parent.$.modalDialog.handler.dialog('close');
							easyui_info(result.title);
							advanceDataGrid.datagrid('reload');
						} else {
							parent.$.messager.alert('错误', result.title, 'error');
						}
					}
						});
			}
		});
	}else{
		form.form("submit",
				{
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
					parent.$.modalDialog.handler.dialog('close');
					easyui_info(result.title);
					advanceDataGrid.datagrid('reload');
					
				} else {
					parent.$.messager.alert('错误', result.title, 'error');
				}
				}
			});
	}
}
//预审单位
function advanceGrid(mdDialog,projectid,advanceid){
	var grid = mdDialog.find("#advanceAddGrid").datagrid({
		title : "资格预审机构",
        height: 210,
        width: 885,
        collapsible: false,
        url : urls.organQueryUrl,
        queryParams : {projectid:projectid,advanceid:advanceid},
        rownumbers : true,
        singleSelect: true,
        idField: 'organid',
        columns: [[//显示的列
                   {field: 'organid', title: '序号', width: 100, sortable: true, checkbox: true },
                   { field: 'organ_code', title: '机构代码', width: 150, sortable: true,halign : 'center',
                       editor: { type: 'validatebox', options: {required: true,missingMessage:'请输入机构代码',validType:{length:[0,50]}} }
                   },{ field: 'organ_name', title: '机构名称', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: {required: true,missingMessage:'请输入机构名称',validType:{length:[0,50]}} }
                   },{ field: 'iscombo', title: '是否联合体', width: 100,formatter: sfformatter,halign : 'center',
                         editor: { 
                        	 type : 'combobox', 
                        	 options : {  
                        		 required: true,
                        		 data: sfArray,
                        		 editable :false,
                        		 valueField: "value", 
                        		 textField: "text"}
                        	 }
                   },{ field: 'ispass', title: '是否通过预审', width: 100,formatter: sfformatter,halign : 'center',
                	   editor: { 
	                  	 type : 'combobox', 
	                	 options : {  
	                		 required: true,
	                		 data: sfArray,
	                		 editable :false,
	                		 valueField: "value", 
	                		 textField: "text"}
	                	 }
                   },{ field: 'organ_content', title: '机构说明', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,missingMessage:'请输入机构说明',validType:{length:[0,500]}} }
                   },{ field: 'score', title: '评分', width: 90,halign : 'center',
                         editor: { type: 'numberbox', options: { required: true,missingMessage:'请输入评分',validType:{length:[0,20]}} }
                   },{ field: 'remark', title: '备注', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: {validType:{length:[0,500]}} }
                   }]],
        toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
            	var data = mdDialog.find("#advanceAddGrid").datagrid("getData");//grid列表
            	var total = data.total;//grid的总条数
            	mdDialog.find("#advanceAddGrid").datagrid('appendRow', {row: {}});//追加一行
            	grid.datagrid("beginEdit", total);//开启编辑
            }
        },'-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var row = mdDialog.find("#advanceAddGrid").datagrid('getChecked');
                if(row ==''){
                	easyui_warn("请选择要删除的数据！");
                	return;
                }else{
                	parent.$.messager.confirm("提示", "确认将选中删除?", function (r) {
                         if (r) {
                        	 var index = mdDialog.find("#advanceAddGrid").datagrid('getRowIndex',row[0]);
                        	 //选择要删除的行
                        	 mdDialog.find("#advanceAddGrid").datagrid('deleteRow',index);
                         }
                     });
                }
             }
        }],
        onDblClickRow:function (rowIndex, rowData) {
        	mdDialog.find("#advanceAddGrid").datagrid('beginEdit', rowIndex);
        },
        onClickRow:function(rowIndex,rowData){
        	mdDialog.find("#advanceAddGrid").datagrid('beginEdit', rowIndex);
        }
    });
	return grid;
}
function advanceGridDetail(mdDialog,projectid,advanceid){
	var grid = mdDialog.find("#advanceAddGrid").datagrid({
		title : "资格预审机构",
        height: 180,
        width: 885,
        collapsible: false,
        url : urls.organQueryUrl,
        queryParams : {projectid:projectid,advanceid:advanceid},
        rownumbers : true,
        singleSelect: true,
        idField: 'organid',
        columns: [[//显示的列
                   { field: 'organ_code', title: '机构代码', width: 150, sortable: true,halign : 'center',
                       editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'organ_name', title: '机构名称', width: 200,halign : 'center',
                        editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'iscombo', title: '是否联合体', width: 100,formatter: sfformatter,halign : 'center',
                         editor: { 
                        	 type : 'combobox', 
                        	 options : {  
                        		 required: true,
                        		 data: sfArray,
                        		 editable :false,
                        		 valueField: "value", 
                        		 textField: "text"}
                        	 }
                   },{ field: 'ispass', title: '是否通过预审', width: 100,formatter: sfformatter,halign : 'center',
                	   editor: { 
	                  	 type : 'combobox', 
	                	 options : {  
	                		 required: true,
	                		 data: sfArray,
	                		 editable :false,
	                		 valueField: "value", 
	                		 textField: "text"}
	                	 }
                   },{ field: 'organ_content', title: '机构说明', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   },{ field: 'score', title: '评分', width: 90,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true,validType:{length:[0,20]}} }
                   },{ field: 'remark', title: '备注', width: 200,halign : 'center',
                         editor: { type: 'validatebox', options: { required: true} }
                   }]]
    });
	return grid;
}
//专家列表
var currentRow_expert = -1;
function qualExpertGrid(mdDialog,advanceid){
	var grid = mdDialog.find("#advanceExpert").datagrid({
		height: 180,
		title: '预审专家列表',
		collapsible: false,
		url : urls.advanceExpertGrid,
		queryParams : {advanceid:advanceid},
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
		        		   var data = mdDialog.find("#advanceExpert").datagrid("getData");//grid列表
		            	   var total = data.total;//grid的总条数
		            	   if(total!=0){
		            		   if(mdDialog.find('#advanceExpert').datagrid('validateRow', currentRow_expert)){
		            			   	mdDialog.find('#advanceExpert').datagrid('endEdit', currentRow_expert);
		            				mdDialog.find("#advanceExpert").datagrid('appendRow', {row: {}});//追加一行
		                           	mdDialog.find("#advanceExpert").datagrid("beginEdit", total);//开启编辑
		                           	currentRow_expert = mdDialog.find('#advanceExpert').datagrid('getRows').length-1;
		            		   }else{
		            			   easyui_warn("当前存在正在编辑的行！");
		            		   }
		            	   }else{
		                       	mdDialog.find("#advanceExpert").datagrid('appendRow', {row: {}});//追加一行
		                       	mdDialog.find("#advanceExpert").datagrid("beginEdit", total);//开启编辑
		                       	currentRow_expert = mdDialog.find('#advanceExpert').datagrid('getRows').length-1;
		            	   }
		            	   
		        	   }
		           },'-', {id:"expert_del",
		        	   text: '删除', iconCls: 'icon-remove', handler: function () {
		        		   var row = mdDialog.find("#advanceExpert").datagrid('getSelections');
		                   if(row ==''){
		                   		easyui_warn("请选择要删除的数据！");
		                   		return;
		                   }else{
		                	   var  rowIndex= mdDialog.find("#advanceExpert").datagrid('getRowIndex',row[0]);
		                	   
		    	               	parent.$.messager.confirm("删除确认", "确认删除选中的数据？", function (r) {
		    	                        if (r) {
		    	                        	if(currentRow_expert==rowIndex){
		    	                        		currentRow_expert = -1;
		    	                        	}else if(rowIndex<currentRow_expert){
		    	                        		currentRow_expert = currentRow_expert-1;
		    	                        	}
		    	                        	
		    	                       		mdDialog.find("#advanceExpert").datagrid('deleteRow',rowIndex);
		    	                        }
		    	                  });
		                   }
		        	   }
		           }],
		           onDblClickRow:function (rowIndex, rowData) {
		        	 //结束上一行编辑，开启新行的编辑
		        	   if(currentRow_expert != rowIndex && mdDialog.find('#advanceExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#advanceExpert').datagrid('endEdit', currentRow_expert);
		        		   //开启新行编辑
		        			mdDialog.find("#advanceExpert").datagrid('beginEdit', rowIndex);
		        			currentRow_expert = rowIndex;
		        	   }else{
		        		   easyui_warn("当前存在正在编辑的行！");
		        	   }
		           },onClickRow:function(rowIndex, rowData){//单击事件主要用来关闭当前编辑状态
		        	   
		        	   if( mdDialog.find('#advanceExpert').datagrid('validateRow', currentRow_expert)){
		        		   //结束上一行编辑
		        		   mdDialog.find('#advanceExpert').datagrid('endEdit', currentRow_expert);
		        		   currentRow_expert = -1;
		        	   }
		           }
	});
	return grid;
}

function qualExpertGridDetail(mdDialog,advanceid){
	var grid = mdDialog.find("#advanceExpert").datagrid({
		height: 180,
		title: '预审专家列表',
		collapsible: false,
		url : urls.advanceExpertGrid,
		queryParams : {advanceid:advanceid},
		singleSelect: true,
		rownumbers : true,
		idField: 'purexpertid',
		columns: [[//显示的列
		           {field: 'purexpertid', title: '序号', width: 100,  checkbox: true },
		           { field: 'expertName', title: '专家名称', width: 130,halign : 'center'
		           },{ field: 'expertType', title: '专家类型', width: 100,halign : 'center'
		           },{ field: 'expertPhone', title: '联系方式', width: 140,halign : 'center'
		           },{ field: 'bidmajor', title: '评标专业', width: 300,halign : 'center'
		           },{ field: 'responsibleArea', title: '负责领域', width: 150,halign : 'center'
		           },{ field: 'expertid', title: 'expertId', width: 100,halign : 'center',hidden:true
		           },{ field: 'advanceid', title: 'advanceid', width: 100,halign : 'center',hidden:true
		           },{ field: 'remark', title: '说明', width: 300,halign : 'center'
		           }]]
	});
	return grid;
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
			activityId : activityId,
			firstNode : true,
			lastNode : false
		};
	advanceDataGrid.datagrid("load", param);
}
/**
 * 修改
 */
function advanceEdit(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "预审结果修改",
		iconCls : 'icon-add',
		width : 900,
		height : 650,
		href : urls.advanceaddUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			advanceGrid(mdDialog, row.projectid,row.advanceid);
			qualExpertGrid(mdDialog,row.advanceid);
			comboboxFuncByCondFilter(menuid,"inquiryResult", "JUDGERESULT", "code", "name", mdDialog);//
			var f = parent.$.modalDialog.handler.find('#advanceAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#ysjg"),true, row.inquiryDeclarePath, "24","inquiryDeclarePath");
		},
		buttons : [ {
			text : "保存",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#advanceAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceorgan").val(rowstr);
				
				//保存专家
				var grid = mdDialog.find("#advanceExpert");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceExpertData").val(rowstr);
				
				
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(row.projectid);
				parent.$.modalDialog.handler.find("#advanceid").val(row.advanceid);
				submitForm(urls.editCommitUrl,"advanceAddForm","");
			}
		}, {
			text : "送审",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var grid = mdDialog.find("#advanceAddGrid");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');//获取所有的grid数据
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceorgan").val(rowstr);
				
				
				//保存专家
				var grid = mdDialog.find("#advanceExpert");
				var data = grid.datagrid("getData");
				var total = data.total;
				for(var i=0;i<total;i++){
					grid.datagrid('endEdit', i);//把所有的编辑行锁定
				}
				var rows = grid.datagrid('getRows');
				var rowstr = JSON.stringify(rows);
				parent.$.modalDialog.handler.find("#advanceExpertData").val(rowstr);
				
				parent.$.modalDialog.handler.find("#menuid").val(menuid);
				parent.$.modalDialog.handler.find("#projectid").val(row.projectid);
				parent.$.modalDialog.handler.find("#advanceid").val(row.advanceid);
				submitForm(urls.editCommitUrl,"advanceAddForm","1");
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
/**
 * 项目送审
 */
function sendWF(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	var advanceid = row.advanceid;
	if(advanceid ==null){
		easyui_warn("该数据还未录入，请先录入数据！",null);
		return;
	}
	parent.$.messager.confirm("送审确认", "确认要将选中项目送审？", function(r) {
		if (r) {
			$.post(urls.sendWFUrl, {
				menuid : menuid,
				activityId :activityId,
				advanceid : row.advanceid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					advanceDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 项目已送审撤回
 */
function revokeWF(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	parent.$.messager.confirm("撤回确认", "确认要将选中项目撤回？", function(r) {
		if (r) {
			$.post(urls.revokeWFUrl, {
				menuid : menuid,
				activityId :activityId,
				advanceid : row.advanceid,
				wfid :row.wfid
			}, function(result) {
				easyui_auto_notice(result, function() {
					advanceDataGrid.datagrid('reload');
				});
			}, "json");
		}
	});
}
/**
 * 工作流信息
 */
function workflowMessage(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	showWorkFlowModalDialog(selectRow[0].wfid);
}
function sfformatter(value, rowData, rowIndex) {
    for (var i = 0; i < sfArray.length; i++) {
        if (sfArray[i].value == value) {
            return sfArray[i].text;
        }
    }
}
/**
 * 详情
 */
function advanceView(){
	var selectRow = advanceDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	var row = advanceDataGrid.datagrid("getSelections")[0];
	parent.$.modalDialog({
		title : "预审结果详情",
		iconCls : 'icon-add',
		width : 900,
		height : 630,
		href : urls.advanceDetailUrl,
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			var sessionId = parent.$.modalDialog.handler.find('#sessionId').val();
			advanceGridDetail(mdDialog, row.projectid,row.advanceid);
			qualExpertGridDetail(mdDialog,row.advanceid);
			var f = parent.$.modalDialog.handler.find('#advanceAddForm');
			f.form("load", row);
			showFileDiv(mdDialog.find("#ysjg"),false, row.inquiryDeclarePath, "24","inquiryDeclarePath");
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
