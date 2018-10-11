$(function() {
	
	$('#roleTree').tree({
		url : contextpath + 'base/datascope/DataRightDetailsController/findRoleDSList.do',
		method : 'post',
		animate : true,
		queryParams : {
			resourceid : resourceid
		},
		onContextMenu : function(e, node) {
			
		},
		onLoadSuccess : function(node, data) {
			if (data.length == 0) { //如果没有配置数据权限，则展示默认数据权限
				// $('#dataRightDetail_layout').layout('remove', 'west');
				$("input[name$='scopemainname']").val('【系统默认数据权限】');
				$.post(contextpath + 'base/datascope/DataRightDetailsController/defaultDataRightDetails.do', {resourceid:resourceid}, function(data){
					if (data.success)
						defDSDetails(data.body);
					else {
						easyui_show(data.title);
					}
				}, "json");
			}
		},
		onClick : function(node) {
			$.post(contextpath + 'base/datascope/datascopeController/getDataScopeDetailByID.do', {scopemainid:node.scopemainid}, function(data){
				
				clickdatascopemainDetail(data, '470px', true);
			}, "json");
		}
	});
});

/**
 * 系统默认数据权限
 */
function defDSDetails(body) {

	var id = 'ct_defaultDataRight';
	if (body.isTree == false) {
		$('#condtions').tabs('add', {
	        title: '系统默认权限',
	        content: '<h4>'+body.filterMsg + '</h4>',
	        closable: false,
	        border : false
	    });
		$('#defaultDataRight_panel').panel();
	} else {
		$('#condtions').tabs('add', {
	        title: '系统默认权限',
	        content: getTabContent(),
	        closable: false
	    });
		$('#lay_defaultAgency').layout();
		
    	
    	$("div[id$='defaultAgency_div']").find("input[id$='elementname']").searchbox('setValue', '预算单位');
    	var mtCombobox = $("div[id$='defaultAgency_div']").find("input[id$='matchtype']");
    	
    	mtCombobox.combobox('loadData', [{ value : '0', text : '全部'}, { value : '1', text : '多选'}]);
    	mtCombobox.combobox('setValue', '1');
    	
    	$("div[id$='defaultAgency_div']").find("input[name$='scopevalue']").textbox({readonly : true});
    	
    	
    	$("div[id$='defaultAgency_div']").find('ul').tree(
				{
					data : body.treeList,
					animate : true,
					checkbox : true,
					cascadeCheck : false,
					onBeforeCheck : function(node, checked) {
						if (node.isChecked == '1' && checked)
							return true;
						else
							return false;
					},
					onClick : function(node) {
						
					}
				}
		);
	}

}

function getTabContent() {
	
	var content = '<tr id="defaultAgencyPanel" class=""><td class="defaultAgency_td">';
	content += '<form>';
	content += '<div name="cond" id="defaultAgency_div" class="cond" style="border:solid 1px white;height:470px;width:310px;background-color:#eee;float:left;">';
	content += '<div id="lay_defaultAgency" class="easyui-layout" data-options="fit:true">';
	content += '<div data-options="region:\'north\',split:true,border:false" style="height:120px;background-color:#eee;float:left;">';
	content += '<table>';
	content += '<tr><td>';
	content += '<input id="exists" class="easyui-validatebox" name="isinclude" type="radio" checked="checked" required="true" value="1"';
	content += ' disabled=true';
	content +=	'>包含</input>';
	content += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
	content += '<input id="noExists" class="easyui-validatebox" name="isinclude" type="radio" required="true" value="0"';
	content += 'disabled=true';
	content += '>排除</input>';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="elementname" class="easyui-searchbox" name="elementname" required="true" style="width:180px"';
	content += ' readonly=true';
	content+=' />';
	content += '</td></tr>';
	content += '<tr><td>';
	content += '<input id="matchtype" class="easyui-combobox" name="matchtype" style="width:180px"';
	content += ' readonly=true';
	content += '/>';
	content += '</td></tr>';
	content += '<tr><td id="scopevalTD">';
	content += '<input type="text" name="scopevalue" style="width:180px"';
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
	content += '</td></tr>';
	
	return content;
}

