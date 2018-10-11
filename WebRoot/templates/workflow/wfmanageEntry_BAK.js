var urls = {
	wftree : 'queryWorkflowTree.do',
	detail : 'getWorkflowProcdefDetail.do',
	save : 'saveWorkflowProcdef.do',
	del : 'delWorkflowProcdef.do',
	deleteWfVersion : contextpath + 'workflow/WfProcessDefinitionController/deleteWfVersion.do',
	deploy:'deployWorkflowDef.do',
	getDeployedJpdlContent:'getDeployedJpdlContent.do',
	queryWfPrivilege:'queryWfPrivilege.do',
	queryBackAttr:'queryBackAttr.do',
	queryTaskPageUrl:'queryTaskPageUrl.do',
	queryWorkflowVersion:'queryWorkflowVersion.do',
	editWorkflowProcVersion:'editWorkflowProcVersion.do',
	getWorkflowProcversion:'getWorkflowProcversion.do',
	wfcopyEntry : contextpath + 'workflow/WfProcessDefinitionController/wfcopyEntry.do',
	wfcopyopt : contextpath + 'workflow/WfProcessDefinitionController/wfcopyopt.do'
}

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
				
					if(node.type=='leaf'){
						$('#contextmenu_del').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
						
					}else if(node.type=='category'){
// $('#contextmenu_add').menu('show', {
// left : e.pageX,
// top : e.pageY
// });
					}else if(node.type=='workflow'){
						$('#contextmenu_addversion').menu('show', {
							left : e.pageX,
							top : e.pageY
						});
					}
				},
				onLoadSuccess : function(node, data) {
					switch (addFlag) {
					case 0: // 页面刷新
						var firstNode = getFirstLeafNode();
						if (firstNode) {
							$('#wftree').tree('select', firstNode.target);
							showDetail(firstNode);
							showProcVersion(firstNode.key);
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
	initEasyuiWidget();

	$('#wftab').tabs({
		onSelect:function(title,index){
			if(title=='工作流操作定义'){
				showOpDef(getTreeSelect().key);
				enable_onchange();
			}
		}
	});

	$('#panel2').panel({
		onResize:panel2_resize
	});
	
	$('#vgrid').datagrid({
		url :'',
		columns : [ [ {
			field : 'ck',
			checkbox:true
		},{
			field : 'version',
			title : '版本',
			width : 60
		},{
			field : 'cnstatus',
			title : '启用标识',
			width : 60
		},{
			field : 'startdate',
			title : '启用日期',
			width : 80
		} ,{
			field : 'enddate',
			title : '停用日期',
			width : 80
		}  ] ],
		singleSelect : true,
		onClickRow : function(rowIndex, rowData) {

		},
		onLoadSuccess : function() {

		},
		toolbar : '#vgrid_toolbar',
		fit:true,
		border:false,
		rownumbers:true,
		pagination:false
	});
	
	$("#wftab").tabs({
		onSelect:function(title){
			var node=$('#wftree').tree('getSelected');
			if("工作流版本列表"==title){
				 // 加载version数据
				var par=$('#wftree').tree('getParent',node.target);

				// 加载工作流版本
					showProcVersion(node.key);
			}else{
				showProcDef(node.id);
			}
		}
	});
});

function showDetail(node) {

	switch(node.type){
	case 'leaf':
		// 流程
		$('#panel1').panel("close");
		$('#panel3').panel("close");
		$('#panel2').panel("open");
		loadJpdl(node);
		break;
		
	case 'workflow':
		
		disable_onchange(); // 禁用onchange
		$('#panel2').panel("close");
		$('#panel1').panel("close");
		$('#panel3').panel("open");
		$('#key').val(node.key);
		var par=$('#wftree').tree('getParent',node.target);
		// 加载工作流版本
		var tab = $('#wftab').tabs('getSelected');
		var index = $('#wftab').tabs('getTabIndex',tab);
		if(index==1){
			showProcVersion(node.key);
		}else{
			showProcDef(node.id);
		}
		enable_onchange();
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
	console.log(':onchange fired!');
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
	}
}

/**
 * 复制工作流
 */
function menu_copy_req() {
	if (flag_edit) {
		$.messager.alert('警告', '当前正在进行数据编辑,请先结束编辑状态再进行下一步操作!', 'warnning');
		return;
	}
	
	var node = $('#wftree').tree('getSelected');
	if (node) {
		
		parent.$.modalDialog({
			title : '流程版本定义复制',
			width : 300,
			height : 200,
			iconCls:'icon-copy',
			href : urls.wfcopyEntry,
			onLoad : function() {
				
				var mdDialog = parent.$.modalDialog.handler;
				
				mdDialog.find('#objectWfName').treeDialog({
					title :'工作流选择',
					dialogWidth : 420,
					dialogHeight : 500,
					hiddenName:'objectWfKey',
					multiSelect: false, //单选树
					dblClickRow: true,
					url : contextpath + 'workflow/WfProcessDefinitionController/queryWorkflowConfigTree.do',
					filters:{
						code: "工作流编码",
						name: "工作流名称"
					}
				});
				
				mdDialog.find('#sourceWfName').textbox('setValue', node.text);
				mdDialog.find('#sourceWfID').val(node.id);
				var parentNode = $('#wftree').tree('getParent', node.target);
				mdDialog.find('#objectWfName').treeDialog('setText', parentNode.text);
				mdDialog.find('#objectWfName').treeDialog('setValue', parentNode.code);
			},
			buttons : [{
				text : '确定',
				iconCls: 'icon-ok',
				handler : function() {
					var mdDialog = parent.$.modalDialog.handler;
					mdDialog.find('#wf_copy_form').form('submit', {
						url : urls.wfcopyopt,
						onSubmit : function() {
							return mdDialog.find('#wf_copy_form').form('validate');
						},
						success : function(data) {
							try {
								data = eval("(" + data + ")");
							} catch (e) {

							}
							if (data.success) {
								easyui_info(data.title, function() {
									parent.$.modalDialog.handler.dialog('close');
//									$('#wftree').tree('reload');
//									var id = data.body.id;
//									var node = $('#wftree').tree('find', id);
//									$('#wftree').tree('select', node.target);
									if (node != undefined) {

										var new_id = NULL_NODE_ID;
									
										// 复制工作流版本
										var copyWfVersion = {
											id : new_id,
											text : data.body.version,
											type:'leaf',
											iconCls:'icon-version'
										};
										addFlag = 1;
										parentNode = $('#wftree').tree('find', data.body.objectWfID);
										$('#wftree').tree('append', {
											parent : parentNode.target,
											data : [copyWfVersion],
										});
										
										
										$('#content').form('clear');
										$('#name').val(data.body.name);
										$('#deftype').val(data.body.category);
										
										$('#startdate').datebox('setValue',data.body.startdate);
										$('#enddate').datebox('setValue',data.body.enddate);
										$('#content').form('validate');

										var nm = $('#wftree').tree('find', new_id);
										last_node_handle=nm;
										$('#wftree').tree('select', nm.target);
										
										$('#panel1').panel("close");
										$('#panel3').panel("close");
										$('#panel2').panel("open");
										$.post(urls.getDeployedJpdlContent, {procVersionId:node.id},
												function(msg) {
													 $('#jpdl').val(msg);
													 parseJpdl();
													 // 加载节点权限
													 loadWfPrivilege(node);
													 
													 // 加载退回属性
													 loadBackAttr(node);
													 
													 // 加载节点的url
													 loadTaskUrl(node);		
													 
													 // 加载流程版本信息
													processInfo.name= data.body.objectWfName;
													processInfo.key= data.body.key;
													processInfo.version= data.body.version + "";
													processInfo.startdate= data.body.startdate;
													processInfo.enddate=data.body.enddate;
													showProcessProp();
										});
										
										$('#linkbutton_save').linkbutton("enable");
										$('#linkbutton_cancel').linkbutton("enable");
										
										flag_edit=true;
									}
								});
							}
						}
					});
				}
			}]
		});
	
	}
}

/**
 * 删除工作流
 */
function menu_del_req(){
	var node=$('#wftree').tree('getSelected');
	if(node){
		$.messager.confirm('确认','是否删除该工作流版本?',function(r){
			if(r){
				var nid=node.id;
				$.post(urls.deleteWfVersion,{ id:nid},function(msg){
					easyui_auto_notice(msg);
					menu_refresh();
				},"JSON");
			}
		});
		
	};
}

function prepareNewWorkflow(name, category) {
	$('#content').form('clear');
	$('#name').val(name);
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


/**
 * 加载jpdl文件内容
 */
function loadJpdl(node){
	$.post(urls.getDeployedJpdlContent, {procVersionId:node.id},
			function(msg) {
				 $('#jpdl').val(msg);
				 parseJpdl();
				 // 加载节点权限
				 loadWfPrivilege(node);
				 
				 // 加载退回属性
				 loadBackAttr(node);
				 
				 // 加载节点的url
				 loadTaskUrl(node);		
				 
				 // 加载流程版本信息
				 loadProcVersion(node);
	});
}



function loadWfPrivilege(node){
	
	var parnode=$('#wftree').tree('getParent',node.target);
	
	$.post(urls.queryWfPrivilege,{
		key:parnode.key,
		version: node.text
	},function(list){
		if(list){
			for(var i in list){
				var row=list[i];
				if(row.activityname){
					if(actiMap[row.activityname]){
						actiMap[row.activityname].privilegeid=row.roleid;
						actiMap[row.activityname].privilege=row.rolename;
					}
				}
				
			}
		}
	},'JSON');
}
/**
 * 加载是否可退回信息
 * 
 * @param node
 */
function loadBackAttr(node){
	
	var parnode=$('#wftree').tree('getParent',node.target);
	
	$.post(urls.queryBackAttr,{
		key:parnode.key,
		version: node.text
	},function(list){
		for(var i in list){
			var tagName=list[i].activityname;
			var acti=actiMap[tagName];
			if(acti){
				acti.returnable=list[i].returnable;
				acti.withdrawable=list[i].withdrawable;
			}
		}
	},"JSON");
}

function loadTaskUrl(node){
var parnode=$('#wftree').tree('getParent',node.target);
	
	$.post(urls.queryTaskPageUrl,{
		key:parnode.key,
		version: node.text
	},function(list){
		for(var i in list){
			var tagName=list[i].activityname;
			var acti=actiMap[tagName];
			if(acti){
				acti.pageUrl=list[i].pageUrl;
			}
		}
	},"JSON");
}
/**
 * 加载流程版本信息，主要为启用日期和停用日期
 * 
 * @param node
 */
function loadProcVersion(node){
	
var parnode=$('#wftree').tree('getParent',node.target);
	
	$.post(urls.getWorkflowProcversion,{
		key:parnode.key,
		version: node.text
	},function(msg){
	
		processInfo.startdate=msg.startdate;
		processInfo.enddate=msg.enddate;
		showProcessProp();
	},"JSON");
}
/**
 * 部署流程
 */
function saveAndDeploy(){

	if(!processInfo.name){
		easyui_warn('请填写工作流名称!');
		return;
	}
	if(!processInfo.key){
		easyui_warn('请填写工作流标识!');
		return;
	}
	
	if(!processInfo.version){
		easyui_warn('请填写工作流版本!');
		return;
	}
	
	$.messager.confirm('确认','您是否已经绘制好了流程图，是否确认提交并发布新的流程?',function(r){
		if(r){
			generateJpdl();
			var resourceName=processInfo.key+"_ver"+processInfo.version+'.jpdl.xml';
		
			$.post(urls.deploy,{
				xml: webEncode(document.getElementById("jpdl").value),
				resourceName:webEncode(resourceName),
				name:processInfo.name,
				key:processInfo.key,
				version:processInfo.version,
				startdate:processInfo.startdate,
				enddate:processInfo.enddate,
				backAttr:getBackAttr(),  // 是否可退回属性
				privilege:webEncode(getPrivilege()) ,// 生成节点权限
				pageUrl:webEncode(getPageUrl())
			},function(msg){
				easyui_auto_notice(msg);
			},"JSON");
		}
	});
}

function getBackAttr(){
	var backattr=new Array();
	for(var i in actiArray){
		var acti=actiArray[i];
		if(acti.tagName=='task'){
			var text=acti.name+','+acti.returnable+","+acti.withdrawable;
			backattr.push(text);
		}
	}
	return backattr.join(";");
}

function getPageUrl(){
	var pageUrl=new Array();
	for(var i in actiArray){
		var acti=actiArray[i];
		if(acti.tagName=='task'){
			if(acti.pageUrl!=undefined&& acti.pageUrl!=null&&acti.pageUrl.trim().length>0)
			var text=acti.name+','+acti.pageUrl;
			pageUrl.push(text);
		}
	}
	return pageUrl.join(";");
}
/**
 * 部署流程
 */
function editAndDeploy(){

	if(!processInfo.name){
		easyui_warn('请填写工作流名称!');
		return;
	}
	if(!processInfo.key){
		easyui_warn('请填写工作流标识!');
		return;
	}
	
	if(!processInfo.version){
		easyui_warn('请填写工作流版本!');
		return;
	}
	
	$.messager.confirm('确认','您是否已经绘制好了流程图，是否确认提交并发布新的流程?',function(r){
		if(r){
			var resourceName=processInfo.key+"_ver"+processInfo.version+'.jpdl.xml';
			$.post(urls.deploy,{
				xml: webEncode(document.getElementById("jpdl").value),
				resourceName:webEncode(resourceName),
				wfDefId:$('#wftree').tree('getSelected').id,
				privilage:webEncode(getPrivilage()) // 生成节点权限
			},function(msg){
				easyui_auto_notice(msg);
			},"JSON");
		}
	});
}


function menu_versionadd_req(){
	var node=$('#wftree').tree('getSelected');
	// 取得名称 ,key,版本
	var children=null;
	try{
	children=$('#wftree').tree('getChildren',node.target);
	}catch(e){
		children=new Array();
	}
	var maxVersion=1;
	for(var i = 0 ;i<children.length;i++){
		var text=children[i].text;
		if(+text>=maxVersion){
			maxVersion=+text+1;
		}
	}
	var key=node.key;
	var name=node.text;
	
	// 创建节点
	var newNode={
		id:NULL_NODE_ID,
		text:maxVersion,
		iconCls:'icon-version',
		key:key,
		name:name
	};
	
	$('#wftree').tree('append',{
		parent:node.target,
		data:[newNode]
	});
	// 选中节点
	var curnode=$('#wftree').tree('find',NULL_NODE_ID);
	$('#wftree').tree('select',curnode.target);
	// 打开设计器
	$('#panel1').panel("close");
	$('#panel3').panel("close");
	$('#panel2').panel("open");
	
	// 初始化xml
	var initXml='<?xml version="1.0" encoding="UTF-8"?><process name="'
		+name+'" xmlns="http://jbpm.org/4.4/jpdl" key="'+key+'" version="'+maxVersion+'"></process>';
	$('#jpdl').val(initXml);
	 parseJpdl();
}

function tabid_searcher(){
	alert(1);
}

function showProcDef(id){
	var realid=+id-900000000;
	$('#content').form('load','getWorkflowProcdefDetail.do?id='+realid);	
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
function showProcVersion(key){
	$('#vgrid').datagrid({
		url : urls.queryWorkflowVersion+'?key='+key,
		columns : [ [ {
			field : 'ck',
			checkbox:true
		},{
			field : 'version',
			title : '版本',
			width : 60
		},{
			field : 'cnstatus',
			title : '启用标识',
			width : 60
		},{
			field : 'startdate',
			title : '启用日期',
			width : 80
		} ,{
			field : 'enddate',
			title : '停用日期',
			width : 80
		}  ] ],
		singleSelect : true,
		onClickRow : function(rowIndex, rowData) {

		},
		onLoadSuccess : function() {

		},
		toolbar : '#vgrid_toolbar',
		fit:true,
		border:false,
		rownumbers:true,
		pagination:false
	});
}