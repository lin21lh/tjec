
var baseUrl = contextpath + "ppms/wdjg/WdzsController/";
var urls = {
	queryUrl : baseUrl + "queryProject.do",
	queryWdzsInitUrl : baseUrl + "queryWdzsInit.do",
	downloadAllUrl : baseUrl + "downloadAll.do"
};
//默认加载
$(function() {
	
	comboboxFuncByCondFilter(menuid,"xmdqhj", "PROXMDQHJ", "code", "name");//项目环节
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
var projectDataGrid;

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
			menuid : menuid
		},
		url : url,
		loadMsg : "正在加载，请稍候......",
		toolbar : "#toolbar_center",
		showFooter : true,
		columns : [ [ {field : "projectid",checkbox : true}
						,{field : "xmdqhjName",title : "项目环节",halign : 'center',width:120,sortable:true}
			          ,{field : "proName",title : "项目名称",halign : 'center',width:190,sortable:true}
			          ,{field : "proTypeName",title : "项目类型",halign : 'center',	width:80,sortable:true}
			          ,{field : "amount",title : "项目总投资（万元）",halign : 'right',align:'right',	width:125,sortable:true,formatter:function(value,row,index){return "<font color='blue'>"+value.toFixed(2)+"</font>";}}
			          ,{field : "proYear",title : "合作年限",halign : 'center',align:'right',width:70,sortable:true	}
			          ,{field : "proTradeName",title : "所属行业",halign : 'center',	width:100	,sortable:true}
			          ,{field : "proPerateName",title : "运作方式",halign : 'center',	width:100,sortable:true	}
			          ,{field : "proReturnName",title : "回报机制",halign : 'center',	width:120,sortable:true}
			          ,{field : "proSendtime",title : "项目发起时间",halign : 'center',	width:120,sortable:true}
			          ,{field : "proSendtypeName",title : "项目发起类型",halign : 'center',	width:120,sortable:true	}
			          ,{field : "proSendperson",title : "项目发起人名称",halign : 'center',	width:120,sortable:true	}
			          ,{field : "proPerson",title : "项目联系人",halign : 'center',	width:100,sortable:true }
			          ,{field : "proPhone",title : "联系人电话",halign : 'center',	width:100,sortable:true }
			          ,{field : "createusername",title : "创建人",halign : 'center',	width:130,sortable:true	}
			          ,{field : "createtime",title : "创建时间",halign : 'center',	width:130,sortable:true	}
			          
			         
		             ] ]
	});
}

/**
 * 项目查询
 */
function topQuery(){
	var param = {
			xmdqhj : $("#xmdqhj").combobox('getValues').join(","),
			proName : $("#proName").val(),
			proPerson : $("#proPerson").val(),
			proTrade :  $("#proTrade").val(),
			proPerate : $("#proPerate").combobox('getValues').join(","),
			proReturn : $("#proReturn").combobox('getValues').join(","),
			proSendtype : $("#proSendtype").combobox('getValues').join(","),
			proType : $("#proType").combobox('getValues').join(","),
			menuid : menuid
		};
	projectDataGrid.datagrid("load", param);
}

/**
 * 项目文档
 * @return
 */
function wdzsDetail(){
	var selectRow = projectDataGrid.datagrid('getChecked');
	if(selectRow==null || selectRow.length==0||selectRow.length>1){
		easyui_warn("请选择一条数据！",null);
		return;
	}
	parent.$.modalDialog({
		title : "项目文档展示",
		iconCls : 'icon-view',
		width : 900,
		height : 530,
		href : urls.queryWdzsInitUrl,
		queryParams : {
			projectid : selectRow[0].projectid
		},
		onLoad : function() {
			var mdDialog = parent.$.modalDialog.handler;
			
		},
		buttons : [ {
			text : "下载全部",
			iconCls : "icon-save",
			handler : function() {
				var mdDialog = parent.$.modalDialog.handler;
				var as = mdDialog.find("#wdzsTable a");
				if (as.length <= 0){
					easyui_warn("无可下载文档！",null);
					return;
				} else {
					var items = '';
					for(var i=0;i<as.length;i++){
						if (i==0){
							items += as[i].id;
						} else {
							items += "," + as[i].id;
						}
					}
					
					/*发送下载请求*/
					location.href = urls.downloadAllUrl + "?items=" + items + "&projectid=" + mdDialog.find("#wd_projectid").val();
					/*ajax不支持下载*/
//					$.ajax({
//						type : "post",
//						url : urls.downloadAllUrl,
//						data : {
//							items : items
//						},
//						async : false,
//						success : function(result){
//							
//						}
//					});
				}
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
