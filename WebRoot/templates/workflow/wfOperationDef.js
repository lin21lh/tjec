var opurls = {
	query : 'findWorkflowOpdef.do',
	detail : 'getWorkflowOpdefDetail.do',
	save : '',
	add : '',
	del : ''
};
function initOperGrid() {
	$('#oper_grid').datagrid({
		columns : [ [ {
			field : 'ck',
			checkbox:true
		},{
			field : 'name',
			title : '操作名称',
			width : 100
		},{
			field : 'classname',
			title : '类名',
			width : 400
		},{
			field : 'methodname',
			title : '方法名',
			width : 150
		} ] ],
		width : '100%',
		height : '100%',
		singleSelect : true,
		onClickRow : function(rowIndex, rowData) {

		},
		onLoadSuccess : function() {

		},
		toolbar : '#op_toolbar',
		fit:true,
		border:false,
		rownumbers:true
	});
}

function oper_query_func() {
	var sel = $('#wftree').tree('getSelected');
	if (sel) {
		$('#oper_grid').datagrid({
			url : opurls.query + '?wfDefId=' + sel.id
		});
	}
}

function showOpDef(key){
	$('#oper_grid').datagrid({
		url : opurls.query + '?key=' + key
	});
}

function oper_clear_func(){
	
}


function getOperSel(){
	var sel=$('#oper_grid').datagrid('getSelected');
	if(sel){
		return sel;
	}else{
		easyui_warn('请选择一行数据!');
		return null;
	}
}

function oper_del_func(){
	var treesel=getTreeSelect();
	var sel=getOperSel();
	if(sel){
		$.messager.confirm('确认','是否删除？',function(r){
			if(r){
				$.post('delWorkflowOpdef.do',{
					id:sel.id
				},function(r){
					easyui_auto_notice(r, function(){
						showOpDef(treesel.key);
					});
				},'JSON');
			}
		});
		
	}
}
function oper_view_func(){

	var sel=getOperSel();
	if(sel){
		parent.$.modalDialog({
			title : '详情',
			width : 600,
			height : 220,
			iconCls:'icon-view',
			href : 'workflow/WfProcessDefinitionController/wfOperationForm.do',
			onLoad: function(){
					var f = parent.$.modalDialog.handler.find('#operform');
					f.form('load','workflow/WfProcessDefinitionController/getWorkflowOpdefDetail.do?id='+sel.id);	
			},
			buttons: [	
					    {
					    	text:"关闭",
					    	iconCls: "icon-cancel",
					    	handler: function(){
					    		parent.$.modalDialog.handler.dialog('close');
					    	}
					    }
					]
		});
	}
}

function oper_edit_func(){
	
	var treesel=getTreeSelect();
	var sel=getOperSel();
	if(sel){
		parent.$.modalDialog({
			title : '修改',
			width : 600,
			height : 220,
			iconCls:'icon-edit',
			href : 'workflow/WfProcessDefinitionController/wfOperationForm.do',
			onLoad: function(){
					var f = parent.$.modalDialog.handler.find('#operform');
					f.form('load','workflow/WfProcessDefinitionController/getWorkflowOpdefDetail.do?id='+sel.id);	
			},
			buttons: [	
			          {
					    	text:"保存",
					    	iconCls: "icon-save",
					    	handler: function(){
					    		var f = parent.$.modalDialog.handler.find('#operform');
					    		f.form('submit', {
					    			url : 'workflow/WfProcessDefinitionController/saveWorkflowOpdef.do',
					    			onSubmit : function() {
					    				return true; 
					    			},
					    			success : function(data) {
					    				data=eval("("+data+")");
					    				easyui_auto_notice(data, function(){
					    					parent.$.modalDialog.handler.dialog('close');
					    					showOpDef(treesel.key);
					    				});
					    			}
					    		});
					    	}
					    },
					    {
					    	text:"关闭",
					    	iconCls: "icon-cancel",
					    	handler: function(){
					    		parent.$.modalDialog.handler.dialog('close');
					    	}
					    }
					]
		});
	}
}

function oper_add_func(){
	var treesel=getTreeSelect();

		parent.$.modalDialog({
			title : '添加',
			width : 600,
			height : 220,
			iconCls:'icon-add',
			href : 'workflow/WfProcessDefinitionController/wfOperationForm.do',
			onLoad: function(){
					var f = parent.$.modalDialog.handler.find('#operform');
					f.form('clear');	
					parent.$.modalDialog.handler.find('#opkey').val(treesel.key);
			},
			buttons: [	
			          {
					    	text:"保存",
					    	iconCls: "icon-save",
					    	handler: function(){
					    		var f = parent.$.modalDialog.handler.find('#operform');
					    		f.form('submit', {
					    			url : 'workflow/WfProcessDefinitionController/saveWorkflowOpdef.do',
					    			onSubmit : function() {
					    				return true; 
					    			},
					    			success : function(data) {
					    				data=eval("("+data+")");
					    				easyui_auto_notice(data, function(){
					    					parent.$.modalDialog.handler.dialog('close');
					    					showOpDef(treesel.key);
					    				});
					    			}
					    		});
					    		
					    	}
					    },
					    {
					    	text:"关闭",
					    	iconCls: "icon-cancel",
					    	handler: function(){
					    		parent.$.modalDialog.handler.dialog('close');
					    	}
					    }
					]
		});
}