var urls = {
	wftree : 'queryWorkflowConfigTree.do',
	detail : 'getWorkflowProcdefDetail.do',
	save : 'saveWorkflowProcdef.do',
	del : 'delWorkflowProcdef.do',
	deploy:'deployWorkflowDef.do',
	getDeployedJpdlContent:'getDeployedJpdlContent.do',
	queryWfPrivilege:'queryWfPrivilege.do',
	queryBackAttr:'queryBackAttr.do',
	queryTaskPageUrl:'queryTaskPageUrl.do',
	editWorkflowProcVersion:'editWorkflowProcVersion.do',
	getWorkflowProcversion:'getWorkflowProcversion.do'
};

var flag_edit = false;
var addFlag = 0; // 0代表页面加载， 1代表添加新节点

var NULL_NODE_ID = 999999; // 代表新增未保存的菜单
var last_node_handle = null;
var onchange_enable = false; // 禁用onchange事件

var nodeArray=new Array();

$(function() {
	$('#wftree').tree(
			{
				url : urls.wftree,
				method : 'post',
				animate : true,
				onContextMenu : function(e, node) {
					e.preventDefault();
					if (flag_edit) {
						$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!',
								'warnning');
						return;
					}

					$(this).tree('select', node.target);
					if(node.type=='category'){
						$('#contextmenu_add').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					}else if(node.type=='workflow'){
						/*
						 * $('#contextmenu_addversion').menu('show', { left :
						 * e.pageX, top : e.pageY });
						 */
					}
				},
				onLoadSuccess : function(node, data) {
					switch (addFlag) {
					case 0: // 页面刷新
						var firstNode = getFirstLeafNode();
						if (firstNode) {
							$('#wftree').tree('select', firstNode.target);
							showDetail(firstNode);
						}else{
							var roots = $('#wftree').tree('getRoots');
							if(roots.length>0){
								$('#wftree').tree('select', roots[0].target);
								showDetail(roots[0]);
							}
						}
						break;
						
					case 1:// 增加新节点

						break;
					}
					
				},
				onClick : function(node) {
					
					// 如果没有正在编辑
					if (last_node_handle == null || (!flag_edit)) {
						showDetail(node);
						last_node_handle = node;
					} else {
						// 正在编辑
						$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!',
								'warnning');
						$('#wftree').tree('select', last_node_handle.target);
					}
				}

			});
	$('#content').form({
		onLoadSuccess:function(){
			$('#linkbutton_save').linkbutton("disable");
			$('#linkbutton_cancel').linkbutton("disable");
			enable_onchange();
		}
	});
	initOperGrid();
	
	$('#wftab').tabs({
		onSelect:function(title,index){
			if(title=='工作流操作定义'){
				showOpDef(getTreeSelect().key);
				enable_onchange();
			}
		}
	});
	
	
	$('#tabname').gridDialog({
		title :'数据表选择',
		dialogWidth : 640,
		dialogHeight : 400,
		singleSelect: true,
		dblClickRow : true,
		hiddenName:'tabid',
		valueField:'tableid',
		textField:'tablename',
		prompt:'请选择数据表',
		filter: {
					cncode: "表名",
					code: "tablecode",
					cnname: "中文表名",
					name : "tablename"
				},
		url : 'base/tabsdef/SysDicTableController/searchTables.do',
		loadMsg: "正在加载表数据，请稍候......",
		cols : [[
			{field : "tableid", checkbox : true}, 
			{field : "tablecode",title : "表名",width : 180},
	    	{field : "tablename", title : "中文表名", width : 180},
	    	{field : "cntabletype", title : "表类型", width : 200}
	    ]],
	    assignFunc: function(){
	    	enable_onchange();
	    	form_onchange();
	    },
	    clearFunc: function(){
// enable_onchange();
	    	form_onchange();
	    }
	});
	
// $('#tabname').searchbox({
// searcher:function(value,name){
// // alert(value + "," + name)
//			
// openTableSelectDialog(value,name,$('#tabname'),$('#tabid'));
// $('#linkbutton_save').linkbutton("enable");
// $('#linkbutton_cancel').linkbutton("enable");
// },
// prompt:'请选择数据表'
// });
	
// $('#defaultui').combobox({
// valueField: 'value',
// textField: 'label',
// data: [{
// label: '是',
// value: true
// },{
// label: '否',
// value: false
// }]
// });
	$('#panel2').panel({
		onResize:panel2_resize
	});
});

function showDetail(node) {

	switch(node.type){
	
	case 'workflow':
		
		disable_onchange(); // 禁用onchange
		$('#panel2').panel("close");
		$('#panel1').panel("close");
		$('#panel3').panel("open");
		$('#key').textbox("setValue", node.key);
		var par=$('#wftree').tree('getParent',node.target);
		showProcDef(node.id);
		showOpDef(node.key);
// enable_onchange();
		break;
	case 'category':
		// 分类
		$('#panel2').panel("close");
		$('#panel3').panel("close");
		$('#panel1').panel("open");
		break;
	}
}

// 取得第一个节点，
function getFirstLeafNode() {
	var roots = $('#wftree').tree('getRoots');

	for(var i in roots){
		var children=$('#wftree').tree('getChildren',roots[i].target);
		if(children){
			return children[0];
		}
	}
	return roots[0];
	
	
}


function disable_onchange(){
	onchange_enable=false;
}

function enable_onchange(){
	onchange_enable=true;
}
/**
 * 所有 form元素的onchange 事件
 */
function form_onchange(){
	if(onchange_enable){
		flag_edit=true;
		
		// 置亮 save 和 cancel按钮
		$('#linkbutton_save').linkbutton("enable");
		$('#linkbutton_cancel').linkbutton("enable");
	}
}

function searcher_func(value,name){
	var qname=encodeURIComponent(value);
	$('#wftree').tree(	{
		url : urls.wftree+'?name='+qname,
	});
}

/**
 * 取得树上选中的节点
 */ 
function getTreeSelect() {
	return $('#wftree').tree("getSelected");
}
/**
 * 添加工作流
 */
function menu_add_req(){
	if (flag_edit) {
		$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!', 'warnning');
		return;
	}
	var node = getTreeSelect();
	if (node != undefined) {

		var new_id = NULL_NODE_ID;
		var new_name = '新工作流';
	
		// 添加新菜单
		var newmenu = {
			id : new_id,
			text : new_name,
			type:'workflow',
			iconCls:'icon-wfnode'
		};
		addFlag = 1;
		$('#wftree').tree('append', {
			parent : node.target,
			data : [ newmenu ],
			
		});
		
		
		
		prepareNewWorkflow(new_name,node.text);

		var nm = $('#wftree').tree('find', new_id);
		last_node_handle=nm;
		$('#wftree').tree('select', nm.target);
		
		$('#panel1').panel("close");
		$('#panel2').panel("close");
		$('#panel3').panel("open");
		
		$('#linkbutton_save').linkbutton("enable");
		$('#linkbutton_cancel').linkbutton("enable");
		
		flag_edit=true;
		
		$('#key').textbox({editable:true});
		$('#name').textbox({editable:true});
	}
}

/**
 * 删除工作流
 */
function menu_del_req(){
	var node=$('#wftree').tree('getSelected');
	if(node){
		$.messager.confirm('确认','是否删除该工作流?',function(r){
			if(r){
				var nid=node.id;
				$.post(urls.del,{ id:nid},function(msg){
					easyui_auto_notice(msg);
					menu_refresh();
				},"JSON");
			}
		});
		
	};
}

function prepareNewWorkflow(name, category) {
	$('#content').form('clear');
	$('#name').textbox("setValue", name);
	$('#deftype').val(category);
	
	var date=new Date();
	var date_str=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
	$('#startdate').datebox('setValue',date_str);
	$('#content').form('validate');
}

/**
 * 保存工作流定义
 */
function saveEdit() {

	$('#content').form('submit', {
		url : urls.save,
		onSubmit : function() {
			return true;
		},
		success : function(data) {
			try {
				data = eval("(" + data + ")");
			} catch (e) {

			}
			if (data == null || data == undefined) {
				$.messager.alert('警告', '保存失败!', 'warnning');
				return;
			}
			if (data.success == true) {
				easyui_auto_notice(data);

				$('#linkbutton_save').linkbutton("disable");

				$('#linkbutton_cancel').linkbutton("disable");

				flag_edit=false;
				addFlag=1;
				menu_refresh();
			} else {
				easyui_auto_notice(data);

			}

		}
	});

}

/**
 * 刷新树
 */ 

function menu_refresh() {
	addFlag = 0;
	$('#wftree').tree({
		url : urls.wftree
	});
}
/**
 * 撤销修改
 */
function rejectEdit() {

	$('#wftree').tree('reload');
	$('#linkbutton_save').linkbutton("disable");
	$('#linkbutton_cancel').linkbutton("disable");
	flag_edit=false;

}


function tabid_searcher(){
	alert(1);
}

function openTableSelectDialog(value,name,tabnameSearchbox,tabidInputBox){
	parent.$.fastModalDialog({
		title : '数据表选择',
		width : 450,
		height : 400,
		iconCls:'icon-search',
		html:'<div><table id="grid_9981" ></table></div>',
		dialogID:'dlg90',
		onOpen: function(){
				var grid_ = parent.$.fastModalDialog.handler['dlg90'].find('#grid_9981');
				grid_.datagrid({
					url:'base/tabsdef/SysDicTableController/queryAllTables.do',
					columns : [[
			            {
			            	field : "ck",
			            	checkbox : true
			            },
			            {
			            	field : "tablecode",
			            	title : "表名",
			            	width : 120
			            },
			            {
			            	field : "tablename",
			            	title : "中文表名",
			            	width : 200
			            }
			           
					]],
					singleSelect:true,
					fit:true,
					pagination:false
				});
		},
		buttons: [	
					{
						text:"选择",
						iconCls: "icon-ok",
						handler: function(){
							var sel=parent.$.fastModalDialog.handler['dlg90'].find('#grid_9981').datagrid('getSelected');
							tabnameSearchbox.searchbox('setValue',sel.tablename);
							tabidInputBox.val(sel.tableid);
							parent.$.fastModalDialog.handler['dlg90'].dialog('close');
						}
					},
				    {
				    	text:"取消",
				    	iconCls: "icon-cancel",
				    	handler: function(){
				    		parent.$.fastModalDialog.handler['dlg90'].dialog('close');
				    	}
				    }
				]
	});
}
function panel2_resize(w,h){
	$('#canvas_panel').panel('resize',{
		width:w-342,
		height:h-60
	});
	
	$('#widgets').datagrid('resize',{
		width:110,
		height:h-60
	});
	
	$('#prop').datagrid('resize',{
		width:220,
		height:h-60
	});
	
}


function showProcDef(id){
	var realid=id;
	$('#content').form('load','getWorkflowProcdefDetail.do?id='+realid);	
	$('#key').textbox({editable:false});
	$('#name').textbox({editable:false});
}