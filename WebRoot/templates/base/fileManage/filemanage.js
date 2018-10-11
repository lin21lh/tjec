var fileDataGrid = null;
function loadFileDataGrid(keyid, elementcode){
	$("#fileDataGrid").datagrid({
		fit : true,
		stripe : true,
		singleSelect : false,
		rownumbers : true,
		pagination : true, 
		pageNumber : 1,
		pageSize : 10,
		pageList : [10,20,30,40,50],
		queryParams : {
			keyid : keyid,
			elementcode : elementcode
		},
		toolbar : [{
			text:'添加',
			iconCls:'icon-add',
			handler:function(){
				$('#addFileDiv').dialog({
					iconCls:'icon-save',
					title : '附件添加',
					width : 600,
					resizable:true,
					modal:true,
					height : 450,
					href : contextpath + '/base/filemanage/fileManageController/addFile.do?keyid=' + keyid + '&elementcode='+elementcode,
					onLoad: function(){
						
					},buttons : [{text:'保存', iconCls:'icon-save',
	                    handler:function(){
	                        save_files();
	                    }
						}, {text:'关闭', iconCls:'icon-cancel',
	                    handler:function(){
	                       $('#addFileDiv').dialog('close');
	                    }
	                }]
				});
			}
		},
		{
			text:'下载',
			iconCls:'icon-load',
			handler:function(){
				
				var record = $("#fileDataGrid").datagrid("getSelections");
				if (record.length == 0) {
					easyui_warn("请选择要下载的附件！");
					return;
				} else if (record.length > 1) {
					easyui_warn("仅能选择一条附件下载！");
					return;
				}
				
				 window.open(contextpath + 'base/filemanage/fileManageController/downLoadFile.do?itemid=' + record[0].itemid, "_blank");
			}
		},{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				var record = $("#fileDataGrid").datagrid("getSelections");
				if (record.length == 0) {
					easyui_warn("请选择要删除的附件！");
					return;
				}
				$.messager.confirm("确认删除", "确认要删除选中的附件吗？", function(r) {
					if (r) {
						var itemids = "";
						for (var i=0; i<record.length; i++) {
							if (itemids.length > 0)
								itemids += ",";
							itemids += record[i].itemid;
						}
						$.post(contextpath + 'base/filemanage/fileManageController/delete.do', {
							itemids : itemids
							}, function(result) {
								if (result.success) {
									easyui_info(result.title, function() {
										$("#fileDataGrid").datagrid("reload");
									});
								} else {
									easyui_warn(result.title);
								}
						}, 'json');
					}
				});
			}
		},{
			text:'预览',
			iconCls:'icon-item',
			handler:function(){
				var record = $("#fileDataGrid").datagrid("getSelections");
				if (record.length == 0) {
					easyui_warn("请选择要预览的附件！");
					return;
				} else if (record.length > 1) {
					easyui_warn("仅能选择一条附件预览！");
					return;
				}
				
				$('#addFileDiv').dialog({
					iconCls:'icon-save',
					title : '附件预览',
					width : 600,
					resizable:true,
					modal:true,
					height : 450,
					href : contextpath + '/base/filemanage/fileManageController/showFile.do?itemid=' + record[0].itemid,
					buttons : [{text:'关闭', iconCls:'icon-cancel',
	                    handler:function(){
	                       $('#addFileDiv').dialog('close');
	                    }
	                }]
				});
			}
		}],
		url : contextpath + 'base/filemanage/fileManageController/query.do',
		loadMsg : "正在加载，请稍候......",
		frozenColumns : [[{
            	field : "itemid",
            	checkbox : true
		}]], 
		columns : [[{
            	field : "title",
            	title : "标题",
            	halign : 'center',
            	width : 440
            }, {
            	field : "filename",
            	title : "文件名称",
            	halign : 'center',
            	width : 200
            }, {
            	field : "usercode",
            	title : "上传用户",
            	halign : 'center',
            	width : 140
            }, {
            	field : "createtime",
            	title : '上传时间',
            	halign : 'center',
            	width : 200
            }, {
            	field : "remark",
            	title : "注释",
            	halign : 'center',
            	width : 200
            }
		]]
	});
}

function save_files() {
	$("#uploadify").uploadify('settings', 'formData', { 'title' : $("#file_title").val(), 'remark' : $("#file_remark").val(), 'savemode': 1});
	$("#uploadify").uploadify("upload", "*");
}
