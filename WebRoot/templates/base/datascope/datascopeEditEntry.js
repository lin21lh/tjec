
$(function() {
	$('#datascopeTree').tree(
		{
			url : contextpath + 'base/datascope/datascopeController/findDataScopeMain.do',
			method : 'post',
			animate : true,
			onContextMenu : function(e, node) {},
			onLoadSuccess : function(node, data) {},
			onClick : function(node) {
				$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetailByID.do', {scopemainid:node.id}, function(data){
					clickdatascopemainDetail(data, '470px', false);
					loadRelation(node.id);
				}, "json");
			}
	});
	
});

function loadRelation(scopemainid) {
	scopemainid = scopemainid ? scopemainid : '';
	$("#userroleresdatagrid").datagrid({
		fit : true,
		stripe : true, //默认false，斑马条纹
		url : contextpath + "base/datascope/datascopeController/findRelationByScopemainID.do",
		queryParams : {scopemainid:scopemainid}
	});
}

/**
 * 添加数据权限
 */
function adddatascope() {
	$("input[name$='scopemainname']").val('');
	$("input[name$='scopemainid']").val();
	var node = $('#datascopeTree').tree('getSelected');
	if (node != null) {
		$('#datascopeTree').tree('reload');
	}
	var condTabs = $('#condtions');
	var all_tabs = condTabs.tabs('tabs');
	for (var n=all_tabs.length-1; n>=0; n--) {
		condTabs.tabs('close', condTabs.tabs('getTabIndex',all_tabs[n]));
	}
}

/**
 * 点击数据权限树 赋值
 * @param data
 */
function clickdatascopemainDetail(data, height, readonly) {
	
	if (data.success) {
		var condTabs = $('#condtions');
		var all_tabs = condTabs.tabs('tabs');
		for (var n=all_tabs.length-1; n>=0; n--) {
			condTabs.tabs('close', condTabs.tabs('getTabIndex',all_tabs[n]));
		}
		
		var scopemain = data.body;
		$("input[name$='scopemainid']").val(scopemain.scopemainid);
		$("input[name$='scopemainname']").val(scopemain.scopemainname);
		var scopesubs = scopemain.subList ? scopemain.subList : new Array();
		for (var i=0; i<scopesubs.length; i++) {
        	var id = 'ct_'+scopesubs[i].scopesubname;
            $('#condtions').tabs('add', {
                title: scopesubs[i].scopesubname,
                content: "<tr id=" + id + " class=" + id + "></tr>",
                closable: false
            });
            var scopeitems = scopesubs[i].itemList;
            for (var j=0; j<scopeitems.length; j++) {
            	addCondition(height, readonly);
            	var mtCombobox = $("div[id$='" + currenttdid + "']").find("input[id$='matchtype']");
            	mtCombobox.combobox({
            		onSelect : function(record) {
            			if (record.id == '12') {
            				scopeValBox.textbox('clear');
            				scopeValBox.textbox('disable');
            			} else {
            				scopeValBox.textbox('enable');
            			}
            		}
            	});
            	$("div[id$='" + currenttdid + "']").find("input[id$='elementname']").gridDialog('setValue', scopeitems[j].elementcode);
            	$("div[id$='" + currenttdid + "']").find("input[id$='elementname']").gridDialog('setText', scopeitems[j].elementname);
            	switch (scopeitems[j].datatype) {
					case 'C':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude'][value="+scopeitems[j].isinclude+"]").attr("checked", "checked");
						mtCombobox.combobox('loadData', [{ id : '0', text : '全部'}, { id : '1', text : '多选'}]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", false);
						scopeValBox.textbox({readonly : true});
						
		            	$.post(contextpath + 'base/dic/dicElementValSetController/queryDicToDataScope.do', {'elementcode' :  scopeitems[j].elementcode,'scopeitemid':scopeitems[j].scopeitemid, currenttdid : currenttdid}, function(data) {
		            		$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree(
		    						{
		    							data : data.treeData,
		    							checkbox : true,
		    							cascadeCheck : false,
		    							onBeforeCheck : function(node, checked) {
		    								if (node.isChecked == '1' && checked)
		    									return true;
		    								else {
		    									return !readonly;
		    								}
		    							},
		    							onClick : function(node) {
		    								if (!readonly)
		    									$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree('check', node.target);
		    							}
		    						
		    						}
		    				);
		            		
		            	},'JSON');
						
					    break;
					case 'S':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '8', text : '左匹配'}, { id : '9', text : '右匹配'}, { id : '10', text : '全匹配'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.textbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'N':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.numberbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'Y':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}, { id : '13', text : '不为空' }]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.numberbox({readonly : false, value : scopeitems[j].scopevalue});
						break;
					case 'D':
						mtCombobox.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, {id : '12', text : '为空'}]);
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						scopeValBox.datebox({readonly : false, value : scopeitems[j].scopevalue});
						break;
            	}
            	
            	$("div[id$='" + currenttdid + "']").find("input[id$='matchtype']").combobox('setValue', scopeitems[j].matchtype);

				if(scopeitems[j].matchtype == 0) {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
				} else
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").show();
			
            }
		}
	} else {
		easyui_warn(data.title);
	}
		
}

function deleteDataScope() {
	var node = $('#datascopeTree').tree('getSelected');
	if (!node) {
		easyui_warn('请选择要删除的数据权限！');
		return ;
	}
	$.messager.confirm('确认', '是否删除该数据权限?', function(r) {
		if (r) {
			$.post(contextpath + 'base/datascope/datascopeController/deletedatascope.do', {scopemainid : node.id}, function(result) {
				if (result.success) {
					easyui_show(result.title);
					$('#datascopeTree').tree('reload');
					var roots = $('#datascopeTree').tree('getRoots');
					if (roots.length > 0) {
						$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetailByID.do', {scopemainid:roots[0].id}, function(data){
							clickdatascopemainDetail(data, '490px', false);
						}, "json");
					}
				} else {
					easyui_warn(result.title);
				}
			});
		}
	});
}

/**
 * 新增条件项
 */
function addPanel(height){
    $.messager.prompt('录入规则名', '请录入规则', function(msg){
        if (msg){
        	var id = 'ct_'+msg;
            $('#condtions').tabs('add', {
                title: msg,
                content: "<tr id=" + id + " class=" + id + "></tr>",
                closable: false
            });
            
            addCondition(height, false);
        }
    });

}

/**
 * 删除条件项
 */
function removePanel(){
    $.messager.confirm('提示', '确定要删除吗?', function(r){
    	if (r) {
	        var tab = $('#condtions').tabs('getSelected');
	        if (tab){
	            var index = $('#condtions').tabs('getTabIndex', tab);
	            $('#condtions').tabs('close', index);
	        }
    	}
    });
}

var currenttdid = undefined;

var scopeValBox = null;

/**
 * 条件选中
 * @param id
 */
function divClick(id) {
	currenttdid = id;
	$("div[name$='cond']").css("border", "solid 0px");
	$("div[id$='" + id + "']").css("border", "solid 1px yellow");
}

/**
 * 新增条件项内容
 * @param title
 * @param height
 * @param readonly
 * @returns {String}
 */
function addContent(title, height, readonly) {
	var tdid = parseInt(Math.random() *100000+1);
	currenttdid = tdid;
	var content = '<td class="' + tdid + '">';
	content += '<form>';
	content += '<div name="cond" class="cond" id=' + tdid + ' onclick="divClick(' + tdid + ')" style="border:solid 1px white;height:' + height+ ';width:310px;background-color:#eee;float:left;">';
	content += '<div id="lay_' + tdid + '" class="easyui-layout" data-options="fit:true">';
	content += '<div data-options="region:\'north\',split:true,border:false" style="height:120px;background-color:#eee;float:left;">';
	content += '<table>';
	content += '<tr><td>';
	content += '<input id="exists" class="easyui-validatebox" name="isinclude" type="radio" checked="checked" required="true" value="1"';
	if (readonly)
		content += ' disabled=true';
	content +=	'>包含</input>';
	content += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	content += '<input id="noExists" class="easyui-validatebox" name="isinclude" type="radio" required="true" value="0"';
	if (readonly)
		content += 'disabled=true';
	content += '>排除</input>';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="elementname" class="easyui-validatebox combobox" name="elementname" style="width:180px" required="true"';
	if (readonly)
		content += ' readonly=true';
	content+=' />';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="matchtype" class="easyui-combobox" style="width:180px" name="matchtype"';
	if (readonly)
		content += ' readonly=true';
	content += '/>';
	content += '</td></tr>';
	content += '<tr><td id="scopevalTD">';
	content += '<input type="text" name="scopevalue" style="width:180px"';
	if (readonly)
		content += ' readonly=true';
	content += '/>';
	content += '</td></tr>';
	content += '</table>';
	content += '</div>';
	content += '<div name="scopevalPanel" data-options="region:\'center\',border:false" style="background-color:#eee;float:left;">';
	content += '<ul id="scopevalTree" class="easyui-tree"></ul>';
	content += '</div>';
	content += '</div>';
	content += '</form>';
	content += '</td>';
	
	return content;
}

/**
 * 新增条件
 */
function addCondition(height, readonly) {
	var tab = $('#condtions').tabs('getSelected');
	if (tab) {
		var title = tab.panel('options').title;
		$('#ct_' + title + '').append(addContent(title, height, readonly));
		$('#lay_' + currenttdid).layout();
		//数据项
		
		var matchtypeCombo = $("div[id$='" + currenttdid + "']").find("input[name$='matchtype']");
		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
		$("div[id$='" + currenttdid + "']").find("input[name$='elementname']").gridDialog({
			title :'要素选择',
			dialogWidth : 500,
			dialogHeight : 400,
			singleSelect: true,
			dblClickRow : true,
			showSearchWin : !readonly,
			showClearBtn : !readonly,
			missingMessage:"请选择条件字段",
			hiddenID : 'elementcode_' + currenttdid,
			hiddenName:'elementcode',
			valueField:'elementcode',
			textField:'elementname',
			filter: {
						cncode: "数据项编码",
						code: "elementcode",
						cnname: "数据项名称",
						name : "elementname"
					},
			url : contextpath + 'base/dic/dicElementController/findDicElementToDataScope.do',
			cols : [[
				{field : "ck", checkbox : true}, 
				{field : "elementcode",title : "数据项编码",width : 180},
		    	{field : "elementname", title : "数据项名称", width : 180},
		    	{field : "datatype", hidden : true}
		    ]],
		    assignFunc : function(row) {
	            switch (row.datatype) {
					case 'C':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude'][value=1]").attr("checked", "checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", false);
			           	$.post(contextpath + 'base/dic/dicElementValSetController/queryDicToDataScope.do', {'elementcode' : row.elementcode}, function(data) {
		    				$("div[id$='" + currenttdid + "']").find('ul').tree(
		    						{
		    							data : data.treeData,
		    							animate : true,
		    							checkbox : true,
		    							cascadeCheck : false,
		    							onClick : function(node) {
		    								if (!readonly)
		    									$("div[id$='" + data.currenttdid + "']").find('ul[class$="easyui-tree"]').tree('check', node.target);
		    							}
		    						}
		    				);
		            	},'JSON');
			           	scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			           	scopeValBox.textbox({value : '', type : 'text', iconCls : null, readonly : true});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						$("div[id$='" + currenttdid + "']").find("input[name$='matchtype']")
						matchtypeCombo.combobox('loadData', [{ id : '0', text : '全部'}, { id : '1', text : '多选'}]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
					    break;
					case 'S':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '8', text : '左匹配'}, { id : '9', text : '右匹配'}, { id : '10', text : '全匹配'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.textbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'N':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'Y':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.numberbox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
					case 'D':
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").removeAttr("checked");
						$("div[id$='" + currenttdid + "']").find("input[name$='isinclude']").attr("disabled", true);
						matchtypeCombo.combobox('loadData', [{ id : '4', text : '等于'}, { id : '2', text : '大于等于'}, { id : '3', text : '小于等于'}, { id : '5', text : '不等于'}, { id : '6', text : '大于'}, { id : '7', text : '小于'}, { id : '12', text : '为空' }, { id : '13', text : '不为空' }]);
						matchtypeCombo.combobox('setValue', '');
			    		matchtypeCombo.combobox('reload');
			    		scopeValBox.textbox('destroy');
			    		$("div[id$='" + currenttdid + "']").find("td[id='scopevalTD']").append('<input type="text" style="width:180px" name="scopevalue" />');
			    		scopeValBox = $("div[id$='" + currenttdid + "']").find("input[name$='scopevalue']");
			    		scopeValBox.datebox({readonly : false});
						$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
						break;
	        	}
			}
		});
		matchtypeCombo.combobox({
			valueField : 'id',
			textField : 'text',
			data : [],
			onSelect : function(record) {
				if(record.id != 1) {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").hide();
				} else {
					$("div[id$='" + currenttdid + "']").find("div[name$='scopevalPanel']").show();
				}	
			}
		});
		
		scopeValBox.textbox({readonly : false});
	} else {
		addPanel(height);
	}
}

/**
 * 删除条件
 */
function removeCondition() {
	var tab = $('#condtions').tabs('getSelected');
	if (tab) {
		var title = tab.panel('options').title;
		$('.'+currenttdid+'').remove();
		if ($("#ct_" + title).find("td").length == 0) {
		     var index = $('#condtions').tabs('getTabIndex', tab);
		     $('#condtions').tabs('close', index);
		}
	}
}

function saveCondition() {
	var scopemainname = $("input[name$='scopemainname']").val(); //数据权限名称
	var scopemainid = $("input[name$='scopemainid']").val(); //数据权限ID 维护用
	if (!scopemainname) {
		easyui_info("请输入数据权限名称！");
		return;
	}
	
	var tabs_all = $('#condtions').tabs('tabs');
	if (!tabs_all) {
		easyui_info("请设置数据范围规则！");
		return;
	}
	var data = "[{";
	$.each(tabs_all, function (i, item) {
		var scopesubname = item.panel('options').title;
		if (i != 0)
		data += "}, {";
		data += "\"scopesubname\":\"" + scopesubname + "\"";
		data += ", \"items\":[";
		var forms = item.find("form");
		$.each(forms, function(j, form) {
			if (j != 0)
				data += ", ";
			var fd = $(form).form("getData", true);
			var scopevalues = ""; 
			if (fd.matchtype == 1) {
				var nodes = $(form).find("ul[id='scopevalTree']").tree("getChecked");
				
				for(var i=0; i<nodes.length; i++) {
					if (scopevalues.length > 0)
						scopevalues += ",";
					
					scopevalues += nodes[i].id;
				}
			}

			data += JSON.stringify(fd);
			data =	data.substring(0, data.length -1) + ",\"scopevalues\":\"" + scopevalues + "\"}";
		});
		data += "]";
	});
	data += "}]";
	data = "{\"scopemainname\":\""+ scopemainname+ "\", \"scopemainid\":\"" + scopemainid + "\", \"subs\":" + data + "}";
	
	$.post("base/datascope/datascopeController/saveDatascope.do", {
		str : data
	}, function(msg) {
		easyui_auto_notice(msg);
		$('#datascopeTree').tree('reload');
	}, "JSON");
}